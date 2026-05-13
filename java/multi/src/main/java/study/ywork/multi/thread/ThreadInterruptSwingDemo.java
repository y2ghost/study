package study.ywork.multi.thread;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.concurrent.TimeUnit;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/*
 * Swing组件使用中断示例
 */
public class ThreadInterruptSwingDemo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Interrupt Demo");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(500, 500));
        frame.add(createAnimationContainer());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static Component createAnimationContainer() {
        JPanel panel = new JPanel(new BorderLayout());
        AnimatedPanel animatedPanel = new AnimatedPanel();
        panel.add(animatedPanel);
        JButton button = new JButton("Reverse");
        panel.add(button, BorderLayout.SOUTH);
        button.addActionListener(e -> {
            animatedPanel.getThread().interrupt();

        });
        return panel;
    }

    private static class AnimatedPanel extends JComponent {
        private static final long serialVersionUID = 1L;
        private int angle = 0;
        boolean clockwise = true;
        private Thread thread;

        AnimatedPanel() {
            startAnimation();
        }

        private void startAnimation() {
            thread = new Thread(() -> {
                while (true) {
                    angle++;
                    if (angle >= 360) {
                        angle = 0;
                    }
                    if (thread.isInterrupted()) {
                        clockwise = !clockwise;
                    }

                    repaint();
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        System.err.println(e);
                        clockwise = !clockwise;
                    }
                }
            });
            thread.start();
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(Color.MAGENTA);
            g.fillArc(10, 10, 400, 400, clockwise ? -angle : angle, 30);
        }

        public Thread getThread() {
            return thread;
        }
    }
}
