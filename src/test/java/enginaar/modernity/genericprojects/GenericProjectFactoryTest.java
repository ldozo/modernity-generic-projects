package enginaar.modernity.genericprojects;

import org.junit.Test;
import org.openide.filesystems.FileObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link GenericProjectFactory}.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectFactoryTest extends AbstractGenericProjectTest {

    private final GenericProjectFactory factory = new GenericProjectFactory();

    @Test
    public void plainFolderIsNotAProject() throws Exception {
        FileObject root = createTempRoot();
        assertFalse(factory.isProject(root));
    }

    @Test
    public void gitRepositoryIsAProject() throws Exception {
        FileObject root = createTempRoot();
        root.createFolder(".git");
        assertTrue(factory.isProject(root));
    }

    @Test
    public void permanentProjectIsAProject() throws Exception {
        FileObject root = createTempRoot();
        ProjectConverter.convertToProject(root);
        assertTrue(factory.isProject(root));
    }

    @Test
    public void foreignProjectTypeIsNotRecognized() throws Exception {
        FileObject root = createTempRoot();
        FileObject nbproject = root.createFolder("nbproject");
        nbproject.createData("project", "xml").getOutputStream().close();
        FileObject xml = root.getFileObject("nbproject/project.xml");
        try (java.io.OutputStream out = xml.getOutputStream()) {
            out.write("<?xml version=\"1.0\"?><project><type>some.other.type</type></project>".getBytes());
        }
        assertFalse(factory.isProject(root));
    }

    @Test
    public void temporaryMarkerIsAProject() throws Exception {
        FileObject root = createTempRoot();
        FolderProjectMarker.create(root);
        assertTrue(factory.isProject(root));
    }

    @Test
    public void dataFileIsNotAProject() throws Exception {
        FileObject root = createTempRoot();
        FileObject data = root.createData("file.txt");
        assertFalse(factory.isProject(data));
    }

    @Test
    public void nullDirectoryIsNotAProject() {
        assertFalse(factory.isProject(null));
    }

    @Test
    public void loadProjectLoadsGitRepository() throws Exception {
        FileObject root = createTempRoot();
        root.createFolder(".git");

        org.netbeans.api.project.Project loaded = factory.loadProject(root, null);

        assertNotNull("git repository must be loadable", loaded);
        assertTrue(loaded instanceof GenericProject);
        assertEquals(root, loaded.getProjectDirectory());
    }

    @Test
    public void loadProjectRejectsPlainFolder() throws Exception {
        FileObject root = createTempRoot();
        assertNull(factory.loadProject(root, null));
    }

    @Test
    public void loadedProjectExposesServicesInLookup() throws Exception {
        FileObject root = createTempRoot();
        root.createFolder(".git");

        GenericProject loaded = (GenericProject) factory.loadProject(root, null);

        assertNotNull(loaded.getLookup().lookup(GenericProjectInformation.class));
        assertNotNull(loaded.getLookup().lookup(GenericProjectLogicalViewProvider.class));
        assertNotNull(loaded.getLookup().lookup(GenericProjectOperations.class));
        assertNotNull(loaded.getLookup().lookup(GenericProjectActionProvider.class));
        assertNotNull(loaded.getLookup().lookup(GenericProjectOpenedHook.class));
    }

    @Test
    public void transientProjectExposesServicesInLookup() throws Exception {
        FileObject root = createTempRoot();

        GenericProject project = GenericProjectFactory.createTransientProject(root);

        assertNotNull(project.getLookup().lookup(GenericProjectInformation.class));
        assertNotNull(project.getLookup().lookup(GenericProjectLogicalViewProvider.class));
        assertNotNull(project.getLookup().lookup(GenericProjectOperations.class));
        assertNotNull(project.getLookup().lookup(GenericProjectActionProvider.class));
        assertNotNull(project.getLookup().lookup(GenericProjectOpenedHook.class));
    }

    @Test
    public void projectTypeConstantMatchesConverterType() {
        assertEquals(ProjectConverter.PROJECT_TYPE, GenericProjectFactory.PROJECT_TYPE);
    }
}