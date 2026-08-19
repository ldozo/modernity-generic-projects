package enginaar.modernity.genericprojects;

import org.junit.Test;
import org.openide.filesystems.FileObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link ProjectConverter}.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class ProjectConverterTest extends AbstractGenericProjectTest {

    @Test
    public void constantMatchesFactoryProjectType() {
        assertEquals("enginaar.modernity.genericprojects", ProjectConverter.PROJECT_TYPE);
        assertEquals(ProjectConverter.PROJECT_TYPE, GenericProjectFactory.PROJECT_TYPE);
    }

    @Test
    public void convertCreatesProjectXmlWithType() throws Exception {
        FileObject root = createTempRoot();

        ProjectConverter.convertToProject(root);

        FileObject xml = root.getFileObject("nbproject/project.xml");
        assertNotNull("nbproject/project.xml should be created", xml);
        String content = xml.asText("UTF-8");
        assertTrue("project.xml must contain the project type: " + content,
                content.contains(ProjectConverter.PROJECT_TYPE));
    }

    @Test
    public void convertIsIdempotent() throws Exception {
        FileObject root = createTempRoot();

        ProjectConverter.convertToProject(root);
        ProjectConverter.convertToProject(root);

        FileObject xml = root.getFileObject("nbproject/project.xml");
        assertNotNull(xml);
        assertEquals(1, root.getFileObject("nbproject").getChildren().length);
    }

    @Test
    public void convertedFolderBecomesAProject() throws Exception {
        FileObject root = createTempRoot();

        ProjectConverter.convertToProject(root);

        assertTrue("converted folder must be recognized as a project",
                new GenericProjectFactory().isProject(root));
    }
}