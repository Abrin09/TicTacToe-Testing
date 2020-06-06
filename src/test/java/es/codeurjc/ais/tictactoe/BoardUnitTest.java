package es.codeurjc.ais.tictactoe;

import static org.hamcrest.CoreMatchers.equalTo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import es.codeurjc.ais.tictactoe.TicTacToeGame.Cell;

public class BoardUnitTest {

	private Board sutBoard;

	@Before
	public void setUp() {
		sutBoard = new Board();
		sutBoard.enableAll();
	}

	@Test
	public void anyPlayerWin() {
		assertNull(sutBoard.getCellsIfWinner("O"));
		assertNull(sutBoard.getCellsIfWinner("X"));
		assertFalse(sutBoard.checkDraw());
	}

	@Test
	public void playerLabelOWins() {
		int[] winningLine = this.setBoardCellsToWinO().getWinningLine();
		assertThat(sutBoard.getCellsIfWinner("O"), equalTo(winningLine));
		assertNull(sutBoard.getCellsIfWinner("X"));
		assertFalse(sutBoard.checkDraw());
	}

	@Test
	public void playerLabelXWins() {
		int[] winningLine = this.setBoardCellsToWinX().getWinningLine();
		assertNull(sutBoard.getCellsIfWinner("O"));
		assertThat(sutBoard.getCellsIfWinner("X"), equalTo(winningLine));
		assertFalse(sutBoard.checkDraw());
	}

	@Test
	public void gameResultIsDraw() {
		setBoardCellsToDraw();
		assertNull(sutBoard.getCellsIfWinner("O"));
		assertNull(sutBoard.getCellsIfWinner("X"));
		assertTrue(sutBoard.checkDraw());
	}

	private int[] getWinningLine() {
		return new int[] { 6, 4, 2 };
	}

	private void setBoardCells(String match[]) {
		for (int cellID = 0; cellID < match.length; cellID++) {
			Cell cell = sutBoard.getCell(cellID);
			cell.value = match[cellID];
			cell.active = true;
		}
	}

	private BoardUnitTest setBoardCellsToWinO() {
		String match[] = new String[] { "O", "X", "O", "X", "O", "X", "O" };
		setBoardCells(match);
		return this;
	}

	private BoardUnitTest setBoardCellsToWinX() {
		String match[] = new String[] { "X", "O", "X", "O", "X", "O", "X" };
		setBoardCells(match);
		return this;
	}

	private void setBoardCellsToDraw() {
		String match[] = new String[] { "O", "X", "O", "X", "O", "X", "X", "O", "X" };
		setBoardCells(match);
	}

}
