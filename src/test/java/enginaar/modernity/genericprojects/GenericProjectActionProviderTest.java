package enginaar.modernity.genericprojects;

import org.junit.Test;
import org.openide.filesystems.FileObject;
import org.netbeans.spi.project.ActionProvider;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link GenericProjectActionProvider}.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectActionProviderTest extends AbstractGenericProjectTest {

    private GenericProjectActionProvider createProvider(FileObject root) {
        org.openide.util.lookup.InstanceContent content = new org.openide.util.lookup.InstanceContent();
        GenericProject project = new GenericProject(root, new org.openide.util.lookup.AbstractLookup(content));
        content.add(project);
        content.add(new GenericProjectOperations(project));
        return new GenericProjectActionProvider(project);
    }

    @Test
    public void supportsDeleteCopyMoveAndRename() throws Exception {
        FileObject root = createTempRoot();
        GenericProjectActionProvider provider = createProvider(root);

        assertArrayEquals(new String[]{
            ActionProvider.COMMAND_DELETE,
            ActionProvider.COMMAND_COPY,
            ActionProvider.COMMAND_MOVE,
            ActionProvider.COMMAND_RENAME
        }, provider.getSupportedActions());
    }

    @Test
    public void allSupportedActionsAreEnabled() throws Exception {
        FileObject root = createTempRoot();
        GenericProjectActionProvider provider = createProvider(root);

        assertTrue(provider.isActionEnabled(ActionProvider.COMMAND_DELETE, null));
        assertTrue(provider.isActionEnabled(ActionProvider.COMMAND_COPY, null));
        assertTrue(provider.isActionEnabled(ActionProvider.COMMAND_MOVE, null));
        assertTrue(provider.isActionEnabled(ActionProvider.COMMAND_RENAME, null));
    }

    @Test
    public void unknownCommandIsDisabled() throws Exception {
        FileObject root = createTempRoot();
        GenericProjectActionProvider provider = createProvider(root);

        assertFalse(provider.isActionEnabled("unknown.command", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokingUnknownCommandThrows() throws Exception {
        FileObject root = createTempRoot();
        GenericProjectActionProvider provider = createProvider(root);

        provider.invokeAction("unknown.command", null);
    }
}