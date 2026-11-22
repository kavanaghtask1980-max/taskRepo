package tests.sanity;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import base.BaseTest;

public class JenkinsSanityTests extends BaseTest {

    @Test(groups = {"sanity"}, description = "Verify global search locates seeded baseline jobs")
    public void verifyGlobalSearchFindsSeededJob(ITestContext context) {
        String targetJob = "seed-smoke-freestyle";
        app.searchJenkins(targetJob);

        Assert.assertTrue(
            app.getPageTitle().contains(targetJob),
            "Expected search navigation to open '" + targetJob + "', got: " + app.getPageTitle()
        );
    }
}
