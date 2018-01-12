package seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.entities.SecKill;
import org.seckill.exception.RepeatException;
import org.seckill.exception.SecKillException;
import org.seckill.service.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SecKillServiceTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecKillService secKillService;

    @Test
    public void getSecKillList() throws Exception {
        List<SecKill> list = secKillService.getSecKillList();
        for (SecKill secKill: list){
            logger.info(secKill.toString());
        }
    }

    @Test
    public void getSecKillById() throws Exception {
        SecKill secKill = secKillService.getSecKillById(1000);
        logger.info("secKill=" + secKill);
    }

    @Test
    public void exposerSecKillUrlLogic() throws Exception {
        long id = 1001;
        Exposer exposer = secKillService.exposerSecKillUrl(id);
        if (exposer.isExposed()){
            logger.info(exposer.toString());
            long userPhone = 15236894682L;
            String md5 = exposer.getMd5();
            try {
                SecKillExecution execution = secKillService.executeSecKill(id, userPhone ,md5);
                logger.info(execution.toString());
            } catch (RepeatException e) {
                logger.error(e.getMessage());
            } catch (SecKillException e1) {
                logger.error(e1.getMessage());
            }
        } else {
            //秒杀未开启
            logger.warn(exposer.toString());
        }
        //Exposer{exposed=true, md5='d8a8a39fdede518a2960165ca5026271', seckillId=1000, now=0, start=0, end=0}
    }

    @Test
    public void executeSecKill() throws Exception {
        long id = 1000L;
        long userPhone = 15236894682L;
        String md5 = "d8a8a39fdede518a2960165ca5026271";
        try {
            SecKillExecution execution = secKillService.executeSecKill(id, userPhone ,md5);
            logger.info(execution.toString());
        } catch (RepeatException e) {
            logger.error(e.getMessage());
        } catch (SecKillException e1) {
            logger.error(e1.getMessage());
        }
        //SecKillExecution{seckillId=1000, state=1, stateInfo='秒杀成功', successKill=SuccessKill{seckillId=1000, userPhone=15236894682, state=0, createTime=Sun Dec 24 21:28:27 CST 2017, secKill=null}}
    }

}