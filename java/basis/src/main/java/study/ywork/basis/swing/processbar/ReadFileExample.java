package study.ywork.basis.swing.processbar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.ProgressMonitorInputStream;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ReadFileExample {
    public static void main(String[] args) throws IOException {
        JFrame frame = createFrame("ProgressMonitorInputStream Example");
        JButton button = new JButton("read file");
        button.addActionListener(createReadFileListener(frame));
        button.setEnabled(true);
        frame.add(button, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    private static ActionListener createReadFileListener(Component parent) throws IOException {
        File file = createTestFile();
        file.deleteOnExit();
        UIManager.put("ProgressMonitor.progressText", "Reading a test file");

        return (ae) -> {
            new Thread(() -> {
                ProgressMonitorInputStream pMonitorInputStream;
                try (BufferedInputStream bis = new BufferedInputStream(
                    pMonitorInputStream = new ProgressMonitorInputStream(parent, "Reading " + file.getName(),
                        new FileInputStream(file)))) {
                    ProgressMonitor progressMonitor = pMonitorInputStream.getProgressMonitor();
                    progressMonitor.setMillisToDecideToPopup(100);
                    progressMonitor.setMillisToPopup(100);

                    byte[] buffer = new byte[2048];
                    while ((bis.read(buffer)) != -1) {
                        System.out.println(new String(buffer));
                        progressMonitor.setNote(bis.available() / 1000 + " more kb to read.");
                        Thread.sleep(2);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        };
    }

    private static File createTestFile() throws IOException {
        Path testPath = Files.createTempFile("test", ".txt");
        String textData = IntStream.range(1, 1000000).mapToObj(Integer::toString).collect(Collectors.joining());
        Files.write(testPath, textData.getBytes());
        return testPath.toFile();
    }

    public static JFrame createFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 700));
        return frame;
    }
}