package enginaar.modernity.genericprojects;

import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.openide.util.ImageUtilities;

public class AdditionalProjectInformation
        implements ProjectInformation {

    private final AdditionalProject project;

    public AdditionalProjectInformation(
            AdditionalProject project) {

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
    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
    }

    @Override
    public void removePropertyChangeListener(
            PropertyChangeListener listener) {
    }
}
