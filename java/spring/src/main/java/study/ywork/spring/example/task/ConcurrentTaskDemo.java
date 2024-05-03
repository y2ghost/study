package study.ywork.spring.example.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentTaskDemo {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.runTasks();
        ConcurrentTaskExecutor exec = context.getBean(ConcurrentTaskExecutor.class);
        ExecutorService service = (ExecutorService) exec.getConcurrentExecutor();
        service.shutdown();
        context.close();
    }

    @Configuration
    public static class MyConfig {
        @Bean
        MyBean myBean() {
            return new MyBean();
        }

        @Bean
        TaskExecutor taskExecutor() {
            ConcurrentTaskExecutor t = new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3));
            t.setTaskDecorator((r) -> {
                return () -> {
                    MyTask task = (MyTask) r;
                    task.run();
                    System.out.printf("time taken for task: %s , %s%n", task.getI(),
                        (System.currentTimeMillis() - task.getStart()));
                };
            });
            return t;
        }
    }

    private static class MyBean {
        @Autowired
        private TaskExecutor executor;

        public void runTasks() {
            for (int i = 0; i < 10; i++) {
                executor.execute(new MyTask(i));
            }
        }
    }

    private static class MyTask implements Runnable {
        private final int i;
        private final long start;

        MyTask(int i) {
            this.i = i;
            this.start = System.currentTimeMillis();
        }

        @Override
        public void run() {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.printf("任务 %d. 线程: %s%n", i, Thread.currentThread().getName());
        }

        public int getI() {
            return i;
        }

        public long getStart() {
            return start;
        }
    }
}
