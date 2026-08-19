package enginaar.modernity.genericprojects;

import org.junit.Test;
import org.openide.filesystems.FileObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link GenericProject} and {@link GenericProjectInformation}.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectTest extends AbstractGenericProjectTest {

    @Test
    public void projectExposesItsDirectory() throws Exception {
        FileObject root = createTempRoot();
        GenericProject project = new GenericProject(root, createEmptyLookup());
        assertEquals(root, project.getProjectDirectory());
    }

    @Test
    public void projectLookupReturnsItself() throws Exception {
        FileObject root = createTempRoot();
        GenericProject project = createProject(root);
        assertEquals(project, project.getLookup().lookup(GenericProject.class));
    }

    @Test
    public void informationNameAndDisplayNameMatchFolder() throws Exception {
        FileObject root = createTempRoot();
        GenericProject project = new GenericProject(root, createEmptyLookup());
        GenericProjectInformation info = new GenericProjectInformation(project);
        assertEquals(root.getNameExt(), info.getName());
        assertEquals(root.getNameExt(), info.getDisplayName());
    }

    @Test
    public void informationReturnsProject() throws Exception {
        FileObject root = createTempRoot();
        GenericProject project = new GenericProject(root, createEmptyLookup());
        GenericProjectInformation info = new GenericProjectInformation(project);
        assertEquals(project, info.getProject());
    }

    @Test
    public void iconDoesNotThrowWithoutUiModules() throws Exception {
        FileObject root = createTempRoot();
        GenericProject project = new GenericProject(root, createEmptyLookup());
        GenericProjectInformation info = new GenericProjectInformation(project);
        assertNull("without the NetBeans UI modules the icon is not resolvable",
                info.getIcon());
    }

    @Test
    public void gitIconResourceChosenForGitRepositories() {
        assertEquals("enginaar/modernity/genericprojects/git-icon_16.svg",
                GenericProjectIcons.GIT_ICON);
    }

    @Test
    public void iconResourcesExistOnClasspath() {
        assertNotNull("git icon resource missing",
                getClass().getResourceAsStream(
                        "git-icon_16.svg"));
        assertNotNull("open folder icon resource missing",
                getClass().getResourceAsStream(
                        "open-folder-icon_16.svg"));
    }

    private static org.openide.util.Lookup createEmptyLookup() {
        return new org.openide.util.lookup.AbstractLookup(
                new org.openide.util.lookup.InstanceContent());
    }
}