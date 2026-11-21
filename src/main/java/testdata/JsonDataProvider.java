package testdata;

import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.DataProvider;

public class JsonDataProvider {

    private static final String TEST_DATA_DIR = System.getProperty("user.dir") + "/src/test/resources/testdata/";

    @DataProvider(name = "jenkinsJobData")
    public static Object[][] getJenkinsJobData(Method method) {
        return loadJsonArray("jenkinsJobs.json");
    }

    @DataProvider(name = "seedJobData")
    public static Object[][] getSeedJobData(Method method) {
    return loadJsonArray("seedJobs.json");
    }

    public static Object[][] loadJsonArray(String fileName) {
        List<Object[]> activeRows = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(TEST_DATA_DIR + fileName)) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            for (Object item : jsonArray) {
                JSONObject row = (JSONObject) item;
                String runMode = (String) row.getOrDefault("runMode", "Y");

                if ("Y".equalsIgnoreCase(runMode)) {
                    activeRows.add(new Object[]{ row });
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JSON test data from: " + fileName, e);
        }

        return activeRows.toArray(new Object[0][0]);
    }
}