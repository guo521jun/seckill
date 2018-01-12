package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entities.SuccessKill;

public interface SuccessKillDAO {

    /**
     * 插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询SuccessKill并携带秒杀产品对象实体
     * @param seckillId
     * @return
     */
    SuccessKill queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

}
