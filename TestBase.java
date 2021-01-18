package com.paysquare.deliziahr.BaseClass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.paysquare.deliziahr.Constants.Constants;
import com.paysquare.deliziahr.Util.TestUtility;
import com.paysquare.deliziahr.Util.WebEventListner;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * @author Rahul Mandwe
 *
 */
public class TestBase {
	
	public static WebDriver driver;
	public static Properties pro;
	public static EventFiringWebDriver e_driver;
	public static WebEventListner eventListener;
	public static Logger log;
	public static ChromeOptions chromeOptions;
	
	public static ExtentReports extentreport;
	public static ExtentTest extenttest;
	
	public TestBase()
	{
			log=Logger.getLogger(this.getClass());
			try
			{
				pro=new Properties();
				FileInputStream fis= new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\java\\com\\paysquare\\deliziahr\\config\\Configuration.properties");
				pro.load(fis);
			}catch(FileNotFoundException e){
				e.getStackTrace();
			
			}catch (Exception e1) {
				e1.getStackTrace();
			}
	}
	@BeforeTest
	public void setExtent()
	{
		TestUtility.setDateForLog4j();	
		extentreport =new ExtentReports(System.getProperty("user.dir")+"/DeliziaHRExtentResults/DeliziaExtentReport"+TestUtility.getSystemDate()+".html", null);
	}
	public static void openBrowser()
	{
		String browsername=	pro.getProperty("browser");
		String headless=pro.getProperty("headless");
		
		if(browsername.equalsIgnoreCase("chrome"))
		{
			if(headless.equalsIgnoreCase("yes"))
			{
				chromeOptions =new ChromeOptions(); 
				chromeOptions.addArguments("headless");
				chromeOptions.addArguments("window-size=1200x600");
				System.setProperty("webdriver.chrome.driver",Constants.CHROME_DRIVER_PATH);
				driver=new ChromeDriver(chromeOptions);
			}
			else 
			{
				chromeOptions = new ChromeOptions();
				chromeOptions.setExperimentalOption("useAutomationExtension", false);
				chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
				System.setProperty("webdriver.chrome.driver",Constants.CHROME_DRIVER_PATH);
				driver=new ChromeDriver();
			}
		}
		else if(browsername.equalsIgnoreCase("firefox"))
		{
			System.setProperty("webdriver.gecko.driver", Constants.FIRFOX_DRIVER_PATH);
			driver=new FirefoxDriver();

		}
		else if(browsername.equalsIgnoreCase("ie"))
		{
			System.setProperty("webdriver.ie.driver", Constants.IE_DRIVER_PATH);
			driver=new InternetExplorerDriver();
		}
		else 
		{
			System.out.print("Browser name Passed in property file not Matched Please check.......");
		}
			e_driver = new EventFiringWebDriver(driver);
			eventListener = new WebEventListner();
			e_driver.register(eventListener);
			driver = e_driver;
		  	 
		  driver.manage().window().maximize(); 
		  driver.manage().deleteAllCookies();
		  driver.manage().timeouts().pageLoadTimeout(Constants.PAGE_LOAD_TIMEOUT,TimeUnit.SECONDS);
		  driver.manage().timeouts().implicitlyWait(Constants.IMPLICIT_WAIT,TimeUnit.SECONDS);
		  
		  driver.get(pro.getProperty("url"));
	}
	
	@AfterTest
	public void endReport()
	{
		extentreport.flush();
	}
	@AfterMethod(alwaysRun=true)
	public void tearDown(ITestResult result) throws IOException
	{
		if(result.getStatus()==result.FAILURE)
		{
			extenttest.log(LogStatus.FAIL, "Test Case Failed is "+result.getName());
			extenttest.log(LogStatus.FAIL, "Test Case Failed is "+result.getThrowable());
			
			String screenshotpath=TestUtility.getScreenshot(driver, result.getName());
			extenttest.log(LogStatus.FAIL, extenttest.addScreenCapture(screenshotpath));
			
		}else if(result.getStatus()==result.SKIP)
		{
			extenttest.log(LogStatus.SKIP, "Test case Skipped is "+result.getName());
			
		}else if(result.getStatus()==result.SUCCESS)
		{
			extenttest.log(LogStatus.PASS, "Test case passed is "+result.getName());
		}
	extentreport.endTest(extenttest);
//	driver.quit();
	log.info("browser terminated");
	log.info("-------------------------------------------------------------------------");
	}
}