package enginaar.modernity.genericprojects;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.LocalFileSystem;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Base class for unit tests that run without the full NetBeans module system.
 * <p>
 * Provides a temporary folder mounted on a {@link LocalFileSystem}. The tests
 * only exercise logic that works without the module system; code paths that
 * require {@link org.netbeans.api.project.ProjectManager} are made robust by
 * the production code itself.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public abstract class AbstractGenericProjectTest {

    private final List<File> tempRoots = new ArrayList<>();

    @After
    public void cleanTempRoots() {
        for (File root : tempRoots) {
            deleteRecursively(root);
        }
        tempRoots.clear();
    }

    protected FileObject createTempRoot() throws Exception {
        File dir = Files.createTempDirectory("gp-test").toFile();
        tempRoots.add(dir);
        LocalFileSystem fs = new LocalFileSystem();
        fs.setRootDirectory(dir);
        return fs.getRoot();
    }

    protected GenericProject createProject(FileObject folder) {
        InstanceContent content = new InstanceContent();
        AbstractLookup lookup = new AbstractLookup(content);
        GenericProject project = new GenericProject(folder, lookup);
        content.add(project);
        content.add(new GenericProjectInformation(project));
        content.add(new GenericProjectOpenedHook(project));
        content.add(new GenericProjectLogicalViewProvider(project));
        content.add(new GenericProjectOperations(project));
        content.add(new GenericProjectActionProvider(project));
        return project;
    }

    private static void deleteRecursively(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }
        file.delete();
    }
}