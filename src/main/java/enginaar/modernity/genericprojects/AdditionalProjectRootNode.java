package enginaar.modernity.genericprojects;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

public class AdditionalProjectRootNode
        extends AbstractNode {

    public AdditionalProjectRootNode(
            AdditionalProject project) {

        super(Children.LEAF);

        setDisplayName(
                project.getProjectDirectory()
                        .getNameExt());
    }
}