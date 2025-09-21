package testcases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import base.BaseTest;

public class Sessiontest extends BaseTest {

    String number = "19";
    
    @Test
    public void jobEditBuildInf(ITestContext context) {
        app.log("jobEditBuildInf");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenu("Workspace");
        app.buildNumberMenu(number);
        app.hpSelectBuildJobPopMenu("Edit Build Information");
    }

    @Test
    public void jobDeleteBuild(ITestContext context) {
        app.log("jobDeleteBuild");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenu("Workspace");
        app.buildNumberMenu(number);
        app.hpSelectBuildJobPopMenu("Delete build");
    }

    // @Test
    // public void jobTimings(ITestContext context){
    //     app.log("jobTimings");
    //     app.buildNumberSel();
    //     app.hpSelectBuildJobPopMenu("Workspace");
    //     app.buildNumberMenu(number);
    //     app.hpSelectBuildJobPopMenu("Timings");
    // }

 }