package study.ywork.basis.swing.table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TableAsEditable {
    public static void main(String[] args) {
        TableAsEditableList table = new TableAsEditableList();
        JLabel listDisplayLabel = new JLabel();
        initDisplayLabel(table, listDisplayLabel);
        JFrame frame = createFrame();
        frame.add(new JScrollPane(table));
        frame.add(listDisplayLabel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void initDisplayLabel(TableAsEditableList table, JLabel displayLabel) {
        table.getModel().addTableModelListener(event -> {
            List<String> list = table.getDataAsStringList();
            displayLabel.setText("List: " + list);
        });
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JTable As Editable List Example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 400));
        return frame;
    }

    private static class TableAsEditableList extends JTable {
        private static final long serialVersionUID = 1L;
        private DefaultCellEditor editor;
        private final DefaultTableModel model;
        private JTextField field;

        public TableAsEditableList() {
            super(1, 1);
            this.model = (DefaultTableModel) getModel();
            init();
        }

        private void init() {
            initTable();
            initEditorComponent();
            initSelectionListener();
            initKeyListeners();
        }

        private void initTable() {
            setTableHeader(null);
        }

        private void initEditorComponent() {
            TableColumn column = getColumnModel().getColumn(0);
            field = new JTextField();
            this.editor = new DefaultCellEditor(field);
            editor.setClickCountToStart(1);
            column.setCellEditor(editor);
            field.setBorder(null);
            field.setForeground(new Color(0, 100, 250));
        }

        private void initSelectionListener() {
            getSelectionModel().addListSelectionListener(e -> {
                int selectedRow = getSelectedRow();
                if (selectedRow == -1) {
                    return;
                }
                startEditingAtRow(selectedRow);
            });
        }

        private void initKeyListeners() {
            Action insertRowAfterCurrentRowAction = createEnterKeyAction();
            bindKeyAction(insertRowAfterCurrentRowAction, KeyEvent.VK_ENTER, 0, this, field);
            Action insertRowBeforeCurrentRowAction = createAltEnterKeyAction();
            bindKeyAction(insertRowBeforeCurrentRowAction, KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK, this, field);
            field.addKeyListener(createDelKeyListener());
        }

        private void bindKeyAction(Action action, int keyCode, int acceleratorKey, JComponent... components) {
            for (JComponent component : components) {
                KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, acceleratorKey);
                component.registerKeyboardAction(action, keyStroke, JComponent.WHEN_FOCUSED);
            }
        }

        private Action createEnterKeyAction() {
            return new AbstractAction() {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    final int selectedRow = getSelectedRow();
                    if (selectedRow == -1) {
                        return;
                    }
                    insertNewRow(selectedRow + 1);
                    selectRow(selectedRow + 1);
                }
            };
        }

        private Action createAltEnterKeyAction() {
            return new AbstractAction() {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = getSelectedRow();
                    if (selectedRow == -1) {
                        return;
                    }
                    editor.stopCellEditing();
                    removeEditor();
                    insertNewRow(selectedRow);
                }
            };
        }

        private KeyListener createDelKeyListener() {
            return new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int selectedRow = getSelectedRow();
                    if (selectedRow == -1 || getRowCount() == 1) {
                        return;
                    }
                    boolean emptyField = field.getText().trim().length() == 0;
                    boolean del = e.getKeyCode() == KeyEvent.VK_DELETE;
                    boolean bsp = e.getKeyCode() == KeyEvent.VK_BACK_SPACE;
                    if ((del || bsp) && emptyField) {
                        removeRow(selectedRow);
                        int newSel = del ? selectedRow : selectedRow - 1;
                        if (newSel == -1) {
                            newSel = 0;
                            del = true;
                        }
                        if (newSel > getRowCount() - 1) {
                            newSel = getRowCount() - 1;
                            del = false;
                        }
                        selectRow(newSel);
                        e.consume();
                        if (del) {
                            field.setCaretPosition(0);
                        } else {
                            field.setCaretPosition(field.getDocument().getLength());
                        }
                    }
                }
            };
        }

        private void selectRow(int selectedRow) {
            setRowSelectionInterval(selectedRow, selectedRow);
        }

        private void startEditingAtRow(int row) {
            if (isCellEditable(row, 0)) {
                editCellAt(row, 0);
                changeSelection(row, 0, false, false);
                field.requestFocusInWindow();
            }
        }

        private void insertNewRow(int newRowIndex) {
            model.insertRow(newRowIndex, new String[]{""});
        }

        private void removeRow(int selectedRow) {
            editor.stopCellEditing();
            removeEditor();
            model.removeRow(selectedRow);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public List<String> getDataAsStringList() {
            Vector<Vector> vectors = model.getDataVector();
            return vectors.stream()
                    .filter(Objects::nonNull)
                    .map(vector -> vector.stream().findAny().orElse(null))
                    .filter(Objects::nonNull)
                    .map(Objects::toString)
                    .filter(s -> !s.isEmpty())
                    .toList();
        }
    }
}
