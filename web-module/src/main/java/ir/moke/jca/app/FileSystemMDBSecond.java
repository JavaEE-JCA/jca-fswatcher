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

package ir.moke.jca.app;

import ir.moke.jca.api.Create;
import ir.moke.jca.api.FSWatcher;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import java.io.File;
import java.io.IOException;

@MessageDriven(activationConfig = {@ActivationConfigProperty(propertyName = "dir", propertyValue = "/etc/")})
public class FileSystemMDBSecond implements FSWatcher {

    @Create("*.jpg") //only for test BAD REGEX
    public void createJPG(File f) {
        try {
            System.out.println(f.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
