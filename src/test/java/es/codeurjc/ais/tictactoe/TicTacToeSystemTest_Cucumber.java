package es.codeurjc.ais.tictactoe;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty" }, features = { "classpath:features" }, glue = { "es.codeurjc.ais.tictactoe" })
public class TicTacToeSystemTest_Cucumber {

	@BeforeClass
	public static void setupClass(){
		WebApp.start();
	}

	@AfterClass
	public static void teardownClass() {
		WebApp.stop();
	}
}
