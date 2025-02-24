package study.ywork.basis.swing.table;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class PaginationExample {
    public static void main(String[] args) {
        JFrame frame = createFrame();
        JTable table = new JTable(createObjectDataModel());
        table.setAutoCreateRowSorter(true);
        PaginationDataProvider<Employee> dataProvider = createDataProvider();
        PaginatedTableDecorator<Employee> paginatedDecorator = PaginatedTableDecorator.decorate(table, dataProvider,
                new int[]{5, 10, 20, 50, 75, 100}, 10);
        frame.add(paginatedDecorator.getContentPanel());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static TableModel createObjectDataModel() {
        return new ObjectTableModel<Employee>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object getValueAt(Employee employee, int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return employee.getId();
                    case 1:
                        return employee.getName();
                    case 2:
                        return employee.getPhoneNumber();
                    case 3:
                        return employee.getAddress();
                    default:
                        return null;
                }
            }

            @Override
            public int getColumnCount() {
                return 4;
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return "Id";
                    case 1:
                        return "Name";
                    case 2:
                        return "Phone";
                    case 3:
                        return "Address";
                    default:
                        return null;
                }
            }
        };
    }

    private static PaginationDataProvider<Employee> createDataProvider() {
        final List<Employee> list = new ArrayList<>();
        for (int i = 1; i <= 500; i++) {
            Employee e = new Employee();
            e.setId(i);
            e.setName("name" + i);
            e.setPhoneNumber("phone" + i);
            e.setAddress("address " + i);
            list.add(e);
        }

        return new PaginationDataProvider<Employee>() {
            @Override
            public int getTotalRowCount() {
                return list.size();
            }

            @Override
            public List<Employee> getRows(int startIndex, int endIndex) {
                return list.subList(startIndex, endIndex);
            }
        };
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JTable Pagination example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 300));
        return frame;
    }

    private interface PaginationDataProvider<T> {
        int getTotalRowCount();

        List<T> getRows(int startIndex, int endIndex);
    }

    private static class PaginatedTableDecorator<T> {
        private JTable table;
        private PaginationDataProvider<T> dataProvider;
        private int[] pageSizes;
        private JPanel contentPanel;
        private int currentPageSize;
        private int currentPage = 1;
        private JPanel pageLinkPanel;
        private ObjectTableModel<T> objectTableModel;
        private static final int MAX_PAGING_SIZE = 9;
        private static final String ELLIPSES = "...";

        private PaginatedTableDecorator(JTable table, PaginationDataProvider<T> dataProvider, int[] pageSizes,
                                        int defaultPageSize) {
            this.table = table;
            this.dataProvider = dataProvider;
            this.pageSizes = pageSizes;
            this.currentPageSize = defaultPageSize;
        }

        public static <T> PaginatedTableDecorator<T> decorate(JTable table, PaginationDataProvider<T> dataProvider,
                                                              int[] pageSizes, int defaultPageSize) {
            PaginatedTableDecorator<T> decorator = new PaginatedTableDecorator<>(table, dataProvider, pageSizes,
                    defaultPageSize);
            decorator.init();
            return decorator;
        }

        public JPanel getContentPanel() {
            return contentPanel;
        }

        private void init() {
            initDataModel();
            initPaginationComponents();
            initListeners();
            paginate();
        }

        private void initListeners() {
            objectTableModel.addTableModelListener(this::refreshPageButtonPanel);
        }

        @SuppressWarnings("unchecked")
        private void initDataModel() {
            TableModel model = table.getModel();
            if (!(model instanceof ObjectTableModel)) {
                throw new IllegalArgumentException("TableModel must be a subclass of ObjectTableModel");
            }

            objectTableModel = (ObjectTableModel<T>) model;
        }

        private void initPaginationComponents() {
            contentPanel = new JPanel(new BorderLayout());
            JPanel paginationPanel = createPaginationPanel();
            contentPanel.add(paginationPanel, BorderLayout.NORTH);
            contentPanel.add(new JScrollPane(table));
        }

        private JPanel createPaginationPanel() {
            JPanel paginationPanel = new JPanel();
            pageLinkPanel = new JPanel(new GridLayout(1, MAX_PAGING_SIZE, 3, 3));
            paginationPanel.add(pageLinkPanel);

            if (pageSizes != null) {
                JComboBox<Integer> pageComboBox = new JComboBox<>(
                        Arrays.stream(pageSizes).boxed().toArray(Integer[]::new));
                pageComboBox.addActionListener((ActionEvent e) -> {
                    int currentPageStartRow = ((currentPage - 1) * currentPageSize) + 1;
                    currentPageSize = (Integer) pageComboBox.getSelectedItem();
                    currentPage = ((currentPageStartRow - 1) / currentPageSize) + 1;
                    paginate();
                });
                paginationPanel.add(Box.createHorizontalStrut(15));
                paginationPanel.add(new JLabel("Page Size: "));
                paginationPanel.add(pageComboBox);
                pageComboBox.setSelectedItem(currentPageSize);
            }

            return paginationPanel;
        }

        private void refreshPageButtonPanel(TableModelEvent tme) {
            pageLinkPanel.removeAll();
            int totalRows = dataProvider.getTotalRowCount();
            int pages = (int) Math.ceil((double) totalRows / currentPageSize);
            ButtonGroup buttonGroup = new ButtonGroup();
            if (pages > MAX_PAGING_SIZE) {
                addPageButton(pageLinkPanel, buttonGroup, 1);
                if (currentPage > (pages - ((MAX_PAGING_SIZE + 1) / 2))) {
                    pageLinkPanel.add(createEllipsesComponent());
                    addPageButtonRange(pageLinkPanel, buttonGroup, pages - MAX_PAGING_SIZE + 3, pages);
                } else if (currentPage <= (MAX_PAGING_SIZE + 1) / 2) {
                    addPageButtonRange(pageLinkPanel, buttonGroup, 2, MAX_PAGING_SIZE - 2);
                    pageLinkPanel.add(createEllipsesComponent());
                    addPageButton(pageLinkPanel, buttonGroup, pages);
                } else {
                    pageLinkPanel.add(createEllipsesComponent());
                    int start = currentPage - (MAX_PAGING_SIZE - 4) / 2;
                    int end = start + MAX_PAGING_SIZE - 5;
                    addPageButtonRange(pageLinkPanel, buttonGroup, start, end);
                    pageLinkPanel.add(createEllipsesComponent());
                    addPageButton(pageLinkPanel, buttonGroup, pages);
                }
            } else {
                addPageButtonRange(pageLinkPanel, buttonGroup, 1, pages);
            }

            pageLinkPanel.getParent().validate();
            pageLinkPanel.getParent().repaint();
        }

        private Component createEllipsesComponent() {
            return new JLabel(ELLIPSES, SwingConstants.CENTER);
        }

        private void addPageButtonRange(JPanel parentPanel, ButtonGroup buttonGroup, int start, int end) {
            for (; start <= end; start++) {
                addPageButton(parentPanel, buttonGroup, start);
            }
        }

        private void addPageButton(JPanel parentPanel, ButtonGroup buttonGroup, int pageNumber) {
            JToggleButton toggleButton = new JToggleButton(Integer.toString(pageNumber));
            toggleButton.setMargin(new Insets(1, 3, 1, 3));
            buttonGroup.add(toggleButton);
            parentPanel.add(toggleButton);
            if (pageNumber == currentPage) {
                toggleButton.setSelected(true);
            }
            toggleButton.addActionListener(ae -> {
                currentPage = Integer.parseInt(ae.getActionCommand());
                paginate();
            });
        }

        private void paginate() {
            int startIndex = (currentPage - 1) * currentPageSize;
            int endIndex = startIndex + currentPageSize;
            if (endIndex > dataProvider.getTotalRowCount()) {
                endIndex = dataProvider.getTotalRowCount();
            }
            List<T> rows = dataProvider.getRows(startIndex, endIndex);
            objectTableModel.setObjectRows(rows);
            objectTableModel.fireTableDataChanged();
        }
    }

    private abstract static class ObjectTableModel<T> extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private transient List<T> objectRows = new ArrayList<>();

        public List<T> getObjectRows() {
            return objectRows;
        }

        public void setObjectRows(List<T> objectRows) {
            this.objectRows = objectRows;
        }

        @Override
        public int getRowCount() {
            return objectRows.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            T t = objectRows.get(rowIndex);
            return getValueAt(t, columnIndex);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (objectRows.isEmpty()) {
                return Object.class;
            }

            Object valueAt = getValueAt(0, columnIndex);
            return valueAt != null ? valueAt.getClass() : Object.class;
        }

        public abstract Object getValueAt(T t, int columnIndex);

        @Override
        public abstract String getColumnName(int column);
    }

    private static class Employee {
        private long id;
        private String name;
        private String phoneNumber;
        private String address;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
