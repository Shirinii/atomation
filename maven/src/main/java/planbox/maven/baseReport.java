package planbox.maven;


import org.testng.annotations.AfterMethod;

import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import java.io.File;



import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.common.base.Function;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class baseReport {
	public static ExtentTest test;
	public static ExtentReports report;

	public static WebDriver driver;
	public static WebDriverWait wait;
	public static JavascriptExecutor jse;
	public static ExtentSparkReporter spark;
	public static ExtentReports extent;
	public static ExtentTest logger;
	public static long startTime;
	public static long duration;

	@DataProvider(name = "URLS")
	public Object[][] urlsList() throws Exception {
		// Specify the path to the file
		String filePath = System.getProperty("user.dir") + "/TestData.xlsx";

		// Create a File object using the path
		File file = new File(filePath);

		Object[][] testObjArray = ExcelUtils.getTableArray(file,
				"Sheet1");

		return (testObjArray);

	}

	public static void analyzeLog() {

		wait.until(new Function<WebDriver, Object>() {
			public Object apply(WebDriver driver) {
				final String currentUrl = driver.getCurrentUrl();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
				List<LogEntry> logEntryList = new ArrayList<>(logEntries.getAll());
				for (LogEntry entry : logEntryList) {
					if (entry.getLevel().getName().equals("SEVERE") && ((entry.toJson().toString().contains("fonts")
							|| entry.toJson().toString().contains("The specified blob does not exist")))) {
						// logger.warning();
						logger.log(Status.WARNING,
								MarkupHelper
										.createLabel(
												("<p>" + "THERE IS A SEVER ERROR ON THIS URL" + "<p>" + currentUrl
														+ "<p>" + entry.getLevel() + " " + entry.toJson()),
												ExtentColor.ORANGE));
					} else if (entry.getLevel().getName().equals("SEVERE")
							&& ((!entry.toJson().toString().contains("fonts")
									|| !entry.toJson().toString().contains("The specified blob does not exist")))) {
						// logger.warning("<p>" + entry.getLevel() + " " + entry.toJson());
						logger.log(Status.FAIL,
								MarkupHelper
										.createLabel(
												("<p>" + "THERE IS A SEVER ERROR ON THIS URL" + "<p>" + currentUrl
														+ "<p>" + entry.getLevel() + " " + entry.toJson()),
												ExtentColor.RED));
					}
				}
				return true;
			}

		});
	}

	public static void missingTransaction() {

		String script = "return document.getElementsByClassName('main-content-inner')[0].innerText;";
		String text = (String) jse.executeScript(script);
		String currentUrl = driver.getCurrentUrl();
		if (text.contains("missing")) {
			System.out.println("Missing Transaction" + driver.getCurrentUrl());
			logger.log(Status.WARNING, MarkupHelper
					.createLabel(("<p>" + "THERE IS A Missing Transaction" + "<p>" + currentUrl), ExtentColor.ORANGE));

		}
	}

	@BeforeTest
	public void setUp() {
		//System.setProperty("webdriver.chrome.driver", "/Users/shirin/Downloads/chromedriver");
		//System.setProperty("webdriver.chrome.silentOutput", "true");
		 WebDriverManager.chromedriver().setup();
		 driver = new ChromeDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		jse = (JavascriptExecutor) driver;
		Dimension dimension = new Dimension(1440, 800);
		driver.manage().window().setSize(dimension);
		// for report
		extent = new ExtentReports();
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		String reportPath = "test-output" + File.separator + "ExtentReport";
		spark = new ExtentSparkReporter(reportPath + dateName + ".html");
		extent.attachReporter(spark);
		extent.setSystemInfo("Host Name", "Planbox");
		extent.setSystemInfo("Environment", "Production");
		spark.config().setDocumentTitle("Report");
		spark.config().setReportName("Sanity");
		spark.config().setTheme(Theme.STANDARD);
		spark.config().getReporter();
		startTime = System.currentTimeMillis();
	}

	public static String getScreenShot(WebDriver driver, String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
        String separator = System.getProperty("file.separator"); 
        String destination = System.getProperty("user.dir") + separator + "Screenshotstest" + separator + screenshotName + dateName + ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	@AfterTest
	public void tearDown() {
		extent.flush();
		driver.quit();

	}

	@AfterMethod

	public void getResult(ITestResult result) throws Exception {
		if ((result.getStatus() == ITestResult.FAILURE)) {
			logger.log(Status.FAIL,
					MarkupHelper.createLabel(result.getThrowable() + " - Test Case Failed", ExtentColor.RED));
			String screenshotPath = getScreenShot(driver, result.getName());
			Assert.fail("Test Case Failed Snapshot is attached " + logger.addScreenCaptureFromPath(screenshotPath));
		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));

		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.log(Status.SKIP,
					MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
		}
	}

}
