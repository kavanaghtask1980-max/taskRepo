package testcases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import base.BaseTest;

public class testOrder1 extends BaseTest {

    String number = "41";

    @Test
    public void hpBuildJobMenuNav(ITestContext context) {
        app.log("hpBuildJobMenuNav");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenuOne("Changes");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenuOne("Workspace");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenuOne("Configure");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenuOne("Delete Project");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenuOne("Rename");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenuOne("Credentials");
    }

        @Test
        public void jobChanges(ITestContext context) {
        app.log("jobChanges");
        app.buildNumberSel();
        app.hpSelectBuildJobPopMenu("Workspace");
        app.buildNumberMenu(number);
        app.hpSelectBuildJobPopMenu("Changes");
    }
    
 }