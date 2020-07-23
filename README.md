## JCA File System Watcher 

Custom JavaEE 8 jca resource adapter for watching changes on file system .

#### Simple build and run :     
**TomEE 8**     
`mvn clean compile install ; (cd ear-module/ ; mvn tomee:run)`

**Liberty/OpenLiberty**     
`mvn clean compile install ; (cd ear-module/ ; mvn liberty:run)`


***Example Implementation :***      
```java
@MessageDriven(activationConfig = {@ActivationConfigProperty(propertyName = "dir", propertyValue = "/path/of/directory/")})
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
```
