package study.ywork.basis.swing.tree;

import study.ywork.basis.swing.RandomUtil;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Hashtable;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TreeExample {
    public static void main(String[] args) {
        Hashtable<?, ?> projectHierarchy = TradingProjectDataService.INSTANCE.getProjectHierarchy();
        JTree tree = new JTree(projectHierarchy);
        tree.addTreeSelectionListener(createTreeSelectionListener());
        JFrame frame = createFrame();
        frame.add(new JScrollPane(tree));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static TreeSelectionListener createTreeSelectionListener() {
        return treeSelectionEvent -> {
            TreePath path = treeSelectionEvent.getPath();
            System.out.println("Path: " + path);
            Object c = path.getLastPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) c;
            System.out.println("User Object: " + node.getUserObject());
            System.out.println("---");
        };
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JTree basic example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(500, 400));
        return frame;
    }

    private enum TradingProjectDataService {
        INSTANCE;

        private static final String[] ROLES = {"Project Manager", "Tech Lead", "Developer", "Scrum Master",
                "Business Analyst"};
        private Hashtable<Object, Object> modules = new Hashtable<>();

        TradingProjectDataService() {
            addModule("Trading", "Real Time Trading", "Order System");
            addModule("Future/Option", "Option Analyzer", "Market Scanning System");
            addModule("Fixed Income", "Bond Tool", "Price/Yield Calculator", "Strategy Evaluator");
        }

        private void addModule(String module, String... projects) {
            for (String project : projects) {
                modules.put(module, getProject(module, project));
            }
        }

        private Object getProject(String module, String project) {
            Hashtable<Object, Object> projectMap = new Hashtable<>();
            projectMap.put(project, getEmployeesForProject(module, project));
            return projectMap;
        }

        private Object getEmployeesForProject(String module, String project) {
            String[] employees = new String[ROLES.length];
            for (int i = 0; i < ROLES.length; i++) {
                employees[i] = RandomUtil.getFullName() + " [" + ROLES[i] + "]";
            }
            return employees;
        }

        public Hashtable<?, ?> getProjectHierarchy() {
            return modules;
        }
    }

}
