package ir.moke.fs;

import java.io.IOException;
import java.nio.file.*;

public class MainTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        String filePath = "/home/aaa/file.pdf";
        System.out.println(filePath.matches(".*\\.pdf"));
        System.out.println(filePath.matches("[^_]*.pdf"));
/*
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Path.of("/home/mah454/test/");
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

        while (true) {
            WatchKey key = watchService.take();
            if (key != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("Event : " + event.kind() + " File Effected :" + event.context());
                }
                key.reset();
            }
        }
*/
    }
}
