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
