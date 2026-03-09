package study.ywork.basis.swing.combobox;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class HelloExample {
    public static void main(String[] args) {
        List<Employee> employees = Employee.getTestEmployees();
        JComboBox<Employee> comboBox = new JComboBox<>(employees.toArray(new Employee[employees.size()]));
        comboBox.setRenderer(createListRenderer());
        comboBox.addItemListener(createItemListener());
        comboBox.addActionListener(createActionListener());
        JPanel panel = new JPanel();
        panel.add(comboBox);
        JFrame frame = createFrame();
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    private static ActionListener createActionListener() {
        return e -> {
            JComboBox<Employee> comboBox = (JComboBox<Employee>) e.getSource();
            System.out.println("ActionPerformed: " + comboBox.getSelectedItem());
        };
    }

    private static ItemListener createItemListener() {
        return e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                System.out.println("Selected: " + e.getItem());
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                System.out.println("DeSelected: " + e.getItem());
            } else {
                System.out.println("Other StateChange: " + e.getStateChange());
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
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                Employee emp = (Employee) value;
                this.setText(String.format("%s [%s]", emp.getName(), emp.getDept()));
                if (!isSelected) {
                    this.setBackground(index % 2 == 0 ? background : defaultBackground);
                }
                return this;
            }
        };
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JComboBox Example");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
            final String[] names = {"yyyy", "ttjy", "yxyx", "jytt"};

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

}
