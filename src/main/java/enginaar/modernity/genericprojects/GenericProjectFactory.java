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

@ServiceProvider(service = ProjectFactory.class)
public class GenericProjectFactory
        implements ProjectFactory {

    private static final Logger LOG = Logger.getLogger(GenericProjectFactory.class.getName());

    static {
        LOG.log(Level.INFO, "AdditionalProjectFactory loaded - ServiceProvider registered");
    }

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
            if (content.contains("enginaar.modernity.additionalprojects")) {
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

    @Override
    public Project loadProject(FileObject projectDirectory, ProjectState state) throws IOException {
        if (!isProject(projectDirectory)) {
            return null;
        }

        LOG.log(Level.INFO, "Loading project: {0}", projectDirectory.getPath());

        InstanceContent content = new InstanceContent();
        AbstractLookup lookup = new AbstractLookup(content);

        GenericProject project = new GenericProject(projectDirectory, lookup);

        content.add(project);
        content.add(new GenericProjectInformation(project));
        content.add(new GenericProjectOpenedHook(project));
        content.add(new GenericProjectLogicalViewProvider(project));

        return project;
    }

    @Override
    public void saveProject(Project project) throws IOException {
        // nothing to save
    }

    public static GenericProject createTransientProject(FileObject folder) {
        InstanceContent content = new InstanceContent();
        AbstractLookup lookup = new AbstractLookup(content);

        GenericProject project = new GenericProject(folder, lookup);

        content.add(project);
        content.add(new GenericProjectInformation(project));
        content.add(new GenericProjectOpenedHook(project));
        content.add(new GenericProjectLogicalViewProvider(project));

        return project;
    }
}
