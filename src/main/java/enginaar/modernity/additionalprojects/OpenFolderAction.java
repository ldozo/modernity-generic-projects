package enginaar.modernity.additionalprojects;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

@ActionID(
        category = "File",
        id = "ngr.modernity.additionalprojects.OpenFolderAction"
)
@ActionRegistration(
        displayName = "Open Folder..."
)
@ActionReference(
        path = "Menu/File",
        position = 1450
)
public final class OpenFolderAction
        implements ActionListener {

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

        File selectedFolder
                = chooser.getSelectedFile();

        FileObject folder
                = FileUtil.toFileObject(selectedFolder);

        if (folder == null) {
            return;
        }

        try {

            Project project = ProjectManager
                            .getDefault()
                            .findProject(folder);

            /*
         * Zaten proje ise doğrudan aç.
             */
            if (project != null) {

                OpenProjects.getDefault().open(
                        new Project[]{project},
                        true);

                return;
            }

            /*
         * Proje değil.
             */
            Object[] options = {
                "Open As Folder",
                "Convert To Project",
                "Cancel"
            };

            int choice
                    = JOptionPane.showOptionDialog(
                            null,
                            "This folder is not a NetBeans project.",
                            "Open Folder",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]);

            if (choice == 0) {

                FolderProjectMarker.create(folder);

                ProjectManager.getDefault().clearNonProjectCache();

                project = ProjectManager.getDefault().findProject(folder);

                if (project != null) {

                    OpenProjects.getDefault().open(
                            new Project[]{project},
                            true);
                }

                return;
            }

            /*
             * Convert To Project
             */
            if (choice == 1) {

                ProjectConverter.convertToProject(folder);

                ProjectManager.getDefault()
                        .clearNonProjectCache();

                project = ProjectManager
                        .getDefault()
                        .findProject(folder);

                if (project != null) {

                    OpenProjects.getDefault().open(
                            new Project[]{project},
                            true);
                }

            }
            /*
             * Cancel
             */

        } catch (HeadlessException | IOException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
