package study.ywork.quartz.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import study.ywork.quartz.job.EmailJob;
import study.ywork.quartz.job.SampleJob;

@Configuration
public class JobConfiguration {
    @Bean("emailJob")
    public JobDetail emailJobDetail() {
        return JobBuilder.newJob(EmailJob.class)
                .withIdentity("emailJob", "emailGroup")
                .storeDurably()
                .build();
    }

    @Bean("emailTrigger")
    public Trigger emailJobTrigger(@Qualifier("emailJob") JobDetail emailJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(emailJobDetail)
                .withIdentity("emailTrigger", "emailGroup")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(30)
                        .repeatForever())
                .build();
    }

    @Bean("sampleJob")
    public JobDetail sampleJobDetail() {
        return JobBuilder.newJob(SampleJob.class)
                .withIdentity("sampleJob", "group1")
                .storeDurably()
                .requestRecovery(true)
                .build();
    }

    @Bean("sampleTrigger")
    public Trigger sampleTrigger(@Qualifier("sampleJob") JobDetail sampleJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(sampleJobDetail)
                .withIdentity("sampleTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * * * ?")) // every 30s
                .build();
    }
}
