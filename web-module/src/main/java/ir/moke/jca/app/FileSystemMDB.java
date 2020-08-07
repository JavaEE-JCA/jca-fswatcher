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
import ir.moke.jca.api.Delete;
import ir.moke.jca.api.FSWatcher;
import ir.moke.jca.api.Modify;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import java.io.File;

@MessageDriven(activationConfig = {@ActivationConfigProperty(propertyName = "dir", propertyValue = "/home/mah454/test/")})
public class FileSystemMDB implements FSWatcher {

    @Create(".*\\.pdf")
    public void createPdf(File f) {
        System.out.println("Create file : " + f.getName());
    }

    @Delete(".*\\.pdf")
    public void deletePdf(File f) {
        System.out.println("Delete file : " + f.getName());
    }

    @Modify(".*\\.pdf")
    public void modifyPdf(File f) {
        System.out.println("Modify file : " + f.getName());
    }
}
