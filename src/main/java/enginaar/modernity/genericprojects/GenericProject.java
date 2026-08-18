package enginaar.modernity.genericprojects;

import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 * A lightweight NetBeans {@link Project} that wraps an arbitrary directory.
 * <p>
 * The project does not impose any build system or project layout. It simply
 * exposes the wrapped folder together with a lookup that contains the
 * project services (information, logical view, opened hook).
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProject implements Project {

    private final FileObject projectDirectory;
    private final Lookup lookup;

    /**
     * Creates a generic project for the given directory.
     *
     * @param projectDirectory the directory this project wraps
     * @param lookup the lookup carrying the project services
     */
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