package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import config.ConfigManager;

public class JenkinsApiClient {

    private final HttpClient client;
    private final String baseUrl;
    private final String authHeader;

        // At the top of JenkinsApiClient (alongside baseUrl, authHeader, client):
    private static final Set<String> DEFAULT_PROTECTED_JOBS = Set.of(
        "Jenkins-CI-Automation-Runner",
        "Jenkins-Declarative-Pipeline"
    );

    public int resetAllJobsExcept(String protectedJobName) {
        List<String> jobs = getAllJobNames();
        int deletedCount = 0;

        for (String job : jobs) {
            boolean isProtected = (protectedJobName != null && job.equalsIgnoreCase(protectedJobName))
                    || DEFAULT_PROTECTED_JOBS.stream().anyMatch(p -> p.equalsIgnoreCase(job));

            if (!isProtected) {
                int status = deleteJob(job);
                System.out.println("[API Reset] Deleted '" + job + "' -> HTTP " + status);
                deletedCount++;
            }
        }
        return deletedCount;
    }

    public JenkinsApiClient() {
        this.client = HttpClient.newHttpClient();
        String rawUrl = ConfigManager.getEnv("url");
        this.baseUrl = rawUrl.endsWith("/") ? rawUrl : rawUrl + "/";

        String user = ConfigManager.getEnv("jenkinsUser");
        // Prefer API token for REST calls; fall back to password if token not set
        String token = ConfigManager.getEnv("jenkinsToken");
        String secret = (token != null && !token.isBlank()) ? token : ConfigManager.getEnv("jenkinsPass");

        String encoded = Base64.getEncoder().encodeToString((user + ":" + secret).getBytes(StandardCharsets.UTF_8));
        this.authHeader = "Basic " + encoded;
    }

    public List<String> getAllJobNames() {
        List<String> jobNames = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "api/json?tree=jobs[name]"))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("GET /api/json failed with HTTP " + response.statusCode());
            }

            JSONObject root = (JSONObject) new JSONParser().parse(response.body());
            JSONArray jobs = (JSONArray) root.get("jobs");

            for (Object item : jobs) {
                JSONObject job = (JSONObject) item;
                jobNames.add((String) job.get("name"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Jenkins jobs via REST API", e);
        }
        return jobNames;
    }

        public int deleteJob(String jobName) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "job/" + jobName + "/doDelete"))
                    .header("Authorization", authHeader)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 401 || response.statusCode() == 403) {
                throw new RuntimeException(
                    "Jenkins API rejected deleteJob('" + jobName + "') with HTTP " + response.statusCode()
                    + ". Check that JENKINS_TOKEN (or configs/secrets.properties -> jenkinsToken) is valid!"
                );
            }
            return response.statusCode();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Jenkins job via API: " + jobName, e);
        }
    }


    public int resetAllJobsExcept(String protectedJobName) {
        List<String> jobs = getAllJobNames();
        int deletedCount = 0;

        Set<String> protectedJobs = Set.of(
    "Jenkins-CI-Automation-Runner",
    "Jenkins-Declarative-Pipeline"
);

        for (String job : jobs) {
            if (!job.equalsIgnoreCase(protectedJobName)) {
                int status = deleteJob(job);
                System.out.println("[API Reset] Deleted '" + job + "' -> HTTP " + status);
                deletedCount++;
            }
        }
        return deletedCount;
    }

        public int wipeAllJobs() {
        List<String> jobs = getAllJobNames();
        int deletedCount = 0;

        for (String job : jobs) {
            int status = deleteJob(job);
            System.out.println("[API Vanilla Wipe] Deleted '" + job + "' -> HTTP " + status);
            deletedCount++;
        }
        return deletedCount;
    }

        public int createFreestyleJobViaApi(String jobName, String description) {
        String configXml = """
                <?xml version='1.1' encoding='UTF-8'?>
                <project>
                  <description>%s</description>
                  <keepDependencies>false</keepDependencies>
                  <properties/>
                  <scm class="hudson.scm.NullSCM"/>
                  <canRoam>true</canRoam>
                  <disabled>false</disabled>
                  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
                  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
                  <triggers/>
                  <concurrentBuild>false</concurrentBuild>
                  <builders/>
                  <publishers/>
                  <buildWrappers/>
                </project>
                """.formatted(description);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "createItem?name=" + jobName))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/xml")
                    .POST(HttpRequest.BodyPublishers.ofString(configXml))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Jenkins job via REST API: " + jobName, e);
        }
    }

        public int getEndpointStatus(String relativePath, boolean authenticated) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + relativePath))
                    .GET();
            if (authenticated) {
                builder.header("Authorization", authHeader);
            }
            return client.send(builder.build(), HttpResponse.BodyHandlers.ofString()).statusCode();
        } catch (Exception e) {
            throw new RuntimeException("Failed GET request to: " + relativePath, e);
        }
    }

    public String getEndpointBody(String relativePath) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + relativePath))
                    .header("Authorization", authHeader)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("GET " + relativePath + " returned HTTP " + response.statusCode());
            }
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch body from: " + relativePath, e);
        }
    }

        public int postUnauthenticatedStatus(String relativePath) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + relativePath))
                    .header("Content-Type", "application/xml")
                    .POST(HttpRequest.BodyPublishers.ofString("<project/>"))
                    .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();
        } catch (Exception e) {
            throw new RuntimeException("Failed unauthenticated POST to: " + relativePath, e);
        }
    }
}