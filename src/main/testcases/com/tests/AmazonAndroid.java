package com.tests;


import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import amazon.CheckoutPage;
import amazon.HomePage;
import amazon.LoginPage;
import amazon.PDPPage;
import wrappers.AmazonWrapper;

/**
 * @author sundar.siva
 *
 */
public class AmazonAndroid extends AmazonWrapper {

	/**
	 * Gets the input from the testng.xml file which has the device capabilities
	 * @param automation - automationName capability in appium
	 * @param platformName - platformName capability in appium
	 * @param platformVersion - platformVersion capability in appium
	 * @param deviceName - deviceName capability in appium
	 * @param udid - udid capability in appium
	 * @param application - application capability in appium
	 * @param port - port capability in appium
	 * @param systemPort - systemPort capability in appium
	 */
	@BeforeClass
	@Parameters({ "Automation", "PlatformName", "PlatformVersion", "DeviceName", "udid", "Application", "Port", "systemPort" })
	public void setData(String automation, String platformNam, String platformVer, String deviceNam, String udid,
			String application, String port, String systemPort) {
		testCaseName = "Test";
		tcName = testCaseName;
		testDescription = "Started";
		category = "Functional";
		dataSheetName = "DataPool.xlsx";
		applicationType = application;
		authors = "Sundar";
		capData.put("Automation", automation);
		capData.put("Port", port);
		capData.put("PlatformName", platformNam);
		capData.put("PlatformVersion", platformVer);
		capData.put("DeviceName", deviceNam);
		capData.put("udid", udid);
		capData.put("TestCaseName", testCaseName);
		capData.put("systemPort", systemPort);

	}

	

	/**
	 * TestNG test annotated method which has all the steps to execute. First step of the test case
	 */
	@Test(priority = 1, enabled = true)
	public void Amazon_Login_And_Place_Order_TC001() {

		try {
			testCaseName = "Amazon_Login_And_Place_Order_TC001";
			tcName = testCaseName;
			testDescription = "Validate the login and place order scenario in Amazon Android mobile app";
			startTestReport();
			
			LoginPage login = new LoginPage(driver, test, capData);
			login.signIn();

			
			HomePage home=new HomePage(driver, test, capData);
			home.selectLanguage();
			home.validateLogin();
			home.selectLanguage();
			home.clearCart();
			home.searchProduct();
			
			PDPPage pdp=new PDPPage(driver, test, capData);
			pdp.validateSearchResultPage();
			home.selectLanguage();
			pdp.validatePDPPage();
			pdp.addToCart();
			pdp.naviogatetoShoppingCart();
			
			CheckoutPage checkout=new CheckoutPage(driver, test, capData);
			checkout.validateShoppingCart();
			checkout.proceedtoBuy();
			checkout.selectShippingAddress();
			checkout.selectPayment();
			//checkout.selectPrimeMember();
			checkout.validateCheckoutPage();

		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}
	
	
	
}
