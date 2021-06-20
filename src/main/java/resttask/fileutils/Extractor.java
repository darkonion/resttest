package resttask.fileutils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import resttask.App;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public interface Extractor {

    String TARGET_FOLDER = "src/main/resources";
    Logger LOGGER = Logger.getLogger(App .class.getSimpleName());

    static void extractFile(String pathToZip, char[] password) {
        File file = new File( pathToZip);
        try {
            ZipFile zipFile = new ZipFile(file);

            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }

            FileHeader fileHeader = zipFile.getFileHeader("data.csv");
            zipFile.extractFile(fileHeader, TARGET_FOLDER);

        } catch (IOException e) {
            LOGGER.warning("Error during file extraction " + e.getMessage());
        }
    }

}
