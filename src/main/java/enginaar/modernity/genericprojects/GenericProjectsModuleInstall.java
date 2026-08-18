package enginaar.modernity.genericprojects;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.modules.ModuleInstall;

/**
 * Module lifecycle handler for the Modernity Generic Projects module.
 * <p>
 * Registered through the {@code OpenIDE-Module-Install} manifest attribute.
 * All lifecycle callbacks only produce diagnostic log output.
 *
 * @author Kenan Erarslan (kenan@enginaar.com)
 */
public class GenericProjectsModuleInstall extends ModuleInstall {

    private static final Logger LOG = Logger.getLogger(GenericProjectsModuleInstall.class.getName());

    @Override
    public void restored() {
        LOG.log(Level.INFO, "Modernity Generic Projects module restored");
    }

    @Override
    public boolean closing() {
        LOG.log(Level.INFO, "Modernity Generic Projects module closing");
        return true;
    }

    @Override
    public void close() {
        LOG.log(Level.INFO, "Modernity Generic Projects module closed");
    }

    @Override
    public void uninstalled() {
        LOG.log(Level.INFO, "Modernity Generic Projects module uninstalled");
    }
}