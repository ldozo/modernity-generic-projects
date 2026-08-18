package enginaar.modernity.genericprojects;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Icon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.openide.util.ImageUtilities;

public class GenericProjectInformation implements ProjectInformation {

    private final GenericProject project;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

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

    @Override
    public Icon getIcon() {
        Icon gitIcon = ImageUtilities.loadImageIcon(
                "org/netbeans/modules/git/resources/icons/repository.png",
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
