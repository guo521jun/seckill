package seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SuccessKillDAO;
import org.seckill.entities.SecKill;
import org.seckill.entities.SuccessKill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKillDAOTest {

    @Resource
    private SuccessKillDAO successKillDAO;

    @Test
    public void insertSuccessKill() throws Exception {
        SecKill secKill = new SecKill();
        int count = successKillDAO.insertSuccessKill(1002L,15691768593L);
        System.out.println(count);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        SuccessKill successKill = successKillDAO.queryByIdWithSeckill(1000L,13520167896L);
        System.out.println(successKill);
        System.out.println(successKill.getSecKill());
    }

}