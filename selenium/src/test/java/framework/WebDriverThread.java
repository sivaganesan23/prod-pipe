package framework;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import static framework.DriverType.CHROME;
import static framework.DriverType.valueOf;

import java.net.MalformedURLException;
import java.net.URL;

public class WebDriverThread {
	
	private WebDriver webdriver;
	private DriverType selectedDriverType;
	
	private final DriverType defaultDriverType= CHROME;
	private final String browser=System.getProperty("browser").toUpperCase();
	private final String operatingSystem=System.getProperty("os.name").toUpperCase();
	private final String systemArchitecture=System.getProperty("os.arch");
	private final boolean useRemoteWebDriver=Boolean.getBoolean("remoteDriver");

	
	public WebDriver getDriver() throws Exception{
		
		
		if(null == webdriver){
			selectedDriverType=determineEffectiveDriverType();
			DesiredCapabilities desiredCapabilities=selectedDriverType.getDesiredCapabilities();
			instantiateWebDriver(desiredCapabilities);
		}
		
		return webdriver;
	}
	
	private DriverType determineEffectiveDriverType(){
		DriverType driverType=defaultDriverType;
		try{
			driverType=valueOf(browser);
		}catch(IllegalArgumentException ignored){
			System.err.println("Unknown driver specified,defaulting to '" + driverType + "'...");
		}
		
		return driverType;
	}
	
	public void instantiateWebDriver(DesiredCapabilities desiredCapabilities) throws MalformedURLException{
	
		
		System.out.println(" ");
		System.out.println("Current Operating System: "+operatingSystem);
		System.out.println("Current Architecture: "+systemArchitecture);
		System.out.println("Current Browser Selection: "+selectedDriverType);
		System.out.println(" ");
		
		if(useRemoteWebDriver){
		     URL SeleniumGridURL=new URL(System.getProperty("gridURL"));
		     String desiredBrowserVersion=System.getProperty("desiredBrowserVersion");
		     String desiredPlatform=System.getProperty("desiredPlatform");
		     
		     if(null!=desiredBrowserVersion && !desiredBrowserVersion.isEmpty()){
		    	 desiredCapabilities.setVersion(desiredBrowserVersion);
		     }
		     
		     if(null!=desiredPlatform && !desiredPlatform.isEmpty()){
		    	 
		    	 desiredCapabilities.setPlatform(Platform.valueOf(desiredPlatform.toUpperCase()));
		    	 
		     }
		     
		     webdriver=new RemoteWebDriver(SeleniumGridURL,desiredCapabilities);
		}else{
			
			webdriver=selectedDriverType.getWebDriverObject(desiredCapabilities);
			
		}
		
		
		
	}
	
	public void quitDriver(){
		if(null!=webdriver){
			webdriver.quit();
		}
	}

}
