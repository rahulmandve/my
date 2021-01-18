package com.paysquare.deliziahr.TestCases;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.paysquare.deliziahr.BaseClass.TestBase;
import com.paysquare.deliziahr.Constants.Constants;
import com.paysquare.deliziahr.Pages.InvestmenLICPage;
import com.paysquare.deliziahr.Pages.LoginDAPage;

public class Investmen_LIC_PageTest extends TestBase{

	LoginDAPage login;
	InvestmenLICPage lic;
	
	@BeforeMethod
	public void setUp()
	{
		openBrowser();
		log.info("Application launched successfully");
		login=new LoginDAPage();
		login.login(pro.getProperty("emailid"), pro.getProperty("emailpass"));
		lic=new InvestmenLICPage();
	}
	@Test(priority=4)
	public void verifyDeclaredGrandTotalTest(Method method)
	{
		extenttest=extentreport.startTest(method.getName());
		if(lic.verifyDeclaredGrandTotal()==true)
		{
			Assert.assertTrue(true);
			log.info("Total + FutureNewPolicy=GrandTotal matched");
		}else {
			log.error("Total + FutureNewPolicy and GrandTotal not matched");
			Assert.assertTrue(false);
		}
	}
	@Test(priority=1)
	public void verifyLICMasterCreationTest(Method method)
	{
		extenttest=extentreport.startTest(method.getName());
		if(lic.verifyLICMasterCreation().equals(Constants.RECORD_SAVE_SUCCESS_MSG))
		{
			Assert.assertTrue(true);
			log.info(Constants.RECORD_SAVE_SUCCESS_MSG +" Go to Transaction Page to see Schedule.");
		}else {
			log.error("Please check You have Enter Correct Information");
			Assert.assertTrue(false);
		}
	}
	@Test(priority=2)
	public void verifyAnnualAmountTest(Method method)
	{
		extenttest=extentreport.startTest(method.getName());
		if(lic.verifyAnnualAmount()==true)
		{
			Assert.assertTrue(true);
			log.info("Actual and Excepted Annual Amount Matched");
		}else {
			log.error("Actual and Excepted Annual Amount not Matched");
			Assert.assertTrue(false);
		}
	}
	@Test(priority=3)
	public void verifyDefaultECSIsNO(Method method)
	{
		extenttest=extentreport.startTest(method.getName());
		if(lic.verifyDefaultECSIsNO()==true)
		{
			Assert.assertTrue(true);
			log.info("ECS No radio button is checked");
		}else {
			log.error("ECS No radio button is not checked");
			Assert.assertTrue(false);
		}
	}
	@Test(priority=5)
	public void verifySchedulCreationTest(Method method)
	{
		extenttest=extentreport.startTest(method.getName());
		lic.verifySchedulCreation();
	}
}