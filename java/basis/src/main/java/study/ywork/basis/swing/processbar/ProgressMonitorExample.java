package study.ywork.basis.swing.processbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ProgressMonitorExample {
    public static void main(String[] args) {
        JFrame frame = createFrame("ProgressMonitor Example");
        JButton button = new JButton("start task");
        button.addActionListener(createStartTaskActionListener(frame));
        frame.add(button, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    private static ActionListener createStartTaskActionListener(Component parent) {
        UIManager.put("ProgressMonitor.progressText", "Test Progress");
        return ae ->
                new Thread(() -> {
                    ProgressMonitor pm = new ProgressMonitor(parent, "Test Task", "Task starting", 0, 100);
                    pm.setMillisToDecideToPopup(100);
                    pm.setMillisToPopup(100);

                    for (int i = 1; i <= 100; i++) {
                        pm.setNote("Task step: " + i);
                        pm.setProgress(i);
                        try {
                            TimeUnit.MILLISECONDS.sleep(200);
                        } catch (InterruptedException e) {
                            System.err.println(e);
                            Thread.currentThread().interrupt();
                        }
                    }

                    pm.setNote("Task finished");
                }).start();
    }

    public static JFrame createFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 700));
        return frame;
    }
}
