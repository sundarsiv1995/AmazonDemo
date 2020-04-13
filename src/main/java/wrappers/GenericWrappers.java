package wrappers;

import java.io.File;

import java.io.IOException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotSelectableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.remote.DesiredCapabilities;

import org.testng.Assert;

import org.testng.asserts.SoftAssert;


import com.gargoylesoftware.htmlunit.ElementNotFoundException;

import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.remote.MobileCapabilityType;
import utils.Reporter;

/**
 * @author sundar.siva
 *
 */
public class GenericWrappers extends Reporter {

	public MobileDriver driver;
	protected Properties prop;
	public Map<String, String> capData = new HashMap<String, String>();

	public static String productNameSearchResults="";
	public static String productPriceSearchResults="";
	public static int productQuantity;
    public static boolean offerProduct=false;
	

	public GenericWrappers(MobileDriver driver, ExtentTest test) {
		this.driver = driver;
		this.test = test;
	}

	public GenericWrappers() {

	}
	

	/**
	 * Starts the extent test report by giving name and decription for test case
	 * Assigns author name and category
	 */
	public void startTestReport() {
		test = startTestCase(testCaseName, testDescription);
		test.assignCategory(category);
		test.assignAuthor(authors);
	}


	/**
	 * Closes the application
	 */
	public void closeApp() {
		try {
			driver.closeApp();
		} catch (Exception e) {

		}

	}
	

	/**
	 * Launch the application(Driver Initialization)
	 */
	public void invokeApp() {

		System.out.println("*************** Launching APP ***************");

		URL urlObj = null;
		DesiredCapabilities capabilities = new DesiredCapabilities();
	

		try {
			String dir = System.getProperty("user.dir");
			urlObj = new URL("http://" + "127.0.0.1" + ":" + capData.get("Port") + "/wd/hub");

			switch (capData.get("PlatformName").toLowerCase()) {
			case "android":
				capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, capData.get("PlatformName"));
				capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, capData.get("PlatformVersion"));
				capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, capData.get("DeviceName"));
				capabilities.setCapability(MobileCapabilityType.UDID, capData.get("udid"));
				capabilities.setCapability("automationName", "UiAutomator2");
				capabilities.setCapability("systemPort", capData.get("systemPort"));
				capabilities.setCapability("newCommandTimeout", 9999);
				//capabilities.setCapability("skipServerInstallation", true);
				capabilities.setCapability(MobileCapabilityType.APP, dir + "/app/android/Amazon_shopping.apk");
				capabilities.setCapability("appPackage", "com.amazon.mShop.android.shopping");
			    capabilities.setCapability("appActivity","com.amazon.mShop.home.HomeActivity");
				driver = new AndroidDriver<MobileElement>(urlObj, capabilities);
				break;

			default:
				break;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param property
	 * Clicks an element
	 */
	public void click(String property) {
		By byProperty = getLocator(property);
		MobileElement element = null;
		try {
			element = (MobileElement) driver.findElement(byProperty);
			driver.findElement(byProperty).click();
			System.out.println("Element is Clicked");

		} catch (ElementNotFoundException e) {
			Assert.assertTrue(false, element + "Element not found\n" + e.getMessage());
		} catch (TimeoutException e) {
			Assert.assertTrue(false, element + "Time out error\n" + e.getMessage());
		} catch (ElementNotSelectableException e) {
			Assert.assertTrue(false, element + "Element not Selectable\n" + e.getMessage());
		} catch (ElementNotVisibleException e) {
			Assert.assertTrue(false, element + "Element not Visible\n" + e.getMessage());
		} catch (ElementNotInteractableException e) {
			Assert.assertTrue(false, element + "Element not Interatable\n" + e.getMessage());
		} catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}

	}


	/**
	 * @param property
	 * @param data
	 * Sends the balue to an input field 
	 */
	public void enterText(String property, String data) {
		MobileElement element = null;
		MobileElement element2 = null;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			element.clear();
			element2 = (MobileElement) driver.findElement(getLocator(property));
			element2.sendKeys(data);
			System.out.println("Data is entered to the required Field");
		}
		catch (ElementNotFoundException e) {
			Assert.assertTrue(false, element + "Element not found\n" + e.getMessage());
		} catch (ElementNotSelectableException e) {
			Assert.assertTrue(false, element + "Element not Selectable\n" + e.getMessage());
		} catch (ElementNotVisibleException e) {
			Assert.assertTrue(false, element + "Element not Visible\n" + e.getMessage());
		} catch (ElementNotInteractableException e) {
			Assert.assertTrue(false, element + "Element not Interatable\n" + e.getMessage());
		} catch (TimeoutException e) {
			Assert.assertTrue(false, element + "Time out error\n" + e.getMessage());
		} catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}

	}

	/**
	 * @param property
	 * @param text
	 * @return
	 * Compares the given text with the element text in UI 
	 */
	public boolean verifyText(String property, String text) {
		MobileElement element = null;
		String sText = "";
		boolean val=false;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			sText = element.getText();
			if (sText.equalsIgnoreCase(text)) {
				val= true;
			} 
		}
			 catch (Exception e) {
			//verifyStep(e.getMessage(), "FAIL");
			SoftAssert softAssert = new SoftAssert();
			softAssert.assertTrue(false, e.getMessage());
		}
		return val;
	}


	/**
	 * @param property
	 * Check whether the element is displayed with wait time
	 */
	public void verifyElementIsDisplayed(String property) {
		MobileElement element=null;
		try {
			driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
			element = (MobileElement) driver.findElement(getLocator(property));
			element.isDisplayed();
		}catch (Exception e) {
			Assert.assertTrue(false, "Element not displayed\n" + e.getMessage());
		} 
	}


	/**
	 * @param property
	 * @return
	 * Check whether the element is displayed or not (returns boolean value)
	 */
	public boolean verifyIsDisplayed(String property) {
		boolean present=false;
		MobileElement element=null;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			element.isDisplayed();
			present=true;
			
		}catch (Exception e) {
			present=false;		} 
		return present;
	}
	

	/**
	 * @param property
	 * @param timeoutInSecs
	 * Verifies whether value is present or not
	 */
	public void verifyElementIsPresent(String property, long timeoutInSecs){
		try{
			long startTime = System.currentTimeMillis();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			while (System.currentTimeMillis() < (startTime + (timeoutInSecs * 1000))) {
				if (verifyIsDisplayed(property)){
					driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
					return;
				}
			}
			Assert.assertTrue(false, property + " is displayed or not validation");
		}
		catch (ElementNotFoundException e)
		{
			Assert.assertTrue(false, property + "Element not found\n" + e.getMessage());
		}

		catch (TimeoutException e) {
			Assert.assertTrue(false, property + "Time out error\n" + e.getMessage());
		}
		catch (ElementNotSelectableException e){
			Assert.assertTrue(false, getLocator(property) + "Element not Selectable\n" + e.getMessage());
		}
		catch (ElementNotVisibleException e) {
			Assert.assertTrue(false, property + "Element not Visible\n" + e.getMessage());
		}
		catch (ElementNotInteractableException e) {
			Assert.assertTrue(false, property + "Element not Interatable\n" + e.getMessage());
		}
		catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}
	}
	
	/**
	 * @param property
	 * @param timeoutInSecs
	 * Verfies element not present in the UI
	 */
	public void verifyElementIsNotPresent(String property, long timeoutInSecs) {
		try{
			long startTime = System.currentTimeMillis();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			while (System.currentTimeMillis() < (startTime + (timeoutInSecs * 1000))) {
				if (verifyIsDisplayed(property)) {
					driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
					return;
				}
			}
			Assert.assertTrue(true, property + " is displayed or not validation");

		} catch (ElementNotFoundException e) {
			Assert.assertTrue(true, property + "Element not found\n" + e.getMessage());
		} catch (TimeoutException e) {
			Assert.assertTrue(false, property + "Time out error\n" + e.getMessage());
		} catch (ElementNotSelectableException e) {
			Assert.assertTrue(false, getLocator(property) + "Element not Selectable\n" + e.getMessage());
		} catch (ElementNotVisibleException e) {
			Assert.assertTrue(false, property + "Element not Visible\n" + e.getMessage());
		} catch (ElementNotInteractableException e) {
			Assert.assertTrue(false, property + "Element not Interatable\n" + e.getMessage());
		}

		catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}

	}

 
	/**
	 * @param property
	 * @return
	 * Gets the text from the UI 
	 */
	public String getText(String property) {
		String bReturn = "";
		MobileElement element = null;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			return element.getText();
		} catch (ElementNotFoundException e) {
			Assert.assertTrue(false, element + "Element not found\n" + e.getMessage());
		} catch (TimeoutException e) {
			Assert.assertTrue(false, element + "Time out error\n" + e.getMessage());
		} catch (ElementNotSelectableException e) {
			Assert.assertTrue(false, element + "Element not Selectable\n" + e.getMessage());
		} catch (ElementNotVisibleException e) {
			Assert.assertTrue(false, element + "Element not Visible\n" + e.getMessage());
		} catch (ElementNotInteractableException e) {
			Assert.assertTrue(false, element + "Element not Interatable\n" + e.getMessage());
		}
		catch (Exception e) {
			Assert.assertTrue(false, e.getMessage());
		}
		return bReturn;
	}

	
	/**
	 * @param property
	 * @param attribute
	 * @return
	 * Gets the attribute value for the element
	 */
	public String getAttribute(String property, String attribute) {
		String bReturn = "";

		MobileElement element = null;

		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			return element.getAttribute(attribute);
		} catch (ElementNotFoundException e) {
			Assert.assertTrue(false, element + "Element not found\n" + e.getMessage());
		} catch (TimeoutException e) {
			Assert.assertTrue(false, element + "Time out error\n" + e.getMessage());
		} catch (ElementNotSelectableException e) {
			Assert.assertTrue(false, element + "Element not Selectable\n" + e.getMessage());
		} catch (ElementNotVisibleException e) {

			Assert.assertTrue(false, element + "Element not Visible\n" + e.getMessage());
		} catch (ElementNotInteractableException e) {

			Assert.assertTrue(false, element + "Element not Interatable\n" + e.getMessage());
		}

		catch (Exception e) {

			Assert.assertTrue(false, e.getMessage());
		}

		return bReturn;
	}

	/* (non-Javadoc)
	 * @see utils.Reporter#takeSnap()
	 * takes screenshot
	 */
	public long takeSnap() {
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;
		try {
			FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE),
					new File("./reports/images/" + number + ".jpg"));
		} catch (IOException e) {
			//reportStep("The snapshot could not be taken", "WARN");
		} catch (Exception e) {

			System.out.println("The app has been closed.");
		}
		return number;
	}


	

	/**
	 * @param property
	 * @return
	 * Gets the locator from property file and extracts it based on id,name,xpath, etc..
	 */
	public By getLocator(String property) {
		String locator = property;

		String locatorType = locator.split("===")[0];
		String locatorValue = locator.split("===")[1];

		if (locatorType.toLowerCase().equals("id"))
			return By.id(locatorValue);
		else if (locatorType.toLowerCase().equals("name"))
			return By.name(locatorValue);
		else if ((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
			return By.className(locatorValue);
		else if ((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag")))
			return By.className(locatorValue);
		else if (locatorType.toLowerCase().equals("xpath"))
			return By.xpath(locatorValue);

		else
			return null;
	}

	/**
	 * @param property
	 * @return
	 * checks whether the checkbox, radio button is selected or not
	 */
	public boolean isSelected(String property) {
		boolean value = false;
		MobileElement element = null;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			value = driver.findElement(getLocator(property)).isSelected();

		} catch (ElementNotFoundException e) {
			//verifyStep(element + "Element not found\n" + e.getMessage(), "FAIL");
			Assert.assertTrue(false, element + "Element not found\n" + e.getMessage());
		} catch (TimeoutException e) {

			//verifyStep(element + "Time out error\n" + e.getMessage(), "FAIL");
			Assert.assertTrue(false, element + "Time out error\n" + e.getMessage());
		} catch (ElementNotSelectableException e) {
			//verifyStep(element + "Element not Selectable\n" + e.getMessage(), "FAIL");
			Assert.assertTrue(false, element + "Element not Selectable\n" + e.getMessage());
		} catch (ElementNotVisibleException e) {

			//verifyStep(element + "Element not Visible\n" + e.getMessage(), "FAIL");
			Assert.assertTrue(false, element + "Element not Visible\n" + e.getMessage());
		} catch (ElementNotInteractableException e) {

			//verifyStep(element + "Element not Interatable\n" + e.getMessage(), "FAIL");
			Assert.assertTrue(false, element + "Element not Interatable\n" + e.getMessage());
		}

		catch (Exception e) {

			//verifyStep(e.getMessage(), "FAIL");
			Assert.assertTrue(false, e.getMessage());
		}
	
			return value;

	}
	/**/

	/**
	 * @param property
	 * closes the keyboard
	 */
	public void keypadDown() {
		driver.hideKeyboard();
	}

  

	/**
	 * @param property
	 * clears the text from the input field
	 */
	public void clearElement(String property) {
		MobileElement element = null;
		try {
			element = (MobileElement) driver.findElement(getLocator(property));
			element.clear();
		}
		catch (ElementNotFoundException e){
			// verifyStep(element + "Element not found\n" + e.getMessage(), "FAIL");
			Assert.assertTrue(false,element + "Element not found\n" + e.getMessage());
		}
		catch (TimeoutException e) {
			// verifyStep(element + "Time out error\n" + e.getMessage(), "FAIL");
			Assert.assertTrue(false,element + "Time out error\n" + e.getMessage());
		}
		catch (ElementNotSelectableException e)	{
			// verifyStep(element + "Element not Selectable\n" + e.getMessage(), "FAIL");
			Assert.assertTrue(false,element + "Element not Selectable\n" + e.getMessage());
		}
		catch (ElementNotVisibleException e) {
			// verifyStep(element + "Element not Visible\n" + e.getMessage(), "FAIL");
			Assert.assertTrue(false,element + "Element not Visible\n" + e.getMessage());
		}
		catch (ElementNotInteractableException e) {
			// verifyStep(element + "Element not Interatable\n" + e.getMessage(), "FAIL");
			Assert.assertTrue(false,element + "Element not Interatable\n" + e.getMessage());
		}
		catch (Exception e) {
			// verifyStep(e.getMessage(), "FAIL");
			Assert.assertTrue(false,e.getMessage());
		}

	}
  
	/**
	 * @param pfName
	 * swipes on the screen from bottom to top that is swipe down
	 */
	public void swipeFullFromBottomToTop(String pfName) {
		System.out.println("Swiping......");
		try {
			Thread.sleep(2000);
			org.openqa.selenium.Dimension scrnSize = driver.manage().window().getSize();
			int startx = (int) (scrnSize.width / 2);
			int starty = (int) (scrnSize.height*0.3);
			int endy = (int) (scrnSize.height*0.8);
			if (pfName.equalsIgnoreCase("android")) {
				// System.out.println("swiping android");
				((AndroidDriver<WebElement>) driver).swipe(startx, endy, startx, starty, 1000);
			} 

		} catch (InterruptedException e) {
			Assert.assertTrue(false,e.getMessage());
		}
	}
	/**
	 * @param property
	 * @return
	 * verifies whether element present or not
	 */
	public boolean verifyElement(String property) {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		boolean present = true;
		try {

			
			driver.findElement(getLocator(property));
			return present;

		} catch (Exception e) {
			present = false;
			return present;
		}

	}
	
	/**
	 * @param pfName
	 * @param property
	 * Swipes to the element on the screen
	 */
	public void swipeToElement(String pfName, String property) {
		while (true) {
			if (verifyElement(property)) {
				break;
			}
			swipeFullFromBottomToTop(pfName);
		}
	}

  
	/**
	 * @param pfName
	 * swipes on the screen from top to bottom that is swipe down
	 */
	public void swipeFullFromTopToBottom(String pfName) {

		try {
			Thread.sleep(2000);
			org.openqa.selenium.Dimension scrnSize = driver.manage().window().getSize();
			int startx = (int) (scrnSize.width / 2);
			int endy = (int) (scrnSize.height - 1);
			int starty = (int) (scrnSize.height * 0.2);
			// int endx = (int) (scrnSize.width /2);
			if (pfName.equalsIgnoreCase("android")) {

				((AndroidDriver<WebElement>) driver).swipe(startx, starty, startx, endy, 3000);
			} 
			
		} catch (InterruptedException e) {
			Assert.assertTrue(false,e.getMessage());
		}

	}
	/**
	 * @param pfName
	 * @param property
	 * Swipes to the given elemet in upward direction
	 */
	public void swipeToElementUpwards(String pfName, String property) {
		while (true) {
			if (verifyElement(property)) {
				break;
			}
			swipeFullFromTopToBottom(pfName);
		}
		
	}
	/**
	 * Launch the app on the device
	 */
	public void launchApp() {
		System.out.println("Launching the app");
		driver.launchApp();
	}

	/**
	 * clicks on the android back button
	 */
	public void clickAndroidBack() {

		((AndroidDriver) driver).pressKeyCode(AndroidKeyCode.BACK);

	}

	
}
