package resttask.services;

import com.google.gson.Gson;
import okhttp3.*;
import resttask.dtos.Record;
import resttask.httpClient.HttpClientConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.substringsBetween;

public class SendingService {

    private Gson gson;
    private OkHttpClient httpClient;
    private static final Logger LOGGER = Logger.getLogger(SendingService.class.getSimpleName());

    private SendingService() {
        gson = new Gson();
        httpClient = HttpClientConfiguration.getHttpClient();
    }

    public static SendingService getInstance() {
        return new SendingService();
    }

    public void sendData(Path dataFilePath) {
        prepareDataForSend(dataFilePath)
                .forEach(this::executeRequest);
    }

    private List<Request> prepareDataForSend(Path dataFile) {
        List<String> recordsDataCSV;
        try {
            recordsDataCSV = Files.readAllLines(dataFile);
        } catch (IOException e) {
            LOGGER.warning("Error during CSV file loading");
            recordsDataCSV = emptyList();
        }

        return recordsDataCSV.stream()
                .skip(1)
                .map(rec -> csvToRecord(rec.split(";")))
                .filter(Record::isValidEmail)
                .map(this::prepareRequest)
                .collect(Collectors.toList());
    }

    private Request prepareRequest(Record newRecord) {
        RequestBody payload = RequestBody.create(gson.toJson(newRecord), MediaType.parse("application/json"));
        return new Request.Builder()
                .url("http://54.70.230.245:80/")
                .post(payload)
                .build();
    }

    private Record csvToRecord(String[] csvRecord) {
        HashMap<Integer, List<String>> parameters = new HashMap();
        for (int i = 3; i < csvRecord.length; i++) {
            String parametersArray = normalize(csvRecord[i]);
            parameters.put(i, asList(substringsBetween(parametersArray, "'", "'")));
        }

        return Record.builder()
                .firstName(normalize(ofNullable(csvRecord[0]).orElse("")))
                .lastName(normalize(ofNullable(csvRecord[1]).orElse("")))
                .email(normalize(ofNullable(csvRecord[2]).orElse("")))
                .param0(ofNullable(parameters.get(3)).orElse(emptyList()))
                .param1(ofNullable(parameters.get(4)).orElse(emptyList()))
                .param2(ofNullable(parameters.get(5)).orElse(emptyList()))
                .param3(ofNullable(parameters.get(6)).orElse(emptyList()))
                .param4(ofNullable(parameters.get(7)).orElse(emptyList()))
                .param5(ofNullable(parameters.get(8)).orElse(emptyList()))
                .param6(ofNullable(parameters.get(9)).orElse(emptyList()))
                .param7(ofNullable(parameters.get(10)).orElse(emptyList()))
                .param8(ofNullable(parameters.get(11)).orElse(emptyList()))
                .build();
    }

    public boolean executeRequest(Request request) {
        try (Response response = httpClient.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            LOGGER.warning("Error during request sending: " + e.getMessage());
            return false;
        }
    }

    private String normalize(String toNormalize) {
        return toNormalize.trim().replace("\"", "");
    }
}