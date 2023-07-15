package study.ywork.multi.thread;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.concurrent.CountDownLatch;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

public class CountDownLatchDemo {
    private static final int LATCH_COUNT = 4;

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(LATCH_COUNT);
        JFrame frame = createFrame();
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));

        for (int i = 1; i <= LATCH_COUNT; i++) {
            ProgressThread progressThread = new ProgressThread(latch, i * 10);
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
            System.err.println(e);
        }

        JFrame frame = new JFrame("CountDownLatch Example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(ProgressThread.PROGRESS_WIDTH, 170));
        return frame;
    }

    private static class ProgressThread extends Thread {
        public static final int PROGRESS_WIDTH = 350;
        private static final int CATCH_UP_COUNT = 75;
        private final JProgressBar progressBar;
        private final CountDownLatch latch;
        private final int slowness;

        public ProgressThread(CountDownLatch latch, int slowness) {
            this.latch = latch;
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
            while (true) {
                progressBar.setValue(++c);
                if (c > 100) {
                    break;
                }

                try {
                    Thread.sleep(c < CATCH_UP_COUNT ? slowness : 100);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }

                if (c == CATCH_UP_COUNT) {
                    // 减少数量
                    latch.countDown();
                    try {
                        // 等待计数为0
                        latch.await();
                    } catch (InterruptedException e) {
                        System.err.println(e);
                    }
                }
            }
        }
    }
}
