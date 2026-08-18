package enginaar.modernity.genericprojects;

import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

public class GenericProject implements Project {

    private final FileObject projectDirectory;
    private final Lookup lookup;

    public GenericProject(
            FileObject projectDirectory,
            Lookup lookup) {

        this.projectDirectory = projectDirectory;
        this.lookup = lookup;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDirectory;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
}