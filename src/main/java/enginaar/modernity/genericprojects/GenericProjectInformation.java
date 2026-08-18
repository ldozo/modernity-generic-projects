package enginaar.modernity.genericprojects;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Icon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.openide.util.ImageUtilities;

/**
 * {@link ProjectInformation} implementation for {@link GenericProject}.
 * <p>
 * The display name is derived from the wrapped directory name. The icon is
 * chosen from the folder content: Git repositories use the Git repository
 * icon, everything else falls back to the generic NetBeans project icon.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectInformation implements ProjectInformation {

    private final GenericProject project;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Creates project information for the given generic project.
     *
     * @param project the generic project to describe
     */
    public GenericProjectInformation(GenericProject project) {
        this.project = project;
    }

    @Override
    public String getName() {
        return project.getProjectDirectory().getNameExt();
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    /**
     * Resolves the icon for the project. Git repositories get the Git
     * repository icon; all other generic projects get the standard project
     * icon.
     *
     * @return the resolved icon, or {@code null} if no icon is available
     */
    @Override
    public Icon getIcon() {
        Icon gitIcon = ImageUtilities.loadImageIcon(
                "enginaar/modernity/genericprojects/git-icon_16.svg",
                true);

        if (gitIcon != null) {
            return gitIcon;
        }

        return ImageUtilities.loadImageIcon(
                "org/netbeans/modules/project/ui/resources/projectTab.png",
                true);
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
