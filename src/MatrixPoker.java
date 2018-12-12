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
import javax.swing.JOptionPane;

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
	static int  playeronseat = 0;

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

	public static void login(WebDriver driver, WebDriverWait wait) {

		String user = JOptionPane.showInputDialog("Enter Username");
		System.out.println("USERNAME : "+user);
		String password = JOptionPane.showInputDialog("Enter Password");
		System.out.println("PASSWORD : "+password);
		String captcha = JOptionPane.showInputDialog("Enter Captcha");
		System.out.println("CAPTCHA : "+captcha);
		
		
		driver.findElement(By.id("uname")).sendKeys(user);
		driver.findElement(By.id("upassword")).sendKeys(password);
		driver.findElement(By.id("logincaptcha1")).sendKeys(captcha);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("logsubmit")));
		driver.findElement(By.id("logsubmit")).click();
		System.out.println("Login Done For Player : " + user);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("agree")));
		driver.findElement(By.id("agree")).click();
		System.out.println("ACCEPTING TERMS DONE");
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
		Select SelBank = new Select(driver.findElement(By.id("selectBankAdmin")));
		SelBank.selectByIndex(1);
		
		driver.findElement(By.id("depositamount")).sendKeys("12000");
		driver.findElement(By.id("note")).sendKeys("Test");

		driver.findElement(By.xpath("//*[contains(text(),'Deposit')][contains(@id,'depositsubmit')]")).click();
		Thread.sleep(3000);
		String depositmsg = driver.findElement(By.xpath("//*[contains(@id,'depoistContainer_')]/div[2]/div")).getText();
		System.out.println(depositmsg);
	}

	public static void ApproveDeposit() throws InterruptedException {
		
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, 20);
		System.out.println("Browser Opening Done");
		driver.manage().window().maximize();
		driver.get("http://18.136.60.168");
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
		
	public static void Memo(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu_MEMO")));
		driver.findElement(By.id("menu_MEMO")).click();
		System.out.println("Successfully clicked on Memo tab.");
		driver.findElement(By.xpath("/html/body/div/div/section/div/div[1]/ul/li[1]/a")).click();
		System.out.println("Successfully clicked on Memo Inbox tab.");
		driver.findElement(By.xpath("//*[@id=\"tabs-1\"]")).click();
		System.out.println("Selected is Memo Inbox container");
		//Check the inbox message count.

		WebElement emailList = driver.findElement(By.xpath("/html/body/div/div/section/div/div[2]/div/div/div[1]/div[1]/div[1]/div/label/span"));
		System.out.println(emailList.getSize());
		System.out.println("Above is inbox message count.");
		//To open Memo Inbox tab.
		
		driver.findElement(By.xpath("/html/body/div/div/section/div/div[2]/div/div/div[1]/div[1]/div[3]/table/tbody/tr/td[2]")).click();
		System.out.println("Successfully opened the inbox message.");
		Thread.sleep(3000);
		
		//To open Memo Compose Tab
		driver.findElement(By.xpath("/html/body/div/div/section/div/div[1]/ul/li[2]/a")).click();
		System.out.println("Successfully opened Compose tab");
		
		//Enter field in Memo Compose form
		driver.findElement(By.id("to")).sendKeys("Error");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("subject")));
		driver.findElement(By.id("subject")).sendKeys("Test Subject");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("message")));
		driver.findElement(By.id("message")).sendKeys("Test message");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("composememo")));
		driver.findElement(By.id("composememo")).click();
		Thread.sleep(3000);

		System.out.println("Successfully send memo message.");
		Thread.sleep(3000);
		
		//To open Memo Outbox Tab
		driver.findElement(By.xpath("/html/body/div/div/section/div/div[1]/ul/li[3]/a")).click();
		System.out.println("Successfully opened Outbox tab");

		//To open Auto Memo Tab
		driver.findElement(By.xpath("/html/body/div/div/section/div/div[1]/ul/li[4]/a")).click();
		System.out.println("Successfully opened Auto Memo tab");
		
		driver.findElement(By.xpath("/html/body/div/div/section/div/div[2]/div/div/div[4]/div/div[2]/div[1]/button")).click();
		System.out.println("Successfully sent auto memo message");
		
		driver.findElement(By.xpath("/html/body/div/div/section/div/div[2]/div/div/div[4]/div/div[2]/div[2]/button")).click();
		System.out.println("Successfully sent auto memo message");
		
	}
	
	public static void LogOut(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("logout")));
		driver.findElement(By.id("logout")).click();
		System.out.println("Successfully logged out from the browser.");
		
		driver.close();
	}
	
	public static void OpenTable(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu_LOBBY")));
		driver.findElement(By.id("menu_LOBBY")).click();
		System.out.println("NAVIGATION TO LOBBY DONE");
		Thread.sleep(3000);
		
		List<WebElement> rows = driver.findElements(By.xpath("//*[contains(@id,'utill_table_body_tr_')]"));	
		int count = rows.size();
		System.out.println("TOTAl NUMBER OF TABLE : "+count);
		
		for(int i=0;i<count;i++) {
			String tablename = driver.findElement(By.id("utill_table_body_td_"+i+"_0")).getText();
			System.out.println("TABLENAME : "+i+" : "+tablename);
		}

//		String numberOfPlayer = driver.findElement(By.id("utill_table_body_td_0_2")).getText();
//		System.out.println(numberOfPlayer+" Player Seated");
//		String tablename = driver.findElement(By.id("utill_table_body_td_0_0")).getText();
//		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@id,'utill_table_body_td_0_4')]")));
//		driver.findElement(By.xpath("//*[contains(@id,'utill_table_body_td_0_4')]")).click();
		
		
		// [take Seat on First Table]
		String tablename = driver.findElement(By.id("utill_table_body_tr_0")).getText();
		driver.findElement(By.id("utill_table_body_tr_0")).click();
		System.out.println("OPENED TABLENAME : "+tablename.toString());
		
		
		// [Search table and take sit]
//		Boolean tableFound = false;
//		int num =0;
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//
//		while (tableFound == false) {
//			String tablename = driver.findElement(By.id("utill_table_body_td_"+num+"_0")).getText();
//			System.out.println(tablename);
//			if(tablename.equals("LOAS15")) {
//				driver.findElement(By.id("utill_table_body_tr_"+num+"")).click();
//				tableFound = true;
//			}
//			num = num+1;
//			js.executeScript("var y=document.getElementsByClassName(\"slimScrollDiv\");y.scrollTop=document.getElementById(\"utill_table_body_tr_"+num+"\").offsetTop;");
//			
//		}
	}

	public static void TakeSeat(WebDriver driver, WebDriverWait wait) throws InterruptedException {

		JavascriptExecutor js = (JavascriptExecutor) driver;  
		Thread.sleep(10000);

		//FINDING SEAT AVAILABLITY TO SEAT
		for(int i=1;i<=9;i++) {
			Boolean seatAvailability = (Boolean) js.executeScript("return myRoot.seat_"+i+".visible");

			if(seatAvailability == true) {
				System.out.println("Player Seating at "+i+" Position on table");
				js.executeScript("myRoot.seat_"+i+".dispatchEvent('click');", "return myRoot.seat_"+i);
				playeronseat = i;
				break;
			}
		}

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@id,'utill_pok_')]")));		
		driver.findElement(By.xpath("//*[contains(@id,'utill_pok_')]")).click();
		System.out.println("Player Seat Taking Done");		
	}
	
	public static void GamePlay(WebDriver driver, WebDriverWait wait) throws InterruptedException{
		
		Thread.sleep(5000);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String playerBalance =  (String) js.executeScript("return myRoot.player_"+playeronseat+".panel.pBank.text");
		int balance = Integer.parseInt(playerBalance);
		System.out.println("Player balance : "+ balance);
		while (balance > 0) {
			
			Boolean action = (Boolean) js.executeScript("return myRoot.paHolderMain.ActionControls.visible");
			
			if(action == true) {
				js.executeScript("myRoot.paHolderMain.ActionControls.btnCheckCall.dispatchEvent('click');");
				System.out.println("Player Performed Action");
				
				playerBalance =  (String) js.executeScript("return myRoot.player_1.panel.pBank.text");
				balance = Integer.parseInt(playerBalance);
				System.out.println("Player balance : "+ balance);
			}
		}
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
		String exePath = "src\\Driver\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exePath);

		WebDriver driver = new ChromeDriver();
		 WebDriverWait wait = new WebDriverWait(driver, 20);
		 System.out.println("BROWSER OPENING DONE");

		 driver.manage().window().maximize();
		 driver.get("https://pokermatrix66.org/");
		 System.out.println("POKER WEBSITE OPENING DONE");
		 
		 
			login(driver, wait);
			NavigateToLobby(driver, wait);
			OpenTable(driver, wait);
			TakeSeat(driver,wait);
			GamePlay(driver,wait);

//		File f = new File("D://user.txt");
//		BufferedReader b = new BufferedReader(new FileReader(f));
//		String readLine = ""; 
//
//		while ((readLine = b.readLine()) != null) {
//
//			WebDriver driver = new ChromeDriver();
//			WebDriverWait wait = new WebDriverWait(driver, 20);
//			System.out.println("Browser Opening Done");
//
//			driver.manage().window().maximize();
//			driver.get("https://pokermatrix66.org/");
//			System.out.println("Poker website Opening Done");
//
//			String user = readLine;
//
//			login(driver, wait, user);
//			NavigateToLobby(driver, wait);
//			OpenTable(driver, wait);
//			TakeSeat(driver,wait);
//			GamePlay(driver,wait);
//
//		}
//		b.close(); 

	}
}
