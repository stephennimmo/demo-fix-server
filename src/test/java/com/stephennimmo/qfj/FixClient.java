package com.stephennimmo.qfj;

import quickfix.*;

import java.io.InputStream;

public class FixClient {

    public static void main(String[] args) throws ConfigError, InterruptedException {
        FixClient fixClient = new FixClient();
        fixClient.run();
    }

    void run() throws ConfigError, InterruptedException {
        Application application = new TestApplication();
        InputStream settingsInputStream = FixEngine.class.getResourceAsStream("/test.settings");
        SessionSettings sessionSettings = new SessionSettings(settingsInputStream);
        MessageStoreFactory storeFactory = new MemoryStoreFactory();
        LogFactory logFactory = new ScreenLogFactory(sessionSettings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        Initiator initiator = new SocketInitiator(application, storeFactory, sessionSettings, logFactory, messageFactory);
        initiator.start();
        Thread.currentThread().join();
    }

    public class TestApplication extends ApplicationAdapter implements Application {

    }

}
