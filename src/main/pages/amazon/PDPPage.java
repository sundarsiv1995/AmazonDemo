package amazon;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.relevantcodes.extentreports.ExtentTest;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import junit.framework.Assert;
import utils.ExcelFetch;
import wrappers.AmazonWrapper;
import wrappers.GenericWrappers;

/**
 * @author sundar.siva
 *
 */
public class PDPPage extends AmazonWrapper {
	private static Properties prop;
	public Map<String, String> capData1 = new HashMap<String, String>();

	/**
	 * @param driver
	 * @param test - extent test for reporting
	 * @param capData1 - device capabilities
	 * Constructor which sets the driver,extent test and device capabilities
	 */
	public PDPPage(MobileDriver driver, ExtentTest test, Map<String, String> capData1) {
		this.driver = driver;
		this.test = test;
		this.capData1 = capData1;
		prop = new Properties();
		try {
			if (capData1.get("PlatformName").equalsIgnoreCase("Android")) {
				prop.load(new FileInputStream(new File("./Locators/Android/pdp.properties")));
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
	 * Search a product and validate the price and name in search result page
	 */
	public PDPPage validateSearchResultPage() throws InterruptedException {
		
		Thread.sleep(5000);
		swipeFullFromBottomToTop("android");
		swipeFullFromBottomToTop("android");
		swipeFullFromBottomToTop("android");
		
		
		GenericWrappers.productNameSearchResults=getText(prop.getProperty("text.prodSearchPageTitle"));
		String[] fullPrice=getText(prop.getProperty("text.prodSearchPagePrice")).split(" ");
		System.out.println("price full : "+fullPrice[0]);
		System.out.println("name full : "+GenericWrappers.productNameSearchResults);
		
		if(!fullPrice[0].contains("₹"))
		{
			org.testng.Assert.assertTrue(false,"₹ not present in search results page");
		}
		GenericWrappers.productPriceSearchResults=fullPrice[0];
		
		verifyStep("Product Name search page :"+GenericWrappers.productNameSearchResults, "INFO");
		verifyStep("Product Price search page :"+GenericWrappers.productPriceSearchResults, "INFO");

		click(prop.getProperty("text.prodSearchPageTitle"));
		return this;
	}
	
	
	/**
	 * @return
	 * Validate PDP page like quantity, name, and price
	 */
	public PDPPage validatePDPPage() {
		
			
		verifyElementIsDisplayed(prop.getProperty("text.productTitle"));
		verifyElementIsDisplayed(prop.getProperty("image.productReview"));
		verifyElementIsDisplayed(prop.getProperty("button.shareProduct"));
		verifyElementIsDisplayed(prop.getProperty("button.ProductImage"));
		
		verifyStep("Product details page displayed", "PASS");
	
		String pdpPageProductTitle=getText(prop.getProperty("text.productTitle"));
		verifyStep("Product name PDP page : "+pdpPageProductTitle, "INFO");

		org.testng.Assert.assertEquals(pdpPageProductTitle, GenericWrappers.productNameSearchResults);
		
		//swipeToElement("android", prop.getProperty("text.productPrice"));
		swipeFullFromBottomToTop("android");
		
		if(verifyIsDisplayed(prop.getProperty("text.pdpPageSavingsPrice")))
		{
			GenericWrappers.offerProduct=true;
		}
		
		String pdpPageProductPrice=getText(prop.getProperty("text.productPrice"));
		pdpPageProductPrice=pdpPageProductPrice.replace("rupees ", "₹");
		verifyStep("Product price PDP page : "+pdpPageProductPrice, "INFO");

		org.testng.Assert.assertEquals(pdpPageProductPrice, GenericWrappers.productPriceSearchResults);
			
		swipeToElement("android", prop.getProperty("button.wishListButton"));
		
		if(!verifyIsDisplayed(prop.getProperty("button.addtoCartButton")))
		{
			swipeToElementUpwards("android", prop.getProperty("button.oneTimePurchase"));
			click(prop.getProperty("button.oneTimePurchase"));
		}
		
		swipeToElement("android", prop.getProperty("button.addtoCartButton"));
		
		GenericWrappers.productQuantity=Integer.parseInt(getText(prop.getProperty("text.quantityDropdown")));
		
		verifyStep("Product quantity PDP page : "+GenericWrappers.productQuantity, "INFO");

		verifyElementIsDisplayed(prop.getProperty("button.addtoCartButton"));
	
	
			return this;
		}
	
	/**
	 * @return
	 * @throws InterruptedException
	 * Adds the selected product to the cart and validates the cart count
	 */
	public PDPPage addToCart() throws InterruptedException {
		
		verifyElementIsDisplayed(prop.getProperty("text.cartCount"));
		int cartCountBefore=Integer.parseInt(getText(prop.getProperty("text.cartCount")));
		
		click(prop.getProperty("button.addtoCartButton"));
		
		Thread.sleep(5000);
		
		verifyElementIsDisplayed(prop.getProperty("text.cartCount"));
		int cartCountAfter=Integer.parseInt(getText(prop.getProperty("text.cartCount")));
		
		
		if(cartCountBefore+GenericWrappers.productQuantity==cartCountAfter)
		{
			System.out.println("Product added to cart");
		}
	
		
		return this;
	}
	
	/**
	 * @return
	 * Clicks on cart icon on top and navigates to shopping cart page
	 */
	public PDPPage naviogatetoShoppingCart() {
		
		verifyElementIsDisplayed(prop.getProperty("button.cartButton"));
		click(prop.getProperty("button.cartButton"));
		
		return this;
	}
	
	

	

	

}
