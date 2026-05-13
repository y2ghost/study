package study.ywork.basis.swing.table;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class HighLightFilterExample {
    public static void main(String[] args) {
        JFrame frame = createFrame();
        TableModel tableModel = createTableModel();
        JTable table = new JTable(tableModel);
        JTextField filterField = RowFilterUtil.createRowFilter(table);
        RendererHighlighted renderer = new RendererHighlighted(filterField);
        table.setDefaultRenderer(Object.class, renderer);
        JPanel jp = new JPanel();
        jp.add(filterField);
        frame.add(jp, BorderLayout.NORTH);

        JScrollPane pane = new JScrollPane(table);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(pane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static TableModel createTableModel() {
        Vector<String> columns = new Vector<>(Arrays.asList("Name", "Address", "Age"));
        Vector<Vector<Object>> rows = new Vector<>();

        for (int i = 1; i <= 30; i++) {
            Vector<Object> v = new Vector<>();
            v.add("name" + i);
            v.add("address" + i);
            v.add(18);
            rows.add(v);
        }

        return new DefaultTableModel(rows, columns);
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JTable Row filter example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 450));
        return frame;
    }

    private static class RowFilterUtil {
        public static JTextField createRowFilter(JTable table) {
            RowSorter<? extends TableModel> rs = table.getRowSorter();
            if (rs == null) {
                table.setAutoCreateRowSorter(true);
                rs = table.getRowSorter();
            }

            TableRowSorter<? extends TableModel> rowSorter = (rs instanceof TableRowSorter)
                    ? (TableRowSorter<? extends TableModel>) rs
                    : null;

            if (rowSorter == null) {
                throw new NullPointerException("Cannot find appropriate rowSorter: " + rs);
            }

            final JTextField tf = new JTextField(15);
            tf.getDocument().addDocumentListener(new DocumentListener() {
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
                    String text = tf.getText();
                    if (text.trim().length() == 0) {
                        rowSorter.setRowFilter(null);
                    } else {
                        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    }
                }
            });

            return tf;
        }
    }

    private static class LabelHighlighted extends JLabel {
        private static final long serialVersionUID = 1L;
        private transient List<Rectangle2D> rectangles = new ArrayList<>();
        private Color colorHighlight = Color.YELLOW;

        public void reset() {
            rectangles.clear();
            repaint();
        }

        public void highlightText(String textToHighlight) {
            if (textToHighlight == null) {
                return;
            }

            reset();
            final String textToMatch = textToHighlight.toLowerCase().trim();

            if (textToMatch.length() == 0) {
                return;
            }

            textToHighlight = textToHighlight.trim();
            final String labelText = getText().toLowerCase();

            if (labelText.contains(textToMatch)) {
                FontMetrics fm = getFontMetrics(getFont());
                float w = -1;
                final float h = (float) fm.getHeight() - 1;
                int i = 0;

                while (true) {
                    i = labelText.indexOf(textToMatch, i);
                    if (i == -1) {
                        break;
                    }

                    if (w == -1) {
                        String matchingText = getText().substring(i, i + textToHighlight.length());
                        w = fm.stringWidth(matchingText);
                    }

                    String preText = getText().substring(0, i);
                    float x = fm.stringWidth(preText);
                    rectangles.add(new Rectangle2D.Float(x, 1, w, h));
                    i = i + textToMatch.length();
                }

                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            if (!rectangles.isEmpty()) {
                Graphics2D g2d = (Graphics2D) g;
                Color c = g2d.getColor();
                for (Rectangle2D rectangle : rectangles) {
                    g2d.setColor(colorHighlight);
                    g2d.fill(rectangle);
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.draw(rectangle);
                }
                g2d.setColor(c);
            }

            super.paintComponent(g);
        }
    }

    private static class RendererHighlighted extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        private JTextField searchField;

        public RendererHighlighted(JTextField searchField) {
            this.searchField = searchField;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, selected, hasFocus, row, column);
            JLabel original = (JLabel) c;
            LabelHighlighted label = new LabelHighlighted();
            label.setFont(original.getFont());
            label.setText(original.getText());
            label.setBackground(original.getBackground());
            label.setForeground(original.getForeground());
            label.setHorizontalTextPosition(original.getHorizontalTextPosition());
            label.highlightText(searchField.getText());
            return label;
        }
    }

}
