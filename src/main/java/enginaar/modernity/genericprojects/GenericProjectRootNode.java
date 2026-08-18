package enginaar.modernity.genericprojects;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

public class GenericProjectRootNode
        extends AbstractNode {

    public GenericProjectRootNode(
            GenericProject project) {

        super(Children.LEAF);

        setDisplayName(
                project.getProjectDirectory()
                        .getNameExt());
    }
}