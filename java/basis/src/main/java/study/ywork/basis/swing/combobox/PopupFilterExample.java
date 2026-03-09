package study.ywork.basis.swing.combobox;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/*
 * 显示搜索文本例子，搜索输入弹出POPUP
 */
public class PopupFilterExample {
    public static void main(String[] args) {
        List<Employee> employees = Employee.getTestEmployees();
        JComboBox<Employee> comboBox = new JComboBox<>(employees.toArray(new Employee[employees.size()]));
        ComboBoxFilterDecorator<Employee> decorate = ComboBoxFilterDecorator.decorate(comboBox,
                PopupFilterExample::employeeFilter);
        comboBox.setRenderer(new CustomComboRenderer(decorate.getFilterLabel()));

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

    private static class CustomComboRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;
        public static final Color backgroundColor = new Color(250, 250, 255);
        private static final Color defaultBackground = (Color) UIManager.get("List.background");
        private JLabel searchLabel;

        public CustomComboRenderer(JLabel filterLabel) {
            this.searchLabel = filterLabel;
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
            text = HtmlHighlighter.highlightText(text, searchLabel.getText());
            this.setText(text);
            if (!isSelected) {
                this.setBackground(index % 2 == 0 ? backgroundColor : defaultBackground);
            }
            return this;
        }

        public static String getEmployeeDisplayText(Employee emp) {
            return String.format("%s [%s]", emp.getName(), emp.getDept());
        }
    }

    private static class ComboBoxFilterDecorator<T> {
        private Popup filterPopup;
        private JLabel filterLabel;
        private JComboBox<T> comboBox;
        private BiPredicate<T, String> userFilter;
        java.util.List<T> items;
        private TextHandler textHandler = new TextHandler();
        private Object selectedItem;

        public ComboBoxFilterDecorator(JComboBox<T> comboBox, BiPredicate<T, String> userFilter) {
            this.comboBox = comboBox;
            this.userFilter = userFilter;
        }

        public static <T> ComboBoxFilterDecorator<T> decorate(JComboBox<T> comboBox,
                                                              BiPredicate<T, String> userFilter) {
            ComboBoxFilterDecorator<T> decorator = new ComboBoxFilterDecorator<>(comboBox, userFilter);
            decorator.init();
            return decorator;
        }

        private void init() {
            prepareComboFiltering();
            initFilterLabel();
            initComboPopupListener();
            initComboKeyListener();
        }

        private void prepareComboFiltering() {
            DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) comboBox.getModel();
            items = new ArrayList<>();
            for (int i = 0; i < model.getSize(); i++) {
                items.add(model.getElementAt(i));
            }
        }

        private void initComboKeyListener() {
            comboBox.addKeyListener(new KeyAdapter() {
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
                            resetFilterPopup();
                            return;
                        case KeyEvent.VK_ESCAPE:
                            if (selectedItem != null) {
                                comboBox.setSelectedItem(selectedItem);
                            }
                            resetFilterPopup();
                            return;
                        case KeyEvent.VK_BACK_SPACE:
                            textHandler.removeCharAtEnd();
                            break;
                        default:
                            textHandler.add(keyChar);
                    }

                    if (!comboBox.isPopupVisible()) {
                        comboBox.showPopup();
                    }

                    if (!textHandler.text.isEmpty()) {
                        showFilterPopup();
                        performFilter();
                    } else {
                        resetFilterPopup();
                    }
                    e.consume();
                }
            });
        }

        private void initFilterLabel() {
            filterLabel = new JLabel();
            filterLabel.setOpaque(true);
            filterLabel.setBackground(new Color(255, 248, 220));
            filterLabel.setFont(filterLabel.getFont().deriveFont(Font.PLAIN));
            filterLabel.setBorder(BorderFactory.createLineBorder(Color.gray));
        }

        public JLabel getFilterLabel() {
            return filterLabel;
        }

        private void initComboPopupListener() {
            comboBox.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    // 不做事
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    resetFilterPopup();
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    resetFilterPopup();
                }
            });
        }

        private void showFilterPopup() {
            if (textHandler.getText().isEmpty()) {
                return;
            }

            if (filterPopup == null) {
                Point p = new Point(0, 0);
                SwingUtilities.convertPointToScreen(p, comboBox);
                Dimension comboSize = comboBox.getPreferredSize();
                filterLabel.setPreferredSize(new Dimension(comboSize));
                filterPopup = PopupFactory.getSharedInstance()
                        .getPopup(comboBox, filterLabel, p.x, p.y - filterLabel.getPreferredSize().height);
                selectedItem = comboBox.getSelectedItem();

            }
            filterPopup.show();
        }

        private void resetFilterPopup() {
            if (!textHandler.isEditing()) {
                return;
            }
            if (filterPopup != null) {
                filterPopup.hide();
                filterPopup = null;
                filterLabel.setText("");
                textHandler.reset();

                Object currentItem = comboBox.getSelectedItem();
                DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) comboBox.getModel();
                model.removeAllElements();
                for (T item : items) {
                    model.addElement(item);
                }

                model.setSelectedItem(currentItem);
                this.selectedItem = currentItem;
            }
        }

        private void performFilter() {
            filterLabel.setText(textHandler.getText());
            DefaultComboBoxModel<T> model = (DefaultComboBoxModel<T>) comboBox.getModel();
            model.removeAllElements();
            java.util.List<T> filteredItems = new ArrayList<>();

            for (T item : items) {
                if (userFilter.test(item, textHandler.getText())) {
                    model.addElement(item);
                } else {
                    filteredItems.add(item);
                }
            }
            if (model.getSize() == 0) {
                filterLabel.setForeground(Color.red);
            } else {
                filterLabel.setForeground(Color.blue);
            }

            filteredItems.forEach(model::addElement);
        }

        private static class TextHandler {
            private String text = "";
            private boolean editing;

            public void add(char c) {
                text += c;
                editing = true;
            }

            public void removeCharAtEnd() {
                if (text.length() > 0) {
                    text = text.substring(0, text.length() - 1);
                    editing = true;
                }
            }

            public void reset() {
                text = "";
                editing = false;
            }

            public String getText() {
                return text;
            }

            public boolean isEditing() {
                return editing;
            }
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
