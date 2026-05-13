package study.ywork.basis.swing.misc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ChooseFile {
    public static void main(String[] args) {
        JFrame frame = createFrame();
        JLabel fileLabel = new JLabel();

        JButton openFileBtn = new JButton("Open File");
        openFileBtn.addActionListener(ae -> chooseFile(fileLabel, frame));

        JPanel panel = new JPanel();
        panel.add(openFileBtn);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(fileLabel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void chooseFile(JLabel fileLabel, JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        String location = AppPrefs.FILE_LOCATION.get(System.getProperty("user.home"));
        fileChooser.setCurrentDirectory(new File(location));

        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileLabel.setText(selectedFile.getAbsolutePath());
            AppPrefs.FILE_LOCATION.put(selectedFile.getParentFile().getAbsolutePath());
        }
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JTree Remembering File Chooser location via Java Prefs");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(500, 400));
        return frame;
    }

    private enum AppPrefs {
        FILE_LOCATION;

        private static Preferences prefs = Preferences.userRoot().node(AppPrefs.class.getName());

        String get(String defaultValue) {
            return prefs.get(this.name(), defaultValue);
        }

        void put(String value) {
            prefs.put(this.name(), value);
        }
    }
}
