package ir.moke.jca.adapter;

import ir.moke.jca.api.FSWatcher;

import javax.resource.ResourceException;
import javax.resource.spi.Activation;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.InvalidPropertyException;
import javax.resource.spi.ResourceAdapter;

@Activation(messageListeners = FSWatcher.class)
public class FileSystemWatcherActivationSpec implements ActivationSpec {

    public FileSystemWatcherActivationSpec() {
        System.out.println("+-------------------------------------------+");
        System.out.println("|      FileSystemWatcherActivationSpec      |");
        System.out.println("+-------------------------------------------+");
    }

    private ResourceAdapter resourceAdapter;
    private String dir;

    @Override
    public void validate() throws InvalidPropertyException {

    }

    @Override
    public ResourceAdapter getResourceAdapter() {
        return this.resourceAdapter;
    }

    @Override
    public void setResourceAdapter(ResourceAdapter resourceAdapter) throws ResourceException {
        this.resourceAdapter = resourceAdapter;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
