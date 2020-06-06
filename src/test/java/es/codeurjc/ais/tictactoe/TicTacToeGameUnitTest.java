package es.codeurjc.ais.tictactoe;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.ArgumentCaptor;

import es.codeurjc.ais.tictactoe.TicTacToeGame.CellMarkedValue;
import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;

public class TicTacToeGameUnitTest {

	private TicTacToeGame sutGame;
	private Player playerX;
	private Player playerO;
	private String[] player1Info;
	private String[] player2Info;
	private Connection mockConnectionPlayerO;
	private Connection mockConnectionPlayerX;
	private CellMarkedValue markedCell;
	private WinnerValue winner;
	private int[] winLine;

	private List<Connection> connections = new CopyOnWriteArrayList<>();

	@Before
	public void setUp() {
		winLine = new int[] { 6, 4, 2 };
		sutGame = new TicTacToeGame();
		player1Info = new String[] { "O", "First Player" };
		player2Info = new String[] { "X", "Second Player" };

		mockConnectionPlayerO = mock(Connection.class);
		mockConnectionPlayerX = mock(Connection.class);
		connections.add(mockConnectionPlayerO);
		connections.add(mockConnectionPlayerX);
		sutGame.addConnection(mockConnectionPlayerO);
		sutGame.addConnection(mockConnectionPlayerX);

		playerO = new Player(0, player1Info[0], player1Info[1]);
		playerX = new Player(1, player2Info[0], player2Info[1]);
		sutGame.addPlayer(playerO);
		sutGame.addPlayer(playerX);
	}

	@Test
	public void verify_JOIN_GAME_events_test() {
		verify(mockConnectionPlayerO, times(2)).sendEvent(eq(EventType.JOIN_GAME), argThat(hasItems(playerO, playerX)));
		verify(mockConnectionPlayerX, times(2)).sendEvent(eq(EventType.JOIN_GAME), argThat(hasItems(playerO, playerX)));

		for (Connection playerConnection : connections) {
			verifySendEventSetTurn(playerConnection);
		}

		reset(mockConnectionPlayerO);
		reset(mockConnectionPlayerX);
	}

	@Test
	public void verify_firstPut_wins_test() {
		for (int cellID = 0; cellID < 7; cellID++) {
			int playerID = (cellID % 2 == 0) ? 0 : 1;
			assertTrue(sutGame.checkTurn(playerID));
			sutGame.mark(cellID);
			for (Connection playerConnection : connections) {
				verifySendEventMark(playerConnection, cellID, playerID);
			}
		}

		for (Connection playerConnection : connections) {
			verifySendGameOverWithWinner(playerConnection, playerO, winLine);
		}

		reset(mockConnectionPlayerX);
		reset(mockConnectionPlayerO);
		sutGame.restart();
	}

	@Test
	public void verify_firstPut_loses_test() {
		int[] match = new int[] { 1, 6, 3, 4, 7, 2 };

		for (int pos = 0; pos < match.length; pos++) {
			int playerID = (pos % 2 == 0) ? 0 : 1;
			assertTrue(sutGame.checkTurn(playerID));
			sutGame.mark(match[pos]);
			for (Connection playerConnection : connections) {
				verifySendEventMark(playerConnection, match[pos], playerID);
			}
		}

		for (Connection playerConnection : connections) {
			verifySendGameOverWithWinner(playerConnection, playerX, winLine);
		}

		reset(mockConnectionPlayerX);
		reset(mockConnectionPlayerO);
		sutGame.restart();
	}

	@Test
	public void verify_anyPlayerWin_gameResultDraw_test() {
		int match[] = new int[] { 1, 0, 3, 2, 5, 4, 6, 7, 8 };

		for (int pos = 0; pos <= match.length - 1; pos++) {
			int playerID = (pos % 2 == 0) ? 0 : 1;
			assertTrue(sutGame.checkTurn(playerID));
			sutGame.mark(match[pos]);
			for (Connection playerConnection : connections) {
				verifySendEventMark(playerConnection, match[pos], playerID);
			}
		}

		for (Connection playerConnection : connections) {
			verifySendGameOverDraw(playerConnection);
		}

		reset(mockConnectionPlayerX);
		reset(mockConnectionPlayerO);
		sutGame.restart();

	}

	private void verifySendEventSetTurn(Connection connection) {
		verify(connection, times(1)).sendEvent(eq(EventType.SET_TURN), any());

	}

	private void verifySendEventMark(Connection connection, int cellID, int playerID) {
		ArgumentCaptor<CellMarkedValue> argument = ArgumentCaptor.forClass(CellMarkedValue.class);
		verify(connection, atLeastOnce()).sendEvent(eq(EventType.MARK), argument.capture());
		markedCell = argument.getValue();
		assertThat(markedCell.player, (playerID == playerX.getId()) ? equalTo(playerX) : equalTo(playerO));
		assertThat(markedCell.cellId, equalTo(cellID));

	}

	private void verifySendGameOverWithWinner(Connection connection, Player winPlayer, int[] winLine) {
		ArgumentCaptor<WinnerValue> winnerArgument = ArgumentCaptor.forClass(WinnerValue.class);
		verify(connection, atLeastOnce()).sendEvent(eq(EventType.GAME_OVER), winnerArgument.capture());
		winner = winnerArgument.getValue();
		assertThat(winner.player, equalTo(winPlayer));
		assertThat(winner.pos, equalTo(winLine));
	}

	private void verifySendGameOverDraw(Connection connection) {
		ArgumentCaptor<WinnerValue> winnerArgument = ArgumentCaptor.forClass(WinnerValue.class);
		verify(connection, atLeastOnce()).sendEvent(eq(EventType.GAME_OVER), winnerArgument.capture());
		winner = winnerArgument.getValue();
		assertThat(winner, equalTo(null));

	}

}
