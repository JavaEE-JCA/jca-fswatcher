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
