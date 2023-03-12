package planbox.maven;

import org.testng.annotations.Test;


import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class SanityTestOveral extends baseReport {

	public static void viewIdeasInGridView() {
		String xpath = "//*[contains(@id,'idea-modal')]";
		WebElement ideasInGridView = wait.until(driver -> (WebElement) jse.executeScript(
				"return document.evaluate(arguments[0], document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;",
				xpath));
		jse.executeScript("var evt = document.createEvent('MouseEvents');"
				+ "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
				+ "arguments[0].dispatchEvent(evt);", ideasInGridView);
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		int tabSize = tabs.size();
		driver.switchTo().window(tabs.get(tabSize - 1));
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
		analyzeLog();
	}

	public static void viewIdeasInListView() {
		String xpath = "//*[@class='new-idea-theme-title']//*[contains(@href,'/Ideas/')]";

		WebElement ideasInListAndTileView = wait.until(driver -> (WebElement) jse.executeScript(
				"return document.evaluate(arguments[0], document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;",
				xpath));
		jse.executeScript("var evt = document.createEvent('MouseEvents');"
				+ "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
				+ "arguments[0].dispatchEvent(evt);", ideasInListAndTileView);
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
		analyzeLog();
	}

	public static void viewIdeasInTileView() {
		String xpath = "//*[@class='idea-card-block']//*[contains(@href,'/Ideas/')]";

		WebElement ideasInListAndTileView = wait.until(driver -> (WebElement) jse.executeScript(
				"return document.evaluate(arguments[0], document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;",
				xpath));
		jse.executeScript("var evt = document.createEvent('MouseEvents');"
				+ "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
				+ "arguments[0].dispatchEvent(evt);", ideasInListAndTileView);
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
		analyzeLog();
	}

	public static void IdeasInIdeatabInChallenge() {
		WebElement toggleIdeaMenue = null;
		try {
			toggleIdeaMenue = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[contains(@class,'dropdown-toggle')]//*[@id='selectedIdeaLayout']")));
		} catch (Exception changeInAppreanceOfToggle) {
			toggleIdeaMenue = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[contains(@class,'dropdown-toggle')]//*[@id='selectedIdeaLayout']")));
		}
		jse.executeScript("var evt = document.createEvent('MouseEvents');"
				+ "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
				+ "arguments[0].dispatchEvent(evt);", toggleIdeaMenue);
		WebElement toggleListView = (WebElement) (jse)
				.executeScript("return document.querySelector('[data-view=\\\"list\\\"]');");
		WebElement toggleGridView = (WebElement) (jse)
				.executeScript("return document.querySelector('[data-view=\\\"grid\\\"]');");
		if (toggleGridView != null && toggleGridView.getAttribute("class").contains("active")) {
			try {
				viewIdeasInGridView();
			} catch (Exception problemInShowingIdeas) {
				logger.log(Status.FAIL,
						MarkupHelper.createLabel("No IDEAS DISPLAY in idea tab in challenge view", ExtentColor.RED));
				System.out.println("There is a problem on displaying IDEAS in idea tab in challenge view");
			}
		} else if (toggleListView != null && toggleListView.getAttribute("class").contains("active")) {
			try {
				viewIdeasInListView();
			} catch (Exception problemInShowingIdeas) {
				logger.log(Status.FAIL,
						MarkupHelper.createLabel("No IDEAS DISPLAY in idea tab in challenge view", ExtentColor.RED));
				System.out.println("There is a problem on displaying IDEAS in idea tab in challenge view");
			}
		} else {
			try {
				viewIdeasInTileView();
			} catch (Exception problemInShowingIdeas) {
				logger.log(Status.FAIL,
						MarkupHelper.createLabel("No IDEAS DISPLAY in idea tab in challenge view", ExtentColor.RED));
				System.out.println("There is a problem on displaying IDEAS in idea tab in challenge view");
			}

		}
	}

	public static void ClickOnIdeasTabInChalenges() {

		if (driver.findElements(By.id("challengeIdeas")).isEmpty()) {
			IdeasInIdeatabInChallenge();

		} else {

			jse.executeScript("var evt = document.createEvent('MouseEvents');"
					+ "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
					+ "arguments[0].dispatchEvent(evt);",
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("challengeIdeas"))));
			wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
			analyzeLog();
			missingTransaction();
		}
	}

	@Test(priority = 1, dataProvider = "URLS")
	public static void main(String url, String key, String challengeUrl, String ideaUrl, String homePageUrl,
			String name) throws InterruptedException {

		logger = extent.createTest(name);
		long startTime = System.currentTimeMillis();

		driver.get(url);
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
		wait.until(ExpectedConditions.or(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'container')]")),
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='row-fluid']"))));
		analyzeLog();
		try {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='secretkey']"))).sendKeys(key);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-secrectkey-login"))).click();}
		catch(Exception Exceptionhappended) {
			jse.executeScript("window.history.go(-1)");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='secretkey']"))).sendKeys(key);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btn-secrectkey-login"))).click();
			System.out.println("catch");
		}
		wait.until(ExpectedConditions.or(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='is-boxes']")),
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class, 'header-navbar')]"))));
		analyzeLog();
		driver.get(homePageUrl);
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class,'home-page')]")));
		analyzeLog();
		missingTransaction();
		driver.get(challengeUrl);
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
		WebElement toggleChallengeMenue = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[@class='dropdown-toggle']//*[@id='selectedChallengeLayout']")));
		

		jse.executeScript("var evt = document.createEvent('MouseEvents');"
				+ "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
				+ "arguments[0].dispatchEvent(evt);", toggleChallengeMenue);
		WebElement tileToggle = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-view='tile']")));
		WebElement listToggle = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-view='list']")));
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
		if (listToggle.getAttribute("class").contains("active")) {
			tileToggle.click();
			wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
		}
		analyzeLog();
		missingTransaction();
		WebElement v = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("challenge-tiles-container")));
		List<WebElement> subDivElements = v.findElements(By.tagName("div"));
		int numberOfSubDivElements = subDivElements.size();
		System.out.println(numberOfSubDivElements + name);
		if (numberOfSubDivElements == 0) {
			logger.log(Status.WARNING, MarkupHelper.createLabel(
					"There is a NO ACTIVE CHALLENGE in active filter for this client", ExtentColor.ORANGE));
			System.out.println("There is NO CHALLENGE in active filter on this client" + "  " + name);
			WebElement filtersToggle = wait
					.until(ExpectedConditions.presenceOfElementLocated(By.id("currentChallengeActiveFilterApplied")));
			jse.executeScript("var evt = document.createEvent('MouseEvents');"
					+ "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
					+ "arguments[0].dispatchEvent(evt);", filtersToggle);
			WebElement closeFilter = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-filter='closed']")));
			jse.executeScript("var evt = document.createEvent('MouseEvents');"
					+ "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
					+ "arguments[0].dispatchEvent(evt);", closeFilter);
			wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
			boolean var = false;
			try {
				wait.until(ExpectedConditions
						.visibilityOfAllElementsLocatedBy(By.xpath("//*[@class='challenge-card-size']")));
			} catch (Exception noChallengeInCloseTab) {
				var = true;
			}

			if (var == true) {
				logger.log(Status.WARNING, MarkupHelper.createLabel(
						"There is a NO CHALLENGE in the closed filter for this client", ExtentColor.ORANGE));
				System.out.println("There is NO CHALLENGE in the closed filter on this client");
			} else {
				WebElement challengeCard = wait.until(ExpectedConditions
						.visibilityOfElementLocated(By.xpath("//*[contains(@class,'general-tooltip')]")));
				jse.executeScript("var evt = document.createEvent('MouseEvents');"
						+ "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
						+ "arguments[0].dispatchEvent(evt);", challengeCard);
				wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
				ClickOnIdeasTabInChalenges();
			}

		} else {
			List<WebElement>  items =  wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("equalize-title")));
			System.out.println(items);
			for (WebElement i : items) {
				WebElement cardStatus = wait
						.until(ExpectedConditions.visibilityOf(i.findElement(By.className("challenge-stat-card"))));

				WebElement ideaNum = wait
						.until(ExpectedConditions.visibilityOf(cardStatus.findElement(By.className("big-info-text"))));
				int number = Integer.parseInt(ideaNum.getText());
				if (number >= 1) {
					jse.executeScript("var evt = document.createEvent('MouseEvents');"
							+ "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
							+ "arguments[0].dispatchEvent(evt);", i.findElement(By.className("general-tooltip")));
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@class, 'row')]")));
					wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
					analyzeLog();
					missingTransaction();
					ClickOnIdeasTabInChalenges();
					IdeasInIdeatabInChallenge();
					break;
				}
			}
		}

		driver.get(ideaUrl);
		wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
		WebElement toggleIdeaMenue = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(@class,'dropdown-toggle')]//*[@id='selectedIdeaLayout']")));
		analyzeLog();
		missingTransaction();

		jse.executeScript("var evt = document.createEvent('MouseEvents');"
				+ "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
				+ "arguments[0].dispatchEvent(evt);", toggleIdeaMenue);

		WebElement toggleListView = (WebElement) (jse)
				.executeScript("return document.querySelector('[data-view=\\\"list\\\"]');");
		WebElement toggleGridView = (WebElement) (jse)
				.executeScript("return document.querySelector('[data-view=\\\"grid\\\"]');");

		if (toggleListView != null && toggleListView.getAttribute("class").contains("active")) {
			viewIdeasInListView();
			missingTransaction();
		} else if (toggleGridView != null && toggleGridView.getAttribute("class").contains("active")) {
			viewIdeasInGridView();
			missingTransaction();

		} else {
			viewIdeasInTileView();
			missingTransaction();
		}
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		logger.log(Status.INFO, MarkupHelper.createLabel("time duration: " + duration, ExtentColor.BLACK));
	}
}
