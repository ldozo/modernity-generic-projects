package enginaar.modernity.genericprojects;

import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = ProjectFactory.class)
public class AdditionalProjectFactory
        implements ProjectFactory {

    @Override
    public boolean isProject(FileObject projectDirectory) {

        if (projectDirectory == null
                || !projectDirectory.isFolder()) {
            return false;
        }

        if (projectDirectory.getFileObject(".git") != null) {
            return true;
        }

        if (projectDirectory.getFileObject(
                "nbproject/project.xml") != null) {
            return true;
        }

        return projectDirectory.getFileObject(
                ".netbeans-folder-project") != null;
    }

    @Override
    public Project loadProject(
            FileObject projectDirectory,
            ProjectState state)
            throws IOException {

        if (!isProject(projectDirectory)) {
            return null;
        }

        InstanceContent content = new InstanceContent();
        AbstractLookup lookup = new AbstractLookup(content);

        AdditionalProject project = new AdditionalProject(
                projectDirectory,
                lookup);

        content.add(project);
        content.add(new AdditionalProjectInformation(project));
        content.add(new AdditionalProjectOpenedHook(project));

        return project;
    }

    @Override
    public void saveProject(Project project)
            throws IOException {
        // nothing to save
    }

    public static AdditionalProject createTransientProject(
            FileObject folder) {

        InstanceContent content = new InstanceContent();

        AbstractLookup lookup = new AbstractLookup(content);

        AdditionalProject project = new AdditionalProject(
                folder,
                lookup);

        content.add(project);

        content.add(new AdditionalProjectInformation(project));
        content.add(new AdditionalProjectOpenedHook(project));
        content.add(new AdditionalProjectLogicalViewProvider(project));

        return project;
    }

}
