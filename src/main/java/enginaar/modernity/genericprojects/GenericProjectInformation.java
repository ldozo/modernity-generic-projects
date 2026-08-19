package enginaar.modernity.genericprojects;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Icon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;

/**
 * {@link ProjectInformation} implementation for {@link GenericProject}.
 * <p>
 * The display name is derived from the wrapped directory name. Git
 * repositories use the Git repository icon; all other generic projects keep
 * NetBeans' normal folder icon.
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
     * repository icon; all other generic projects use NetBeans' normal folder
     * icon.
     *
     * @return the resolved icon, or {@code null} when no custom icon applies
     */
    @Override
    public Icon getIcon() {
        if (isGit()) {
            Icon icon = GenericProjectIcons.gitIcon();
            if (icon != null) {
                return icon;
            }
        }
        return null;
    }

    private boolean isGit() {
        return project.getProjectDirectory().getFileObject(".git") != null;
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