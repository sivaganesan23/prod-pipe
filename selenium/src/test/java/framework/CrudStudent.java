package framework;



import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class CrudStudent extends DriverFactory {
	WebDriver driver;
	
	
	@BeforeTest
	public void beforeTest() throws Exception{
	
		
		driver=DriverFactory.getDriver();
		driver.manage().window().maximize();
		driver.get("http://IPADDRESS/studentapp/");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Test(priority=1)
	public void verifyStudentManditoryFields() {
		
		driver.findElement(By.xpath("//input[@value='register']")).click();
		
		if(!driver.findElement(By.xpath("//font[text()='Please fill all the fields']")).isDisplayed()) {
			//Assert.assertFalse("Please fill all the fields text should display", false);
			Assert.fail("Please fill all the fields error message is not displayed");
		}else {
			Assert.assertTrue(true, "Please fill all the fields error message is displayed");
			
			
		}
		
	}
	
	@Test(priority=2,dependsOnMethods= {"verifyStudentManditoryFields"})
	public void createStudent() {
		
		driver.findElement(By.name("fullname")).sendKeys("Virat Kohli");
		driver.findElement(By.name("address")).sendKeys("Delhi");
		driver.findElement(By.name("age")).sendKeys("22");
		driver.findElement(By.name("qual")).sendKeys("BE");
		driver.findElement(By.name("percent")).sendKeys("99");
		driver.findElement(By.name("yop")).sendKeys("2016");
		
		driver.findElement(By.xpath("//input[@value='register']")).click();
		
		if(!isPresentAndDisplayed(driver.findElement(By.xpath("//td[text()='Virat Kohli']")))) {
			//Assert.assertFalse("Please fill all the fields text should display", false);
			Assert.fail("Student name not added successfully");
		}else {
			Assert.assertTrue(true, "Student name added successfully");
			
			
		}
		
		
	}
	
	@Test(priority=3,dependsOnMethods= {"createStudent"})
	public void editStudent() {
		int row=0;
		WebElement element=driver.findElement(By.xpath("//table"));
		row=getrownumber(element,"Virat Kohli");
		element.findElement(By.xpath("//tr["+row+"]/td[8]/a")).click();
		
		driver.findElement(By.name("stdname")).clear();
		
		driver.findElement(By.name("stdname")).sendKeys("Virat Kohli new");
		
		driver.findElement(By.xpath("//input[contains(@value,'Save')]")).click();
		
		if(!isPresentAndDisplayed(driver.findElement(By.xpath("//td[text()='Virat Kohli new']")))) {
			//Assert.assertFalse("Please fill all the fields text should display", false);
			Assert.fail("Student details not edit successfully");
		}else {
			Assert.assertTrue(true, "Student details edited successfully");
			
			
		}
		
		
	}
	
	@Test(priority=4,dependsOnMethods= {"editStudent"})
	public void deleteStudent() throws Exception{
		int row=0;
		WebElement element=driver.findElement(By.xpath("//table"));
		row=getrownumber(element,"Virat Kohli new");
		element.findElement(By.xpath("//tr["+row+"]/td[9]/a")).click();


	
			if(!(driver.findElements(By.xpath("//td[text()='Virat Kohli new']")).size()>0)) {
				
				Assert.assertTrue(true,"Student deleted successfully");
			}else {
				Assert.fail("Student not deleted");
				
				
			}
		
		
		
		
	}
	
	@AfterTest
	public void afterTest() throws Exception{
	driver.quit();
	}
	
	public static boolean isPresentAndDisplayed(final WebElement element) {
		  try {
		    return element.isDisplayed();
		  } catch (NoSuchElementException e) {
		    return false;
		  }
		}
	
public static int getrownumber(WebElement element,String actual) {
	int rows=0;
	int columns=0;
	int i=0;
	int j=0;
	boolean flag=false;
	String expected=null;
	rows=element.findElements(By.xpath("//tr")).size();
	
	for(i=2;i<=rows;i++) {
		columns=element.findElement(By.xpath("//tr["+i+"]")).findElements(By.tagName("td")).size();
		for(j=1;j<=columns;j++) {
			expected=element.findElement(By.xpath("//tr["+i+"]/td["+j+"]")).getText();
			
			if(expected.equalsIgnoreCase(actual)) {
				flag=true;
				break;
			}
					
		}
		
		if(flag==true) {
			break;
		}
	}
	
	
	return i;
}

}



