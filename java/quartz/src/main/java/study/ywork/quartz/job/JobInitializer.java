package study.ywork.quartz.job;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import study.ywork.quartz.domain.ApplicationJob;
import study.ywork.quartz.repository.ApplicationJobRepository;

@Component
public class JobInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(JobInitializer.class);
    private final ApplicationJobRepository jobRepository;
    private final Scheduler scheduler;

    public JobInitializer(ApplicationJobRepository jobRepository, Scheduler scheduler) {
        this.jobRepository = jobRepository;
        this.scheduler = scheduler;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Initializing Quartz Job : {}", event.getTimestamp());
        for (ApplicationJob job : jobRepository.findAll()) {
            if (job.isEnabled() && (job.getCompleted() == null || !job.getCompleted())) {
                JobDetail detail = JobBuilder.newJob(SampleJob.class)
                        .withIdentity(job.getName(), "appJobs")
                        .storeDurably()
                        .build();

                Trigger trigger = TriggerBuilder.newTrigger()
                        .forJob(detail)
                        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(30)
                                .repeatForever())
                        .build();

                try {
                    scheduler.scheduleJob(detail, trigger);
                } catch (SchedulerException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
