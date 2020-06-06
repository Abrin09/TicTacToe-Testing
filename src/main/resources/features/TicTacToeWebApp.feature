Feature: TicTacToeWebApp
 
  Test the Tic-tac-toe game with 2 Chrome's users using Cucumber

  
  Scenario: first player wins
	    Given two players registered
	    When the movements in match
			|0|1|3|2|6|
	    Then the result message is:"Player-1 wins! Player-2 looses."
  
  Scenario: second player wins
	    Given two players registered
	    When the movements in match
			|0|1|3|4|5|7|
	    Then the result message is:"Player-2 wins! Player-1 looses."
	    		  
  Scenario: any player win
	    Given two players registered
	    When the movements in match
			|1|0|3|2|5|4|6|7|8|
	   Then the result message is:"Draw!"
	   
	