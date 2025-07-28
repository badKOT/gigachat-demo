package self.project.langchain.gigachat.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChatUtils {
    public static String getAuthKey() throws IOException {
        Path path = Paths.get("src/main/resources/application.yml");

        String read = Files.readAllLines(path).get(1).split(":\s")[1];
        return read;
    }
}
