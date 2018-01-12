package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SecKillDAO;
import org.seckill.entities.SecKill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDAOTest {

    long id = 1004;
    @Autowired
    private RedisDAO redisDAO;

    @Autowired
    private SecKillDAO secKillDAO;

    @Test
    public void testSecKill() throws Exception {
        //get和put
        SecKill secKill = redisDAO.getSecKill(id);
        if(secKill == null){
            secKill = secKillDAO.queryById(id);
            if (secKill != null){
                String result = redisDAO.putSecKill(secKill);
                System.out.println(result);
                secKill = redisDAO.getSecKill(id);
                System.out.println(secKill);
            }
        }
    }

}