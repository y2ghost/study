package study.ywork.basis.swing.text;

import study.ywork.basis.swing.RandomUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class SuggestionExample {
    public static void main(String[] args) {
        JFrame frame = createFrame();
        JTextField textField = new JTextField(10);
        SuggestionDropDownDecorator.decorate(textField,
                new TextComponentSuggestionClient(SuggestionExample::getSuggestions));
        JTextPane textPane = new JTextPane();
        SuggestionDropDownDecorator.decorate(textPane,
                new TextComponentWordSuggestionClient(SuggestionExample::getSuggestions));
        frame.add(textField, BorderLayout.NORTH);
        frame.add(new JScrollPane(textPane));
        frame.setVisible(true);
    }

    private static List<String> words = RandomUtil.getWords(2, 400)
            .stream()
            .map(String::toLowerCase)
            .collect(Collectors.toList());

    private static List<String> getSuggestions(String input) {
        if (input.isEmpty()) {
            return Collections.emptyList();
        }
        return words.stream().filter(s -> s.startsWith(input)).limit(20).toList();
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("Suggestion Dropdown Example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 300));
        return frame;
    }

    private static interface SuggestionClient<C extends JComponent> {

        Point getPopupLocation(C invoker);

        void setSelectedText(C invoker, String selectedValue);

        List<String> getSuggestions(C invoker);

    }

    private static class SuggestionDropDownDecorator<C extends JComponent> {
        private final C invoker;
        private final SuggestionClient<C> suggestionClient;
        private JPopupMenu popupMenu;
        private JList<String> listComp;
        DefaultListModel<String> listModel;
        private boolean disableTextEvent;

        public SuggestionDropDownDecorator(C invoker, SuggestionClient<C> suggestionClient) {
            this.invoker = invoker;
            this.suggestionClient = suggestionClient;
        }

        public static <C extends JComponent> void decorate(C component, SuggestionClient<C> suggestionClient) {
            SuggestionDropDownDecorator<C> d = new SuggestionDropDownDecorator<>(component, suggestionClient);
            d.init();
        }

        public void init() {
            initPopup();
            initSuggestionCompListener();
            initInvokerKeyListeners();
        }

        private void initPopup() {
            popupMenu = new JPopupMenu();
            listModel = new DefaultListModel<>();
            listComp = new JList<>(listModel);
            listComp.setBorder(BorderFactory.createEmptyBorder(0, 2, 5, 2));
            listComp.setFocusable(false);
            popupMenu.setFocusable(false);
            popupMenu.add(listComp);
        }

        private void initSuggestionCompListener() {
            if (invoker instanceof JTextComponent tc) {
                tc.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        update(e);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        update(e);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        update(e);
                    }

                    private void update(DocumentEvent e) {
                        if (disableTextEvent) {
                            return;
                        }
                        SwingUtilities.invokeLater(() -> {
                            List<String> suggestions = suggestionClient.getSuggestions(invoker);
                            if (suggestions != null && !suggestions.isEmpty()) {
                                showPopup(suggestions);
                            } else {
                                popupMenu.setVisible(false);
                            }
                        });
                    }
                });
            }
        }

        private void showPopup(List<String> suggestions) {
            listModel.clear();
            suggestions.forEach(listModel::addElement);
            Point p = suggestionClient.getPopupLocation(invoker);
            if (p == null) {
                return;
            }
            popupMenu.pack();
            listComp.setSelectedIndex(0);
            popupMenu.show(invoker, (int) p.getX(), (int) p.getY());
        }

        private void initInvokerKeyListeners() {
            invoker.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        selectFromList(e);
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        moveUp(e);
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        moveDown(e);
                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        popupMenu.setVisible(false);
                    }
                }
            });
        }

        private void selectFromList(KeyEvent e) {
            if (popupMenu.isVisible()) {
                int selectedIndex = listComp.getSelectedIndex();
                if (selectedIndex != -1) {
                    popupMenu.setVisible(false);
                    String selectedValue = listComp.getSelectedValue();
                    disableTextEvent = true;
                    suggestionClient.setSelectedText(invoker, selectedValue);
                    disableTextEvent = false;
                    e.consume();
                }
            }
        }

        private void moveDown(KeyEvent keyEvent) {
            if (popupMenu.isVisible() && listModel.getSize() > 0) {
                int selectedIndex = listComp.getSelectedIndex();
                if (selectedIndex < listModel.getSize()) {
                    listComp.setSelectedIndex(selectedIndex + 1);
                    keyEvent.consume();
                }
            }
        }

        private void moveUp(KeyEvent keyEvent) {
            if (popupMenu.isVisible() && listModel.getSize() > 0) {
                int selectedIndex = listComp.getSelectedIndex();
                if (selectedIndex > 0) {
                    listComp.setSelectedIndex(selectedIndex - 1);
                    keyEvent.consume();
                }
            }
        }
    }

    private static class TextComponentSuggestionClient implements SuggestionClient<JTextComponent> {
        private Function<String, List<String>> suggestionProvider;

        public TextComponentSuggestionClient(Function<String, List<String>> suggestionProvider) {
            this.suggestionProvider = suggestionProvider;
        }

        @Override
        public Point getPopupLocation(JTextComponent invoker) {
            return new Point(0, invoker.getPreferredSize().height);
        }

        @Override
        public void setSelectedText(JTextComponent invoker, String selectedValue) {
            invoker.setText(selectedValue);
        }

        @Override
        public List<String> getSuggestions(JTextComponent invoker) {
            return suggestionProvider.apply(invoker.getText().trim());
        }
    }

    private static class TextComponentWordSuggestionClient implements SuggestionClient<JTextComponent> {
        private Function<String, List<String>> suggestionProvider;

        public TextComponentWordSuggestionClient(Function<String, List<String>> suggestionProvider) {
            this.suggestionProvider = suggestionProvider;
        }

        @Override
        public Point getPopupLocation(JTextComponent invoker) {
            int caretPosition = invoker.getCaretPosition();
            try {
                Rectangle2D rectangle2D = invoker.modelToView2D(caretPosition);
                return new Point((int) rectangle2D.getX(), (int) (rectangle2D.getY() + rectangle2D.getHeight()));
            } catch (BadLocationException e) {
                System.err.println(e);
            }
            return null;
        }

        @Override
        public void setSelectedText(JTextComponent tp, String selectedValue) {
            int cp = tp.getCaretPosition();
            try {
                if (cp == 0 || tp.getText(cp - 1, 1).trim().isEmpty()) {
                    tp.getDocument().insertString(cp, selectedValue, null);
                } else {
                    int previousWordIndex = Utilities.getPreviousWord(tp, cp);
                    String text = tp.getText(previousWordIndex, cp - previousWordIndex);
                    if (selectedValue.startsWith(text)) {
                        tp.getDocument().insertString(cp, selectedValue.substring(text.length()), null);
                    } else {
                        tp.getDocument().insertString(cp, selectedValue, null);
                    }
                }
            } catch (BadLocationException e) {
                System.err.println(e);
            }
        }

        @Override
        public List<String> getSuggestions(JTextComponent tp) {
            try {
                int cp = tp.getCaretPosition();
                if (cp != 0) {
                    String text = tp.getText(cp - 1, 1);
                    if (text.trim().isEmpty()) {
                        return Collections.emptyList();
                    }
                }
                int previousWordIndex = Utilities.getPreviousWord(tp, cp);
                String text = tp.getText(previousWordIndex, cp - previousWordIndex);
                return suggestionProvider.apply(text.trim());
            } catch (BadLocationException e) {
                System.err.println(e);
            }
            return Collections.emptyList();
        }
    }

}
