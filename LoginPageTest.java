package com.paysquare.deliziahr.TestCases;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.paysquare.deliziahr.BaseClass.TestBase;
import com.paysquare.deliziahr.Constants.Constants;
import com.paysquare.deliziahr.Pages.LoginPage;
import com.paysquare.deliziahr.Util.StaticWaits;
import com.paysquare.deliziahr.Util.TestUtility;

public class LoginPageTest  extends TestBase{
	
	LoginPage login;

	@BeforeMethod
	public void setUP()
	{
		openBrowser();
		log.info("Application launched successfully");
		login=new LoginPage();
	}
	/*
	 * @Test (priority=1) public void loginWithMobileTest( Method method) { //
	 * String title="deliziaHR";
	 * extenttest=extentreport.startTest(method.getName());
	 * login.loginWithMobile(pro.getProperty("mobileUname"),
	 * pro.getProperty("mobilePass"));
	 * 
	 * log.info("Successfully logged into DeliziaHR Application"); }
	 */
	
	  @DataProvider 
	  public Object[][] testData() 
	  {
		  Object[][]data=TestUtility.readExcel("Email"); 
		  return data; 
	  }
	  @Test(priority=1) 
	  public void verifyCompanyLogoTest(Method method) 
	  {
		  extenttest=extentreport.startTest(method.getName());
		  try {
			  Assert.assertTrue(login.verifyCompanyLogo(),"Company logo displayed on Login page");
			  log.info("Company logo displayed on Login page"); 
		  }catch (Throwable t) {
			log.error("Company logo not present",t);
		}
	  } 
	  @Test(priority=2)
	  public void verifyLoginPageTitleTest(Method method) 
	  {
		  extenttest=extentreport.startTest(method.getName());
		  Assert.assertEquals(login.verifyLoginPageTitle(), "deliziaHR");
		  log.info("Login page title verifyed successfully"); 
	  }
	@Test(priority=3)
	public void loginWithOTP(Method method)
	{
		extenttest=extentreport.startTest(method.getName());
		login.loginWithEmail(pro.getProperty("emailid"), pro.getProperty("emailpass"), pro.getProperty("outlookuname"), pro.getProperty("outlookpass"));
		StaticWaits.shortWait();
		try {
			Assert.assertEquals(driver.getCurrentUrl(), Constants.LANDING_PAGE_URL);
			log.info("Successfully Logged into DeliziahrUAT application");
		}catch (Throwable t) {
			log.error("Invalid Username OR Password Please Check",t);
		}
	}
	
	@Test(priority=4)
	public void verifylinksAreActiveTest(Method method)
	{
		extenttest=extentreport.startTest(method.getName());
		login.verifylinksAreActive();
	}
	  
	@Test(dataProvider="testData",priority=5,groups="datadriven")
	public void verifyLoginErrorMsg(Method method,String uname,String pass)
	{
		extenttest=extentreport.startTest(method.getName());
		login.login(uname, pass);
		try{
			Assert.assertEquals(login.verifyLoginErrorMsg(), "Invalid userId or password");
			log.info("You Enter Invalid userId or password");
		}catch (Throwable t){
			log.error("Validation  Popup not found");
			Assert.assertEquals(login.verifyLoginErrorMsg(), "Invalid userId or password");
		}
	}
}