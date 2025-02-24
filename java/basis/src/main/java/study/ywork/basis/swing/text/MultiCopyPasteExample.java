package study.ywork.basis.swing.text;

import study.ywork.basis.exception.StudyException;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class MultiCopyPasteExample {
    public static void main(String[] args) {
        JTextArea ta = new JTextArea(5, 5);
        JTextField tf = new JTextField();
        JTextPane tp = new JTextPane();
        ta.setText("mark torch even owl young ");
        tf.setText("Money is the root of all money");
        tp.setText("skip atmosphere screw shot grounds X-ray foot extreme confidence");
        PasteFromClipboardHistorySupport copyPasteSupport = new PasteFromClipboardHistorySupport();
        copyPasteSupport.register(ta, tf, tp);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(300);
        splitPane.setLeftComponent(new JScrollPane(ta));
        splitPane.setRightComponent(new JScrollPane(tp));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(copyPasteSupport.getEditMenu());

        JFrame frame = createFrame("Paste from clipboard history");
        frame.setJMenuBar(menuBar);
        frame.add(tf, BorderLayout.NORTH);
        frame.add(splitPane);
        frame.setVisible(true);
    }

    public static JFrame createFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 300));
        return frame;
    }

    private static class PasteFromClipboardHistorySupport {
        private static int clipboardHistorySetSize = 20;
        private LinkedHashSet<String> clipboardSet = new LinkedHashSet<>(clipboardHistorySetSize);
        private JPopupMenu popupMenu;
        private JMenu editMenu;
        private TextAction cutAction;
        private TextAction copyAction;
        private TextAction pasteAction;
        private TextAction pasteHistoryAction;

        public PasteFromClipboardHistorySupport() {
            init();
        }

        private void init() {
            editMenu = new JMenu("Edit");
            popupMenu = new JPopupMenu();
            this.cutAction = newTextAction(this::onCutAction, "Cut", KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK);
            this.copyAction = newTextAction(this::onCopyAction, "Copy", KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
            this.pasteAction = newTextAction(this::onPasteAction, "Paste", KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);
            this.pasteHistoryAction = newTextAction(this::onPasteHistoryAction, "Paste From History", KeyEvent.VK_V,
                    InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK);

            popupMenu.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    pasteHistoryAction.setEnabled(!clipboardSet.isEmpty());
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    // NOTHING HERE
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    // NOTHING HERE
                }
            });
        }

        private TextAction newTextAction(Consumer<JTextComponent> textEventConsumer, String name, int key, int mask) {
            TextAction ta = new TextAction(name) {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    textEventConsumer.accept(super.getFocusedComponent());
                }
            };
            ta.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(key, mask));
            popupMenu.add(new JMenuItem(ta));
            editMenu.add(new JMenuItem(ta));
            return ta;
        }

        private void onPasteAction(JTextComponent tc) {
            String s = getSystemClipboardContent();
            if (s == null) {
                return;
            }

            tc.paste();
            CollectionUtil.addToCollectionWithLimit(clipboardSet, s, clipboardHistorySetSize);
        }

        private void onCopyAction(JTextComponent tc) {
            String s = tc.getSelectedText();
            if (s == null) {
                return;
            }

            tc.copy();
            CollectionUtil.addToCollectionWithLimit(clipboardSet, s, clipboardHistorySetSize);
        }

        private void onCutAction(JTextComponent tc) {
            String s = tc.getSelectedText();
            if (s == null) {
                return;
            }

            tc.cut();
            CollectionUtil.addToCollectionWithLimit(clipboardSet, s, clipboardHistorySetSize);
        }

        private void onPasteHistoryAction(JTextComponent tc) {
            showPasteHistoryPopup(tc);
        }

        public void register(JTextComponent... textComponents) {
            if (textComponents == null) {
                return;
            }

            for (JTextComponent tc : textComponents) {
                register(tc, cutAction, KeyEvent.VK_X);
                register(tc, copyAction, KeyEvent.VK_C);
                register(tc, pasteAction, KeyEvent.VK_V);
                tc.setComponentPopupMenu(popupMenu);
            }
        }

        private void register(JTextComponent tc, TextAction action, int key) {
            tc.registerKeyboardAction(action::actionPerformed, KeyStroke.getKeyStroke(key, InputEvent.CTRL_DOWN_MASK),
                    JComponent.WHEN_FOCUSED);
        }

        public JMenu getEditMenu() {
            return editMenu;
        }

        private void showPasteHistoryPopup(JTextComponent tc) {
            JPopupMenu historyPopup = new JPopupMenu();
            historyPopup.setBackground(Color.WHITE);
            historyPopup.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            JLabel label = new JLabel("Clipboard History");
            label.setForeground(Color.BLUE);
            historyPopup.add(label);

            Iterator<String> itr = new LinkedList<>(clipboardSet).descendingIterator();
            while (itr.hasNext()) {
                String s = itr.next();
                JMenuItem item = new JMenuItem(
                        s.length() > 60 ? s.substring(0, 30) + " .... " + s.substring(s.length() - 30) : s);
                item.setBackground(Color.WHITE);
                historyPopup.add(item);
                item.addActionListener(ae -> {
                    try {
                        tc.getDocument().insertString(tc.getCaretPosition(), s, null);
                    } catch (BadLocationException e) {
                        System.err.println(e);
                    }
                });
            }

            Point p = tc.getCaret().getMagicCaretPosition();
            historyPopup.show(tc, p.x, p.y);
        }

        public static String getSystemClipboardContent() {
            try {
                return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                System.out.println(e.getMessage());
                throw new StudyException(e);
            }
        }
    }

    private static class CollectionUtil {
        public static <T, C extends Collection<T>> void addToCollectionWithLimit(C c, T itemToAdd, int limit) {
            List<T> list = new ArrayList<>(c);
            list.add(itemToAdd);
            while (list.size() > limit) {
                list.remove(0);
            }
            c.clear();
            c.addAll(list);
        }
    }
}
