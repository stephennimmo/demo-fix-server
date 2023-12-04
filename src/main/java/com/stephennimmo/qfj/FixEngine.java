package com.stephennimmo.qfj;

import io.quarkus.logging.Log;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import quickfix.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

@ApplicationScoped
public class FixEngine {

    private Acceptor acceptor;

    void onStart(@Observes StartupEvent ev) throws ConfigError, FileNotFoundException {
        Log.info("The application is starting...");
        Application application = new ServerApplication();
        InputStream settingsInputStream = FixEngine.class.getResourceAsStream("/qfj.settings");
        SessionSettings sessionSettings = new SessionSettings(settingsInputStream);
        MessageStoreFactory storeFactory = new MemoryStoreFactory();
        LogFactory logFactory = new ScreenLogFactory(sessionSettings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        acceptor = new SocketAcceptor(application, storeFactory, sessionSettings, logFactory, messageFactory);
        acceptor.start();
    }

    void onStop(@Observes ShutdownEvent ev) {
        Log.info("The application is stopping...");
        if (!Objects.isNull(acceptor)) {
            acceptor.stop();
        }
    }

    public class ServerApplication extends ApplicationAdapter implements Application {

    }

}
