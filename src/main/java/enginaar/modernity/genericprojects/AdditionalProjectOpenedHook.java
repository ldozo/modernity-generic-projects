package enginaar.modernity.genericprojects;

import java.io.IOException;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.filesystems.FileObject;

public class AdditionalProjectOpenedHook
        extends ProjectOpenedHook {

    private final AdditionalProject project;

    public AdditionalProjectOpenedHook(
            AdditionalProject project) {

        this.project = project;
    }

    @Override
    protected void projectOpened() {

        System.out.println(
                "PROJECT OPENED: "
                + project.getProjectDirectory().getPath());
    }

    @Override
    protected void projectClosed() {

        FileObject folder = project.getProjectDirectory();
        System.out.println(
                "PROJECT CLOSED: "
                + project.getProjectDirectory().getPath());
        try {

            /*
             * Kalıcı proje ise dokunma.
             */
            if (folder.getFileObject(
                    "nbproject/project.xml") != null) {
                return;
            }

            /*
             * Geçici folder project markerını sil.
             */
            FolderProjectMarker.delete(folder);

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
