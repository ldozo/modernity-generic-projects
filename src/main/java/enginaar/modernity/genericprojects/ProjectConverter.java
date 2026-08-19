package enginaar.modernity.genericprojects;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.ProjectManager;
import org.openide.filesystems.FileObject;

/**
 * Converts a plain folder into a permanent NetBeans project.
 * <p>
 * Conversion writes a {@code nbproject/project.xml} descriptor whose project
 * type is {@code enginaar.modernity.genericprojects}. Such projects are then
 * recognized by {@link GenericProjectFactory} and can be reopened through the
 * regular NetBeans project open flows.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public final class ProjectConverter {

    private static final Logger LOG = Logger.getLogger(ProjectConverter.class.getName());

    /** The project type identifier written to {@code project.xml}. */
    public static final String PROJECT_TYPE = "enginaar.modernity.genericproject";

    private ProjectConverter() {
    }

    /**
     * Converts the given folder into a permanent NetBeans project.
     *
     * @param folder the folder to convert
     * @throws IOException if the {@code nbproject/project.xml} file cannot be
     *         created or written
     */
    public static void convertToProject(FileObject folder) throws IOException {
        FileObject nbproject = folder.getFileObject("nbproject");

        if (nbproject == null) {
            nbproject = folder.createFolder("nbproject");
            LOG.log(Level.FINE, "Created nbproject folder: {0}", nbproject.getPath());
        }

        FileObject projectXml = nbproject.getFileObject("project.xml");

        if (projectXml == null) {
            projectXml = nbproject.createData("project", "xml");
            LOG.log(Level.FINE, "Created project.xml: {0}", projectXml.getPath());
        }

        String xml = String.format("""
                     <?xml version="1.0" encoding="UTF-8"?>
                     <project>
                        <type>
                            %s
                        </type> 
                     </project>""", PROJECT_TYPE);

        try (OutputStream out = projectXml.getOutputStream()) {
            out.write(xml.getBytes(StandardCharsets.UTF_8));
        }
        ProjectManager.getDefault().clearNonProjectCache();
        LOG.log(Level.INFO, "Converted to permanent project: {0}", folder.getPath());
    }
}