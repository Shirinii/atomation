package planbox.maven;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class cc {
	public static void viewIdeasInGridView() {

	ChromeOptions chromeOptions = new ChromeOptions();
	chromeOptions.addArguments("--remote-allow-origins=*");
	WebDriver driver = new ChromeDriver(chromeOptions);
	driver.get("https://www.google.com");
}}
