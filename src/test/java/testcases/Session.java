package testcases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import base.BaseTest;

public class Session extends BaseTest {

    String number = "19";

    @Test
    public void hpBuildJobMenuNav(ITestContext context) {
        app.log("hpBuildJobMenuNav");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenuOne("Changes");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenuOne("Workspace");
        // app.buildNumberSel();
        // app.hpSelectBuildJobPopMenuOne("Build Now");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenuOne("Configure");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenuOne("Delete Project");
        // app.buildNumberSel();
        // app.hpSelectBuildJobPopMenuOne("Modules");
        // app.buildNumberSel();
        // app.hpSelectBuildJobPopMenuOne("GitHub");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenuOne("Rename");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenuOne("Credentials");
    }

    


 }