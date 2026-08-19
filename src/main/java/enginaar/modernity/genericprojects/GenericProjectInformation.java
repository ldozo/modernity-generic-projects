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
 * The display name is derived from the wrapped directory name. Git
 * repositories use the Git repository icon, all other generic projects use
 * the enginar logo as their custom project icon.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectInformation implements ProjectInformation {

    private static final String GIT_ICON = "enginaar/modernity/genericprojects/git-icon_16.svg";
    private static final String ENGINAR_LOGO = "enginaar/modernity/genericprojects/enginar_logo.svg";

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

    /**
     * Resolves the icon resource for a project. Git repositories use the Git
     * repository icon, all other generic projects use the enginar logo.
     *
     * @param git whether the project is a Git repository
     * @return the icon resource path
     */
    static String resolveIconResource(boolean git) {
        return git ? GIT_ICON : ENGINAR_LOGO;
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
     * repository icon; all other generic projects use the enginar logo as
     * their custom project icon.
     *
     * @return the resolved icon, or {@code null} if no icon is available
     */
    @Override
    public Icon getIcon() {
        String resource = resolveIconResource(isGit());
        Icon icon = ImageUtilities.loadImageIcon(resource, false);
        if (icon != null) {
            return icon;
        }

        return ImageUtilities.loadImageIcon(
                "org/netbeans/modules/project/ui/resources/projectTab.png",
                true);
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
