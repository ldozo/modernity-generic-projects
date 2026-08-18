package enginaar.modernity.genericprojects;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.modules.ModuleInstall;

public class GenericProjectsModuleInstall extends ModuleInstall {

    private static final Logger LOG = Logger.getLogger(GenericProjectsModuleInstall.class.getName());

    @Override
    public void restored() {
        LOG.log(Level.INFO, "Modernity Additional Projects module restored");
    }

    @Override
    public boolean closing() {
        LOG.log(Level.INFO, "Modernity Additional Projects module closing");
        return true;
    }

    @Override
    public void close() {
        LOG.log(Level.INFO, "Modernity Additional Projects module closed");
    }

    @Override
    public void uninstalled() {
        LOG.log(Level.INFO, "Modernity Additional Projects module uninstalled");
    }
}