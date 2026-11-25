package tests.security;

import org.testng.Assert;
import org.testng.annotations.Test;

import api.JenkinsApiClient;

public class SecurityAccessTests {

    @Test(groups = {"security"}, description = "Verify unauthenticated state-changing POST requests are rejected with HTTP 403")
    public void verifyUnauthenticatedAccessIsRejected() {
        JenkinsApiClient api = new JenkinsApiClient();

        int unauthCreateStatus = api.postUnauthenticatedStatus("createItem?name=unauth-security-probe");
        Assert.assertEquals(
            unauthCreateStatus,
            403,
            "Expected unauthenticated POST /createItem to be rejected with HTTP 403"
        );
    }

    
}
