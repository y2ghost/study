package study.ywork.multi.thread;

import javax.swing.JComponent;
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/*
 * 信号量示例
 * 方法Semaphore#acquire()增加了底层的计数
 * 如果计数大于最大值，此方法将阻塞，直到count的当前值小于或等于最大值
 */
public class SemaphoreDemo {
    private static final int WORKER_COUNT = 6;
    private static final int PERMITS = 2;

    public static void main(String[] args) throws Exception {
        List<WorkerPanel> workers = initPanels();
        Collections.shuffle(workers);
        Semaphore semaphore = new Semaphore(PERMITS);

        ExecutorService es = Executors.newFixedThreadPool(WORKER_COUNT);
        while (true) {
            for (WorkerPanel worker : workers) {
                // 它会阻塞，直到获得许可为止
                semaphore.acquire();
                es.execute(() -> {
                    worker.work();
                    semaphore.release();
                });
            }
        }
    }

    private static List<WorkerPanel> initPanels() {
        JFrame frame = new JFrame("Permits: " + PERMITS);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(250, 200);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        List<WorkerPanel> panels = new ArrayList<>();
        for (int i = 0; i < WORKER_COUNT; i++) {
            WorkerPanel wp = new WorkerPanel();
            frame.add(wp);
            panels.add(wp);
        }

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return panels;
    }

    private static class WorkerPanel extends JComponent {
        private static final long serialVersionUID = 1L;
        private static final int PANEL_SIZE = 60;
        private int angle = 0;
        private boolean active;

        public WorkerPanel() {
            setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        }

        public void work() {
            active = true;
            int cycle = ThreadLocalRandom.current().nextInt(100, 200);
            for (int i = 0; i < cycle; i++) {
                angle += 5;
                if (angle >= 360) {
                    angle = 0;
                }

                repaint();
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                }
            }

            active = false;
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(active ? Color.BLUE : Color.LIGHT_GRAY);
            g.drawArc(0, 0, PANEL_SIZE, PANEL_SIZE, 0, 360);
            g.fillArc(0, 0, PANEL_SIZE, PANEL_SIZE, angle, 30);
        }
    }
}
