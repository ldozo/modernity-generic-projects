package enginaar.modernity.genericprojects;

import java.io.IOException;
import org.openide.filesystems.FileObject;

public final class FolderProjectMarker {

    public static final String MARKER
            = ".netbeans-folder-project";

    private FolderProjectMarker() {
    }

    public static void create(
            FileObject folder)
            throws IOException {

        if (folder.getFileObject(MARKER)
                == null) {

            folder.createData(MARKER);
        }
    }

    public static boolean exists(
            FileObject folder) {

        return folder.getFileObject(MARKER)
                != null;
    }

    public static void delete(
            FileObject folder)
            throws IOException {

        FileObject marker
                = folder.getFileObject(MARKER);

        if (marker != null) {
            marker.delete();
        }
    }
}
