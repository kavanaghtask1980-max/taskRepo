package tests.security;

import org.testng.Assert;
import org.testng.annotations.Test;

import api.JenkinsApiClient;

public class SecurityAccessTests {

    @Test(groups = {"security"}, description = "Verify protected administrative endpoints reject unauthenticated requests")
    public void verifyUnauthenticatedAccessIsRejected() {
        JenkinsApiClient api = new JenkinsApiClient();

        int unauthManageStatus = api.getEndpointStatus("manage/api/json", false);
        Assert.assertEquals(
            unauthManageStatus,
            403,
            "Expected unauthenticated request to /manage/api/json to be rejected with HTTP 403"
        );
    }
}
