package enginaar.modernity.additionalprojects;

import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.nodes.Node;

public class AdditionalProjectLogicalViewProvider
        implements LogicalViewProvider {

    private final AdditionalProject project;

    public AdditionalProjectLogicalViewProvider(
            AdditionalProject project) {

        this.project = project;
    }

    @Override
    public Node createLogicalView() {

        return new AdditionalProjectRootNode(
                project);
    }

    @Override
    public Node findPath(
            Node root,
            Object target) {

        return null;
    }
}