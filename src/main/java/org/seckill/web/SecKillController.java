package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SecKillExecution;
import org.seckill.dto.SecKillResult;
import org.seckill.entities.SecKill;
import org.seckill.enums.SecKillStateEnum;
import org.seckill.exception.RepeatException;
import org.seckill.exception.SecKillClosedException;
import org.seckill.service.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")//模块/资源/id
public class SecKillController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecKillService secKillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getList(Model model){
        List<SecKill> list = secKillService.getSecKillList();
        model.addAttribute("list", list);
        return "list";
    }

    //详情页处理
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId == null){
            return "redirect:/seckill/list";
        }
        SecKill secKill = secKillService.getSecKillById(seckillId);
        if(secKill == null){
            return "forward:/seckill/list";
        }
        model.addAttribute("secKill", secKill);
        return "detail";
    }

    //Ajax和JSON
    @ResponseBody
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public SecKillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
        SecKillResult<Exposer> result = null;
        try {
            Exposer exposer = secKillService.exposerSecKillUrl(seckillId);
            result = new SecKillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SecKillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public SecKillResult<SecKillExecution> execute(@PathVariable("seckillId") Long seckillId, @CookieValue(value = "killPhone", required = false) Long phone, @PathVariable("md5") String md5){
        if (phone == null){
            return new SecKillResult<SecKillExecution>(false, "No Register");
        }
        SecKillResult<SecKillExecution> result = null;
        try {
            SecKillExecution secKillExecution = secKillService.executeSecKill(seckillId, phone, md5);
            result = new SecKillResult<SecKillExecution>(true, secKillExecution);
        } catch (RepeatException e1) {
            SecKillExecution execution1 = new SecKillExecution(seckillId, SecKillStateEnum.REPEAT_KILL);
            return new SecKillResult<SecKillExecution>(true, execution1);
        } catch (SecKillClosedException e2) {
            SecKillExecution execution2 = new SecKillExecution(seckillId, SecKillStateEnum.END);
            return new SecKillResult<SecKillExecution>(true, execution2);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            SecKillExecution execution = new SecKillExecution(seckillId, SecKillStateEnum.INNER_ERROR);
            return new SecKillResult<SecKillExecution>(true, execution);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    public SecKillResult<Long> time(){
        Date now = new Date();
        return new SecKillResult<Long>(true, now.getTime());
    }
}
