package org.seckill.dto;

import org.seckill.entities.SuccessKill;
import org.seckill.enums.SecKillStateEnum;

public class SecKillExecution {

    private long seckillId;
    //秒杀执行结果状态
    private int state;
    //秒杀执行结果描述信息
    private String stateInfo;

    //秒杀成功对象
    private SuccessKill successKill;

    public SecKillExecution(long seckillId, SecKillStateEnum stateEnum, SuccessKill successKill) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.successKill = successKill;
    }

    public SecKillExecution(long seckillId, SecKillStateEnum stateEnum) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKill getSuccessKill() {
        return successKill;
    }

    public void setSuccessKill(SuccessKill successKill) {
        this.successKill = successKill;
    }

    @Override
    public String toString() {
        return "SecKillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKill=" + successKill +
                '}';
    }
}
