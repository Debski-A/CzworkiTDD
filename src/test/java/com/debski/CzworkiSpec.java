package com.debski;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.debski.Czworki.Colors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class CzworkiSpec {
	private Czworki tested;
	private OutputStream out;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void before() {
		out = new ByteArrayOutputStream();
		this.tested = new Czworki(new PrintStream(out));
	}
	
	@Test
	public void whenInstantiatedThenBoardIsEmpty() {
		assertThat(tested.getNumberOfDiscs(), is(0));
	}
	
	@Test
	public void whenPutDiscToEmptyColumnThenRowEquals0() {
		assertThat(tested.putDisc(1), is(0));
	}
	
	@Test
	public void whenPutDiscThenRowEquals1() {
		tested.putDisc(1);
		assertThat(tested.putDisc(1), is(1));
	}
	
	@Test
	public void whenPutDiscThenNumberOfDiscsIncreasesBy1() {
		tested.putDisc(1);
		tested.putDisc(1);
		tested.putDisc(2);
		assertThat(tested.getNumberOfDiscs(), is(3));
	}
	
	@Test
	public void whenPutDiscOutOfBoundsThenRuntimeException() {
		exception.expect(RuntimeException.class);
		exception
			.expectMessage("Nieprawidlowa kolumna");
		tested.putDisc(8);
	}
	
	@Test
	public void whenPutDiscToFullColumnThenRuntimeException() {
		tested.putDisc(1);//1
		tested.putDisc(1);//2
		tested.putDisc(1);//3
		tested.putDisc(1);//4
		tested.putDisc(1);//5
		tested.putDisc(1);//6
		exception.expect(RuntimeException.class);
		exception
			.expectMessage("Brak miejsca w kolumnie nr 1");
		tested.putDisc(1);
	}
	
	@Test
	public void whenFirstPlayerPlaysThenDiscColorIsRed() {
		tested.putDisc(1);
		assertThat(tested.getSelectedPlayer(), is(Colors.CZERWONY));
	}
	
	@Test
	public void whenSecondPlayerPlaysThenDiscColorIsGreen() {
		tested.putDisc(1);
		tested.putDisc(4);
		assertThat(tested.getSelectedPlayer(), is(Colors.ZIELONY));
	}
	
	@Test
	public void whenAskedForCurrentPlayerThenRed() {
		tested.getSelectedPlayer();
		assertThat(out.toString(), containsString("Kolejny gracz: Czerwony"));
	}
	
	@Test
	public void whenDiscIsIntroducedThenBoardIsPrinted() {
		tested.putDisc(0);
		assertThat(out.toString(), containsString("|C| | | | | | |"));
	}
	
	@Test
	public void whenFullBoardThenFinished() {
		for(int column = 0; column <= 6; column++) {
			for(int x = 0; x < 6; x++) {
				tested.putDisc(column);
			}
		}
		assertTrue("Gra musi być zakończona", tested.isFinished());
		
	}
	
	@Test
	public void whenGameStartsThenNotFinished() {
		assertFalse("Gra nie może być zakończona", tested.isFinished());
	}
	
	@Test
	public void whenMoreThan3AdjacentDiscsInColumnThenWin() {
		tested.putDisc(2); //C
		tested.putDisc(1); //Z
		tested.putDisc(2); //C
		tested.putDisc(1); //Z
		tested.putDisc(2); //C
		tested.putDisc(3); //Z
		tested.putDisc(2); //C
		assertThat(out.toString(), containsString("Wygrywa: Czerwony"));
	}
	
	@Test
	public void whenMoreThan3AdjacentDiscsInRowThenWin() {
		tested.putDisc(0); //C
		tested.putDisc(1); //Z
		tested.putDisc(1); //C
		tested.putDisc(2); //Z
		tested.putDisc(1); //C
		tested.putDisc(3); //Z
		tested.putDisc(3); //C
		tested.putDisc(4); //Z
		assertThat(out.toString(), containsString("Wygrywa: Zielony"));
	}
	
	@Test
	public void whenMoreThan3AdjacentDiscsInDiagonalFromLeftBotToRightTopThenWin() {
		int[] inputs = {4, 2, 3, 3, 4, 4, 5, 5, 5, 5};
		for(int column : inputs) {
			tested.putDisc(column);
		}
		assertThat(out.toString(), containsString("Wygrywa: Zielony"));
	}
	@Test
	public void whenMoreThan3AdjacentDiscsInDiagonalFromLeftTopToRightBotThenWin() {
		int[] inputs = {2, 2, 3, 2, 2, 3, 3, 4, 4, 6, 5};
		for(int column : inputs) {
			tested.putDisc(column);
		}
		assertThat(out.toString(), containsString("Wygrywa: Czerwony"));
	}

}
