package com.paysquare.deliziahr.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.paysquare.deliziahr.BaseClass.TestBase;
import com.paysquare.deliziahr.Constants.Constants;

public class TestUtility extends TestBase {

	public static Workbook book;
	public static Sheet sheet;

	public static String hostName = "smtp.gmail.com";// "smtp-mail.outlook.com"

	public static void setDateForLog4j() {
		SimpleDateFormat dateformat = new SimpleDateFormat("_ddMMyyyy_HHmmss");
		System.setProperty("current_date", dateformat.format(new Date()));
		PropertyConfigurator.configure("./src/main/java/com/paysquare/deliziahr/config/Log4j.properties");
	}

	public static String getSystemDate() {
		DateFormat dateFormat = new SimpleDateFormat("_ddMMyyyy_HHmmss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static void waitStillElementvisible(WebDriver driver, WebElement element, int timeout) {
		new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(element));
	}

	public static String getScreenshot(WebDriver driver, String screenshotname) throws IOException {
		String datename = new SimpleDateFormat("_ddMMyyyy_HHmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);

		String destination = System.getProperty("user.dir") + "/FailedTestsScreenshot/" + screenshotname + datename
				+ ".png";
		File filedestination = new File(destination);
		FileUtils.copyFile(source, filedestination);

		return destination;
	}

	public static void sendKeysUsingJS(String value, WebElement element) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].value='" + value + "';", element);
	}

	public static Object[][] readExcel(String sheetname) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(Constants.TEST_DATA_SHEET_PATH);
			book = WorkbookFactory.create(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sheet = book.getSheet(sheetname);
		Object[][] data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
				data[i][j] = sheet.getRow(i + 1).getCell(j).toString();
			}
		}
		return data;
	}

	public static void activeLink(String linkurl) {
		try {
			URL url = new URL(linkurl);

			HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
			urlcon.setConnectTimeout(3000);
			urlcon.connect();
			if (urlcon.getResponseCode() == 200) {
				System.out.println(linkurl + "-" + urlcon.getResponseMessage());
				log.info(linkurl + "-" + urlcon.getResponseMessage());
				log.info(linkurl + " is Working Fine.......");
			}
			if (urlcon.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
				System.out
						.println(linkurl + "-" + urlcon.getResponseMessage() + "-" + HttpURLConnection.HTTP_NOT_FOUND);
				log.info(linkurl + "-" + urlcon.getResponseMessage() + "-" + HttpURLConnection.HTTP_NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String otpReader(String username, String password) {

		int messageCount = 0;
		int unreadMsgCount = 0;
		String emailSubject = null;
		Message emailMessage = null;
		String emailsubjectcontent = "Your one time password";
		String searchText = null;
		String otp = null;

		ArrayList<String> a = new ArrayList<String>();
		Properties sysProps = System.getProperties();
		sysProps.setProperty("mail.store.protocol", "imaps");

		try {
			Session session = Session.getInstance(sysProps, null);
			Store store = session.getStore();
			store.connect(hostName, username, password);
			Folder emailInbox = store.getFolder("INBOX");
			emailInbox.open(Folder.READ_WRITE);
			messageCount = emailInbox.getMessageCount();
			unreadMsgCount = emailInbox.getUnreadMessageCount();
			log.info("Trying to find OTP mail...");
			for (int i = messageCount; i > (messageCount - unreadMsgCount); i--) {
				emailMessage = emailInbox.getMessage(i);
				emailSubject = emailMessage.getSubject();
				if (emailSubject.contains(emailsubjectcontent) && !emailMessage.isSet(Flags.Flag.SEEN)) {
					log.info("OTP mail found");
					String line;
					StringBuffer buffer = new StringBuffer();
					BufferedReader reader = new BufferedReader(new InputStreamReader(emailMessage.getInputStream()));

					while ((line = reader.readLine()) != null) {
						a.add(buffer.append(line).toString());
					}
					log.info("Trying to find OTP...");
					line = a.get(8);
					searchText = line.replaceAll("[^0-9]", "");
					otp = searchText.substring(searchText.length() - 6);
					log.info("OTP fund");
					emailMessage.setFlag(Flags.Flag.SEEN, true);
					break;
				}
				emailMessage.setFlag(Flags.Flag.SEEN, true);
			}
			emailInbox.close(true);
			store.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			log.info("OTP Not found ");
		}
		return otp;
	}

	public static void selectDropDownValue(WebElement element, int index) {
		Select s = new Select(element);
		s.selectByIndex(index);
	}

	public static String selectDropDownValue(int index, WebElement element) {
		Select s = new Select(element);
		s.selectByIndex(index);
		WebElement fpay = s.getFirstSelectedOption();
		return fpay.getText();
	}
	public static void selectDropDownByVisibleText(WebElement element,String text)
	{
		Select s=new Select(element);
		s.selectByVisibleText(text);		
	}

	public static String randomNumber() {
		String randomNumber = RandomStringUtils.randomNumeric(10);
		return randomNumber;
	}

	public static int randomNumber(int dropdownindex) {
		Random num = new Random();
		return num.nextInt(dropdownindex);
	}

	public static double annualAmount(String preamt, String fpayment) {

		String m = " Monthly ";
		String q = " Quarterly ";
		String h = " Half-Yearly ";
		String y = " Yearly ";
		double monthlyMultiplier = 12;
		double quarterlyMultiplier = 4;
		double halfyearlyMultiplier = 2;
		double yearlyMultiplier = 1;

		if (fpayment.equalsIgnoreCase(m)) {
			double premt = Double.parseDouble(preamt.replace(",", ""));
			return premt * monthlyMultiplier;

		} else if (fpayment.equalsIgnoreCase(q)) {
			double premt = Double.parseDouble(preamt.replace(",", ""));
			return premt * quarterlyMultiplier;

		} else if (fpayment.equalsIgnoreCase(h)) {
			double premt = Double.parseDouble(preamt.replace(",", ""));
			return premt * halfyearlyMultiplier;

		} else if (fpayment.equalsIgnoreCase(y)) {
			double premt = Double.parseDouble(preamt.replace(",", ""));
			return premt * yearlyMultiplier;
		} else {
			return 0;
		}
	}
}