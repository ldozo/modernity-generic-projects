package enginaar.modernity.genericprojects;

import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.filesystems.FileObject;

public class GenericProjectLogicalViewProvider
        implements LogicalViewProvider {

    private final GenericProject project;

    public GenericProjectLogicalViewProvider(
            GenericProject project) {

        this.project = project;
    }

    @Override
    public Node createLogicalView() {
        return DataFolder.findFolder(project.getProjectDirectory()).getNodeDelegate();
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
}