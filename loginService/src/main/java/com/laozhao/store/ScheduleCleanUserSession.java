package com.laozhao.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;


/*
   定时任务 - 清理过期数据
 */
@Component
@EnableScheduling
public class ScheduleCleanUserSession {

    @Autowired
    private StoreManger storeManger;

    @Scheduled(cron = "0 */10 * * * ?") //10分钟跑一次
    private void doCleanWork(){
        storeManger.cleanTimeOutSession();
    }
}
