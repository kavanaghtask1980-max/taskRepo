package testcases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import base.BaseTest;

public class testOrder0 extends BaseTest {

    String number = "41";
    
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

 }