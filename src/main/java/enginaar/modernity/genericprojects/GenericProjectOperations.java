package enginaar.modernity.genericprojects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.CopyOperationImplementation;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.MoveOperationImplementation;
import org.openide.filesystems.FileObject;

/**
 * Default {@link DeleteOperationImplementation},
 * {@link CopyOperationImplementation} and {@link MoveOperationImplementation}
 * for {@link GenericProject}.
 * <p>
 * The actual deletion, copying, moving and renaming of the folder is performed
 * by NetBeans' default project operations. This class only supplies the list of
 * metadata and data files that belong to the project.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectOperations
        implements DeleteOperationImplementation,
        CopyOperationImplementation,
        MoveOperationImplementation {

    private final GenericProject project;

    /**
     * Creates the project operations for the given project.
     *
     * @param project the generic project to operate on
     */
    public GenericProjectOperations(GenericProject project) {
        this.project = project;
    }

    @Override
    public List<FileObject> getMetadataFiles() {
        List<FileObject> metadata = new ArrayList<FileObject>();
        FileObject dir = project.getProjectDirectory();
        FileObject nbproject = dir.getFileObject("nbproject");
        if (nbproject != null) {
            metadata.add(nbproject);
        }
        FileObject marker = dir.getFileObject(".netbeans-folder-project");
        if (marker != null) {
            metadata.add(marker);
        }
        return metadata;
    }

    @Override
    public List<FileObject> getDataFiles() {
        return Collections.singletonList(project.getProjectDirectory());
    }

    @Override
    public void notifyDeleting() throws IOException {
        // nothing to prepare before the deletion
    }

    @Override
    public void notifyDeleted() throws IOException {
        FileObject dir = project.getProjectDirectory();
        if (dir.isValid()) {
            FolderProjectMarker.delete(dir);
        }
    }

    @Override
    public void notifyCopying() throws IOException {
        // nothing to prepare before the copy
    }

    @Override
    public void notifyCopied(Project original, File originalPath, String originalName) throws IOException {
        // nothing to clean up after the copy
    }

    @Override
    public void notifyMoving() throws IOException {
        // nothing to prepare before the move
    }

    @Override
    public void notifyMoved(Project original, File originalPath, String originalName) throws IOException {
        // nothing to clean up after the move
    }
}