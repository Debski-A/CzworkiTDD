package com.debski;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Czworki {
	public enum Colors {
		CZERWONY("C"),
		ZIELONY("Z"),
		PUSTE(" ");
		
		private String shortName;
		private String name;
		
		Colors(String shortName) {
			this.shortName = shortName;
			switch(this.shortName) {
			case "C": {
				name = "Czerwony";
				break;
			}
			case "Z": {
				name = "Zielony";
				break;
			}
			case " ": {
				name = "Puste";
				break;
			}
			}
		}
	}
	
	private static final int ROWS = 6;
	private static final int COLUMNS = 7;
	
	private Colors board[][];
	private int numberOfDiscs;
	
	private Colors selectedPlayer;
	public Colors getSelectedPlayer() {
		out.println(status);
		return this.selectedPlayer;
	}
	
	private String status = "Kolejny gracz: Czerwony\n\n";
	private PrintStream out;
	
	public Czworki(PrintStream out) {
		board = new Colors[ROWS][COLUMNS];
		selectedPlayer = Colors.PUSTE;
		numberOfDiscs = 0;
		this.out = out;
		
		for(Colors[] row : board) {
			Arrays.fill(row, Colors.PUSTE);
		}
	}
	
	public int getNumberOfDiscs() {
		return this.numberOfDiscs;
	}
	
	public boolean isFinished() {
		return this.numberOfDiscs == ROWS * COLUMNS;
	}
	
//	//TODO DO ZBADABNIA
//	public int getNumberOfDiscs2() {
//		return IntStream.range(0, COLUMNS)
//				.map(this::getNumberOfDiscsInColumn).sum();
//	}
//	
//	public int getNumberOfDiscsInColumn(int column) {
//		return (int) IntStream.range(0,  ROWS)
//				.filter(row -> !Colors.PUSTE
//				.equals(board[row][column]))
//				.count();
//	}

	
	public int putDisc(int column) {
		checkInput(column);
		choosePlayer();
		int row;
		for(row = 0; row < ROWS; row++) {
			if(board[row][column].equals(Colors.PUSTE)) {
				board[row][column] = selectedPlayer;
				numberOfDiscs++;
				break;
			}
		}
		if(row == ROWS) throw new RuntimeException("Brak miejsca w kolumnie nr " + column);
		checkForWin(row, column);
		printBoard();
		return row;
	}
	
	private void checkInput(int column) {
		if(column < 0 || column > 6) throw new RuntimeException("Nieprawidlowa kolumna");
	}
	
	private void choosePlayer() {
		if(selectedPlayer.equals(Colors.CZERWONY)) {
			selectedPlayer = Colors.ZIELONY;
			status = "Kolejny gracz: Czerwony\n\n";
		}
		else {
			selectedPlayer = Colors.CZERWONY;
			status = "Kolejny gracz: Zielony\n\n";
		}
	}
	
	private void printBoard() {
		
		for(int x = ROWS-1; 0 <= x; x--) {
			StringJoiner boardGraphicStatus = new StringJoiner("|", "|", "|");
			for(int y = 0; y < COLUMNS; y++) {
				boardGraphicStatus.add(board[x][y].shortName);
			}
			out.println(boardGraphicStatus.toString());
		}
		out.print(status);
	}
	
	private void checkForWin(int row, int column) {
		Pattern winPattern = Pattern.compile(".*" + selectedPlayer.shortName + "{4}.*");
		
		 String vertical = IntStream.range(0, ROWS)
                 .mapToObj(r -> board[r][column].shortName).reduce(String::concat).get();
		 if(winPattern.matcher(vertical).matches()) {
			 status = "Wygrywa: " + selectedPlayer.name;
			 return;
		 } 
		 
		 String horizontal = IntStream.range(0, COLUMNS)
				 .mapToObj(c -> board[row][c].shortName).reduce(String::concat).get().trim();
		 if(winPattern.matcher(horizontal).matches()) {
			 status = "Wygrywa: " + selectedPlayer.name;
			 return;
		 }
		 
		 StringJoiner diagonalLeftBotToRightTop = new StringJoiner("");
		 int startOffset = Math.min(row, column);
		 int myColumn = column - startOffset;
		 int myRow = row - startOffset;
		 do {
			 diagonalLeftBotToRightTop.add(board[myRow++][myColumn++].shortName);
		 } while(myRow < ROWS && myColumn < COLUMNS);
		 if(winPattern.matcher(diagonalLeftBotToRightTop.toString()).matches()) {
			 status = "Wygrywa: " + selectedPlayer.name;
			 return;
		 }
		 
		 StringJoiner diagonalRightBotToLeftTop = new StringJoiner("");
		 startOffset = Math.min(row, (COLUMNS - 1) - column);
		 myColumn = column + startOffset;
		 myRow = row - startOffset;
		 do {
			 diagonalRightBotToLeftTop.add(board[myRow++][myColumn--].shortName);
		 } while(myRow < ROWS && myColumn >= 0);
		 if(winPattern.matcher(diagonalRightBotToLeftTop.toString()).matches()) {
			 status = "Wygrywa: " + selectedPlayer.name;
			 return;
		 }
		 
		 
	}
	
	
	public static void main(String[] args) {
		Czworki czworki = new Czworki(System.out);
		int[] inputs = {2, 2, 3, 2, 2, 3, 3, 4, 4, 6, 5};
		for(int column : inputs) {
			czworki.putDisc(column);
		}
	}

}
