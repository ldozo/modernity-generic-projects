package enginaar.modernity.genericprojects;

import org.junit.Test;
import org.openide.filesystems.FileObject;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link GenericProjectRootNode}.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectRootNodeTest extends AbstractGenericProjectTest {

    @Test
    public void displayNameMatchesFolderName() throws Exception {
        FileObject root = createTempRoot();
        GenericProject project = new GenericProject(root, createLookup());

        GenericProjectRootNode node = new GenericProjectRootNode(project);

        assertEquals(root.getNameExt(), node.getDisplayName());
        assertEquals(root.getNameExt(), node.getName());
    }

    @Test
    public void nodeHasNoChildren() throws Exception {
        FileObject root = createTempRoot();
        GenericProject project = new GenericProject(root, createLookup());

        GenericProjectRootNode node = new GenericProjectRootNode(project);

        assertEquals(0, node.getChildren().getNodesCount(true));
    }

    private org.openide.util.Lookup createLookup() {
        return new org.openide.util.lookup.AbstractLookup(
                new org.openide.util.lookup.InstanceContent());
    }
}