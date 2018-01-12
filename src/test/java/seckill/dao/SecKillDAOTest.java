package seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SecKillDAO;
import org.seckill.entities.SecKill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 配置spring和junit整合junit启动时加载spring ioc容器
 * 告诉junit spring配置文件
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SecKillDAOTest {

    //注入dao实现类依赖
    @Resource
    private SecKillDAO secKillDAO;

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        SecKill secKill = secKillDAO.queryById(id);
        System.out.println("name: " + secKill.getName());
        System.out.println("SecKill: " + secKill);
    }

    @Test
    public void queryForAll() throws Exception {
        List<SecKill> secKills = secKillDAO.queryForAll(0, 100);
        for (SecKill secKill: secKills){
            System.out.println(secKill);
        }
    }

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int count = secKillDAO.reduceNumber(1000, killTime);
        System.out.println(count);
    }
}