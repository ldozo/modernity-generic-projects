package enginaar.modernity.genericprojects;

import java.util.Set;
import java.util.logging.Logger;
import java.awt.Image;
import javax.swing.Icon;
import org.netbeans.spi.project.ui.LogicalViewProvider;
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
 * using NetBeans' default data-object nodes. Internal project files
 * ({@code nbproject}, {@code .netbeans-folder-project}, {@code .git}) are
 * filtered out of the tree. {@code findPath} locates the node corresponding
 * to a given target file.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectLogicalViewProvider
        implements LogicalViewProvider {

    private static final Logger LOG = Logger.getLogger(GenericProjectLogicalViewProvider.class.getName());

    private static final Set<String> HIDDEN_NAMES = Set.of(
            "nbproject",
            ".netbeans-folder-project",
            ".git");

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
        if (target instanceof FileObject fo) {
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

    private static boolean isHidden(Node node) {
        return HIDDEN_NAMES.contains(node.getName());
    }

    /**
     * Root node of the project tree. Git repositories show the Git repository
     * icon, all other generic projects show the enginar logo. The generic
     * project is exposed in the lookup.
     */
    private static final class ProjectNode extends FilterNode {

        private static final String GIT_ICON = "enginaar/modernity/genericprojects/git-icon_16.svg";
        private static final String ENGINAR_LOGO = "enginaar/modernity/genericprojects/enginar_logo.svg";

        private final GenericProject project;

        ProjectNode(Node original, GenericProject project) {
            super(original, new FilteredChildren(original),
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
            Icon icon = ImageUtilities.loadImageIcon(ENGINAR_LOGO, false);
            if (icon != null) {
                return ImageUtilities.icon2Image(icon);
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
            Icon icon = ImageUtilities.loadImageIcon(ENGINAR_LOGO, false);
            if (icon != null) {
                return ImageUtilities.icon2Image(icon);
            }
            return super.getOpenedIcon(type);
        }

        private boolean isGit() {
            FileObject dir = project.getProjectDirectory();
            return dir != null && dir.getFileObject(".git") != null;
        }
    }

    /**
     * Regular tree node for a visible child. Keeps the default file/folder
     * icons while continuing the filtering on its own children.
     */
    private static final class ChildNode extends FilterNode {

        ChildNode(Node original) {
            super(original, new FilteredChildren(original));
        }
    }

    /**
     * Children that drop the internal project files from the tree.
     */
    private static final class FilteredChildren extends FilterNode.Children {

        FilteredChildren(Node original) {
            super(original);
        }

        @Override
        protected Node[] createNodes(Node key) {
            if (isHidden(key)) {
                return new Node[0];
            }
            return new Node[]{new ChildNode(key)};
        }
    }
}
