import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionStatus {
    String location = getLocation();

    public String checkSubscription () throws IOException, ParseException {
        return getData("state");
    }

    public void checkExpiration() throws IOException, ParseException {
        if (getData("state").equals("ALREADYPAIDFORTHEPROGRAM")) {
            LocalDateTime expiredDate = LocalDateTime.parse(getData("expiredD"));
            LocalDateTime currentDate = LocalDateTime.now();
            if (currentDate.isAfter(expiredDate)) {
                modifyData("state", "HAVENTPAIDFORTHEPROGRAMYET");
                modifyData("expiredD", "null");
            }
        }
    }

    public String getExpiredDate() throws IOException, ParseException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime expiredDate = currentDateTime.plusDays(30);
        System.out.println(expiredDate);
        return String.valueOf(expiredDate);
    }

    public void initialSetup() throws IOException {
        if (!Files.exists(Path.of(location))) {
            putData("state", "HAVENTPAIDFORTHEPROGRAMYET", "expiredD", "null");
        }
    }

    public String getData(String key) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileReader fileReader = new FileReader(location);
        Object object = parser.parse(fileReader);
        JSONObject readObject = (JSONObject) object;
        return (String) readObject.get(key);
    }

    public void putData(String state, String stateValue, String expiredD, String expiredDValue) throws IOException {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put(state, stateValue);
        jsonMap.put(expiredD, expiredDValue);
        JSONObject writeObject = new JSONObject(jsonMap);

        FileWriter fileWriter = new FileWriter(location);
        fileWriter.write(writeObject.toJSONString());
        fileWriter.flush();
        fileWriter.close();
    }

    public void modifyData(String key, String value) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileReader fileReader = new FileReader(location);
        Object object = parser.parse(fileReader);
        JSONObject readObject = (JSONObject) object;

        // Modify the value associated with the specified key
        readObject.put(key, value);

        // Write the updated JSON data back to the file
        FileWriter fileWriter = new FileWriter(location);
        fileWriter.write(readObject.toJSONString());
        fileWriter.flush();
        fileWriter.close();
    }

    public String osChecking() {
        return System.getProperty("os.name").toLowerCase();
    }

    public String getRoamingPath() {
        String osName = osChecking();
        if (osName.startsWith("windows")) {
            System.out.println("it is windows");
            return System.getProperty("user.home") + "\\AppData\\Roaming";
        } else if (osName.startsWith("linux")){
            System.out.println("it is linux");
            return System.getProperty("user.home") + "/.config/";
        } else {
            System.out.println("it is mac or other");
            return "infoFolder";
        }
    }

    public String getLocation () {
        String folderPath = getRoamingPath() + "EasyPaste";
        File folder = new File(folderPath);

        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("location creation completed!");
            } else {
                System.out.println("Failed to create location");
            }
        } else {
            System.out.println("Folder already exists");
        }

        return folderPath + "/info.json";
    }

}