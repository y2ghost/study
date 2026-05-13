package study.ywork.basis.swing.combobox;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/*
 * 可编辑ComboBox例子
 */
public class EditableExample {
    public static void main(String[] args) {
        JTable table = TableHelper.createTable();
        JComboBox<String> searchComboBox = new JComboBox<>();
        searchComboBox.setEditable(true);
        searchComboBox.addActionListener(createSearchActionListener(searchComboBox, table));

        JPanel panel = new JPanel();
        panel.add(new JLabel("Search"));
        panel.add(searchComboBox);

        JFrame frame = createFrame();
        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(table));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static ActionListener createSearchActionListener(JComboBox<String> comboBox, JTable table) {
        return (e) -> {
            if ("comboBoxChanged".equals(e.getActionCommand())) {
                String searchText = (String) comboBox.getSelectedItem();
                searchText = searchText.trim().toLowerCase();

                if (TableHelper.searchInTable(table, searchText)) {
                    DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBox.getModel();
                    if (!searchText.isEmpty() && model.getIndexOf(searchText) == -1) {
                        model.addElement(searchText);
                    }
                }
            }
        };
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JComboBox Example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 500));
        return frame;
    }

    private static class TableHelper {
        public static JTable createTable() {
            Vector<String> columns = new Vector<>();
            columns.add("Name");
            columns.add("Dept");
            columns.add("Phone");
            columns.add("Address");

            Vector<Vector<String>> rows = new Vector<>();
            for (Employee employee : Employee.getTestEmployees()) {
                Vector<String> row = new Vector<>();
                row.add(employee.getName());
                row.add(employee.getDept());
                row.add(employee.getPhone());
                row.add(employee.getAddress());
                rows.add(row);
            }

            return new JTable(rows, columns);
        }

        public static boolean searchInTable(JTable table, String searchText) {
            if (searchText == null) {
                return false;
            }

            int beforeFilterRowCount = table.getRowCount();
            RowSorter<? extends TableModel> rs = table.getRowSorter();
            if (rs == null) {
                table.setAutoCreateRowSorter(true);
                rs = table.getRowSorter();
            }

            TableRowSorter<? extends TableModel> rowSorter = (TableRowSorter<? extends TableModel>) rs;
            if (searchText.length() == 0) {
                rowSorter.setRowFilter(null);
            } else {
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(searchText)));
            }

            int afterFilterRowCount = table.getRowCount();
            return afterFilterRowCount != 0 && afterFilterRowCount != beforeFilterRowCount;
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
