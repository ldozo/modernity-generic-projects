package enginaar.modernity.genericprojects;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.ProjectManager;

/**
 * Internal utilities shared by the generic project classes.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
final class ProjectUtils {

    private static final Logger LOG = Logger.getLogger(ProjectUtils.class.getName());

    private ProjectUtils() {
    }

    /**
     * Asks NetBeans to re-evaluate project recognition so a just-created or
     * converted folder is picked up immediately. When the project API is not
     * available (for example in plain unit tests) the call is skipped.
     */
    static void clearNonProjectCache() {
        try {
            ProjectManager manager = ProjectManager.getDefault();
            if (manager != null) {
                manager.clearNonProjectCache();
            }
        } catch (Throwable t) {
            LOG.log(Level.FINE, "ProjectManager unavailable, skipping cache clear", t);
        }
    }
}