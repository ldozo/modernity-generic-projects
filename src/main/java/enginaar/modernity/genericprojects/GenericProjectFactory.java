package enginaar.modernity.genericprojects;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

/**
 * {@link ProjectFactory} responsible for recognizing and loading generic
 * folder projects.
 * <p>
 * A directory is recognized when any of the following conditions holds:
 * <ul>
 *   <li>it is a Git repository (contains a {@code .git} entry),</li>
 *   <li>it contains a permanent {@code nbproject/project.xml} of type
 *       {@code enginaar.modernity.genericprojects}</li>
 *   <li>it contains the temporary marker file
 *       {@code .netbeans-folder-project}.</li>
 * </ul>
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
@ServiceProvider(service = ProjectFactory.class)
public class GenericProjectFactory
        implements ProjectFactory {

    private static final Logger LOG = Logger.getLogger(GenericProjectFactory.class.getName());

    /** The project type identifier written to {@code nbproject/project.xml}. */
    public static final String PROJECT_TYPE = ProjectConverter.PROJECT_TYPE;

    static {
        LOG.log(Level.INFO, "GenericProjectFactory loaded - ServiceProvider registered");
    }

    /**
     * Decides whether the given directory should be treated as a generic
     * folder project.
     *
     * @param projectDirectory the candidate directory
     * @return {@code true} if the directory is a Git repository, a permanent
     *         folder project, or a temporary folder project
     */
    @Override
    public boolean isProject(FileObject projectDirectory) {

        if (projectDirectory == null || !projectDirectory.isFolder()) {
            return false;
        }

        if (projectDirectory.getFileObject(".git") != null) {
            LOG.log(Level.FINE, "Detected Git repository: {0}", projectDirectory.getPath());
            return true;
        }

        if (projectDirectory.getFileObject("nbproject/project.xml") != null) {
            FileObject projectXml = projectDirectory.getFileObject("nbproject/project.xml");
            String content = "";
            try {
                content = projectXml.asText("UTF-8");
            } catch (IOException ex) {
                LOG.log(Level.WARNING, "Failed to read project.xml: {0}", projectXml.getPath());
            }
            if (content.contains(PROJECT_TYPE)) {
                LOG.log(Level.FINE, "Detected permanent folder project: {0}", projectDirectory.getPath());
                return true;
            }
        }

        if (projectDirectory.getFileObject(".netbeans-folder-project") != null) {
            LOG.log(Level.FINE, "Detected temporary folder project: {0}", projectDirectory.getPath());
            return true;
        }

        return false;
    }

    /**
     * Loads a generic project for the given directory.
     *
     * @param projectDirectory the project directory
     * @param state the project state supplied by the NetBeans project API
     * @return the loaded project, or {@code null} if the directory is not a
     *         generic folder project
     * @throws IOException if the project cannot be loaded
     */
    @Override
    public Project loadProject(FileObject projectDirectory, ProjectState state) throws IOException {
        if (!isProject(projectDirectory)) {
            return null;
        }

        LOG.log(Level.INFO, "Loading project: {0}", projectDirectory.getPath());

        return createProject(projectDirectory);
    }

    @Override
    public void saveProject(Project project) throws IOException {
        // nothing to save
    }

    public static GenericProject createTransientProject(FileObject folder) {
        return createProject(folder);
    }

    private static GenericProject createProject(FileObject folder) {
        InstanceContent content = new InstanceContent();
        AbstractLookup lookup = new AbstractLookup(content);

        GenericProject project = new GenericProject(folder, lookup);

        content.add(project);
        content.add(new GenericProjectInformation(project));
        content.add(new GenericProjectOpenedHook(project));
        content.add(new GenericProjectLogicalViewProvider(project));
        content.add(new GenericProjectOperations(project));
        content.add(new GenericProjectActionProvider(project));

        return project;
    }
}
