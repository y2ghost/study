package study.ywork.basis.swing.button;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/*
 * 按钮演示例子
 */
public class ButtonsExample {
    public static void main(String[] args) {
        UIManager.put("ToggleButton.select", new Color(190, 186, 164));
        JFrame frame = createFrame();
        ButtonGroup buttonGroup = new ButtonGroup();
        JPanel buttonPanel = new JPanel();
        ActionListener listener = actionEvent -> System.out.println(actionEvent.getActionCommand() + " Selected");
        for (int i = 0; i < 5; i++) {
            JToggleButton b = new JToggleButton(Integer.toString(i + 1));
            b.addActionListener(listener);
            buttonGroup.add(b);
            buttonPanel.add(b);
        }
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JToggleButton Group example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 300));
        return frame;
    }
}
