package pages;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.MobileDriver;
import utils.ExcelFetch;
import wrappers.AmazonWrapper;

/**
 * @author sundar.siva
 *
 */
public class LoginPage extends AmazonWrapper {
	private static Properties prop;
	public Map<String, String> capData1 = new HashMap<String, String>();

	/**
	 * @param driver
	 * @param test - extent test for reporting
	 * @param capData1 - device capabilities
	 * Constructor which sets the driver,extent test and device capabilities
	 */
	public LoginPage(MobileDriver driver, ExtentTest test, Map<String, String> capData1) {
		this.driver = driver;
		this.test = test;
		this.capData1 = capData1;
		prop = new Properties();
		try {
			if (capData1.get("PlatformName").equalsIgnoreCase("Android")) {
				prop.load(new FileInputStream(new File("./Locators/Android/logIn.properties")));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 * @throws InterruptedException
	 * Gets the input from the excel sheet and login to the application
	 */
	public LoginPage signIn() throws InterruptedException {
		
		ExcelFetch excelFetch = new ExcelFetch();
		Map<String, String> signinDetailsMap = excelFetch.getDataFromExcel("Login_And_Place_Order_TC01", "account1");
		System.out.println("map" + signinDetailsMap);

		verifyStep("App launched", "PASS");


		verifyElementIsDisplayed(prop.getProperty("button.signInButtonNavigation"));
		click(prop.getProperty("button.signInButtonNavigation"));
			
		Thread.sleep(3000);
		
		verifyElementIsDisplayed(prop.getProperty("edit.emailField"));
		enterText(prop.getProperty("edit.emailField"), signinDetailsMap.get("Username"));
		click(prop.getProperty("button.continueButton"));
		
		verifyStep("Login Page displayed", "PASS");


		verifyElementIsDisplayed(prop.getProperty("edit.passwordField"));
		enterText(prop.getProperty("edit.passwordField"), signinDetailsMap.get("Password"));
		click(prop.getProperty("button.logInButton"));
		
		return this;
	}

	

}
