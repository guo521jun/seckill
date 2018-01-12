package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.entities.SecKill;
import org.seckill.exception.RepeatException;
import org.seckill.exception.SecKillClosedException;
import org.seckill.exception.SecKillException;

import java.util.List;

/**
 * 业务接口：站在使用者的角度去设计接口
 * 三个方面：方法的定义粒度，参数，返回类型
 */
public interface SecKillService {
    /**
     * 查询所有秒杀记录
     * @return
     */
    List<SecKill> getSecKillList();

    /**
     * 查询一个秒杀记录
     * @param seckillId
     * @return
     */
    SecKill getSecKillById(long seckillId);

    /**
     * 输出秒杀接口的地址
     * 秒杀开启时，输出秒杀接口地址，否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exposerSecKillUrl(long seckillId);

    /**
     * 执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SecKillExecution executeSecKill(long seckillId, long userPhone, String md5) throws SecKillException, RepeatException, SecKillClosedException;
}
