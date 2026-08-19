package enginaar.modernity.genericprojects;

import javax.swing.Icon;
import org.openide.util.ImageUtilities;

/**
 * Shared project icon resources and resolution for {@link GenericProject}.
 * <p>
 * Git repositories use the Git repository icon; all other generic projects
 * fall back to NetBeans' normal folder icon.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
final class GenericProjectIcons {

    static final String GIT_ICON = "enginaar/modernity/genericprojects/git-icon_16.svg";

    private GenericProjectIcons() {
    }

    /**
     * Resolves the Git repository icon.
     *
     * @return the Git repository icon, or {@code null} if it cannot be loaded
     */
    static Icon gitIcon() {
        return ImageUtilities.loadImageIcon(GIT_ICON, false);
    }
}