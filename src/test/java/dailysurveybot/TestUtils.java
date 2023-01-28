package dailysurveybot;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestUtils {

    /**
     * Получение файла в виде строки из папки resources
     *
     * @param fileName имя файла
     * @return содержимое файла в виде строки
     */
    public static String readFileAsString(String fileName) throws Exception {
        URL resource = TestUtils.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File not found! " + fileName);
        }
        return new String(Files.readAllBytes(Paths.get(resource.getFile())));
    }
}
