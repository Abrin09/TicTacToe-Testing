package es.codeurjc.ais.tictactoe;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TicTacToeSystemTest_CucumberSteps {
	private static WebDriver driverX;
	private static WebDriver driverO;
	static String player1;
	static String player2;
	private static final String SUT_URL = "http://localhost:8080";

	@Given("^two players registered$")
	public void two_players_registered() throws Throwable {		
		player1 = new String("Player-1");
		player2 = new String("Player-2");
		
		driverX = new ChromeDriver();
		driverX.get(SUT_URL);
		driverX.findElement(By.id("nickname")).sendKeys(player1);
		driverX.findElement(By.id("startBtn")).click();

		driverO = new ChromeDriver();
		driverO.get(SUT_URL);
		driverO.findElement(By.id("nickname")).sendKeys(player2);
		driverO.findElement(By.id("startBtn")).click();
		

	}

	@When("^the movements in match$")
	public void the_movements_in_match(List<Integer> args) throws Throwable {
		for (int n = 0; n < args.size(); n++) {
			String cell = "cell-" + args.get(n);
			if (n % 2 == 0) {
				driverX.findElement(By.id(cell)).click();
			} else {
				driverO.findElement(By.id(cell)).click();
			}
		}
	}

	@Then("^the result message is:\"([^\"]*)\"$")
	public void the_result_message_is(String arg1) throws Throwable {
		String msg1 = driverX.switchTo().alert().getText();
		String msg2 = driverO.switchTo().alert().getText();

		assertThat(msg1).isEqualTo(arg1);
		assertThat(msg2).isEqualTo(arg1);

		teardown(driverX);
		teardown(driverO);

	}

	public void teardown(WebDriver driver) {
		if (driver != null) {
			driver.quit();
		}

	}

	public void setDriverAndPlayer(WebDriver driver, String URL, String playerName) {
		driver = new ChromeDriver();
		driver.get(URL);
		driver.findElement(By.id("nickname")).sendKeys(playerName);
		driver.findElement(By.id("startBtn")).click();

	}
}