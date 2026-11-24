package tests.negative;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;

public class NegativeValidationTests extends BaseTest {

    @Test(groups = {"negative"}, description = "Verify New Item form blocks empty job names and rejects duplicate job names")
    public void verifyEmptyAndDuplicateJobValidation() {
        app.openNewItemPage();

        // 1. Boundary check: OK button stays disabled when job name is blank
        Assert.assertFalse(app.isNewItemOkEnabled(), "Expected OK button to be disabled when job name is empty");

        // 2. Negative check: entering an existing seeded job name triggers duplicate error message
        String validationMessage = app.enterNewItemNameAndGetValidationError("seed-smoke-freestyle");
        Assert.assertTrue(
            validationMessage.contains("already exists"),
            "Expected duplicate job validation warning on New Item screen, got: " + validationMessage
        );
    }
}
