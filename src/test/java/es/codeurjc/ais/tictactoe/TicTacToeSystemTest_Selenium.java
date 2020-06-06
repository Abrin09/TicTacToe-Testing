package es.codeurjc.ais.tictactoe;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TicTacToeSystemTest_Selenium {

	private static WebDriver driverX;
	private static WebDriver driverO;
	private static String player1Name;
	private static String player2Name;
	private static String[] stringGameMessage;
	private static final String SUT_URL = "http://localhost:8080";

	@Before
	public void setupTest() throws MalformedURLException {
		player1Name = new String("Player-1");
		player2Name = new String("Player-2");
		stringGameMessage = new String[] { "Player-1 wins! Player-2 looses.", "Player-2 wins! Player-1 looses.",
				"Draw!" };

		driverX = new ChromeDriver();
		driverX.get(SUT_URL);
		driverX.findElement(By.id("nickname")).sendKeys(player1Name);
		driverX.findElement(By.id("startBtn")).click();

		driverO = new ChromeDriver();
		driverO.get(SUT_URL);
		driverO.findElement(By.id("nickname")).sendKeys(player2Name);
		driverO.findElement(By.id("startBtn")).click();

	}

	@BeforeClass
	public static void setupClass() throws Throwable {
		WebApp.start();
	}

	@After
	public void teardown() {
		teardown(driverX);
		teardown(driverO);

	}

	@AfterClass
	public static void teardownClass() {
		WebApp.stop();
	}

	@Test
	public void firstPlayerWin() {
		int iterable[] = new int[] { 0, 1, 3, 2, 6 };
		cellIterator(iterable);

		String msg1 = driverX.switchTo().alert().getText();
		String msg2 = driverO.switchTo().alert().getText();

		assertThat(msg1).isEqualTo(stringGameMessage[0]);
		assertThat(msg2).isEqualTo(stringGameMessage[0]);

	}

	@Test
	public void secondPlayerWin() {
		int iterable[] = new int[] { 0, 1, 3, 4, 5, 7 };
		cellIterator(iterable);

		String msg1 = driverX.switchTo().alert().getText();
		String msg2 = driverO.switchTo().alert().getText();
		assertThat(msg1).isEqualTo(stringGameMessage[1]);
		assertThat(msg2).isEqualTo(stringGameMessage[1]);

	}

	@Test
	public void gameIsDraw() {
		int iterable[] = new int[] { 1, 0, 3, 2, 5, 4, 6, 7, 8 };
		cellIterator(iterable); 

		String msg1 = driverX.switchTo().alert().getText();
		String msg2 = driverO.switchTo().alert().getText();
		assertThat(msg1).isEqualTo(stringGameMessage[2]);
		assertThat(msg2).isEqualTo(stringGameMessage[2]);

	}

	static void cellIterator(int[] iterable) {
		for (int i = 0; i < iterable.length; i++) {
			if (i % 2 == 0) {
				driverX.findElement(By.id("cell-" + iterable[i])).click();
			} else {
				driverO.findElement(By.id("cell-" + iterable[i])).click();
			}
		}

	}

	public void setDriverAndPlayer(WebDriver driver, String URL, String playerName) {
		driver = new ChromeDriver();
		driver.get(URL);
		driver.findElement(By.id("nickname")).sendKeys(playerName);
		driver.findElement(By.id("startBtn")).click();

	}

	public void teardown(WebDriver driver) {
		if (driver != null) {
			driver.quit();
		}

	}
}
