import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionStatus {
    String location = getLocation();

    public String checkSubscription () throws IOException, ParseException {
        initialSetup();
        return getData("state");
    }

    public void initialSetup() throws IOException {
        if (!Files.exists(Path.of(location))) {
            putData("state", "HAVENTPAIDFORTHEPROGRAMYET", "expiredIn", "0");
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