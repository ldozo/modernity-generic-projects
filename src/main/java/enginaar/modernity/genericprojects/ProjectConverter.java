package enginaar.modernity.genericprojects;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.ProjectManager;
import org.openide.filesystems.FileObject;

public final class ProjectConverter {

    private static final Logger LOG = Logger.getLogger(ProjectConverter.class.getName());

    private ProjectConverter() {
    }

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

        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <project>
                    <type>
                        enginaar.modernity.genericprojects
                    </type>
                </project>
                """;

        try (OutputStream out = projectXml.getOutputStream()) {
            out.write(xml.getBytes(StandardCharsets.UTF_8));
        }
        ProjectManager.getDefault().clearNonProjectCache();
        LOG.log(Level.INFO, "Converted to permanent project: {0}", folder.getPath());
    }
}