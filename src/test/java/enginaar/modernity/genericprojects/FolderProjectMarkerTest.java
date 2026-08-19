package enginaar.modernity.genericprojects;

import org.junit.Test;
import org.openide.filesystems.FileObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link FolderProjectMarker}.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class FolderProjectMarkerTest extends AbstractGenericProjectTest {

    @Test
    public void markerDoesNotExistByDefault() throws Exception {
        FileObject root = createTempRoot();
        assertFalse(FolderProjectMarker.exists(root));
    }

    @Test
    public void createMakesMarkerVisible() throws Exception {
        FileObject root = createTempRoot();
        FolderProjectMarker.create(root);
        assertTrue(FolderProjectMarker.exists(root));
        assertTrue(root.getFileObject(FolderProjectMarker.MARKER).isData());
    }

    @Test
    public void createIsIdempotent() throws Exception {
        FileObject root = createTempRoot();
        FolderProjectMarker.create(root);
        FolderProjectMarker.create(root);
        assertTrue(FolderProjectMarker.exists(root));
        int count = root.getChildren().length;
        assertEquals(1, count);
    }

    @Test
    public void deleteRemovesMarker() throws Exception {
        FileObject root = createTempRoot();
        FolderProjectMarker.create(root);
        FolderProjectMarker.delete(root);
        assertFalse(FolderProjectMarker.exists(root));
    }

    @Test
    public void deleteWhenAbsentIsHarmless() throws Exception {
        FileObject root = createTempRoot();
        FolderProjectMarker.delete(root);
        assertFalse(FolderProjectMarker.exists(root));
    }

    @Test
    public void markerConstantMatchesFileName() {
        assertEquals(".netbeans-folder-project", FolderProjectMarker.MARKER);
    }
}