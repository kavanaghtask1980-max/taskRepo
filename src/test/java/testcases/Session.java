package testcases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import base.BaseTest;

public class Session extends BaseTest {

    String number = "1";

    @Test
    public void hpBuildJobMenuNav(ITestContext context) {
        app.log("hpBuildJobMenuNav");
        app.click("buildNameMenu_css");
        app.hpSelectBuildJobPopMenuOne("Changes");
        app.click("buildNameMenu_css");
        app.hpSelectBuildJobPopMenuOne("Workspace");
        app.click("buildNameMenu_css");
        app.hpSelectBuildJobPopMenuOne("Build Now");
        app.click("buildNameMenu_css");
        app.hpSelectBuildJobPopMenuOne("Configure");
        app.click("buildNameMenu_css");
        app.hpSelectBuildJobPopMenuOne("Delete Maven project");
        app.click("buildNameMenu_css");
        app.hpSelectBuildJobPopMenuOne("Modules");
        app.click("buildNameMenu_css");
        app.hpSelectBuildJobPopMenuOne("GitHub");
        app.click("buildNameMenu_css");
        app.hpSelectBuildJobPopMenuOne("Rename");
        app.click("buildNameMenu_css");
        app.hpSelectBuildJobPopMenuOne("Credentials");
    }

    
    // @Test
    // public void jobChanges(ITestContext context) {
    //     app.log("jobChanges");
    //     app.click("buildNameMenu_css");
    //     app.hpSelectBuildJobPopMenu("Workspace");
    //     app.buildNumberMenu(number);
    //     app.hpSelectBuildJobPopMenu("Changes");
    // }
    
    // @Test
    // public void jobConsoleOutput(ITestContext context) {
    //     app.log("jobConsoleOutput");
    //     app.click("buildNameMenu_css");
    //     app.hpSelectBuildJobPopMenu("Workspace");
    //     app.buildNumberMenu(number);
    //     app.hpSelectBuildJobPopMenu("Console Output");
    // }

    // @Test
    // public void jobEditBuildInf(ITestContext context) {
    //     app.log("jobEditBuildInf");
    //     app.click("buildNameMenu_css");
    //     app.hpSelectBuildJobPopMenu("Workspace");
    //     app.buildNumberMenu(number);
    //     app.hpSelectBuildJobPopMenu("Edit Build Information");
    // }

    // @Test
    // public void jobDeleteBuild(ITestContext context) {
    //     app.log("jobDeleteBuild");
    //     app.click("buildNameMenu_css");
    //     app.hpSelectBuildJobPopMenu("Workspace");
    //     app.buildNumberMenu(number);
    //     app.hpSelectBuildJobPopMenu("Delete build");
    // }

    // @Test
    // public void jobTimings(ITestContext context){
    //   app.log("jobTimings");
    //   app.click("buildNameMenu_css");
    //     app.hpSelectBuildJobPopMenu("Workspace");
    //     app.buildNumberMenu(number);
    //     app.hpSelectBuildJobPopMenu("Timings");
    // }

 }