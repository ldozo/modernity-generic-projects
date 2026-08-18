package enginaar.modernity.genericprojects;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.spi.project.ui.ProjectOpenedHook;
import org.openide.filesystems.FileObject;

public class GenericProjectOpenedHook
        extends ProjectOpenedHook {

    private static final Logger LOG = Logger.getLogger(GenericProjectOpenedHook.class.getName());

    private final GenericProject project;

    public GenericProjectOpenedHook(GenericProject project) {
        this.project = project;
    }

    @Override
    protected void projectOpened() {
        LOG.log(Level.INFO, "PROJECT OPENED: {0}", project.getProjectDirectory().getPath());
    }

    @Override
    protected void projectClosed() {
        FileObject folder = project.getProjectDirectory();
        LOG.log(Level.INFO, "PROJECT CLOSED: {0}", folder.getPath());
        try {
            if (folder.getFileObject("nbproject/project.xml") != null) {
                LOG.log(Level.FINE, "Permanent project, keeping marker: {0}", folder.getPath());
                return;
            }
            FolderProjectMarker.delete(folder);
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Failed to delete marker on close: " + folder.getPath(), ex);
        }
    }
}
