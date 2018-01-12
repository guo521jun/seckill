package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entities.SecKill;

import java.util.Date;
import java.util.List;

public interface SecKillDAO {
    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据id查询秒杀对象
     * @param seckillId
     * @return
     */
    SecKill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     * java没有保存形参的记录 queryForAll(offset, limit)->queryForAll(arg0, arg1)
     */
    List<SecKill> queryForAll(@Param("offset") int offset, @Param("limit") int limit);
}
