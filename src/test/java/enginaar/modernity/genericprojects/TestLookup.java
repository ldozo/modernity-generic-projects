package enginaar.modernity.genericprojects;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * Default lookup used for unit tests that run without the full NetBeans module
 * system. It provides the normal {@code META-INF/services} lookup plus a
 * mock {@link org.netbeans.spi.project.ProjectManagerImplementation} so that
 * {@link org.netbeans.api.project.ProjectManager} can be bootstrapped.
 * <p>
 * Selected through the {@code org.openide.util.Lookup} system property
 * configured in {@code pom.xml} (surefire).
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public final class TestLookup extends ProxyLookup {

    public TestLookup() {
        super(mockLookup(), lookups());
    }

    private static org.openide.util.lookup.AbstractLookup mockLookup() {
        InstanceContent ic = new InstanceContent();
        ic.add(new TestProjectManagerImpl());
        return new AbstractLookup(ic);
    }

    private static Lookup lookups() {
        return Lookups.metaInfServices(TestLookup.class.getClassLoader());
    }
}