package enginaar.modernity.genericprojects;

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
    }
}
