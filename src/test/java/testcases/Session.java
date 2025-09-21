package testcases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import base.BaseTest;

public class Session extends BaseTest {

    String number = "3";

    // @Test
    // public void hpBuildJobMenuNav(ITestContext context) {
    //     app.log("hpBuildJobMenuNav");
    //     app.buildNumberSel();
    //     app.hpSelectBuildJobPopMenuOne("Changes");
    //     app.buildNumberSel();
    //     app.hpSelectBuildJobPopMenuOne("Workspace");
    //     // app.buildNumberSel();
    //     // app.hpSelectBuildJobPopMenuOne("Build Now");
    //     app.buildNumberSel();
    //     app.hpSelectBuildJobPopMenuOne("Configure");
    //     app.buildNumberSel();
    //     app.hpSelectBuildJobPopMenuOne("Delete Project");
    //     // app.buildNumberSel();
    //     // app.hpSelectBuildJobPopMenuOne("Modules");
    //     // app.buildNumberSel();
    //     // app.hpSelectBuildJobPopMenuOne("GitHub");
    //     app.buildNumberSel();
    //     app.hpSelectBuildJobPopMenuOne("Rename");
    //     app.buildNumberSel();
    //     app.hpSelectBuildJobPopMenuOne("Credentials");
    // }

    
    @Test
    public void jobChanges(ITestContext context) {
        app.log("jobChanges");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenu("Workspace");
        app.buildNumberMenu(number);
        app.hpSelectBuildJobPopMenu("Changes");
    }
    
    @Test
    public void jobConsoleOutput(ITestContext context) {
        app.log("jobConsoleOutput");
        app.click("buildNameArrow_css");
        app.hpSelectBuildJobPopMenu("Workspace");
        app.buildNumberMenu(number);
        app.hpSelectBuildJobPopMenu("Console Output");
    }

    @Test
    public void jobEditBuildInf(ITestContext context) {
        app.log("jobEditBuildInf");
        app.click("buildNameArrow_css");
        app.hpSelectBuildJobPopMenu("Workspace");
        app.buildNumberMenu(number);
        app.hpSelectBuildJobPopMenu("Edit Build Information");
    }

    @Test
    public void jobDeleteBuild(ITestContext context) {
        app.log("jobDeleteBuild");
        app.click("buildNameArrow_css");
        app.hpSelectBuildJobPopMenu("Workspace");
        app.buildNumberMenu(number);
        app.hpSelectBuildJobPopMenu("Delete build");
    }

    @Test
    public void jobTimings(ITestContext context){
      app.log("jobTimings");
      app.click("buildNameArrow_css");
        app.hpSelectBuildJobPopMenu("Workspace");
        app.buildNumberMenu(number);
        app.hpSelectBuildJobPopMenu("Timings");
    }

 }