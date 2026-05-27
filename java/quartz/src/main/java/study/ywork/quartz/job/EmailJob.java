package study.ywork.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(EmailJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        logger.info("Executing Email Job at {}", context.getFireTime());
    }
}
