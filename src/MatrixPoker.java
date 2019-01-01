import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.Assertion;

import com.google.common.io.Files;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class MatrixPoker {

	static ExtentReports extent;
	static ExtentTest logger;
	
	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm"); 
	static Calendar cal = Calendar.getInstance();
	//static Date date = new Date();  
	static String datetime = dateFormat.format(cal.getTime());
	static String reportLocation = "/home/ubuntu/pokerauto/reports/PKR_"+datetime+".html";

	static Random ran = new Random();
	static int ranint = ran.nextInt(100000);
	static String username = "Auto" + ranint;
	//static String username = "Abhijeet";
	static String password = "abc123";
	//static String password = "123456";
	static int bankacc = randomNumberInRange(1000000000, 2100000000);
	static String account = Integer.toString(bankacc);
	static int  playeronseat = 0;
	static Boolean isPlayerSeated = false;
	static int playeronthistable = 0;
	static int seatonthistable = 0;
	static long playermainbalance = 0;
	
	public static void UploadFile() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost("http://localhost:8080/MyWebSite1/UploadDownloadFileServlet");

			FileBody bin = new FileBody(new File("E:\\meter.jpg"));
			StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

			HttpEntity reqEntity = MultipartEntityBuilder.create()
					.addPart("bin", bin)
					.addPart("comment", comment)
					.build();


			httppost.setEntity(reqEntity);

			System.out.println("executing request " + httppost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					System.out.println("Response content length: " +    resEntity.getContentLength());
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

	public static int randomNumberInRange(int min, int max) {
		Random random = new Random();
		return random.nextInt((max - min) + 1) + min;
	}
	
	public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {

		String dateName = datetime;
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String destination = "/home/ubuntu/pokerauto/screenshots/"+screenshotName+dateName+".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	public static void openNewTab(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.open('about:blank','_blank');");
	}

	public static void register(WebDriver driver, WebDriverWait wait) throws Exception {

		logger = extent.startTest("REGISTRATION");

		wait.until(ExpectedConditions.elementToBeClickable(By.id("register_user")));
		driver.findElement(By.id("register_user")).click();
		System.out.println("Opening Register Page Done");
		String regscreenshotPath = getScreenshot(driver, "regpage");
		logger.log(LogStatus.PASS, "OPENING REGISTER PAGE DONE" + logger.addScreenCapture(regscreenshotPath));
		Thread.sleep(3000);

		// Random ran = new Random();
		// int ranint = ran.nextInt(1000);
		// String username = "Auto"+ranint;
		// int bankacc = randomNumberInRange(1000000000,2100000000);
		// String account = Integer.toString(bankacc);

		driver.findElement(By.id("runame")).sendKeys(username);
		driver.findElement(By.id("umail")).sendKeys(username + "@test.com");
		driver.findElement(By.id("rupassword")).sendKeys("abc123");
		driver.findElement(By.id("ucpassword")).sendKeys("abc123");
		Select Sel = new Select(driver.findElement(By.id("selectBank")));
		Sel.selectByIndex(1);
		driver.findElement(By.id("aname")).sendKeys("Auto");
		driver.findElement(By.id("anumber")).sendKeys(account);
		Select SelQ = new Select(driver.findElement(By.id("selectquestion")));
		SelQ.selectByIndex(1);
		driver.findElement(By.id("uanswer")).sendKeys("qwerty");
		Thread.sleep(2000);
		driver.findElement(By.id("ragissubmit")).click();

		Thread.sleep(2000);
		Boolean isPresent = driver.findElements(By.id("loginPage")).size() > 0;

		if(isPresent == true) {
			String regdonescreenshotPath = getScreenshot(driver, "regpass");
			logger.log(LogStatus.PASS, "REGISTRATION DONE FOR PLAYER : "+ username + logger.addScreenCapture(regdonescreenshotPath));
		}
		else {
			logger.log(LogStatus.FAIL, "REGISTRATION FAILED FOR PLAYER");
		}
		wait.until(ExpectedConditions.elementToBeClickable(By.id("loginPage")));
		driver.findElement(By.id("loginPage")).click();

		extent.endTest(logger);
	}

	public static void login(WebDriver driver, WebDriverWait wait) throws Exception {

		logger = extent.startTest("LOGIN");

		//		String user = JOptionPane.showInputDialog("Enter Username");
		//		System.out.println("USERNAME : "+user);
		//		String password = JOptionPane.showInputDialog("Enter Password");
		//		System.out.println("PASSWORD : "+password);
		//		String captcha = JOptionPane.showInputDialog("Enter Captcha");
		//		System.out.println("CAPTCHA : "+captcha);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("logsubmit")));
		driver.findElement(By.id("uname")).sendKeys(username);
		logger.log(LogStatus.INFO, "USERNAME ENTERED");
		driver.findElement(By.id("upassword")).sendKeys(password);
		logger.log(LogStatus.INFO, "PASSWORD ENTERED");
		//driver.findElement(By.id("logincaptcha1")).sendKeys(captcha);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("logsubmit")));
		driver.findElement(By.id("logsubmit")).click();
		System.out.println("LOGIN DONE FOR PLAYER : " + username);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("agree")));
		Boolean isPresent = driver.findElements(By.id("agree")).size() > 0;
		if(isPresent == true) {
			String screenshotPath = getScreenshot(driver, "loginpass");
			logger.log(LogStatus.PASS, "LOGIN DONE FOR PLAYER : " + username + logger.addScreenCapture(screenshotPath));
		}
		else {
			String logintext = driver.findElement(By.id("login_error")).getText();
			if(logintext.equals("User Details Does Not Found For This User")) {
				logger.log(LogStatus.INFO, logintext);
				logger.log(LogStatus.FAIL,"USERNAME & PASSWORD NOT CORRECT FOR USER : " + username);
				extent.endTest(logger);
				extent.flush();
				driver.quit();
			}
			else if(logintext.equals("Masukkan UserID")) {
				logger.log(LogStatus.INFO, logintext);
				logger.log(LogStatus.FAIL,"PLEASE ENTER USERNAME");
				extent.endTest(logger);
				extent.flush();
				driver.quit();			
			}
			else if(logintext.equals("Masukkan Password")) {
				logger.log(LogStatus.INFO, logintext);
				logger.log(LogStatus.FAIL,"PLEASE ENTER PASWORD");
				extent.endTest(logger);
				extent.flush();
				driver.quit();			
			}			
		}



		wait.until(ExpectedConditions.elementToBeClickable(By.id("agree")));
		driver.findElement(By.id("agree")).click();
		System.out.println("ACCEPTING TERMS DONE");
		String acceptscreenshotPath = getScreenshot(driver, "acceptpass");
		logger.log(LogStatus.PASS,"ACCEPTING TERMS DONE" + logger.addScreenCapture(acceptscreenshotPath));
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("playerbalance")));
		
		

		String splayermainbalance = driver.findElement(By.id("playerbalance")).getText();	
		String tempplayermainbalance = splayermainbalance.replace(",","");
		//System.out.println(tempplayermainbalance);
		playermainbalance = Long.parseLong(tempplayermainbalance);
		System.out.println("PLAYER MAIN BALANCE : "+playermainbalance);
		String pbalancecreenshotPath = getScreenshot(driver, "pbalancepass");
		logger.log(LogStatus.INFO,"PLAYER MAIN BALANCE : "+ playermainbalance + logger.addScreenCapture(pbalancecreenshotPath));

		extent.endTest(logger);
		extent.flush();
	}

	public static void NavigateToLobby(WebDriver driver, WebDriverWait wait) throws InterruptedException {

		logger = extent.startTest("NAVIGATION TO LOBBY");

		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("aq_tomenu_li_1")));
		driver.findElement(By.id("aq_tomenu_li_1")).click();
		System.out.println("Navigation to Lobby Done");
		logger.log(LogStatus.PASS, "NAVIGATION TO LOBBY DONE");

		//No.of Columns
		//List<WebElement>  col = driver.findElements(By.xpath("//*[contains(@id,'utill_table_body')]"));
		//System.out.println("No of cols are : " +col.size()); 
		//No.of rows 
		//List<WebElement>  rows = driver.findElements(By.id("//*[contains(@id,'utill_table_body')]")); 
		//System.out.println("No of rows are : " + rows.size());

		extent.endTest(logger);
		extent.flush();
	}

	public static void NavigateToCashier(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("aq_tomenu_li_2")));
		driver.findElement(By.id("aq_tomenu_li_2")).click();
		System.out.println("Navigation to Cashier Done");

		WebElement cashiertab = driver.findElement(By.xpath("//*[contains(@id,'tab_container_')]"));
		List<WebElement> cashiersubtab = cashiertab.findElements(By.tagName("li"));

		for (WebElement li : cashiersubtab) {
			if (li.getText().equals("Deposit")) {
				li.click();
				System.out.println("----------> Navigavtion to DEPOSIT Tab Done");
			}
		}
		Thread.sleep(2000);
		for (WebElement li : cashiersubtab) {
			if (li.getText().equals("Withdraw")) {
				li.click();
				System.out.println("----------> Navigavtion to WITHDRAW Tab Done");
			}
		}
		Thread.sleep(2000);
		for (WebElement li : cashiersubtab) {
			if (li.getText().equals("Transactions")) {
				li.click();
				System.out.println("----------> Navigavtion to TRANSACTIONS Tab Done");
			}
		}
		Thread.sleep(2000);
	}

	public static void MakeDepositOnCashierPage(WebDriver driver, WebDriverWait wait) throws InterruptedException {

		logger = extent.startTest("MAKE DEPOSIT ON CASHIER PAGE");

		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("aq_tomenu_li_2")));
		driver.findElement(By.id("aq_tomenu_li_2")).click();
		System.out.println("Navigation to Cashier Done");
		logger.log(LogStatus.PASS, "NAVIGATION TO CASHIER PAGE DONE");
		WebElement cashiertab = driver.findElement(By.xpath("//*[contains(@id,'tab_container_')]"));
		List<WebElement> cashiersubtab = cashiertab.findElements(By.tagName("li"));

		for (WebElement li : cashiersubtab) {
			if (li.getText().equals("Deposit")) {
				li.click();
				System.out.println("----------> Navigavtion to DEPOSIT Tab Done");
				logger.log(LogStatus.PASS, "NAVIGATION TO DEPOSIT TAB DONE");
			}
		}

		Thread.sleep(5000);

		Select SelBank = new Select(driver.findElement(By.id("selectBankAdmin")));
		SelBank.selectByIndex(1);
		driver.findElement(By.id("depositamount")).sendKeys("200000");
		driver.findElement(By.id("note")).sendKeys("AUTOMATION TEST");
		//		String captcha = JOptionPane.showInputDialog("Enter Captcha");
		//		System.out.println("USERNAME : "+captcha);
		//		driver.findElement(By.id("logincaptcha1")).sendKeys(captcha);
		//driver.findElement(By.xpath("//*[contains(text(),'Kirim')][contains(@id,'depositsubmit')]")).click();
		driver.findElement(By.name("depositsubmit")).click();
		Thread.sleep(2000);
		String depositmsg = driver.findElement(By.xpath("//*[contains(@id,'depoistContainer_')]/div[2]/div")).getText();
		System.out.println(depositmsg);

		if(depositmsg.equals("Already Your Previous Request Is In Pending Mode.")) {
			logger.log(LogStatus.WARNING, depositmsg);
		}
		else if(depositmsg.equals("Your Deposit Request Sent Successfully")) {
			logger.log(LogStatus.PASS, depositmsg);
		}
		else {
			logger.log(LogStatus.FAIL, depositmsg);
		}

		//Assert.assertEquals("Your Deposit Request Sent Successfully", depositmsg);
		extent.endTest(logger);
		extent.flush();
	}

	public static void MakeWithdrawOnCashierPage(WebDriver driver, WebDriverWait wait) throws InterruptedException {

		logger = extent.startTest("MAKE WITHDRAW ON CASHIER PAGE");

		wait.until(ExpectedConditions.elementToBeClickable(By.id("aq_tomenu_li_2")));
		driver.findElement(By.id("aq_tomenu_li_2")).click();
		System.out.println("Navigation to Cashier Done");
		logger.log(LogStatus.PASS, "NAVIGATION TO CASHIER PAGE DONE");

		WebElement cashiertab = driver.findElement(By.xpath("//*[contains(@id,'tab_container_')]"));
		List<WebElement> cashiersubtab = cashiertab.findElements(By.tagName("li"));

		for (WebElement li : cashiersubtab) {
			if (li.getText().equals("Withdraw")) {
				li.click();
				System.out.println("----------> Navigavtion to WITHDRAW Tab Done");
				logger.log(LogStatus.PASS, "NAVIGATION TO WITHDRAW TAB DONE");
			}
		}
		Thread.sleep(5000);

		driver.findElement(By.id("withdrawamount")).sendKeys("1000");
		//String pass = JOptionPane.showInputDialog("Enter Password");
		//System.out.println("Password : "+pass);
		driver.findElement(By.id("password")).sendKeys(password);

		WebElement withbutton = driver.findElement(By.xpath("//*[contains(@id,'withdrawsubmit')]"));
		System.out.println(withbutton.getAttribute("id"));
		String butoonid = withbutton.getAttribute("id");

		driver.findElement(By.id(butoonid)).click();
		Thread.sleep(2000);
		String withdrawmsg = driver.findElement(By.xpath("//*[contains(@id,'withdrawContainer_')]/div[1]/div")).getText();
		System.out.println(withdrawmsg);

		String splayernewbalance = driver.findElement(By.id("playerbalance")).getText();	
		String tempplayernewbalance = splayernewbalance.replace(",","");
		Long playernewbalance = Long.parseLong(tempplayernewbalance);
		System.out.println("PLAYER NEW BALANCE : "+playernewbalance);
		System.out.println("PLAYER BALANCE CHANGE AFTER WITHDRAW: "+(playernewbalance-playermainbalance));

		if(withdrawmsg.equals("Already Your Previous Request Is In Pending Mode.")) {
			logger.log(LogStatus.WARNING, withdrawmsg);
		}
		else if(withdrawmsg.equals("Your Withdraw Request Sent Successfully")) {
			logger.log(LogStatus.PASS, withdrawmsg);
		}
		else {
			logger.log(LogStatus.FAIL, withdrawmsg);
		}

		//Assert.assertEquals("Your Withdraw Request Sent Successfully", withdrawmsg);

		extent.endTest(logger);
		extent.flush();
	}

	public static void ApproveDeposit(String user) throws InterruptedException {

		logger = extent.startTest("APPROVE DEPOST FROM ADMIN");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("window-size=1200x600");
		
		WebDriver driver = new ChromeDriver(options);
		//WebDriver driver = new FirefoxDriver();

		WebDriverWait wait = new WebDriverWait(driver, 20);
		System.out.println("Browser Opening Done");
		driver.manage().window().maximize();
		driver.get("http://18.136.60.168");
		System.out.println("Opening Admin URL Successfully Done");
		logger.log(LogStatus.PASS, "ADMIN WEBSITE OPENING DONE");
		Thread.sleep(3000);

		driver.findElement(By.id("login")).sendKeys("testadmin");
		logger.log(LogStatus.INFO, "USERNAME ENTERED : testadmin");
		driver.findElement(By.id("password")).sendKeys("testadmin");
		logger.log(LogStatus.INFO, "PASSWORD ENTERED : testadmin");
		driver.findElement(By.id("login-submit")).click();
		System.out.println("Login to admin Done");
		logger.log(LogStatus.PASS, "LOGIN TO ADMIN DONE");

		wait.until(ExpectedConditions.elementToBeClickable(By.id("primary")));
		driver.findElement(By.xpath("//*[@id=\"primary\"]/ul/li[7]")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"primary\"]/ul/ul[7]/li[6]")));
		driver.findElement(By.xpath("//*[@id=\"primary\"]/ul/ul[7]/li[6]")).click();
		System.out.println("Navigation to Deposit Page Done");
		logger.log(LogStatus.PASS, "NAVIGATION TO TRANSACTION REPORT AND THEN DEPOSIT DONE");
		Thread.sleep(2000);

		List<WebElement>  rows = driver.findElements(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr"));
		int depositEnteries = rows.size();
		System.out.println("No of Deposit Request rows are : " + depositEnteries);
		logger.log(LogStatus.INFO, "NO OF DEPOSIT REQUEST PENDING : " + depositEnteries);

		for(int i =1;i<=depositEnteries;i++)
		{
			String match = driver.findElement(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr["+i+"]/td[3]")).getText();

			String[] usermatch = match.split("\n", 2);
			match = usermatch[0];
			System.out.println("User : "+match);

			if(match.equals(user))
			{
				driver.findElement(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr["+i+"]/td[1]/input")).sendKeys(Keys.SPACE);
				driver.findElement(By.xpath("//*[@id=\"actionMenu\"]/tbody/tr/td[5]/a")).click();
				System.out.println("Deposit Approved for : " + user);
				logger.log(LogStatus.PASS, "DEPOSIT APPROVER FOR USER : " + user);
				break;
			}
		}
		Thread.sleep(3000);
		driver.close();

		extent.endTest(logger);
		extent.flush();
	}

	public static void ValidateBalanceAfterDepositApprove(WebDriver driver, WebDriverWait wait) throws InterruptedException {

		logger = extent.startTest("BALANCE AFTER DEPOSIT APPROVE");

		String splayernewbalance = driver.findElement(By.id("playerbalance")).getText();	
		String tempplayernewbalance = splayernewbalance.replace(",","");
		Long playernewbalance = Long.parseLong(tempplayernewbalance);
		System.out.println("PLAYER OLD BALANCE : "+playermainbalance);
		logger.log(LogStatus.INFO, "PLAYER OLD BALANCE : "+playermainbalance);
		System.out.println("PLAYER NEW BALANCE : "+playernewbalance);
		logger.log(LogStatus.INFO, "PLAYER NEW BALANCE : "+playernewbalance);
		System.out.println("PLAYER BALANCE CHANGE AFTER DEPOSIT: "+(playernewbalance-playermainbalance));
		logger.log(LogStatus.INFO, (playernewbalance-playermainbalance)+" AMOUNT DEPOSIT MADE");

		if((playernewbalance-playermainbalance)==25000) {
			logger.log(LogStatus.PASS, "DEPOSIT SUCCESS");
		}
		else {
			logger.log(LogStatus.FAIL, "DEPOSIT NOT SUCCESS");
		}

		extent.endTest(logger);
		extent.flush();
	}

	public static void ApproveWithdraw(String user) throws InterruptedException {

		logger = extent.startTest("APPROVE WITHDRAW ON ADMIN");

		//WebDriver driver = new ChromeDriver();
		WebDriver driver = new FirefoxDriver();

		WebDriverWait wait = new WebDriverWait(driver, 20);
		System.out.println("Browser Opening Done");
		driver.manage().window().maximize();
		driver.get("http://18.136.60.168");
		System.out.println("Opening Admin URL Successfully Done");
		logger.log(LogStatus.PASS, "ADMIN WEBSITE OPENING DONE");
		Thread.sleep(3000);

		driver.findElement(By.id("login")).sendKeys("testadmin");
		driver.findElement(By.id("password")).sendKeys("testadmin");
		driver.findElement(By.id("login-submit")).click();
		System.out.println("Login to admin Done");
		logger.log(LogStatus.PASS, "LOGIN TO ADMIN DONE");

		wait.until(ExpectedConditions.elementToBeClickable(By.id("primary")));
		driver.findElement(By.xpath("//*[@id=\"primary\"]/ul/li[7]")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"primary\"]/ul/ul[7]/li[6]")));
		driver.findElement(By.xpath("//*[@id=\"primary\"]/ul/ul[7]/li[6]")).click();
		driver.findElement(By.xpath("//*[@id=\"main\"]/div[2]/div[1]/div/a[2]")).click();
		System.out.println("Navigation to Withdraw Page Done");
		logger.log(LogStatus.PASS, "NAVIGATION TO TRANSACTION REPORT AND THEN WITHDRAW DONE");
		Thread.sleep(2000);

		List<WebElement>  rows = driver.findElements(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr"));
		int withdrawEnteries = rows.size();
		System.out.println("No of Withdraw Request rows are : " + withdrawEnteries);
		logger.log(LogStatus.INFO, "NO OF WITHDRAW REQUEST PENDING : " + withdrawEnteries);

		for(int i =1;i<=withdrawEnteries;i++)
		{
			String match = driver.findElement(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr["+i+"]/td[3]")).getText();

			String[] usermatch = match.split("\n", 2);
			match = usermatch[0];
			System.out.println("User : "+match);

			if(match.equals(user))
			{
				driver.findElement(By.xpath("//*[@id=\"DataTables_Table_0\"]/tbody/tr["+i+"]/td[1]/input")).sendKeys(Keys.SPACE);
				driver.findElement(By.xpath("//*[@id=\"actionMenu\"]/tbody/tr/td[5]/a")).click();
				System.out.println("WITHDRAW Approved for : " + user);
				logger.log(LogStatus.PASS, "WITHDRAW APPROVER FOR USER : " + user);
				break;
			}
		}
		Thread.sleep(3000);
		driver.close();

		extent.endTest(logger);
		extent.flush();
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
			if (li.getText().equals("Formulir referral")) {
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
			if (li.getText().equals("Daftar komisi")) {
				li.click();
				System.out.println("----------> Navigavtion to Commission List Tab Done");
			}
		}
	}
	
	public static void MakeReferralUser(WebDriver driver,WebDriverWait wait) throws InterruptedException {
		logger = extent.startTest("MAKE A REFERRAl USER");
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("aq_tomenu_li_5")));
		driver.findElement(By.id("aq_tomenu_li_5")).click();
		logger.log(LogStatus.INFO, "NAVIGATION TO REFFERAL DONE");
		
		WebElement referraltab = driver.findElement(By.xpath("//*[contains(@id,'referral_container_')]"));
		List<WebElement> referralsubtab = referraltab.findElements(By.tagName("li"));

		for (WebElement li : referralsubtab) {
			if (li.getText().equals("Formulir referral")) {
				li.click();
				System.out.println("----------> Navigavtion to Referral Form Tab Done");
				logger.log(LogStatus.INFO, "NAVIGATION TO REFFERAL FORM TAB DONE");
			}
		}
		int ranrefint = ran.nextInt(10000);
		String referralusername = "AutoR" + ranrefint;
		driver.findElement(By.id("user_name")).sendKeys(referralusername);
		driver.findElement(By.id("pwd")).sendKeys("abc123");
		driver.findElement(By.id("confirm_pwd")).sendKeys("abc123");
		driver.findElement(By.id("email")).sendKeys(referralusername + "@test.com");
		Select SelB = new Select(driver.findElement(By.id("selectBank")));
		SelB.selectByIndex(1);
		driver.findElement(By.id("accountholdername")).sendKeys("AutoR");
		int referralbankacc = randomNumberInRange(1000000000, 2100000000);
		String referralaccount = Integer.toString(referralbankacc);
		driver.findElement(By.id("accountnumber")).sendKeys(referralaccount);
		Select SelQ = new Select(driver.findElement(By.id("selectquestion")));
		SelQ.selectByIndex(1);
		driver.findElement(By.id("securityanswer")).sendKeys("qwerty");
		Thread.sleep(2000);
		driver.findElement(By.id("sendreferral")).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("referral_tabs-1")));
		
		Boolean isPresent = driver.findElements(By.id("referral_tabs-1")).size() > 0;

		if(isPresent == true) {
			logger.log(LogStatus.PASS, "REFERRAL REGISTRATION DONE FOR PLAYER : "+ referralusername);
		}
		else {
			logger.log(LogStatus.FAIL, "REGISTRATION FAILED FOR PLAYER");
		}		
			
		extent.endTest(logger);
		extent.flush();
		
		logger = extent.startTest("CHECK REFFERED USER ON LIST TAB");
		
		for (WebElement li : referralsubtab) {
			if (li.getText().equals("Referral List")) {
				li.click();
				System.out.println("----------> Navigavtion to Referral List Tab Done");
			}
		}
		Thread.sleep(2000);
		List<WebElement> totalref = driver.findElements(By.xpath("//*[@id=\"referrallist\"]/tbody/tr"));	
		int totalnumberofref = totalref.size();
		System.out.println("TOTAL REFERRAL : "+totalnumberofref);
		
		for(int i=1 ; i<=totalnumberofref ; i++) {
			String user = driver.findElement(By.xpath("//*[@id=\"referrallist\"]/tbody/tr["+i+"]/td[2]")).getText();
			System.out.println(user);
			if (user.equals(referralusername)){
				logger.log(LogStatus.PASS, "USER ENTRY FOUND");
				break;
			}
		}
		
		extent.endTest(logger);
		extent.flush();
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

		logger = extent.startTest("OPENING TABLE");

		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("aq_tomenu_li_1")));
		driver.findElement(By.id("aq_tomenu_li_1")).click();
		System.out.println("NAVIGATION TO LOBBY DONE");
		logger.log(LogStatus.PASS, "NAVIGATION TO LOBBY DONE");
		Thread.sleep(3000);

		List<WebElement> rows = driver.findElements(By.xpath("//*[contains(@id,'utill_table_body_tr_')]"));	
		int totalnumberoftable = rows.size();
		System.out.println("TOTAl NUMBER OF TABLE : "+totalnumberoftable);
		logger.log(LogStatus.INFO, "TOTAl NUMBER OF TABLE ON LOBBY: "+totalnumberoftable);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		Long svalue =  (Long) js.executeScript("var y=document.getElementsByClassName(\"slimScrollDiv\");return y.scrollTop=document.getElementById(\"utill_table_body_tr_1\").offsetTop;");
		int scroll = 0;

		for(int i=0;i<totalnumberoftable;i++) {

			String tablename = driver.findElement(By.id("utill_table_body_td_"+i+"_0")).getText();
			System.out.println("TABLENAME "+i+" : "+tablename);
			logger.log(LogStatus.INFO, "TABLENAME "+i+" : "+tablename);
			String rowdata = driver.findElement(By.id("utill_table_body_tr_"+i+"")).getText();
			String ponthistable = rowdata.substring(rowdata.length() - 3,rowdata.length() - 2);
			playeronthistable = Integer.parseInt(ponthistable);
			String sonthistable = rowdata.substring(rowdata.length() - 1);
			seatonthistable = Integer.parseInt(sonthistable);
			System.out.println(playeronthistable+" PLAYER SEATED ON TABLE "+tablename+" WITH SEAT "+seatonthistable);
			logger.log(LogStatus.INFO, playeronthistable+" PLAYER SEATED ON TABLE "+tablename+" WITH SEAT "+seatonthistable);
			Thread.sleep(2000);
			if(playeronthistable >= seatonthistable) {
				System.out.println("TABLE "+tablename+" IS FULL");
				logger.log(LogStatus.WARNING, "TABLE "+tablename+" IS FULL");
				js.executeScript("$('.slimscroll-area').scrollTop("+scroll+");");
				scroll = (int) (scroll +svalue);
			}
			else {
				//driver.findElement(By.id("utill_table_body_tr_"+i+"")).click();
				js.executeScript("document.getElementById(\"utill_table_body_tr_"+i+"\").click();");
				System.out.println("OPENED TABLENAME : "+tablename);
				logger.log(LogStatus.PASS, "OPENED TABLENAME : "+tablename);
				Thread.sleep(5000);
				break;
			}				
		}

		extent.endTest(logger);
		extent.flush();

		TakeSeat(driver, wait);

		// ----------take Seat on First Table---------------------------
		//		String tablename = driver.findElement(By.id("utill_table_body_tr_0")).getText();
		//		driver.findElement(By.id("utill_table_body_tr_0")).click();
		//		System.out.println("OPENED TABLENAME : "+tablename.toString());
		//-------------------------------------------------------------------
		//----------Search table and take sit-------------------------------
		//		Boolean tableFound = false;
		//		int num =0;
		//		int scroll = 0;
		//		JavascriptExecutor js = (JavascriptExecutor) driver;
		//		Long svalue =  (Long) js.executeScript("var y=document.getElementsByClassName(\"slimScrollDiv\");return y.scrollTop=document.getElementById(\"utill_table_body_tr_1\").offsetTop;");
		//
		//		while (tableFound == false) {
		//			String tablename = driver.findElement(By.id("utill_table_body_td_"+num+"_0")).getText();
		//			System.out.println("TABLENAME : "+num+" : "+tablename);
		//			if(tablename.equals("LAOS31")) {
		//				driver.findElement(By.id("utill_table_body_tr_"+num+"")).click();
		//				System.out.println("OPENED TABLENAME : "+tablename);
		//				tableFound = true;
		//			}
		//			num = num+1;
		//			js.executeScript("$('.slimscroll-area').scrollTop("+scroll+");");
		//			scroll = (int) (scroll +svalue);
		//
		//		}
		//---------------------------------------------------------------------------------
	}

	public static void TakeSeat(WebDriver driver, WebDriverWait wait) throws InterruptedException {

		logger = extent.startTest("TAKING SEAT ON TABLE");

		JavascriptExecutor js = (JavascriptExecutor) driver;  
		Thread.sleep(10000);

		//FINDING SEAT AVAILABLITY TO SIT
		for(int i=1;i<=seatonthistable;i++) {
			Boolean seatAvailability = (Boolean) js.executeScript("return myRoot.seat_"+i+".visible");
			if(seatAvailability == true) {
				System.out.println("Player Seating at "+i+" Position on table");
				logger.log(LogStatus.PASS, "PLAYER SEATING AT "+i+" POSITION ON TABLE");
				js.executeScript("myRoot.seat_"+i+".dispatchEvent('click');", "return myRoot.seat_"+i);
				playeronseat = i;
				break;
			}
		}

//		WebElement takeseatmax = driver.findElement(By.xpath("//*[contains(@id,'utill_slider_')]"));
//		String sliderid = takeseatmax.getAttribute("id");
//		System.out.println("MIN BUYIN : "+ takeseatmax.getAttribute("min") );
//		System.out.println("MAX BUYIN : "+ takeseatmax.getAttribute("max") );
//		String maxslidervalue = takeseatmax.getAttribute("max");
//		System.out.println("VALUE BUYIN : "+ takeseatmax.getAttribute("value") );

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@id,'utill_pok_')]")));		
		driver.findElement(By.xpath("//*[contains(@id,'utill_pok_')]")).click();
		System.out.println("Player Seat Taking Done");
		logger.log(LogStatus.PASS, "PLAYER TAKING SEAT DONE");
		isPlayerSeated = true;
		Thread.sleep(3000);

		extent.endTest(logger);
		extent.flush();

		GamePlay(driver, wait);
	}

	public static void OpenTableandTakeSeat(WebDriver driver, WebDriverWait wait) throws InterruptedException {

		//---------------NAVIGATED TO LOBBY -----------------------
		Thread.sleep(3000);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("menu_LOBBY")));
		driver.findElement(By.id("menu_LOBBY")).click();
		System.out.println("NAVIGATION TO LOBBY DONE");
		Thread.sleep(3000);
		//----------------------------------------------------------
		//---------------GETTING NUMBER OF TABLE IN LOBBY ----------
		List<WebElement> rows = driver.findElements(By.xpath("//*[contains(@id,'utill_table_body_tr_')]"));	
		int totalnumberoftable = rows.size();
		System.out.println("TOTAl NUMBER OF TABLE : "+totalnumberoftable);
		Boolean isPlayerSeated = false;
		//----------------------------------------------------------
		//---------------GETTING SCROLL VALUE-----------------------
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Long svalue =  (Long) js.executeScript("var y=document.getElementsByClassName(\"slimScrollDiv\");return y.scrollTop=document.getElementById(\"utill_table_body_tr_1\").offsetTop;");
		int scroll = 0;
		//-----------------------------------------------------------
		for(int i=0;i<totalnumberoftable;i++) {
			if (isPlayerSeated == false) {

				String tablename = driver.findElement(By.id("utill_table_body_td_"+i+"_0")).getText();
				System.out.println("TABLENAME "+i+" : "+tablename);
				String rowdata = driver.findElement(By.id("utill_table_body_tr_"+i+"")).getText();
				String ponthistable = rowdata.substring(rowdata.length() - 3,rowdata.length() - 2);
				int playeronthistable = Integer.parseInt(ponthistable);
				String sonthistable = rowdata.substring(rowdata.length() - 1);
				int seatonthistable = Integer.parseInt(sonthistable);
				System.out.println(playeronthistable+" PLAYER SEATED ON TABLE WITH SEAT "+seatonthistable);
				if(playeronthistable >= seatonthistable) {
					System.out.println("TABLE "+tablename+" IS FULL");
					js.executeScript("$('.slimscroll-area').scrollTop("+scroll+");");
					scroll = (int) (scroll +svalue);
				}
				else {
					driver.findElement(By.id("utill_table_body_tr_"+i+"")).click();
					System.out.println("OPENED TABLENAME : "+tablename);
					Thread.sleep(10000);
					//isPlayerSeated = true;
					for(int j=1;j<=9;j++) {
						Boolean seatAvailability = (Boolean) js.executeScript("return myRoot.seat_"+j+".visible");
						if(seatAvailability == true) {
							System.out.println("PLAYER SEATING AT "+j+" POSITION ON TABLE");
							js.executeScript("myRoot.seat_"+j+".dispatchEvent('click');", "return myRoot.seat_"+j);

							WebElement takeseatmax = driver.findElement(By.xpath("//*[contains(@id,'utill_slider_')]"));
							System.out.println("MIN BUYIN : "+ takeseatmax.getAttribute("min") );
							System.out.println("MAX BUYIN : "+ takeseatmax.getAttribute("max") );
							System.out.println("VALUE BUYIN : "+ takeseatmax.getAttribute("value") );

							wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@id,'utill_pok_')]")));		
							driver.findElement(By.xpath("//*[contains(@id,'utill_pok_')]")).click();
							System.out.println("Player Seat Taking Done");

							playeronseat = j;
							isPlayerSeated = true;
							break;
						}
						else {
							System.out.println("ON TABLE "+tablename+" SEAT LOCATION "+j+" IS OCCUPIED");
						}
					}
				}				
			}
		}
	}

	public static void GamePlay(WebDriver driver, WebDriverWait wait) throws InterruptedException{

		logger = extent.startTest("GAMEPLAY");

		Thread.sleep(5000);

		JavascriptExecutor js = (JavascriptExecutor) driver;
		String playerBalance =  (String) js.executeScript("return myRoot.player_"+playeronseat+".panel.pBank.text");
		String pbalance = playerBalance.replace(",", "");
		System.out.println("BALANCE CHECK FOR INT : "+ pbalance);
		int balance = Integer.parseInt(pbalance);
		System.out.println("Player balance : "+ balance);
		while (balance > 0) {

			Boolean standupvisible = (Boolean) js.executeScript("return myRoot.standUp.visible");

			if(standupvisible==true) {
				Boolean action = (Boolean) js.executeScript("return myRoot.paHolderMain.ActionControls.visible");

				if(action == true) {
					js.executeScript("myRoot.paHolderMain.ActionControls.btnCheckCall.dispatchEvent('click');");
					System.out.println("Player Performed Action");

					playerBalance =  (String) js.executeScript("return myRoot.player_"+playeronseat+".panel.pBank.text");
					balance = Integer.parseInt(playerBalance);
					System.out.println("Player balance : "+ balance);

					logger.log(LogStatus.INFO, "PLAYER PERFORMED ACTION AND BALANCE ON TABLE IS : "+ balance);
				}				
			}
			else {
				js.executeScript("myRoot.backToLoby.dispatchEvent('click');");
				logger.log(LogStatus.INFO, "BACK TO LOBBY DONE");
				//driver.findElement(By.xpath("//*[contains(@id,'utill_pok_')]")).click();
				Thread.sleep(5000);
				extent.endTest(logger);
				extent.flush();
				OpenTable(driver, wait);				
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

	public static void main(String[] args) throws Exception {

		extent = new ExtentReports(reportLocation);
		logger = extent.startTest("OPENING POKER WEBSITE");
		
		String exePath = "/home/ubuntu/pokerauto/driver/chromedriver";
		//String exePath = "C:\\geckodriver.exe";

		System.setProperty("webdriver.chrome.driver", exePath);
		//System.setProperty("webdriver.gecko.driver", exePath);
		
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("window-size=1200x600");

		WebDriver driver = new ChromeDriver(options);
		//WebDriver driver = new FirefoxDriver();

		WebDriverWait wait = new WebDriverWait(driver, 20);
		System.out.println("BROWSER OPENING DONE");
		logger.log(LogStatus.PASS,"BROWSER OPENING DONE");

		driver.manage().window().maximize();
		driver.get("http://stage.freepoker99.com");
		System.out.println("POKER WEBSITE OPENING DONE");
		String pokerscreenshotPath = getScreenshot(driver, "pokeropenpass");
		logger.log(LogStatus.PASS,"POKER WEBSITE OPENING DONE"+logger.addScreenCapture(pokerscreenshotPath));
		
		extent.endTest(logger);
		extent.flush();


		register(driver, wait);
		login(driver, wait);
		MakeDepositOnCashierPage(driver, wait);
		ApproveDeposit(username);
		ValidateBalanceAfterDepositApprove(driver, wait);
		//MakeWithdrawOnCashierPage(driver, wait);
		//ApproveWithdraw(username);
		//MakeReferralUser(driver, wait);
		NavigateToLobby(driver, wait);
		OpenTable(driver, wait);

		//		File f = new File("C:\\user.txt");
		//		BufferedReader b = new BufferedReader(new FileReader(f));
		//		String readLine = ""; 
		//
		//		while ((readLine = b.readLine()) != null) {
		//
		//			extent = new ExtentReports(reportLocation);
		//			logger = extent.startTest("OPENING POKER WEBSITE");
		//			String user = "";
		//			String pass = "";
		//
		//			//WebDriver driver = new ChromeDriver();
		//			WebDriver driver = new FirefoxDriver();
		//			WebDriverWait wait = new WebDriverWait(driver, 20);
		//			System.out.println("Browser Opening Done");
		//			logger.log(LogStatus.PASS, "Browser Opening Done");
		//
		//			driver.manage().window().maximize();
		//			driver.get("http://stage.freepoker99.com");
		//			System.out.println("Poker website Opening Done");
		//			logger.log(LogStatus.PASS, "Poker website Opening Done");
		//
		//			String userandpass = readLine;
		//			String[] splited = userandpass.split("\\s+");
		//			System.out.println("USERNAME : "+ splited[0]);
		//			logger.log(LogStatus.INFO,"USERNAME FETCHED FROM FILE : "+ splited[0]);
		//			user = splited[0];
		//			System.out.println("PASSWORD : "+ splited[1]);
		//			logger.log(LogStatus.INFO,"PASSWORD FETCHED FROM FILE : "+ splited[1]);
		//			pass = splited[1];
		//
		//			extent.endTest(logger);
		//			extent.flush();
		//
		//			register(driver, wait);
		////			login(driver, wait,user,pass);
		////			MakeDepositOnCashierPage(driver, wait);
		////			ApproveDeposit(user);
		////			ValidateBalanceAfterDepositApprove(driver, wait);
		////			MakeWithdrawOnCashierPage(driver, wait,pass);
		////			ApproveWithdraw(user);
		////			NavigateToLobby(driver, wait);
		////			OpenTable(driver, wait);
		//
		//			//driver.close();			
		//
		//			driver.get(reportLocation);
		//		}
		//		b.close(); 
	}
}
