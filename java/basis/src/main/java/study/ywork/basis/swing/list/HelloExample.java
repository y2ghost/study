package study.ywork.basis.swing.list;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class HelloExample {
    public static void main(String[] args) {
        List<Employee> employees = Employee.getTestEmployees();
        JList<Employee> jList = new JList<>(employees.toArray(new Employee[employees.size()]));
        jList.setCellRenderer(createListRenderer());
        jList.addListSelectionListener(createListSelectionListener(jList));
        JScrollPane pane = new JScrollPane(jList);

        JFrame frame = createFrame();
        frame.add(pane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static ListSelectionListener createListSelectionListener(JList<Employee> list) {
        return e -> {
            if (!e.getValueIsAdjusting()) {
                System.out.println(list.getSelectedValue());
            }
        };
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

            for (int i = 1; i <= 30; i++) {
                Employee e = new Employee();
                e.setName("testName");
                e.setAddress("testAddress");
                e.setDept("IT");
                e.setPhone("11111111111");
                list.add(e);
            }

            return list;
        }
    }
}
