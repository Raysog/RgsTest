import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Ivan on 01.09.2019.
 */
public class RgsTest {
    /**
     * @param args
     */
	private WebDriver driver = null;
	private Map<WebElement, String> enterValues = null;
	
		@Before
	    public void startValues() throws Exception {
	        System.setProperty("webdriver.gecko.driver", "src/drivers/geckodriver.exe");
	        System.setProperty("webdriver.chrome.driver", "src/drivers/chromedriver.exe");
	        switch (System.getProperty("driver")) {
	        case "chrome":
	        	driver = new ChromeDriver();
				break;
	        case "firefox":
	        	driver = new FirefoxDriver();
			default:
				System.out.println("Нет такого браузера");
				break;
			}
	        
	        enterValues = new HashMap<WebElement, String>();
	        driver.manage().window().maximize();
	    }
	
	 	@Test
	    public void formTest(){
	        driver.navigate().to("http://www.rgs.ru");
	        
	        WebElement strahovanie = driver.findElement(By.xpath("//a[@class='hidden-xs'][contains(text(), 'Меню')]"));
	        strahovanie.click();
	        
	        WebElement dms = strahovanie.findElement(By.xpath("//a[contains(text(), 'ДМС')]"));
	        dms.click();
	        
	        try {
	            driver.findElement(By.xpath("//h1[contains(text(), 'добровольное медицинское страхование')]"));
	            System.out.println("Заголовок существует");
	        } catch (NoSuchElementException ex) {
	            System.out.println("Заголовок не найден");
	        }
	        
	        WebElement buttonOk = driver.findElement(By.xpath("//a[@class=\"btn btn-default text-uppercase hidden-xs adv-analytics-navigation-desktop-floating-menu-button\"]"));
	        buttonOk.click();
	        
	        addElement("text","//input[@name=\"LastName\"]", "Фамилия", null);
	        addElement("text","//input[@name=\"FirstName\"]", "Имя", null);
	        addElement("text","//input[@name=\"MiddleName\"]", "Отчетсво", null);
	        addElement("select", "//select", "Москва", "77"); 	
	        addElement("phone","//input[contains(@data-bind,'Phone')]", "(999) 987-65-43", null);      
	        addElement("text","//input[@name=\"Email\"]", "qwertyqwerty", null);
	        addElement("text","//textarea[@name=\"Comment\"]", "testtesttesttest", null);
	        addElement("checkBox","//input[@class=\"checkbox\"]", "on", null);
	        
	        
	        for (WebElement webElement : enterValues.keySet()) {
	        	assertEquals("value of element \""+ webElement.getAttribute("name") + "\" is false",enterValues.get(webElement) , webElement.getAttribute("value")); 
	    	}
	        
	        By buttonLocator = By.xpath("//button[@id=\"button-m\"]");
	        WebElement button = driver.findElement(buttonLocator);
	        button.click();
	        assertEquals("У Поля Эл. почта не присутствует сообщение об ошибке Введите корректный email", 
	        			 "Введите адрес электронной почты",  
	        			 driver.findElement(By.xpath("//span[@class=\"validation-error-text\"]")).getAttribute("innerText"));
	 	}
	 	
	 	@After
	    public void stopTest() throws Exception {
	        driver.quit();
	    }
    
    private void addElement(String element, String xpath, String value, String idValue) {
    	By locator = By.xpath(xpath);
    	WebElement webElement = driver.findElement(locator);
    	WebDriverWait elementWaiter = new WebDriverWait(driver, 50, 1000);
    	String elementValue = value;
    	;
    	switch (element) {
    		case "text":
    	    	elementWaiter.until(ExpectedConditions.elementToBeClickable(locator));
    			webElement.sendKeys(elementValue);
    	        enterValues.put(webElement, elementValue);
    			break;
    		case "phone":
    	    	elementWaiter.until(ExpectedConditions.elementToBeClickable(locator));
    			webElement.sendKeys(elementValue);
    			enterValues.put(webElement, "+7 " + elementValue);
    			break;
    		case "select":
    	    	elementWaiter.until(ExpectedConditions.elementToBeClickable(locator));
    	        Select select = new Select(webElement);
    	        select.selectByVisibleText(elementValue);
    	        enterValues.put(webElement, idValue);
    			break;
    		case "checkBox":
    			webElement.click();
    	        enterValues.put(webElement, elementValue);
    			break;
    		default:
    			System.out.println("Нет такого элемента");
    			
    	}
    	
    	
    }

}