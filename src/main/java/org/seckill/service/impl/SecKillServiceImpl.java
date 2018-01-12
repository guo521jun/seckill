package org.seckill.service.impl;

import org.seckill.dao.SecKillDAO;
import org.seckill.dao.SuccessKillDAO;
import org.seckill.dao.cache.RedisDAO;
import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.entities.SecKill;
import org.seckill.entities.SuccessKill;
import org.seckill.enums.SecKillStateEnum;
import org.seckill.exception.RepeatException;
import org.seckill.exception.SecKillClosedException;
import org.seckill.exception.SecKillException;
import org.seckill.service.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class SecKillServiceImpl implements SecKillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired  //也可以使用@Resource或@Inject
    private SecKillDAO secKillDAO;

    //注入service依赖
    @Autowired
    private SuccessKillDAO successKillDAO;

    @Autowired
    private RedisDAO redisDAO;
    //md5盐值字符串，用于混淆MD5
    private final String slat = "fg55t^I^&^$%3326svzcs6+gesgxvb";

    public List<SecKill> getSecKillList() {
        return secKillDAO.queryForAll(0,4);
    }

    public SecKill getSecKillById(long seckillId) {
        return secKillDAO.queryById(seckillId);
    }

    public Exposer exposerSecKillUrl(long seckillId) {

        //优化点：缓存优化，超时的基础上维护一致性
       //1.访问redis
        SecKill secKill = redisDAO.getSecKill(seckillId);
        if (secKill == null){
            //2.访问数据库
            secKill = secKillDAO.queryById(seckillId);
            if(secKill == null){
                return new Exposer(false, seckillId);
            } else {
                //3.放入redis中
                redisDAO.putSecKill(secKill);
            }
        }
        Date startTime = secKill.getStartTime();
        Date endTime = secKill.getEndTime();
        Date nowTime = new Date();
        //long nowTime = 1513871000500L;
        if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 使用注解控制事务方法的优点
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能的短，不要穿插其他的网络操作，RPC/HTTP请求
     * 或者剥离到事务方法的外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SecKillException
     * @throws RepeatException
     * @throws SecKillClosedException
     */
    @Transactional
    public SecKillExecution executeSecKill(long seckillId, long userPhone, String md5) throws SecKillException, RepeatException, SecKillClosedException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SecKillException("seckill data rewrite");
        }
        //执行秒杀逻辑：减库存，记录购买行为
        Date now = new Date();
        try {
            int updateCount = secKillDAO.reduceNumber(seckillId, now);
            if(updateCount <= 0){
                //没有更新到记录,秒杀结束
                throw new SecKillClosedException("seckill is closed");
            } else {
                //记录购买行为
                int insertCount = successKillDAO.insertSuccessKill(seckillId, userPhone);
                //唯一：seckillId,userPhone
                if(insertCount < 0){
                    //重复秒杀
                    throw new RepeatException("seckill repeated");
                } else {
                    SuccessKill successKill = successKillDAO.queryByIdWithSeckill(seckillId, userPhone);
                    //秒杀成功
                    return new SecKillExecution(seckillId, SecKillStateEnum.SUCCESS, successKill);
                }
            }
        } catch (SecKillClosedException e1){
            throw e1;
        } catch (RepeatException e2){
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常转换为运行期异常
            throw new SecKillException("seckill inner error: " + e.getMessage());
        }
    }
}
