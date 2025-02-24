package study.ywork.multi.thread;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.concurrent.CyclicBarrier;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

public class CyclicBarrierDemo {
    private static final int THREAD_COUNT = 4;

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT);
        JFrame frame = createFrame();
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));

        for (int i = 1; i <= THREAD_COUNT; i++) {
            ProgressThread progressThread = new ProgressThread(barrier, i * 10);
            frame.add(progressThread.getProgressComponent());
            progressThread.start();
        }

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JFrame createFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("CyclicBarrier Example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(ProgressThread.PROGRESS_WIDTH, 170));
        return frame;
    }

    private static class ProgressThread extends Thread {
        public static final int PROGRESS_WIDTH = 350;
        private static final int CATCH_UP_MULTIPLE = 25;
        private final JProgressBar progressBar;
        private final CyclicBarrier barrier;
        private final int slowness;

        public ProgressThread(CyclicBarrier barrier, int slowness) {
            this.barrier = barrier;
            this.slowness = slowness;
            progressBar = new JProgressBar();
            progressBar.setPreferredSize(new Dimension(PROGRESS_WIDTH - 30, 25));
            progressBar.setStringPainted(true);
        }

        JComponent getProgressComponent() {
            return progressBar;
        }

        @Override
        public void run() {
            int c = 0;
            boolean reversed = false;

            while (true) {
                progressBar.setValue(reversed ? --c : ++c);
                if (c == 100) {
                    reversed = true;
                } else if (c == 0) {
                    reversed = false;
                }

                try {
                    Thread.sleep(slowness);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }

                if (c % CATCH_UP_MULTIPLE == 0) {
                    try {
                        // 这是障碍点，等待所有线程执行
                        barrier.await();
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            }
        }
    }
}
