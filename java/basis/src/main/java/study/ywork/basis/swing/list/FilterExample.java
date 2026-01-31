package study.ywork.basis.swing.list;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class FilterExample {
    public static void main(String[] args) {
        List<Employee> employees = Employee.getTestEmployees();
        DefaultListModel<Employee> model = new DefaultListModel<>();
        employees.forEach(model::addElement);
        JList<Employee> jList = new JList<>(model);
        jList.setCellRenderer(createListRenderer());
        JPanel panel = JListFilterDecorator.decorate(jList, FilterExample::employeeFilter);
        JFrame frame = createFrame();
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static boolean employeeFilter(Employee emp, String str) {
        return emp.getName().toLowerCase().contains(str.toLowerCase());
    }

    private static ListCellRenderer<? super Employee> createListRenderer() {
        return new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;
            private Color background = new Color(0, 100, 255, 15);
            private Color defaultBackground = (Color) UIManager.get("List.background");

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (c instanceof JLabel label) {
                    Employee emp = (Employee) value;
                    label.setText(String.format("%s [%s]", emp.getName(), emp.getDept()));
                    if (!isSelected) {
                        label.setBackground(index % 2 == 0 ? background : defaultBackground);
                    }
                }
                return c;
            }
        };
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JList Example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 300));
        return frame;
    }

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
            final String[] names = {"yy", "tt", "yx", "jy"};

            for (int i = 1; i <= 30; i++) {
                Employee e = new Employee();
                e.setName(names[i % 4]);
                e.setAddress("testAddress");
                e.setDept("IT");
                e.setPhone("11111111111");
                list.add(e);
            }

            return list;
        }
    }

    private static class JListFilterDecorator {
        public static <T> JPanel decorate(JList<T> jList, BiPredicate<T, String> userFilter) {
            if (!(jList.getModel() instanceof DefaultListModel)) {
                throw new IllegalArgumentException("List model must be an instance of DefaultListModel");
            }
            DefaultListModel<T> model = (DefaultListModel<T>) jList.getModel();
            List<T> items = getItems(model);
            JTextField textField = new JTextField();
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    filter();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filter();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filter();
                }

                private void filter() {
                    model.clear();
                    String s = textField.getText();
                    for (T item : items) {
                        if (userFilter.test(item, s)) {
                            model.addElement(item);
                        }
                    }
                }
            });

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(textField, BorderLayout.NORTH);
            JScrollPane pane = new JScrollPane(jList);
            panel.add(pane);
            return panel;
        }

        private static <T> List<T> getItems(DefaultListModel<T> model) {
            List<T> list = new ArrayList<>();
            for (int i = 0; i < model.size(); i++) {
                list.add(model.elementAt(i));
            }
            return list;
        }
    }
}