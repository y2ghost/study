package study.ywork.basis.swing.table;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.Dimension;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TableModelCreator {
    public static void main(String[] args) {
        JFrame frame = createFrame();
        List<Employee> list = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            Employee e = new Employee();
            e.setName("name" + i);
            e.setPhone("phone" + i);
            e.setDept("dept" + i);
            e.setCellPhone("cell" + i);
            list.add(e);
        }

        TableModel tableModel = TableModelCreator.createTableModel(Employee.class, list);
        JTable table = new JTable(tableModel);
        JScrollPane pane = new JScrollPane(table);
        frame.add(pane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JTable example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 300));
        return frame;
    }

    private static <T> TableModel createTableModel(Class<T> beanClass, List<T> list) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            List<String> columns = new ArrayList<>();
            List<Method> getters = new ArrayList<>();

            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                String name = pd.getName();
                if (name.equals("class")) {
                    continue;
                }

                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                String[] s = name.split("(?=\\p{Upper})");
                StringBuilder displayName = new StringBuilder("");

                for (String s1 : s) {
                    displayName.append(s1).append(" ");
                }

                columns.add(displayName.toString());
                Method m = pd.getReadMethod();
                getters.add(m);
            }

            return new AbstractTableModel() {
                private static final long serialVersionUID = 1L;

                @Override
                public String getColumnName(int column) {
                    return columns.get(column);
                }

                @Override
                public int getRowCount() {
                    return list.size();
                }

                @Override
                public int getColumnCount() {
                    return columns.size();
                }

                @Override
                public Object getValueAt(int rowIndex, int columnIndex) {
                    try {
                        return getters.get(columnIndex).invoke(list.get(rowIndex));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    private static class Employee {
        private String name;
        private String dept;
        private String phone;
        private String cellPhone;

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

        public String getCellPhone() {
            return cellPhone;
        }

        public void setCellPhone(String cellPhone) {
            this.cellPhone = cellPhone;
        }
    }
}
