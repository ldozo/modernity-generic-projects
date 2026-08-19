package enginaar.modernity.genericprojects;

import java.util.List;
import org.junit.Test;
import org.openide.filesystems.FileObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link GenericProjectOperations}.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectOperationsTest extends AbstractGenericProjectTest {

    private GenericProjectOperations createOperations(FileObject root) {
        return new GenericProjectOperations(new GenericProject(root, createLookup(root)));
    }

    private org.openide.util.Lookup createLookup(FileObject root) {
        org.openide.util.lookup.InstanceContent content = new org.openide.util.lookup.InstanceContent();
        return new org.openide.util.lookup.AbstractLookup(content);
    }

    @Test
    public void dataFilesContainProjectDirectory() throws Exception {
        FileObject root = createTempRoot();
        GenericProjectOperations ops = createOperations(root);
        List<FileObject> data = ops.getDataFiles();
        assertEquals(1, data.size());
        assertEquals(root, data.get(0));
    }

    @Test
    public void metadataFilesEmptyForPlainFolder() throws Exception {
        FileObject root = createTempRoot();
        GenericProjectOperations ops = createOperations(root);
        assertTrue(ops.getMetadataFiles().isEmpty());
    }

    @Test
    public void metadataFilesIncludeMarkerAndNbproject() throws Exception {
        FileObject root = createTempRoot();
        FolderProjectMarker.create(root);
        ProjectConverter.convertToProject(root);

        GenericProjectOperations ops = createOperations(root);
        List<FileObject> metadata = ops.getMetadataFiles();

        assertEquals(2, metadata.size());
        boolean hasNbproject = false;
        boolean hasMarker = false;
        for (FileObject fo : metadata) {
            if (fo.getNameExt().equals("nbproject")) {
                hasNbproject = true;
            }
            if (fo.getNameExt().equals(FolderProjectMarker.MARKER)) {
                hasMarker = true;
            }
        }
        assertTrue("metadata must contain nbproject", hasNbproject);
        assertTrue("metadata must contain marker file", hasMarker);
    }

    @Test
    public void notifyDeletedRemovesMarker() throws Exception {
        FileObject root = createTempRoot();
        FolderProjectMarker.create(root);

        GenericProjectOperations ops = createOperations(root);
        ops.notifyDeleted();

        assertFalse(FolderProjectMarker.exists(root));
    }

    @Test
    public void notifyDeletingIsHarmless() throws Exception {
        FileObject root = createTempRoot();
        createOperations(root).notifyDeleting();
        assertTrue(root.isValid());
    }

    @Test
    public void notifyCopyingAndMovingAreHarmless() throws Exception {
        FileObject root = createTempRoot();
        GenericProjectOperations ops = createOperations(root);
        ops.notifyCopying();
        ops.notifyMoving();
        assertTrue(root.isValid());
    }
}