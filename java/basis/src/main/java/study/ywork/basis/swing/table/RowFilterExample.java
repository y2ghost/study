package study.ywork.basis.swing.table;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class RowFilterExample {
    public static void main(String[] args) {
        JFrame frame = createFrame();
        TableModel tableModel = createTableModel();
        JTable table = new JTable(tableModel);

        JTextField filterField = RowFilterUtil.createRowFilter(table);
        JPanel jp = new JPanel();
        jp.add(filterField);
        frame.add(jp, BorderLayout.NORTH);

        JScrollPane pane = new JScrollPane(table);
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

        return new DefaultTableModel(rows, columns) {
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Integer.class : String.class;
            }
        };
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

}
