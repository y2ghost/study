package study.ywork.basis.swing.text;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.function.BiConsumer;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class CutCopyPasteListener {
    public static final String COPY = "COPY";
    public static final String CUT = "CUT";
    public static final String PASTE = "PASTE";

    public static void addCutCopyPasteInterceptor(JTextComponent tc, BiConsumer<String, String> listener) {
        tc.registerKeyboardAction(ae -> handlePaste(tc, listener),
                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_FOCUSED);
        tc.registerKeyboardAction(ae -> handleCopy(tc, listener),
                KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_FOCUSED);
        tc.registerKeyboardAction(ae -> handleCut(tc, listener),
                KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK), JComponent.WHEN_FOCUSED);
    }

    private static void handlePaste(JTextComponent tc, BiConsumer<String, String> listener) {
        try {
            String s = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            tc.paste();
            listener.accept(PASTE, s);

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static void handleCopy(JTextComponent tc, BiConsumer<String, String> listener) {
        String s = tc.getSelectedText();
        tc.copy();
        listener.accept(COPY, s);
    }

    private static void handleCut(JTextComponent tc, BiConsumer<String, String> listener) {
        String s = tc.getSelectedText();
        tc.cut();
        listener.accept(CUT, s);
    }

    public static void main(String[] args) {
        JTextArea ta = new JTextArea(5, 5);
        addCutCopyPasteInterceptor(ta, (type, text) ->
                System.out.printf("action type: %s, action text: %s %n", type, text));
        JFrame frame = createFrame("Intercepting cut copy paste");
        frame.add(new JScrollPane(ta));
        frame.setVisible(true);
    }

    public static JFrame createFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 700));
        return frame;
    }
}
