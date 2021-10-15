import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Amazon {

	
	
	public static void main(String[] args) {

		// get product list of amazon
    	System.setProperty("webdriver.chrome.driver","./drivers/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.amazon.com/");
		
		WebElement search = driver.findElement(By.id("twotabsearchtextbox"));
		
		search.sendKeys("samsung tablet");
		
		// click search 
		driver.findElement(By.id("nav-search-submit-button")).click();
		

		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS) ;
		
		// save list of web elements in the page Name and Price 
		List<WebElement> resultsList = driver.findElements(By.xpath(".//span[@class='a-size-medium a-color-base a-text-normal']"));
		List<WebElement> priceList = driver.findElements(By.xpath(".//span[@class='a-price-whole']"));	


		// connect to database and add products
		Connection conn = null;
		try {  
            Class.forName("com.mysql.jdbc.Driver");  
             
            conn = DriverManager.getConnection("jdbc:mysql://localhost/amazon", "root", "");  
            System.out.println("Connection created");  
              
            }  
            catch (Exception e) {  
            System.out.println(e.toString());  
        }  
		
		
		try {
			Statement stmt = conn.createStatement();
			// insert first 3 products into database 
			for(int i = 0; i < 3; i++) {
				
				String query = "INSERT INTO products VALUES (0, '"+ resultsList.get(i).getText()+ "', "+ priceList.get(i).getText()+")" ;
				stmt.executeUpdate(query);
			}
			
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		// print from database 
		try {
			Statement stmt = conn.createStatement();
			String query = "select * from products ;" ;
			ResultSet rs = stmt.executeQuery(query) ;
			
			System.out.println("========================================");
			while(rs.next()) {
				
				System.out.println("ID\t" + rs.getInt("id"));
				System.out.println("Name\t" + rs.getString("name"));
				System.out.println("Price\t" + rs.getDouble("price"));
				System.out.println("========================================");
			}
			// after printing close connection to database 
			conn.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		driver.close();
	}
	
}
