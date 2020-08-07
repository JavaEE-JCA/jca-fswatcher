/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
            messageEndpointBeanMap.put(messageEndpointFactory, fsWatcherActivationSpec.getBeanClass() != null ? fsWatcherActivationSpec.getBeanClass() : messageEndpointFactory.getEndpointClass());
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
