package testcases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import base.BaseTest;

public class Session1 extends BaseTest {

    String number = "19";
    
    @Test
    public void jobChanges(ITestContext context) {
        app.log("jobChanges");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenu("Workspace");
        app.buildNumberMenu(number);
        app.hpSelectBuildJobPopMenu("Console Output");
    }
    
    @Test
    public void jobConsoleOutput(ITestContext context) {
        app.log("jobConsoleOutput");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenu("Workspace");
        app.buildNumberMenu(number);
        app.hpSelectBuildJobPopMenu("Console Output");
    }

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

    @Test
    public void jobTimings(ITestContext context){
        app.log("jobTimings");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenu("Workspace");
        app.buildNumberMenu(number);
        app.hpSelectBuildJobPopMenu("Timings");
    }

 }