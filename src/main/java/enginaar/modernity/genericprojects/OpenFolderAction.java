package enginaar.modernity.genericprojects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.RequestProcessor;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Opens an arbitrary folder as a NetBeans project.
 * <p>
 * Available under {@code File &rarr; Open Folder...}. The selected directory is
 * opened directly when it is already a project (a Git repository, a permanent
 * folder project, or another recognized project type). Otherwise the user can
 * choose to open it as a temporary folder project or convert it into a
 * permanent NetBeans project.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
@ActionID(
        category = "File",
        id = "enginaar.modernity.genericprojects.OpenFolderAction"
)
@ActionRegistration(
        displayName = "Open Folder...",
        iconBase = "enginaar/modernity/genericprojects/open-folder-icon_16.svg"
)
@ActionReference(
        path = "Menu/File",
        position = 410
)
public final class OpenFolderAction
        implements ActionListener {

    private static final Logger LOG = Logger.getLogger(OpenFolderAction.class.getName());
    private static final RequestProcessor RP = new RequestProcessor("OpenFolderAction", 1);

    public OpenFolderAction() {
        LOG.log(Level.INFO, "OpenFolderAction instantiated - Action registered");
    }

    /**
     * Invoked when the user selects "Open Folder...". Picks a directory and
     * opens it as a project, or offers to open/convert it when it is not a
     * recognized project.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        JFileChooser chooser = new JFileChooser();

        chooser.setFileSelectionMode(
                JFileChooser.DIRECTORIES_ONLY);

        chooser.setMultiSelectionEnabled(false);

        int result = chooser.showOpenDialog(null);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFolder = chooser.getSelectedFile();

        RP.post(() -> {
            FileObject folder = FileUtil.toFileObject(selectedFolder);

            if (folder == null) {
                LOG.log(Level.WARNING, "Selected folder is not a valid FileObject: {0}", selectedFolder);
                return;
            }

            try {
                Project project = ProjectManager.getDefault().findProject(folder);

                if (project != null) {
                    LOG.log(Level.INFO, "Opening existing project: {0}", folder.getPath());
                    openProject(project);
                    return;
                }

                final int choice = showOptionDialog();

                if (choice == 0) {
                    LOG.log(Level.INFO, "Creating temporary folder project: {0}", folder.getPath());
                    FolderProjectMarker.create(folder);
                    ProjectManager.getDefault().clearNonProjectCache();
                    Project newProject = ProjectManager.getDefault().findProject(folder);
                    if (newProject != null) {
                        openProject(newProject);
                    }
                    return;
                }

                if (choice == 1) {
                    LOG.log(Level.INFO, "Converting to permanent project: {0}", folder.getPath());
                    ProjectConverter.convertToProject(folder);
                    ProjectManager.getDefault().clearNonProjectCache();
                    Project newProject = ProjectManager.getDefault().findProject(folder);
                    if (newProject != null) {
                        openProject(newProject);
                    }
                }

            } catch (IOException | IllegalArgumentException ex) {
                LOG.log(Level.SEVERE, "Failed to open folder: " + folder.getPath(), ex);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            null,
                            ex.getLocalizedMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }

    private void openProject(Project project) {
        SwingUtilities.invokeLater(() -> {
            OpenProjects.getDefault().open(new Project[]{project}, true);
            TopComponent projectsTab = WindowManager.getDefault().findTopComponent("projectTabLogical_tc");
            if (projectsTab != null) {
                projectsTab.requestActive();
            }
        });
    }

    private int showOptionDialog() {
        final int[] result = new int[1];
        try {
            SwingUtilities.invokeAndWait(() -> {
                Object[] options = {"Open As Folder", "Convert To Project", "Cancel"};
                result[0] = JOptionPane.showOptionDialog(
                        null,
                        "This folder is not a NetBeans project.",
                        "Open Folder",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
            });
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Option dialog failed", ex);
            result[0] = 2; // Cancel
        }
        return result[0];
    }
}
