package study.ywork.basis.swing.tree;

import study.ywork.basis.swing.RandomUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class HighLightTreeExample {
    private static final String HTML_START_TAG = "<html>";
    private static final String HTML_END_TAG = "</html>";

    public static void main(String[] args) {
        TreeNode projectHierarchyTreeNode = TradingProjectDataService.INSTANCE.getProjectHierarchy();
        JTree tree = new JTree(projectHierarchyTreeNode);
        JTreeUtil.setTreeExpandedState(tree, true);
        TreeFilterDecorator filterDecorator = TreeFilterDecorator.decorate(tree, createUserObjectMatcher());
        tree.setCellRenderer(new TradingProjectTreeRenderer(() -> filterDecorator.getFilterField().getText()));
        JFrame frame = createFrame();
        frame.add(new JScrollPane(tree));
        frame.add(filterDecorator.getFilterField(), BorderLayout.NORTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static BiPredicate<Object, String> createUserObjectMatcher() {
        return (userObject, textToFilter) -> {
            if (userObject instanceof ProjectParticipant pp) {
                return pp.getName().toLowerCase().contains(textToFilter)
                        || pp.getRole().toLowerCase().contains(textToFilter);
            } else if (userObject instanceof Project project) {
                return project.getName().toLowerCase().contains(textToFilter);
            } else {
                return userObject.toString().toLowerCase().contains(textToFilter);
            }
        };
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("JTree Filtering example");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(500, 400));
        return frame;
    }

    private static class HtmlHighlighter {
        private static final String HIGH_LIGHT_TEMPLATE = "<span style='background:rgb(230,230,0);'>$1</span>";

        public static String highlightText(String text, String textToHighlight) {
            if (textToHighlight.length() == 0) {
                return text;
            }

            try {
                text = text.replaceAll("(?i)(" + Pattern.quote(textToHighlight) + ")", HIGH_LIGHT_TEMPLATE);
            } catch (Exception e) {
                return text;
            }

            return text;
        }
    }

    private static class JTreeUtil {
        public static void setTreeExpandedState(JTree tree, boolean expanded) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getRoot();
            setNodeExpandedState(tree, node, expanded);
        }

        public static void setNodeExpandedState(JTree tree, DefaultMutableTreeNode node, boolean expanded) {
            for (TreeNode treeNode : children(node)) {
                setNodeExpandedState(tree, (DefaultMutableTreeNode) treeNode, expanded);
            }

            if (!expanded && node.isRoot()) {
                return;
            }

            TreePath path = new TreePath(node.getPath());
            if (expanded) {
                tree.expandPath(path);
            } else {
                tree.collapsePath(path);
            }
        }

        public static DefaultMutableTreeNode copyNode(DefaultMutableTreeNode oldNode) {
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(oldNode.getUserObject());
            for (TreeNode treeNode : JTreeUtil.children(oldNode)) {
                DefaultMutableTreeNode oldChildNode = (DefaultMutableTreeNode) treeNode;
                DefaultMutableTreeNode newChildNode = new DefaultMutableTreeNode(oldChildNode.getUserObject());
                newNode.add(newChildNode);

                if (!oldChildNode.isLeaf()) {
                    copyChildrenTo(oldChildNode, newChildNode);
                }
            }

            return newNode;
        }

        public static void copyChildrenTo(DefaultMutableTreeNode from, DefaultMutableTreeNode to) {
            for (TreeNode treeNode : JTreeUtil.children(from)) {
                DefaultMutableTreeNode oldChildNode = (DefaultMutableTreeNode) treeNode;
                DefaultMutableTreeNode newChildNode = new DefaultMutableTreeNode(oldChildNode.getUserObject());
                to.add(newChildNode);

                if (!oldChildNode.isLeaf()) {
                    copyChildrenTo(oldChildNode, newChildNode);
                }
            }
        }

        public static List<TreeNode> children(DefaultMutableTreeNode node) {
            return Collections.list(node.children());
        }
    }

    private static class Project {
        private final String name;

        public Project(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Project{" + "name='" + name + '\'' + '}';
        }
    }

    private static class ProjectParticipant {
        private final String name;
        private final String role;

        public ProjectParticipant(String name, String role) {
            this.name = name;
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        @Override
        public String toString() {
            return "ProjectParticipant{" + "name='" + name + '\'' + ", role='" + role + '\'' + '}';
        }
    }

    private enum TradingProjectDataService {
        INSTANCE;

        private static final String[] ROLES = {"Project Manager", "Tech Lead", "Developer", "Scrum Master",
                "Business Analyst"};
        private DefaultMutableTreeNode rootNode;

        TradingProjectDataService() {
            rootNode = new DefaultMutableTreeNode("Trading Project Modules");
            addModule("Trading", "Real Time Trading", "Order System");
            addModule("Future/Option", "Option Analyzer", "Market Scanning System");
            addModule("Fixed Income", "Bond Tool", "Price/Yield Calculator", "Strategy Evaluator");
        }

        private void addModule(String module, String... projects) {
            DefaultMutableTreeNode moduleNode = new DefaultMutableTreeNode(module);
            rootNode.add(moduleNode);
            for (String project : projects) {
                moduleNode.add(getProjectNode(module, project));
            }
        }

        private MutableTreeNode getProjectNode(String module, String project) {
            DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(new Project(project));
            for (int i = 0; i < ROLES.length; i++) {
                projectNode.add(getEmployeeNodeForRole(module, project, ROLES[i]));
            }
            return projectNode;
        }

        private MutableTreeNode getEmployeeNodeForRole(String module, String project, String role) {
            ProjectParticipant projectParticipant = new ProjectParticipant(RandomUtil.getFullName(), role);
            return new DefaultMutableTreeNode(projectParticipant);
        }

        public TreeNode getProjectHierarchy() {
            return rootNode;
        }
    }

    private static class TradingProjectTreeRenderer extends DefaultTreeCellRenderer {
        private static final long serialVersionUID = 1L;
        private static final String SPAN_FORMAT = "<span style='color:%s;'>%s</span>";
        private transient Supplier<String> filterTextSupplier;

        public TradingProjectTreeRenderer(Supplier<String> filterTextSupplier) {
            this.filterTextSupplier = filterTextSupplier;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();
            if (userObject instanceof ProjectParticipant pp) {
                String text = String.format(SPAN_FORMAT, "rgb(0, 0, 150)", renderFilterMatch(node, pp.getName()));
                text += " [" + String.format(SPAN_FORMAT, "rgb(90, 70, 0)", renderFilterMatch(node, pp.getRole()))
                        + "]";
                this.setText(HTML_START_TAG + text + HTML_END_TAG);
            } else if (userObject instanceof Project project) {
                String text = String.format(SPAN_FORMAT, "rgb(0,70,0)", renderFilterMatch(node, project.getName()));
                this.setText(HTML_START_TAG + text + HTML_END_TAG);
            } else {
                String text = String.format(SPAN_FORMAT, "rgb(120,0,0)",
                        renderFilterMatch(node, userObject.toString()));
                this.setText(HTML_START_TAG + text + HTML_END_TAG);
            }
            return this;
        }

        private String renderFilterMatch(DefaultMutableTreeNode node, String text) {
            if (node.isRoot()) {
                return text;
            }
            String textToFilter = filterTextSupplier.get();
            return HtmlHighlighter.highlightText(text, textToFilter);
        }
    }

    private static class TreeFilterDecorator {
        private final JTree tree;
        private DefaultMutableTreeNode originalRootNode;
        private BiPredicate<Object, String> userObjectMatcher;
        private JTextField filterField;

        public TreeFilterDecorator(JTree tree, BiPredicate<Object, String> userObjectMatcher) {
            this.tree = tree;
            this.originalRootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
            this.userObjectMatcher = userObjectMatcher;
        }

        public static TreeFilterDecorator decorate(JTree tree, BiPredicate<Object, String> userObjectMatcher) {
            TreeFilterDecorator tfd = new TreeFilterDecorator(tree, userObjectMatcher);
            tfd.init();
            return tfd;
        }

        public JTextField getFilterField() {
            return filterField;
        }

        private void init() {
            initFilterField();
        }

        private void initFilterField() {
            filterField = new JTextField(15);
            filterField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    filterTree();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filterTree();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filterTree();
                }
            });
        }

        private void filterTree() {
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            String text = filterField.getText().trim().toLowerCase();

            if (text.equals("") && tree.getModel().getRoot() != originalRootNode) {
                model.setRoot(originalRootNode);
                JTreeUtil.setTreeExpandedState(tree, true);
            } else {
                DefaultMutableTreeNode newRootNode = matchAndBuildNode(text, originalRootNode);
                model.setRoot(newRootNode);
                JTreeUtil.setTreeExpandedState(tree, true);
            }
        }

        private DefaultMutableTreeNode matchAndBuildNode(final String text, DefaultMutableTreeNode oldNode) {
            if (!oldNode.isRoot() && userObjectMatcher.test(oldNode.getUserObject(), text)) {
                return JTreeUtil.copyNode(oldNode);
            }

            DefaultMutableTreeNode newMatchedNode = oldNode.isRoot()
                    ? new DefaultMutableTreeNode(oldNode.getUserObject())
                    : null;
            for (TreeNode treeNode : JTreeUtil.children(oldNode)) {
                DefaultMutableTreeNode childOldNode = (DefaultMutableTreeNode) treeNode;
                DefaultMutableTreeNode newMatchedChildNode = matchAndBuildNode(text, childOldNode);

                if (newMatchedChildNode != null) {
                    if (newMatchedNode == null) {
                        newMatchedNode = new DefaultMutableTreeNode(oldNode.getUserObject());
                    }

                    newMatchedNode.add(newMatchedChildNode);
                }
            }

            return newMatchedNode;
        }
    }
}
