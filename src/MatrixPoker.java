import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.security.auth.login.LoginException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

public class MatrixPoker {

	static Random ran = new Random();
	static int ranint = ran.nextInt(1000);
	static String username = "Auto" + ranint;
	static int bankacc = randomNumberInRange(1000000000, 2100000000);
	static String account = Integer.toString(bankacc);

	public static int randomNumberInRange(int min, int max) {
		Random random = new Random();
		return random.nextInt((max - min) + 1) + min;
	}

	public static void openNewTab(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.open('about:blank','_blank');");
	}

	public static void register(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(By.id("register_user")));
		driver.findElement(By.id("register_user")).click();
		System.out.println("Opening Register Page Done");
		Thread.sleep(3000);

		// Random ran = new Random();
		// int ranint = ran.nextInt(1000);
		// String username = "Auto"+ranint;
		// int bankacc = randomNumberInRange(1000000000,2100000000);
		// String account = Integer.toString(bankacc);

		driver.findElement(By.id("runame")).sendKeys(username);
		driver.findElement(By.id("umail")).sendKeys(username + "@test.com");
		driver.findElement(By.id("rupassword")).sendKeys("123123");
		driver.findElement(By.id("ucpassword")).sendKeys("123123");
		Select Sel = new Select(driver.findElement(By.id("selectBank")));
		Sel.selectByIndex(1);
		driver.findElement(By.id("aname")).sendKeys(username);
		driver.findElement(By.id("anumber")).sendKeys(account);
		Select SelQ = new Select(driver.findElement(By.id("selectquestion")));
		SelQ.selectByIndex(1);
		driver.findElement(By.id("uanswer")).sendKeys("qwerty");
		Thread.sleep(10000);
		driver.findElement(By.id("ragissubmit")).click();
		System.out.println("Registration Done For Player : "+ username);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("agree")));
		driver.findElement(By.id("agree")).click();
		System.out.println("Terms Accepting Done");
	}

	public static void login(WebDriver driver, WebDriverWait wait, String user) {

		driver.findElement(By.id("uname")).sendKeys(user);
		driver.findElement(By.id("upassword")).sendKeys("123123");
		driver.findElement(By.id("logsubmit")).click();
		System.out.println("Login Done For Player : " + user);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("agree")));
		driver.findElement(By.id("agree")).click();
		System.out.println("Terms Accepting Done");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public static void NavigateToLobby(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu_LOBBY")));
		driver.findElement(By.id("menu_LOBBY")).click();
		System.out.println("Navigation to Lobby Done");
		
		//No.of Columns
		List<WebElement>  col = driver.findElements(By.xpath("//*[contains(@id,'table_list_data')/table/thead/tr/th]"));
        System.out.println("No of cols are : " +col.size()); 
        //No.of rows 
        List<WebElement>  rows = driver.findElements(By.id("//*[contains(@id,'table_list_data')/table/tbody/tr/th]")); 
        System.out.println("No of rows are : " + rows.size());

	}

	public static void NavigateToCashier(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu_Kasir")));
		driver.findElement(By.id("menu_Kasir")).click();
		System.out.println("Navigation to Cashier Done");

		WebElement cashiertab = driver.findElement(By.xpath("//*[contains(@id,'tab_container_')]"));
		List<WebElement> cashiersubtab = cashiertab.findElements(By.tagName("li"));

		for (WebElement li : cashiersubtab) {
			if (li.getText().equals("Deposit")) {
				li.click();
				System.out.println("----------> Navigavtion to Deposit Tab Done");
			}
		}
		Thread.sleep(2000);
		for (WebElement li : cashiersubtab) {
			if (li.getText().equals("Withdraw")) {
				li.click();
				System.out.println("----------> Navigavtion to Withdrawal Tab Done");
			}
		}
		Thread.sleep(2000);
		for (WebElement li : cashiersubtab) {
			if (li.getText().equals("Transactions")) {
				li.click();
				System.out.println("----------> Navigavtion to Transactions Tab Done");
			}
		}
	}

	public static void MakeDepositOnCashierPage(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		WebElement cashiertab = driver.findElement(By.xpath("//*[contains(@id,'tab_container_')]"));
		List<WebElement> cashiersubtab = cashiertab.findElements(By.tagName("li"));

		for (WebElement li : cashiersubtab) {
			if (li.getText().equals("Deposit")) {
				li.click();
				System.out.println("----------> Navigavtion to Deposit Tab Done");
			}
		}

		Thread.sleep(5000);

		/*
		 * DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		 * LocalDateTime now = LocalDateTime.now(); System.out.println(dtf.format(now));
		 * driver.findElement(By.id("depositdate")).sendKeys(dtf.format(now));
		 */

		driver.findElement(By.xpath("//*[contains(@id,'depoistContainer_')]/div[8]/div/input")).sendKeys("12000");
		driver.findElement(By.xpath("//*[contains(@id,'depoistContainer_')]/div[10]/div/input")).sendKeys("Test");

		driver.findElement(By.xpath("//*[contains(text(),'Deposit')][contains(@id,'depositsubmit')]")).click();
		Thread.sleep(3000);
		String depositmsg = driver.findElement(By.xpath("//*[contains(@id,'depoistContainer_')]/div[1]/div")).getText();
		System.out.println(depositmsg);
	}

	public static void ApproveDeposit() throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 20);
		System.out.println("Browser Opening Done");
		driver.manage().window().maximize();
		driver.get("http://13.251.67.102");
		System.out.println("Opening Admin URL Successfully Done");
		Thread.sleep(3000);

		driver.findElement(By.id("login")).sendKeys("testadmin");
		driver.findElement(By.id("password")).sendKeys("testadmin");
		driver.findElement(By.id("login-submit")).click();
		System.out.println("Login to admin Done");
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("primary")));
		driver.findElement(By.xpath("//*[@id=\"primary\"]/ul/li[1]")).click();
		driver.findElement(By.xpath("//*[@id=\"secondary\"]/ul/li[2]")).click();
		System.out.println("Navigation to Deposit Page Done");
		
		List<WebElement>  rows = driver.findElements(By.cssSelector("#DataTables_Table_0 > tbody > tr"));
		int depositEnteries = rows.size();
        System.out.println("No of rows are : " + depositEnteries);
		
        for(int i =1;i<=depositEnteries;i++)
        {
        	String match = driver.findElement(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr["+i+"]/td[3]")).getText();
        	//System.out.println("User : "+match);
        	
        	if(match.equals(username))
    		{
    			driver.findElement(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr[1]/td[9]/a[1]")).click();
    			System.out.println("Deposit Approved for : " + username);
    		}
        }
        Thread.sleep(3000);
        driver.close();
	}

	public static void NavigateToProfile(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu_Profil")));
		driver.findElement(By.id("menu_Profil")).click();
		System.out.println("Navigation to Profile Done");

		WebElement profiletab = driver.findElement(By.xpath("//*[contains(@id,'tab_container_')]"));
		List<WebElement> profilesubtab = profiletab.findElements(By.tagName("li"));

		for (WebElement li : profilesubtab) {
			if (li.getText().equals("Overview")) {
				li.click();
				System.out.println("----------> Navigavtion to Overview Tab Done");
			}
		}
		System.out.println("--------------------> UserName :" + driver
				.findElement(By.xpath("//*[@id=\"profile_tabs-1\"]/div/div/div/div[2]/div[2]/div/ul/li[1]/span[2]"))
				.getText());
		System.out.println("--------------------> Email :" + driver
				.findElement(By.xpath("//*[@id=\"profile_tabs-1\"]/div/div/div/div[2]/div[2]/div/ul/li[2]/span[2]"))
				.getText());
		System.out.println("--------------------> Balance :" + driver
				.findElement(By.xpath("//*[@id=\"profile_tabs-1\"]/div/div/div/div[2]/div[2]/div/ul/li[7]/span[2]"))
				.getText());
		Thread.sleep(2000);
		for (WebElement li : profilesubtab) {
			if (li.getText().equals("Avatars")) {
				li.click();
				System.out.println("----------> Navigavtion to Avatar Tab Done");
			}
		}
		Thread.sleep(2000);
		for (WebElement li : profilesubtab) {
			if (li.getText().equals("Reset Password")) {
				li.click();
				System.out.println("----------> Navigavtion to ResetPassword Tab Done");
			}
		}
	}

	public static void NavigateToJackpot(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu_JACKPOT")));
		driver.findElement(By.id("menu_JACKPOT")).click();
		System.out.println("Navigation to Jackpot Done");
	}

	public static void NavigateToReferral(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu_REFERRAL")));
		driver.findElement(By.id("menu_REFERRAL")).click();
		System.out.println("Navigation to Referral Done");

		WebElement referraltab = driver.findElement(By.xpath("//*[contains(@id,'referral_container_')]"));
		List<WebElement> referralsubtab = referraltab.findElements(By.tagName("li"));

		for (WebElement li : referralsubtab) {
			if (li.getText().equals("Referral Form")) {
				li.click();
				System.out.println("----------> Navigavtion to Referral Form Tab Done");
			}
		}
		for (WebElement li : referralsubtab) {
			if (li.getText().equals("Referral List")) {
				li.click();
				System.out.println("----------> Navigavtion to Referral List Tab Done");
			}
		}
		for (WebElement li : referralsubtab) {
			if (li.getText().equals("Commission List")) {
				li.click();
				System.out.println("----------> Navigavtion to Commission List Tab Done");
			}
		}
	}

	public static void NavigateToMemo(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu_MEMO")));
		driver.findElement(By.id("menu_MEMO")).click();
		System.out.println("Navigation to Memo Done");

		WebElement memotab = driver.findElement(By.xpath("//*[contains(@id,'memo_container_')]"));
		List<WebElement> memosubtab = memotab.findElements(By.tagName("li"));

		for (WebElement li : memosubtab) {
			if (li.getText().equals("INBOX")) {
				li.click();
				System.out.println("----------> Navigavtion to INBOX Tab Done");
			}
		}
		for (WebElement li : memosubtab) {
			if (li.getText().equals("COMPOSE")) {
				li.click();
				System.out.println("----------> Navigavtion to COMPOSE Tab Done");
			}
		}
		for (WebElement li : memosubtab) {
			if (li.getText().equals("OUTBOX")) {
				li.click();
				System.out.println("----------> Navigavtion to OUTBOX Tab Done");
			}
		}
		for (WebElement li : memosubtab) {
			if (li.getText().equals("AUTO MEMO")) {
				li.click();
				System.out.println("----------> Navigavtion to AUTO MEMO Tab Done");
			}
		}
	}

	public static void OpenTable(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu_LOBBY")));
		driver.findElement(By.id("menu_LOBBY")).click();
		System.out.println("Navigation to Lobby Done");
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@id,'utill_playgame_btn_0_')]")));
		driver.findElement(By.xpath("//*[contains(@id,'utill_playgame_btn_0_')]")).click();
		Thread.sleep(3000);
		String tablename = driver.findElement(By.id("utill_table_body_td_0_0")).getText();
		System.out.println(tablename + "  Table Opening Done");
	}

	public static void chat(WebDriver driver, WebDriverWait wait) {
		wait.until(ExpectedConditions.elementToBeClickable(By.id("openchat")));
		driver.findElement(By.id("openchat")).click();
		System.out.println("Chat Window Opening Done");
		//driver.findElement(By.id("chatbox")).sendKeys("Message from Selenium");
		driver.findElement(By.id("user_history")).click();
		driver.findElement(By.id("user_emoji")).click();
		driver.findElement(By.id("game_history")).click();
		String dChat = driver.findElement(By.id("dealerchat")).getText();
		System.out.println("Dealer Chat :: "+dChat);
		driver.findElement(By.id("close")).click();
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		String exePath = "C:\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exePath);

		/* WebDriver driver = new ChromeDriver();
		 WebDriverWait wait = new WebDriverWait(driver, 20);
		 System.out.println("Browser Opening Done");

		 driver.manage().window().maximize();
		 driver.get("http://stage.freepoker99.com/");
		 System.out.println("Poker website Opening Done"); */

		File f = new File("D://user.txt");
		BufferedReader b = new BufferedReader(new FileReader(f));
		String readLine = ""; 

		while ((readLine = b.readLine()) != null) {

			WebDriver driver = new ChromeDriver();
			WebDriverWait wait = new WebDriverWait(driver, 20);
			System.out.println("Browser Opening Done");

			driver.manage().window().maximize();
			driver.get("http://dev.freepoker99.com/");
			System.out.println("Poker website Opening Done");

			String user = readLine;

			//login(driver, wait, user);
			register(driver,wait);
			NavigateToCashier(driver, wait);
			MakeDepositOnCashierPage(driver, wait);
			ApproveDeposit();
			NavigateToProfile(driver, wait);
			NavigateToJackpot(driver, wait);
			NavigateToReferral(driver, wait);
			NavigateToMemo(driver, wait);
			NavigateToLobby(driver, wait);
			OpenTable(driver, wait);
			chat(driver,wait);
		}
		b.close(); 

	}
}
