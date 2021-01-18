package com.paysquare.deliziahr.TestCases;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.paysquare.deliziahr.BaseClass.TestBase;
import com.paysquare.deliziahr.Pages.LoginDAPage;

public class LoginDAPageTest extends TestBase{
	
	LoginDAPage login;
	
	
	@BeforeTest
	public void setUp()
	{
		openBrowser();
		log.info("Application launched successfully");
		login=new LoginDAPage();
		
	}
	@Test
	public void loginTest(Method method)
	{
		 extenttest=extentreport.startTest(method.getName());
		 
			 login.login(pro.getProperty("emailid"), pro.getProperty("emailpass"));
			 System.out.println(driver.getTitle());
			
			 if(driver.getTitle().equals(":: Epic :: Log In"))
			 {
				 Assert.assertTrue(driver.getTitle().equals(":: Epic :: Log In"), "Unsuccessfull Login");
				 log.info("Successfully Logged into DeliziahrUAT application");
			 }else {
				 log.error("Unsuccessfull Login");
				 Assert.assertTrue(false);
			 }
		
	}

}
