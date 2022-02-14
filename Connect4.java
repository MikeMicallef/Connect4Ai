package Connect4;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sac.game.AlphaBetaPruning;
import sac.game.GameSearchAlgorithm;
import sac.game.GameState;
import sac.game.GameStateImpl;


public class Connect4 extends GameStateImpl{
	
	public final byte Rows; 
	public final byte Cols;  
	public byte[][] board = null;
	public static boolean check =false;
	public static char move = 'x';
	
	
	public Connect4(byte Rows, byte Cols) {
		board=new byte[Rows][Cols];
		this.Rows=Rows;
		this.Cols=Cols;
	}
	
	public Connect4(Connect4 parent) {
		this.Rows=parent.Rows;
		this.Cols=parent.Cols;
		board=new byte[Rows][Cols];
		for (int i=0;i<Rows;i++) {
			for (int j=0;j<Cols;j++) {
				board[i][j]=parent.board[i][j];
			}
		}
		this.setMaximizingTurnNow(parent.isMaximizingTurnNow());
	}
	
	public void PrintBoard() {
		System.out.println("  0   1   2   3   4   5   ");
		for(int row = 0; row < Rows; row++) {
			System.out.print("|");
			for(int col = 0; col < Cols; col++) {
				//empty
				if (board[row][col]==0) {
					System.out.print(" " + " " + " |");}
				//player
				if (board[row][col]==1) {
					System.out.print(" " + "X" + " |");}
				//computer
				if (board[row][col]==2) {
					System.out.print(" " + "O" + " |");}
			}
			System.out.println();
		}
	}
		

		
		

		public boolean checkWin(){
			
			for(int j = 0; j < Cols; j++) {
				if(board[0][j] != 0) {
					return true;
				}
			}
			//Vertical Check
			for(int col = 0; col < Cols; col++) {
				for(int row = 0; row <= Rows-4; row++) {
					if(	board[row][col] != 0 && board[row+1][col] == board[row][col] && board[row+2][col] == board[row][col] && board[row+3][col] == board[row][col]) {
						return true;
					}
				}
			}
			
			
			//Horizontal Check
			for(int row = 0; row < board.length; row++) {
				for(int col = 0; col < board[row].length-4 + 1; col++) {
					if(	board[row][col] != 0 && board[row][col+1] == board[row][col] && board[row][col+2] == board[row][col] && board[row][col+3] == board[row][col]) {
							return true;
						}
				}
			}
			
			
		
			//Diagonal right upwards Check
			for(int row =0; row <= Rows-4; row++) {
				for(int col = 0; col <= Cols-4; col++) {
					if( board[row][col] != 0 && board[row+1][col+1] == board[row][col] && board[row+2][col+2] == board[row][col] && board[row+3][col+3] == board[row][col]) {
						return true;
					}
				}
			}
			
			//Diagonally left upwards
			for(int row = 0; row <= Rows-4; row++) {
				for(int col = Cols-1; col >=3; col--) {
					if(board[row][col] != 0 && board[row+1][col-1] == board[row][col] && board[row+2][col-2] == board[row][col] && board[row+3][col-3] == board[row][col]) {
						return true;
					}
				}
			}
			return false;

			
			
		}
		
		public boolean makeMove(int col) {
			if(board[0][col] != 0) {
				return false;
			}
			for(int row = Rows-1; row >= 0; row--) {
				if(board[row][col] == 0) {
					if (isMaximizingTurnNow()==true)
						board[row][col] = 1; 
					else
						board[row][col] = 2; 
					setMaximizingTurnNow(!isMaximizingTurnNow());  //making computer play
					return true;
				}
			}
			return true;
		}
		
		public String toString() {
			StringBuilder result = new StringBuilder();
			for (int i = 0; i < Rows; i++) {
				for (int j = 0; j < Cols; j++) {
					result.append(board[i][j] + ((j < Rows - 1) ? "," : ""));
				}
				result.append("\n");
			}
			return result.toString();
		}
		
		public static void main(String[] args) throws IOException {
			Connect4 c4=new Connect4((byte)8,(byte)8);
			
			Scanner s =new Scanner(System.in);
		    System.out.println("Enter 1 - Player Vs Player \nEnter 2 - Player Vs Computer");
		    int option = s.nextInt();
		    c4.PrintBoard();
		    
		    if (option == 1) {
		    	int player =1;
		              while(true) {
						BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in),1);
						String line = inReader.readLine();
						int read=Integer.valueOf(line);
						int move = read;
						c4.makeMove(move);
						c4.PrintBoard();
						player=3-player;
						
						
						
						if (c4.checkWin()) {
							if(player==1)
		                        System.out.println("Player 2 has won");
		                    else
		                        System.out.println("Player 1 has won");
		                    break;
							
						}

		            } 
			}
		    
		    if (option ==2) {
		    
		    
		    
			
			
				while(true) {
					BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in),1);
					//players turn
					String line = inReader.readLine();
					int s1=Integer.valueOf(line);
					int move = s1;
					if (!c4.makeMove(move)) {
						System.out.println("Column is full.");
						c4.setMaximizingTurnNow(c4.isMaximizingTurnNow());
					}
				
				

				c4.PrintBoard();
				
				if (c4.checkWin()) {
					System.out.println("Game has ended, player has won!");
					break;
				}
				
				c4.PrintBoard();
				
				Connect4.setHFunction(new Heuristic());
				GameSearchAlgorithm a=new AlphaBetaPruning(c4);
				a.execute();
				System.out.println(a.getMovesScores().toString());
				
				int bestMove=Integer.parseInt(a.getFirstBestMove());
				
				
				c4.makeMove(bestMove);
				
				if (c4.checkWin()) {
					
					System.out.println("Game has ended,Computer has won!");
					break;
				}
				
				c4.PrintBoard();
			}
			c4.PrintBoard();
			}
		    
		}
		
		
		
		@Override
		public int hashCode() {
			byte[] toHash = new byte[Rows*Cols];
			int k=0;
			for (int i = 0; i < Rows; i++) {
				for (int j=0;j<Rows;j++) {
					toHash[k++]=board[i][j];
				}
			}
			return Arrays.hashCode(toHash);
		}

		@Override
		public List<GameState> generateChildren() {
			
			List<GameState> children = new LinkedList<GameState>();
			
			for (int j=0;j<Cols;j++) {
				Connect4 child=new Connect4(this);
				child.makeMove(j);
				child.setMoveName(Integer.toString(j));
				children.add(child);
			}
			
			return children;
		}

		
}