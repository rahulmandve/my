package com.paysquare.deliziahr.Pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.paysquare.deliziahr.BaseClass.TestBase;
import com.paysquare.deliziahr.Util.StaticWaits;
import com.paysquare.deliziahr.Util.TestUtility;

public class LoginDAPage extends TestBase{
	
	@FindBy(name="email")
	WebElement email;
	
	@FindBy(name="password")
	WebElement password;
	
	@FindBy(xpath="//a[contains(text(),'Sign in')]")
	WebElement signinbtn;
	
	@FindBy(xpath="//input[@name='otp']")
	WebElement enterotp;
	
	@FindBy(xpath="//a[contains(text(),'Submit')]")
	WebElement submit;

	
	public LoginDAPage() {
	PageFactory.initElements(driver, this);
	}
	
	public void login(String uname,String pass)
	{
		email.sendKeys(uname);
		log.info("Email Id entered");
		password.sendKeys(pass);
		log.info("Password Entered");
		signinbtn.click();
		log.info("Clicked on Sign in Button");
		StaticWaits.shortWait();
		String otp=TestUtility.otpReader("rahul.mandwe259@gmail.com", "rahul@123");
		enterotp.sendKeys(otp);
		log.info("OTP entered");
		submit.click();
		log.info("clicked on OTP submit Button");
	}
	
}
