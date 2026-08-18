package enginaar.modernity.genericprojects;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 * Root {@link org.openide.nodes.Node} for a {@link GenericProject}.
 * <p>
 * The root node only carries the display name of the wrapped folder; the
 * actual tree is provided by the logical view provider.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectRootNode
        extends AbstractNode {

    /**
     * Creates the root node for the given project.
     *
     * @param project the generic project to present
     */
    public GenericProjectRootNode(
            GenericProject project) {

        super(Children.LEAF);

        setDisplayName(
                project.getProjectDirectory()
                        .getNameExt());
    }
}