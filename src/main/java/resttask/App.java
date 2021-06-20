package resttask;

import resttask.fileutils.Extractor;
import resttask.services.SendingService;

import java.nio.file.Path;
import java.util.logging.Logger;

import static java.nio.file.Files.exists;
import static resttask.fileutils.Extractor.extractFile;


public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getSimpleName());

    public static void main(String[] args) {

        Path csvFilePath = Path.of(Extractor.TARGET_FOLDER + "/data.csv");

        if (exists(csvFilePath)) {
            LOGGER.info("File already extracted, skipping");
        } else {
            if (args.length < 2 || args[0].isBlank() || args[1].isBlank()) {
                LOGGER.warning("Lack of arguments to extract file, shutting down program!");
                return;
            }
            extractFile(args[0], args[1].toCharArray());
        }

        SendingService sendingService = SendingService.getInstance();
        sendingService.sendData(csvFilePath);
    }
}
