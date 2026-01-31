package study.ywork.multi.thread;

/*
 * 一个线程通常会响应另一个线程的操作而行动
 * 如果另一个线程的动作也是对另一个线程的动作的响应，则可能会导致活锁
 * 与死锁一样，活锁的线程无法取得进一步的进展
 * 但是，线程没有被阻塞-它们只是忙于彼此响应而无法继续工作
 */
public class LivelockDemo {
    public static void main(String[] args) {
        final Worker worker1 = new Worker("Worker 1", true);
        final Worker worker2 = new Worker("Worker 2", true);
        final ShareResource s = new ShareResource(worker1);
        new Thread(() -> worker1.work(s, worker2)).start();
        new Thread(() -> worker2.work(s, worker1)).start();
    }

    private static class Worker {
        private String name;
        private boolean active;

        public Worker(String name, boolean active) {
            this.name = name;
            this.active = active;
        }

        public String getName() {
            return name;
        }

        public boolean isActive() {
            return active;
        }

        public synchronized void work(ShareResource resource, Worker otherWorker) {
            while (active) {
                // 等待resource可以
                if (resource.getOwner() != this) {
                    try {
                        wait(10);
                    } catch (InterruptedException e) {
                        System.err.println(e);
                    }

                    continue;
                }

                // 如果其他worker线程活跃，让它先做
                if (otherWorker.isActive()) {
                    System.out
                        .println(getName() + " : handing over the resource to the worker: " + otherWorker.getName());
                    resource.setOwner(otherWorker);
                    continue;
                }

                // 操作myResource
                System.out.println(getName() + ": working on the resource");
                active = false;
                resource.setOwner(otherWorker);
            }
        }
    }

    private static class ShareResource {
        private Worker owner;

        public ShareResource(Worker d) {
            owner = d;
        }

        public synchronized Worker getOwner() {
            return owner;
        }

        public synchronized void setOwner(Worker d) {
            owner = d;
        }
    }

}
