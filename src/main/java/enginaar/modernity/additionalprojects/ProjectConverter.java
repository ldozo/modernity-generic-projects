package enginaar.modernity.additionalprojects;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.netbeans.api.project.ProjectManager;
import org.openide.filesystems.FileObject;

public final class ProjectConverter {

    private ProjectConverter() {
    }

    public static void convertToProject(
            FileObject folder)
            throws IOException {

        FileObject nbproject = folder.getFileObject("nbproject");

        if (nbproject == null) {
            nbproject = folder.createFolder("nbproject");
        }

        FileObject projectXml = nbproject.getFileObject("project.xml");

        if (projectXml == null) {

            projectXml = nbproject.createData("project", "xml");
        }

        String xml =
                """
                <?xml version="1.0" encoding="UTF-8"?>
                <project>
                    <type>
                        enginaar.modernity.additionalprojects
                    </type>
                </project>
                """;

        try (OutputStream out =
                     projectXml.getOutputStream()) {

            out.write(
                    xml.getBytes(
                            StandardCharsets.UTF_8));
        }
        ProjectManager.getDefault().clearNonProjectCache();
    }
}