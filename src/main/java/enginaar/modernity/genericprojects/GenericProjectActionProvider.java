package enginaar.modernity.genericprojects;

import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.support.ProjectOperations;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.openide.util.Lookup;

/**
 * {@link ActionProvider} for {@link GenericProject}.
 * <p>
 * Exposes the project management commands (delete, copy, move, rename) and
 * delegates the actual work to the default project operations shipped with
 * NetBeans. Each command is enabled only when the corresponding operation
 * implementation is present in the project lookup.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectActionProvider
        implements ActionProvider {

    private final GenericProject project;

    /**
     * Creates the action provider for the given project.
     *
     * @param project the generic project the commands apply to
     */
    public GenericProjectActionProvider(GenericProject project) {
        this.project = project;
    }

    @Override
    public String[] getSupportedActions() {
        return new String[]{
            ActionProvider.COMMAND_DELETE,
            ActionProvider.COMMAND_COPY,
            ActionProvider.COMMAND_MOVE,
            ActionProvider.COMMAND_RENAME
        };
    }

    @Override
    public boolean isActionEnabled(String command, Lookup context)
            throws IllegalArgumentException {
        if (ActionProvider.COMMAND_DELETE.equals(command)) {
            return ProjectOperations.isDeleteOperationSupported(project);
        }
        if (ActionProvider.COMMAND_COPY.equals(command)) {
            return ProjectOperations.isCopyOperationSupported(project);
        }
        if (ActionProvider.COMMAND_MOVE.equals(command)
                || ActionProvider.COMMAND_RENAME.equals(command)) {
            return ProjectOperations.isMoveOperationSupported(project);
        }
        return false;
    }

    @Override
    public void invokeAction(String command, Lookup context)
            throws IllegalArgumentException {
        if (ActionProvider.COMMAND_DELETE.equals(command)) {
            DefaultProjectOperations.performDefaultDeleteOperation(project);
        } else if (ActionProvider.COMMAND_COPY.equals(command)) {
            DefaultProjectOperations.performDefaultCopyOperation(project);
        } else if (ActionProvider.COMMAND_MOVE.equals(command)) {
            DefaultProjectOperations.performDefaultMoveOperation(project);
        } else if (ActionProvider.COMMAND_RENAME.equals(command)) {
            DefaultProjectOperations.performDefaultRenameOperation(project, null);
        } else {
            throw new IllegalArgumentException(command);
        }
    }
}