package enginaar.modernity.genericprojects;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ProjectManagerImplementation;
import org.openide.filesystems.FileObject;
import org.openide.util.Mutex;

/**
 * No-op {@link ProjectManagerImplementation} used to bootstrap
 * {@link ProjectManager} in unit tests that run without the full NetBeans
 * module system. Registered via {@code META-INF/services} in test resources.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
class TestProjectManagerImpl implements ProjectManagerImplementation {

    @Override
    public void init(ProjectManagerCallBack callBack) {
    }

    @Override
    public Mutex getMutex() {
        return new Mutex();
    }

    @Override
    public Mutex getMutex(boolean b, Project p, Project... projects) {
        return new Mutex();
    }

    @Override
    public Project findProject(FileObject projectDirectory) {
        return null;
    }

    @Override
    public ProjectManager.Result isProject(FileObject projectDirectory) {
        return null;
    }

    @Override
    public void clearNonProjectCache() {
    }

    @Override
    public Set<Project> getModifiedProjects() {
        return Collections.emptySet();
    }

    @Override
    public boolean isModified(Project project) {
        return false;
    }

    @Override
    public boolean isValid(Project project) {
        return true;
    }

    @Override
    public void saveProject(Project project) throws IOException {
    }

    @Override
    public void saveAllProjects() throws IOException {
    }
}