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

import ir.moke.jca.api.Create;
import ir.moke.jca.api.Delete;
import ir.moke.jca.api.Modify;

import javax.resource.spi.endpoint.MessageEndpoint;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.regex.PatternSyntaxException;

public class FileSystemWatcherThread extends Thread {


    private final FileSystemWatcherResourceAdapter ra;

    private final WatchService watchService;

    public FileSystemWatcherThread(WatchService watchService, FileSystemWatcherResourceAdapter ra) {
        this.ra = ra;
        this.watchService = watchService;
    }

    @Override
    public void run() {
        try {
            while (true) {
                WatchKey key = watchService.take();
                if (key != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        MessageEndpointFactory messageEndpointFactory = ra.getListeners().get(key);
                        MessageEndpoint endpoint = messageEndpointFactory.createEndpoint(null);
                        Class<?> beanClass = ra.getBeanClass(messageEndpointFactory);
                        Path path = (Path) event.context();
                        Method[] methods = beanClass.getDeclaredMethods();
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(Create.class) && event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                                if (isPatternMatch(path, method.getAnnotation(Create.class).value())) {
                                    invoke(endpoint, path, method);
                                }
                            } else if (method.isAnnotationPresent(Delete.class) && event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                                if (isPatternMatch(path, method.getAnnotation(Delete.class).value())) {
                                    invoke(endpoint, path, method);
                                }
                            } else if (method.isAnnotationPresent(Modify.class) && event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                                if (isPatternMatch(path, method.getAnnotation(Modify.class).value())) {
                                    invoke(endpoint, path, method);
                                }
                            }
                        }
                    }
                    key.reset();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isPatternMatch(Path path, String value) {
        boolean patternMatch;
        try {
            patternMatch = path.toString().matches(value);
        } catch (PatternSyntaxException e) {
            patternMatch = false;
        }
        return patternMatch;
    }

    private void invoke(MessageEndpoint endpoint, Path path, Method method) {
        try {
            endpoint.beforeDelivery(method);
            method.invoke(endpoint, path.toFile());
            endpoint.afterDelivery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
