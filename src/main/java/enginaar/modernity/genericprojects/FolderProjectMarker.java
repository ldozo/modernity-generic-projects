package enginaar.modernity.genericprojects;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;

public final class FolderProjectMarker {

    private static final Logger LOG = Logger.getLogger(FolderProjectMarker.class.getName());
    public static final String MARKER = ".netbeans-folder-project";

    private FolderProjectMarker() {
    }

    public static void create(FileObject folder) throws IOException {
        if (folder.getFileObject(MARKER) == null) {
            folder.createData(MARKER);
            LOG.log(Level.FINE, "Created marker: {0}", folder.getPath() + "/" + MARKER);
        }
    }

    public static boolean exists(FileObject folder) {
        return folder.getFileObject(MARKER) != null;
    }

    public static void delete(FileObject folder) throws IOException {
        FileObject marker = folder.getFileObject(MARKER);
        if (marker != null) {
            marker.delete();
            LOG.log(Level.FINE, "Deleted marker: {0}", folder.getPath() + "/" + MARKER);
        }
    }
}
