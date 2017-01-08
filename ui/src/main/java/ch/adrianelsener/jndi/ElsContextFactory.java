package ch.adrianelsener.jndi;

import ch.adrianelsener.train.config.Config;
import ch.adrianelsener.train.config.ConfigPropertyReader;
import ch.adrianelsener.train.gui.swing.SwingUi;
import org.apache.commons.io.IOUtils;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.io.InputStream;
import java.util.Hashtable;

public class ElsContextFactory implements InitialContextFactory {
    private final ElsMemContext memContext = new ElsMemContext();

    public ElsContextFactory() {
        Config config = readConfig();
        memContext.bind("els:config", config);
    }

    private Config readConfig() {
        final InputStream configfis = SwingUi.class.getResourceAsStream("sample.conf");
        final Config config = new ConfigPropertyReader(configfis).getConfig();
        IOUtils.closeQuietly(configfis);
        return config;
    }

    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        return memContext;
    }
}
