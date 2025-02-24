package study.ywork.basis.swing.combobox;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/*
 * 显示搜索文本例子
 */
public class BoxFilterExample {
    public static void main(String[] args) {
        List<Employee> employees = Employee.getTestEmployees();
        JComboBox<Employee> comboBox = new JComboBox<>(employees.toArray(new Employee[employees.size()]));
        ComboBoxFilterDecorator<Employee> decorate = ComboBoxFilterDecorator.decorate(comboBox,
                CustomComboRenderer::getEmployeeDisplayText, BoxFilterExample::employeeFilter);
        comboBox.setRenderer(new CustomComboRenderer(decorate.getFilterTextSupplier()));
        JPanel panel = new JPanel();
        panel.add(comboBox);
        JFrame frame = createFrame();
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static boolean employeeFilter(Employee emp, String textToFilter) {
        if (textToFilter.isEmpty()) {
            return true;
        }

        return CustomComboRenderer.getEmployeeDisplayText(emp).toLowerCase().contains(textToFilter.toLowerCase());
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JComboBox Filter Example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 300));
        return frame;
    }

    private static class HtmlHighlighter {
        private static final String HIGHLIGHT_TEMPLATE = "<span style='background:yellow;'>$1</span>";

        public static String highlightText(String text, String textToHighlight) {
            if (textToHighlight.length() == 0) {
                return text;
            }

            try {
                text = text.replaceAll("(?i)(" + Pattern.quote(textToHighlight) + ")", HIGHLIGHT_TEMPLATE);
            } catch (Exception e) {
                return text;
            }
            return "<html>" + text + "</html>";
        }
    }

    private static class FilterEditor<T> extends BasicComboBoxEditor {
        private final JLabel filterLabel = new JLabel();
        private String text = "";
        boolean editing;
        private Function<T, String> displayTextFunction;
        private Consumer<Boolean> editingChangeListener;
        private Object selected;

        FilterEditor(Function<T, String> displayTextFunction, Consumer<Boolean> editingChangeListener) {
            this.displayTextFunction = displayTextFunction;
            this.editingChangeListener = editingChangeListener;
        }

        public void addChar(char c) {
            text += c;
            if (!editing) {
                enableEditingMode();
            }
        }

        public void removeCharAtEnd() {
            if (text.length() > 0) {
                text = text.substring(0, text.length() - 1);
                if (!editing) {
                    enableEditingMode();
                }
            }
        }

        private void enableEditingMode() {
            editing = true;
            filterLabel.setFont(filterLabel.getFont().deriveFont(Font.PLAIN));
            editingChangeListener.accept(true);
        }

        public void reset() {
            if (editing) {
                filterLabel.setFont(UIManager.getFont("ComboBox.font"));
                filterLabel.setForeground(UIManager.getColor("Label.foreground"));
                text = "";
                editing = false;
                editingChangeListener.accept(false);
            }
        }

        @Override
        public Component getEditorComponent() {
            return getFilterLabel();
        }

        public JLabel getFilterLabel() {
            return filterLabel;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void setItem(Object anObject) {
            if (editing) {
                filterLabel.setText(text);
            } else {
                T t = (T) anObject;
                filterLabel.setText(displayTextFunction.apply(t));
            }
            this.selected = anObject;
        }

        @Override
        public Object getItem() {
            return selected;
        }

        @Override
        public void selectAll() {
            // 不做事
        }

        @Override
        public void addActionListener(ActionListener l) {
            // 不做事
        }

        @Override
        public void removeActionListener(ActionListener l) {
            // 不做事
        }

        public boolean isEditing() {
            return editing;
        }

        public String getText() {
            return text;
        }
    }

    private static class CustomComboRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;
        public static final Color backgroundColor = new Color(225, 240, 255);
        private static final Color defaultBackground = (Color) UIManager.get("List.background");
        private static final Color defaultForeground = (Color) UIManager.get("List.foreground");
        private transient Supplier<String> highlightTextSupplier;

        public CustomComboRenderer(Supplier<String> highlightTextSupplier) {
            this.highlightTextSupplier = highlightTextSupplier;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Employee emp = (Employee) value;
            if (emp == null) {
                return this;
            }
            String text = getEmployeeDisplayText(emp);
            text = HtmlHighlighter.highlightText(text, highlightTextSupplier.get());
            this.setText(text);
            if (!isSelected) {
                this.setBackground(index % 2 == 0 ? backgroundColor : defaultBackground);
            }
            this.setForeground(defaultForeground);
            return this;
        }

        public static String getEmployeeDisplayText(Employee emp) {
            if (emp == null) {
                return "";
            }
            return String.format("%s [%s]", emp.getName(), emp.getDept());
        }
    }

    private static class ComboBoxFilterDecorator<T> {
        private JComboBox<T> comboBox;
        private BiPredicate<T, String> userFilter;
        private Function<T, String> comboDisplayTextMapper;
        java.util.List<T> originalItems;
        private Object selectedItem;
        private FilterEditor<T> filterEditor;

        public ComboBoxFilterDecorator(JComboBox<T> comboBox, BiPredicate<T, String> userFilter,
                                       Function<T, String> comboDisplayTextMapper) {
            this.comboBox = comboBox;
            this.userFilter = userFilter;
            this.comboDisplayTextMapper = comboDisplayTextMapper;
        }

        public static <T> ComboBoxFilterDecorator<T> decorate(JComboBox<T> comboBox,
                                                              Function<T, String> comboDisplayTextMapper, BiPredicate<T, String> userFilter) {
            ComboBoxFilterDecorator<T> decorator = new ComboBoxFilterDecorator<>(comboBox, userFilter,
                    comboDisplayTextMapper);
            decorator.init();
            return decorator;
        }

        private void init() {
            prepareComboFiltering();
            initComboPopupListener();
            initComboKeyListener();
        }

        private void prepareComboFiltering() {
            DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) comboBox.getModel();
            this.originalItems = new ArrayList<>();
            for (int i = 0; i < model.getSize(); i++) {
                this.originalItems.add(model.getElementAt(i));
            }

            filterEditor = new FilterEditor<>(comboDisplayTextMapper, aBoolean -> {
                if (Boolean.TRUE.equals(aBoolean)) {
                    selectedItem = comboBox.getSelectedItem();
                } else {
                    comboBox.setSelectedItem(selectedItem);
                    filterEditor.setItem(selectedItem);
                }
            });

            JLabel filterLabel = filterEditor.getFilterLabel();
            filterLabel.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    filterLabel.setBorder(BorderFactory.createLoweredBevelBorder());
                }

                @Override
                public void focusLost(FocusEvent e) {
                    filterLabel.setBorder(UIManager.getBorder("TextField.border"));
                    resetFilterComponent();
                }
            });
            comboBox.setEditor(filterEditor);
            comboBox.setEditable(true);
        }

        private void initComboKeyListener() {
            filterEditor.getFilterLabel().addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    char keyChar = e.getKeyChar();
                    if (!Character.isDefined(keyChar)) {
                        return;
                    }
                    int keyCode = e.getKeyCode();
                    switch (keyCode) {
                        case KeyEvent.VK_DELETE:
                            return;
                        case KeyEvent.VK_ENTER:
                            selectedItem = comboBox.getSelectedItem();
                            resetFilterComponent();
                            return;
                        case KeyEvent.VK_ESCAPE:
                            resetFilterComponent();
                            return;
                        case KeyEvent.VK_BACK_SPACE:
                            filterEditor.removeCharAtEnd();
                            break;
                        default:
                            filterEditor.addChar(keyChar);
                    }
                    if (!comboBox.isPopupVisible()) {
                        comboBox.showPopup();
                    }
                    if (filterEditor.isEditing() && filterEditor.getText().length() > 0) {
                        applyFilter();
                    } else {
                        comboBox.hidePopup();
                        resetFilterComponent();
                    }
                }
            });
        }

        public Supplier<String> getFilterTextSupplier() {
            return () -> {
                if (filterEditor.isEditing()) {
                    return filterEditor.getFilterLabel().getText();
                }
                return "";
            };
        }

        private void initComboPopupListener() {
            comboBox.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    // 不做事
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    resetFilterComponent();
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    resetFilterComponent();
                }
            });
        }

        private void resetFilterComponent() {
            if (!filterEditor.isEditing()) {
                return;
            }

            DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) comboBox.getModel();
            model.removeAllElements();

            for (T item : originalItems) {
                model.addElement(item);
            }

            filterEditor.reset();
        }

        private void applyFilter() {
            DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) comboBox.getModel();
            model.removeAllElements();
            java.util.List<T> filteredItems = new ArrayList<>();

            for (T item : originalItems) {
                if (userFilter.test(item, filterEditor.getFilterLabel().getText())) {
                    model.addElement(item);
                } else {
                    filteredItems.add(item);
                }
            }

            filterEditor.getFilterLabel()
                    .setForeground(model.getSize() == 0 ? Color.red : UIManager.getColor("Label.foreground"));
            filteredItems.forEach(model::addElement);
        }
    }

    @SuppressWarnings("unused")
    private static class Employee {
        private String name;
        private String dept;
        private String phone;
        private String address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDept() {
            return dept;
        }

        public void setDept(String dept) {
            this.dept = dept;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        @Override
        public String toString() {
            return "Employee{" + "name='" + name + '\'' + ", dept='" + dept + '\'' + ", phone='" + phone + '\''
                    + ", address='" + address + '\'' + '}';
        }

        public static List<Employee> getTestEmployees() {
            List<Employee> list = new ArrayList<>();
            final String[] depts = {"IT", "Account", "Admin", "Sales"};
            final String[] names = {"yyyy", "ttjy", "yxyx", "jytt"};

            for (int i = 1; i <= 30; i++) {
                Employee e = new Employee();
                e.setName(names[i % 4]);
                e.setAddress("testAddress");
                e.setDept(depts[i % 4]);
                e.setPhone("11111111111");
                list.add(e);
            }

            return list;
        }
    }

}
