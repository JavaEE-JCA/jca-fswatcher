package ir.moke.jca.adapter;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Connector
public class FileSystemWatcherResourceAdapter implements ResourceAdapter {


    private FileSystem fileSystem;
    private final Map<WatchKey, MessageEndpointFactory> listeners = new ConcurrentHashMap<>();
    private final Map<MessageEndpointFactory, Class<?>> messageEndpointBeanMap = new ConcurrentHashMap<>();
    private WatchService watchService;

    public FileSystemWatcherResourceAdapter() {
        System.out.println("+--------------------------------------------+");
        System.out.println("|      FileSystemWatcherResourceAdapter      |");
        System.out.println("+--------------------------------------------+");
    }

    @Override
    public void start(BootstrapContext bootstrapContext) throws ResourceAdapterInternalException {
        try {
            fileSystem = FileSystems.getDefault();
            watchService = fileSystem.newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new FileSystemWatcherThread(watchService, this).start();
    }

    @Override
    public void stop() {
        try {
            watchService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endpointActivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) {
        FileSystemWatcherActivationSpec fsWatcherActivationSpec = (FileSystemWatcherActivationSpec) activationSpec;
        try {
            WatchKey wk = fileSystem.getPath(fsWatcherActivationSpec.getDir()).register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            listeners.put(wk, messageEndpointFactory);
            messageEndpointBeanMap.put(messageEndpointFactory, messageEndpointFactory.getEndpointClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endpointDeactivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) {
        for (WatchKey key : listeners.keySet()) {
            listeners.remove(key);
        }
        messageEndpointBeanMap.remove(messageEndpointFactory);
    }

    @Override
    public XAResource[] getXAResources(ActivationSpec[] activationSpecs) throws ResourceException {
        return new XAResource[0];
    }

    public Map<WatchKey, MessageEndpointFactory> getListeners() {
        return listeners;
    }

    public Class<?> getBeanClass(MessageEndpointFactory mef) {
        return this.messageEndpointBeanMap.get(mef);
    }
}
