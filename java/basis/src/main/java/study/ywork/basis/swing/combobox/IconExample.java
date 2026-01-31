package study.ywork.basis.swing.combobox;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/*
 * ICON图片添加显示例子
 */
public class IconExample {
    public static void main(String[] args) {
        List<Employee> employees = Employee.getTestEmployees();
        JComboBox<Employee> comboBox = new JComboBox<>(employees.toArray(new Employee[employees.size()]));
        comboBox.setRenderer(new ExampleRenderer());
        JPanel panel = new JPanel();
        panel.add(comboBox);
        JFrame frame = createFrame();
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JComboBox Example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 300));
        return frame;
    }

    private static class ExampleRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;
        private Map<String, ImageIcon> iconMap = new HashMap<>();
        private Color bgcolor = new Color(0, 100, 255, 15);
        private Color defaultBgcolor = (Color) UIManager.get("List.background");

        public ExampleRenderer() {
            iconMap.put("Account", new ImageIcon(getClass().getResource("/images/account.png")));
            iconMap.put("Sales", new ImageIcon(getClass().getResource("/images/sales.png")));
            iconMap.put("IT", new ImageIcon(getClass().getResource("/images/it.png")));
            iconMap.put("Admin", new ImageIcon(getClass().getResource("/images/admin.png")));
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Employee emp = (Employee) value;
            this.setText(emp.getName());
            this.setIcon(iconMap.get(emp.getDept()));
            if (!isSelected) {
                this.setBackground(index % 2 == 0 ? bgcolor : defaultBgcolor);
            }
            return this;
        }
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
