package amazon;

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
public class HomePage extends AmazonWrapper {
	private static Properties prop;
	public Map<String, String> capData1 = new HashMap<String, String>();

	/**
	 * @param driver
	 * @param test - extent test for reporting
	 * @param capData1 - device capabilities
	 * Constructor which sets the driver,extent test and device capabilities
	 */
	public HomePage(MobileDriver driver, ExtentTest test, Map<String, String> capData1) {
		this.driver = driver;
		this.test = test;
		this.capData1 = capData1;
		prop = new Properties();
		try {
			if (capData1.get("PlatformName").equalsIgnoreCase("Android")) {
				prop.load(new FileInputStream(new File("./Locators/Android/homePage.properties")));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 * Selects the language as English in the pop up displayed
	 */
	public HomePage selectLanguage() {
		verifyElementIsDisplayed(prop.getProperty("text.languageSelectionPopUp"));
		verifyElementIsDisplayed(prop.getProperty("radio.languageEnglishSettings"));
		click(prop.getProperty("radio.languageEnglishSettings"));
		verifyElementIsDisplayed(prop.getProperty("button.saveChangesButton"));
		click(prop.getProperty("button.saveChangesButton"));
		return this;
	}
	
	/**
	 * @return
	 * Validates the elements in the homepage after login
	 */
	public HomePage validateHomePage() {
		verifyElementIsDisplayed(prop.getProperty("image.amazonHomeLogo"));
		verifyElementIsDisplayed(prop.getProperty("button.microphoneButton"));
    	verifyElementIsDisplayed(prop.getProperty("button.locationSelection"));   
		verifyStep("Home Page Validated", "PASS");

		return this;
	}

	
	/**
	 * @return
	 * Validaetes whether the user is logged in successfully by checking the username in menu bar
	 */
	public HomePage validateLogin() {
		
		ExcelFetch excelFetch = new ExcelFetch();
		Map<String, String> signinDetailsMap = excelFetch.getDataFromExcel("Login_And_Place_Order_TC01", "account1");

		verifyElementIsDisplayed(prop.getProperty("button.menuButton"));
		click(prop.getProperty("button.menuButton"));
		
		verifyElementIsDisplayed(prop.getProperty("text.helloText"));
		
		verifyStep("Logged in successfully", "PASS");

		String actualName=getText(prop.getProperty("text.helloText"));
		actualName=actualName.replace("Hello, ", "");
        String expectedName=signinDetailsMap.get("Name");
        
        Assert.assertEquals(actualName, expectedName);
        
    	verifyElementIsDisplayed(prop.getProperty("button.homeButton"));
		click(prop.getProperty("button.homeButton"));
        
		
		return this;
	}

	/**
	 * @return
	 * @throws InterruptedException
	 * Searches the product in the search bar. Gets the input from the excel sheet
	 */
	public HomePage searchProduct() throws InterruptedException {

		ExcelFetch excelFetch = new ExcelFetch();
		Map<String, String> searchData = excelFetch.getDataFromExcel("Login_And_Place_Order_TC01", "account1");
		
		verifyElementIsDisplayed(prop.getProperty("edit.searchBar"));
		click(prop.getProperty("edit.searchBar"));
		
		Thread.sleep(3000);
		verifyElementIsDisplayed(prop.getProperty("edit.searchBar"));
		enterText(prop.getProperty("edit.searchBar"), searchData.get("SearchTerm"));
		
		verifyStep("Product searched", "PASS");

		
		verifyElementIsDisplayed(prop.getProperty("text.searchResult1"));
		click(prop.getProperty("text.searchResult1"));
		
		
		verifyElementIsDisplayed(prop.getProperty("image.searchListPageImage1"));
		
		
		return this;
	}
	
	/**
	 * @return
	 * @throws InterruptedException
	 * Clears the previously added cart items
	 */
	public HomePage clearCart() throws InterruptedException {

		Thread.sleep(5000);
		verifyElementIsDisplayed(prop.getProperty("text.cartCount"));
		int cartCount=Integer.parseInt(getText(prop.getProperty("text.cartCount")));
		
		if(cartCount>0)
		{
			System.out.println("Deleting cart items");
			verifyElementIsDisplayed(prop.getProperty("button.cartButton"));
			click(prop.getProperty("button.cartButton"));
			
			verifyElementIsDisplayed(prop.getProperty("button.deleteButton"));
			click(prop.getProperty("button.deleteButton"));
			
			//verifyElementIsDisplayed(prop.getProperty("text.ProductRemovedConfirmation"));
			Thread.sleep(5000);
			int cartCountUpdated=Integer.parseInt(getText(prop.getProperty("text.cartCount")));

			if(cartCountUpdated==0)
			{
				System.out.println("cart items deleted successfully");
				
				verifyStep("Cart items deleted", "PASS");

			}		
		}
		verifyElementIsDisplayed(prop.getProperty("image.amazonHomeLogo"));
		click(prop.getProperty("image.amazonHomeLogo"));
		
		return this;
	}
	
	

	

}
