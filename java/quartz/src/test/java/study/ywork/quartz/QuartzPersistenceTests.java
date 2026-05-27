package study.ywork.quartz;

import org.junit.jupiter.api.Test;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class QuartzPersistenceTests {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Scheduler scheduler;

    @Test
    void givenEmailJob_whenSchedulerRestart_thenJobAndTriggerReloadedFromDatabase() throws Exception {
        // 验证任务信息
        JobKey jobKey = new JobKey("emailJob", "emailGroup");
        TriggerKey triggerKey = new TriggerKey("emailTrigger", "emailGroup");

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        assertNotNull(jobDetail, "EmailJob should exist in the running scheduler");

        Trigger trigger = scheduler.getTrigger(triggerKey);
        assertNotNull(trigger, "EmailTrigger should exist in the running scheduler");

        // 重启调度器
        scheduler.standby();
        Scheduler restartedScheduler = applicationContext.getBean(Scheduler.class);
        restartedScheduler.start();

        assertTrue(restartedScheduler.isStarted(), "Scheduler should be running after restart");

        JobDetail reloadedJob = restartedScheduler.getJobDetail(jobKey);
        assertNotNull(reloadedJob, "EmailJob should be reloaded from the database after restart");

        Trigger reloadedTrigger = restartedScheduler.getTrigger(triggerKey);
        assertNotNull(reloadedTrigger, "EmailTrigger should be reloaded from the database after restart");
    }

    @Test
    void givenSampleJob_whenSchedulerRestart_thenSampleJobIsReloaded() throws Exception {
        JobKey jobKey = new JobKey("sampleJob", "group1");
        TriggerKey triggerKey = new TriggerKey("sampleTrigger", "group1");

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        assertNotNull(jobDetail, "SampleJob exists in running scheduler");

        Trigger trigger = scheduler.getTrigger(triggerKey);
        assertNotNull(trigger, "SampleTrigger exists in running scheduler");

        scheduler.standby();
        Scheduler restartedScheduler = applicationContext.getBean(Scheduler.class);
        restartedScheduler.start();

        assertTrue(restartedScheduler.isStarted(), "Scheduler should be running after restart");

        JobDetail reloadedJob = restartedScheduler.getJobDetail(jobKey);
        assertNotNull(reloadedJob, "SampleJob should be reloaded from DB after restart");

        Trigger reloadedTrigger = restartedScheduler.getTrigger(triggerKey);
        assertNotNull(reloadedTrigger, "SampleTrigger should be reloaded from DB after restart");
    }
}

