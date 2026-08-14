package enginaar.modernity.additionalprojects;

import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

public class AdditionalProject implements Project {

    private final FileObject projectDirectory;
    private final Lookup lookup;

    public AdditionalProject(
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