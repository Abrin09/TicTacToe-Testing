package es.codeurjc.ais.tictactoe;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import es.codeurjc.ais.tictactoe.TicTacToeGame.CellMarkedValue;
import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;

public class TicTacToeUnitTest {
	private TicTacToeGame gameLikeSUT;
	private Player playerOne;
	private Player playerTow;
	private Connection playerOneConnection;
	private Connection playerTowConnection;
	private CellMarkedValue cellMarkedValue;
	private WinnerValue winnerValue;
	private int MAX_CELLS_BOARD = 7;
	private int[] winnerLine = { 6, 4, 2 };

	@Before
	public void setupTest() {
		gameLikeSUT = new TicTacToeGame();
		playerOneConnection = mock(Connection.class);
		playerTowConnection = mock(Connection.class);
		gameLikeSUT.addConnection(playerOneConnection);
		gameLikeSUT.addConnection(playerTowConnection);
		playerOne = new Player(0, "O", "PlayerOne");
		playerTow = new Player(1, "X", "PlayerTow");
		gameLikeSUT.addPlayer(playerOne);
		gameLikeSUT.addPlayer(playerTow);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void verifyJoinPlayers() {
		verify(playerOneConnection).sendEvent(eq(EventType.JOIN_GAME), anyOf(hasItems(playerOne, playerTow)));
		verify(playerTowConnection).sendEvent(eq(EventType.JOIN_GAME), anyOf(hasItems(playerOne, playerTow)));
		reset(playerOneConnection);
		reset(playerTowConnection);
	}

	@Test
	public void verifyFirstPlayerOutOnBoardWins() {
		for (int cellID = 0; cellID < MAX_CELLS_BOARD; cellID++) {
			int playerID = (cellID % 2 == 0) ? 0 : 1;
			assertTrue(gameLikeSUT.checkTurn(playerID));
			assertTrue(gameLikeSUT.mark(cellID));
			verifySendEventMark(playerOneConnection, cellID, playerID);
			verifySendEventMark(playerTowConnection, cellID, playerID);
		}
		verifySendGameOver(playerOneConnection, playerOne, winnerLine);
		verifySendGameOver(playerTowConnection, playerOne, winnerLine);
		reset(playerOneConnection);
		reset(playerTowConnection);
		gameLikeSUT.restart();
	}

	@Test
	public void verifySecondPlayerOutOnBoardWins() {
		int[] match = { 1, 4, 3, 6, 0, 2, 5, 7 };
		for (int roundPutCell = 0; roundPutCell < MAX_CELLS_BOARD - 1; roundPutCell++) {
			int playerID = (roundPutCell % 2 == 0) ? 0 : 1;
			assertTrue(gameLikeSUT.checkTurn(playerID));
			gameLikeSUT.mark(match[roundPutCell]);
			verifySendEventMark(playerOneConnection, match[roundPutCell], playerID);
			verifySendEventMark(playerTowConnection, match[roundPutCell], playerID);
		}
		verifySendGameOver(playerOneConnection, playerTow, winnerLine);
		verifySendGameOver(playerTowConnection, playerTow, winnerLine);
		reset(playerTowConnection);
		reset(playerOneConnection);
		gameLikeSUT.restart();
	}

	private void verifySendEventMark(Connection connection, int cellID, int playerID) {
		ArgumentCaptor<CellMarkedValue> argument = ArgumentCaptor.forClass(CellMarkedValue.class);
		verify(connection, atLeastOnce()).sendEvent(eq(EventType.MARK), argument.capture());
		cellMarkedValue = argument.getValue();
		assertThat(cellMarkedValue.cellId, equalTo(cellID));
	}

	private void verifySendGameOver(Connection connection, Player winPlayer, int[] winLine) {
		ArgumentCaptor<WinnerValue> winnerArgument = ArgumentCaptor.forClass(WinnerValue.class);
		verify(connection, atLeastOnce()).sendEvent(eq(EventType.GAME_OVER), winnerArgument.capture());
		winnerValue = winnerArgument.getValue();
		assertThat(winnerValue.player, equalTo(winPlayer));
		assertThat(winnerValue.pos, equalTo(winLine));
	}

}