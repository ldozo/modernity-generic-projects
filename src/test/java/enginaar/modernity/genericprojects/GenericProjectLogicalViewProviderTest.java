package enginaar.modernity.genericprojects;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link GenericProjectLogicalViewProvider} tree filtering and
 * path resolution.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectLogicalViewProviderTest extends AbstractGenericProjectTest {

    private static final Set<String> HIDDEN = new HashSet<>(Arrays.asList(
            "nbproject", ".netbeans-folder-project", ".git"));

    @Test
    public void logicalViewShowsOnlyVisibleChildren() throws Exception {
        FileObject root = createTempRoot();
        root.createFolder(".git");
        root.createFolder("nbproject");
        root.createData(".netbeans-folder-project").getOutputStream().close();
        root.createData("README.md").getOutputStream().close();
        FileObject src = root.createFolder("src");
        src.createData("main.java").getOutputStream().close();

        GenericProjectLogicalViewProvider provider = new GenericProjectLogicalViewProvider(
                new GenericProject(root, createLookup()));

        Node rootNode = provider.createLogicalView();
        assertNotNull(rootNode);

        Set<String> names = collectNames(rootNode);
        assertTrue("tree must contain README.md", names.contains("README.md"));
        assertTrue("tree must contain src", names.contains("src"));
        assertTrue("tree must contain main.java", names.contains("main.java"));

        for (String hidden : HIDDEN) {
            assertTrue("tree must not contain hidden entry: " + hidden,
                    !names.contains(hidden));
        }
    }

    @Test
    public void hiddenFoldersAreFilteredRecursively() throws Exception {
        FileObject root = createTempRoot();
        FileObject sub = root.createFolder("sub");
        sub.createFolder(".git");
        sub.createData("keep.txt").getOutputStream().close();

        GenericProjectLogicalViewProvider provider = new GenericProjectLogicalViewProvider(
                new GenericProject(root, createLookup()));

        Node rootNode = provider.createLogicalView();
        Set<String> names = collectNames(rootNode);

        assertTrue(names.contains("sub"));
        assertTrue(names.contains("keep.txt"));
        assertTrue("nested .git must be hidden", !names.contains(".git"));
    }

    @Test
    public void findPathLocatesNestedFile() throws Exception {
        FileObject root = createTempRoot();
        root.createFolder(".git");
        FileObject deep = root.createFolder("src").createFolder("deep");
        FileObject file = deep.createData("config.yaml");
        try (OutputStream out = file.getOutputStream()) {
            out.write("key: value".getBytes());
        }

        GenericProjectLogicalViewProvider provider = new GenericProjectLogicalViewProvider(
                new GenericProject(root, createLookup()));

        Node rootNode = provider.createLogicalView();
        forceLoad(rootNode);
        Node found = provider.findPath(rootNode, file);

        assertNotNull("findPath must locate a nested file", found);
        assertEquals("config.yaml", found.getName());
    }

    @Test
    public void findPathReturnsNullForUnknownTarget() throws Exception {
        FileObject root = createTempRoot();

        GenericProjectLogicalViewProvider provider = new GenericProjectLogicalViewProvider(
                new GenericProject(root, createLookup()));

        Node rootNode = provider.createLogicalView();
        assertNull(provider.findPath(rootNode, new Object()));
    }

    @Test
    public void findPathReturnsNullForNonDataObject() throws Exception {
        FileObject root = createTempRoot();

        GenericProjectLogicalViewProvider provider = new GenericProjectLogicalViewProvider(
                new GenericProject(root, createLookup()));

        Node rootNode = provider.createLogicalView();
        FileObject missing = root.createData("ghost.txt");
        missing.delete();
        assertNull(provider.findPath(rootNode, missing));
    }

    private Set<String> collectNames(Node node) {
        Set<String> names = new HashSet<String>();
        collectNamesRecursive(node, names);
        return names;
    }

    private void collectNamesRecursive(Node node, Set<String> names) {
        names.add(node.getName());
        forceLoad(node);
        for (Node child : node.getChildren().getNodes()) {
            collectNamesRecursive(child, names);
        }
    }

    private void forceLoad(Node node) {
        node.getChildren().getNodesCount(true);
        for (Node child : node.getChildren().getNodes()) {
            forceLoad(child);
        }
    }

    private org.openide.util.Lookup createLookup() {
        return new org.openide.util.lookup.AbstractLookup(
                new org.openide.util.lookup.InstanceContent());
    }
}