package tests.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import api.JenkinsApiClient;

public class JenkinsApiContractTests {

    @Test(groups = {"api"}, description = "Verify REST API /api/json schema contract and non-existent job 404 handling")
    public void verifyApiSchemaContractAndNotFoundStatus() throws Exception {
        JenkinsApiClient api = new JenkinsApiClient();

        // 1. Contract check: /api/json returns 200 with required root keys
        String body = api.getEndpointBody("api/json");
        JSONObject root = (JSONObject) new JSONParser().parse(body);

        Assert.assertTrue(root.containsKey("jobs"), "Expected JSON response to contain 'jobs' array");
        Assert.assertTrue(root.get("jobs") instanceof JSONArray, "Expected 'jobs' to be a JSONArray");
        Assert.assertTrue(root.containsKey("mode"), "Expected JSON response to contain 'mode' field");

        // 2. Negative API contract: unknown job returns HTTP 404
        int notFoundStatus = api.getEndpointStatus("job/non-existent-job-999999/api/json", true);
        Assert.assertEquals(notFoundStatus, 404, "Expected HTTP 404 for unknown job endpoint");
    }
}
