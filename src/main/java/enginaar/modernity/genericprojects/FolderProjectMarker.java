package enginaar.modernity.genericprojects;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;

/**
 * Utility for managing the temporary folder-project marker file.
 * <p>
 * The marker ({@value #MARKER}) is a small empty file that tells NetBeans a
 * plain directory has been opened as a temporary project. It is created when
 * a folder is opened "as folder" and removed when the project closes.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public final class FolderProjectMarker {

    private static final Logger LOG = Logger.getLogger(FolderProjectMarker.class.getName());
    public static final String MARKER = ".netbeans-folder-project";

    private FolderProjectMarker() {
    }

    /**
     * Creates the marker file in the given folder if it does not exist yet.
     *
     * @param folder the folder that should be marked as a temporary project
     * @throws IOException if the marker file cannot be created
     */
    public static void create(FileObject folder) throws IOException {
        if (folder.getFileObject(MARKER) == null) {
            folder.createData(MARKER);
            LOG.log(Level.FINE, "Created marker: {0}", folder.getPath() + "/" + MARKER);
        }
    }

    /**
     * Checks whether the folder is marked as a temporary project.
     *
     * @param folder the folder to inspect
     * @return {@code true} if the marker file is present
     */
    public static boolean exists(FileObject folder) {
        return folder.getFileObject(MARKER) != null;
    }

    /**
     * Deletes the marker file from the given folder, if present.
     *
     * @param folder the folder whose marker should be removed
     * @throws IOException if the marker file cannot be deleted
     */
    public static void delete(FileObject folder) throws IOException {
        FileObject marker = folder.getFileObject(MARKER);
        if (marker != null) {
            marker.delete();
            LOG.log(Level.FINE, "Deleted marker: {0}", folder.getPath() + "/" + MARKER);
        }
    }
}
