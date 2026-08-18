package enginaar.modernity.genericprojects;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Image;
import javax.swing.Action;
import javax.swing.Icon;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * {@link LogicalViewProvider} for {@link GenericProject}.
 * <p>
 * The logical view delegates to the standard {@link DataFolder} node of the
 * wrapped directory, so the project tree simply reflects the folder contents
 * using NetBeans' default data-object nodes. {@code findPath} locates the node
 * corresponding to a given target file.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectLogicalViewProvider
        implements LogicalViewProvider {

    private static final Logger LOG = Logger.getLogger(GenericProjectLogicalViewProvider.class.getName());

    private final GenericProject project;

    /**
     * Creates a logical view provider for the given project.
     *
     * @param project the generic project whose folder is shown
     */
    public GenericProjectLogicalViewProvider(
            GenericProject project) {

        this.project = project;
    }

    @Override
    public Node createLogicalView() {
        Node original = DataFolder.findFolder(project.getProjectDirectory()).getNodeDelegate();
        return new ProjectNode(original, project);
    }

    @Override
    public Node findPath(
            Node root,
            Object target) {
        if (target instanceof FileObject) {
            FileObject fo = (FileObject) target;
            try {
                DataObject dobj = DataObject.find(fo);
                Node targetNode = dobj.getNodeDelegate();
                return findNode(root, targetNode);
            } catch (DataObjectNotFoundException ex) {
                return null;
            }
        }
        return null;
    }

    private Node findNode(Node root, Node target) {
        if (root == target) {
            return root;
        }
        Node[] children = root.getChildren().getNodes();
        for (Node child : children) {
            Node found = findNode(child, target);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private static final class ProjectNode extends FilterNode {

        private static final String GIT_ICON = "enginaar/modernity/genericprojects/git-icon_16.svg";

        private final GenericProject project;

        ProjectNode(Node original, GenericProject project) {
            super(original, new FilterNode.Children(original),
                    new ProxyLookup(Lookups.singleton(project), original.getLookup()));
            this.project = project;
        }

        @Override
        public Image getIcon(int type) {
            if (isGit()) {
                Icon icon = ImageUtilities.loadImageIcon(GIT_ICON, false);
                if (icon != null) {
                    return ImageUtilities.icon2Image(icon);
                }
            }
            return super.getIcon(type);
        }

        @Override
        public Image getOpenedIcon(int type) {
            if (isGit()) {
                Icon icon = ImageUtilities.loadImageIcon(GIT_ICON, false);
                if (icon != null) {
                    return ImageUtilities.icon2Image(icon);
                }
            }
            return super.getOpenedIcon(type);
        }

        private boolean isGit() {
            FileObject dir = project.getProjectDirectory();
            return dir != null && dir.getFileObject(".git") != null;
        }

        @Override
        public Action[] getActions(boolean context) {
            List<Action> actions = new ArrayList<Action>();
            for (Action action : super.getActions(context)) {
                if (action != null) {
                    String className = action.getClass().getName();
                    boolean plainDelete = className.startsWith("org.openide.actions.DeleteAction")
                            || className.startsWith("org.openide.actions.RenameAction");
                    boolean delegate = "org.openide.awt.GeneralAction$DelegateAction".equals(className);
                    if (plainDelete || delegate) {
                        Object name = action.getValue(Action.NAME);
                        if (plainDelete) {
                            continue;
                        }
                        if (name != null) {
                            String n = name.toString();
                            if (n.startsWith("&Delete") || n.startsWith("&Rename")) {
                                continue;
                            }
                        }
                    }
                }
                actions.add(action);
            }
            actions.add(null);
            actions.add(CommonProjectActions.renameProjectAction());
            actions.add(CommonProjectActions.deleteProjectAction());
            actions.add(CommonProjectActions.closeProjectAction());
            Action[] result = actions.toArray(new Action[actions.size()]);
            StringBuilder f = new StringBuilder("GFINAL ctx=" + context + " node=" + getDisplayName() + " n=" + result.length + " [");
            for (int i = 0; i < result.length; i++) {
                Action a = result[i];
                if (a == null) {
                    f.append(i).append("=null;");
                } else {
                    f.append(i).append('=').append(a.getClass().getSimpleName());
                    Object name = a.getValue(Action.NAME);
                    if (name != null) {
                        f.append("('").append(name).append("')");
                    }
                    f.append(';');
                }
            }
            f.append(']');
            LOG.log(Level.INFO, f.toString());
            return result;
        }
    }
}