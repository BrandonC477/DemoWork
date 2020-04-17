import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GomokuServer {

	public static void main(String[] args) throws Exception {
		ServerSocket listener = new ServerSocket(8901);
		System.out.println("Gomoku Server is Running");
		try {
			while (true) {
				Game game = new Game();
				Game.Player playerB = game.new Player(listener.accept(), 'B');
				Game.Player playerW = game.new Player(listener.accept(), 'W');
				playerB.setOpponent(playerW);
				playerW.setOpponent(playerB);
				game.currentPlayer = playerB;
				playerB.start();
				playerW.start();
			}
		} finally {
			listener.close();
		}
	}
}

class Game {

	private Player[][] board = new Player[15][15];
	Player currentPlayer;
	int moveCount = 1;

	// -1 for black win, 0 for no win, 1 for white win, 2 for white overline
	// win,
	// 3 for white 3 and 3 win,4 for white 4 and 4 win
	public int hasWinner(int x, int y) {
		char m = board[x][y].mark;
		// - 1 to not count current node twice
		int vert = (winHelper(x, y, 1, x, y, 0) + winHelper(x, y, 5, x, y, 0) - 1);
		int hor = (winHelper(x, y, 3, x, y, 0) + winHelper(x, y, 7, x, y, 0) - 1);
		int diag1 = (winHelper(x, y, 2, x, y, 0) + winHelper(x, y, 6, x, y, 0) - 1);
		int diag2 = (winHelper(x, y, 4, x, y, 0) + winHelper(x, y, 8, x, y, 0) - 1);

		if ((vert == 5 || hor == 5 || diag1 == 5 || diag2 == 5))
			return m == 'B' ? -1 : 1;
		if (m == 'B' && (vert > 5 || hor > 5 || diag1 > 5 || diag2 > 5))
			return 2;
		if (m == 'B' && ((vert == 3 && hor == 3) || (vert == 3 && diag1 == 3) || (vert == 3 && diag2 == 3)
				|| (hor == 3 && diag1 == 3) || (hor == 3 && diag2 == 3) || (diag1 == 3 && diag2 == 3)))
			return threeAndThree(x, y, vert, hor, diag1, diag2) ? 3 : 0;
		if (m == 'B' && ((vert == 4 && hor == 4) || (vert == 4 && diag1 == 4) || (vert == 4 && diag2 == 4)
				|| (hor == 4 && diag1 == 4) || (hor == 4 && diag2 == 4) || (diag1 == 4 && diag2 == 4)))
			return 4;
		if ((vert >= 5 || hor >= 5 || diag1 >= 5 || diag2 >= 5))
			return 1;
		return 0;
	}

	// dir, 1 = up, 2 = top right, 3 = right, 4 = down right
	// 5 = down, 6 = down left, 7 = left, 8 = top right;
	public int winHelper(int x, int y, int dir, int i, int j, int count) {

		if (!isValid(i, j))
			return count;
		if (board[i][j] != board[x][y])
			return count;

		if (dir == 1)
			return winHelper(x, y, dir, i - 1, j, count + 1);
		else if (dir == 2)
			return winHelper(x, y, dir, i - 1, j + 1, count + 1);
		else if (dir == 3)
			return winHelper(x, y, dir, i, j + 1, count + 1);
		else if (dir == 4)
			return winHelper(x, y, dir, i + 1, j + 1, count + 1);
		else if (dir == 5)
			return winHelper(x, y, dir, i + 1, j, count + 1);
		else if (dir == 6)
			return winHelper(x, y, dir, i + 1, j - 1, count + 1);
		else if (dir == 7)
			return winHelper(x, y, dir, i, j - 1, count + 1);
		else if (dir == 8)
			return winHelper(x, y, dir, i - 1, j - 1, count + 1);
		else
			return count;
	}

	public boolean threeAndThree(int x, int y, int vert, int hor, int dia, int diag) {
		int count = 0;
		if (vert == 3)
			count += tatHelper(x, y, 1, x, y) + tatHelper(x, y, 5, x, y);
		if (hor == 3)
			count += tatHelper(x, y, 3, x, y) + tatHelper(x, y, 7, x, y);
		if (dia == 3)
			count += tatHelper(x, y, 2, x, y) + tatHelper(x, y, 6, x, y);
		if (diag == 3)
			count += tatHelper(x, y, 4, x, y) + tatHelper(x, y, 8, x, y);

		return count > 0 ? false : true;
	}

	// dir, 1 = up, 2 = top right, 3 = right, 4 = down right
	// 5 = down, 6 = down left, 7 = left, 8 = top right;
	public int tatHelper(int x, int y, int dir, int i, int j) {

		if (!isValid(i, j))
			return 1;
		if (board[i][j] == null)
			return 0;
		if (board[i][j] != board[x][y])
			return 1;

		if (dir == 1)
			return tatHelper(x, y, dir, i - 1, j);
		else if (dir == 2)
			return tatHelper(x, y, dir, i - 1, j + 1);
		else if (dir == 3)
			return tatHelper(x, y, dir, i, j + 1);
		else if (dir == 4)
			return tatHelper(x, y, dir, i + 1, j + 1);
		else if (dir == 5)
			return tatHelper(x, y, dir, i + 1, j);
		else if (dir == 6)
			return tatHelper(x, y, dir, i + 1, j - 1);
		else if (dir == 7)
			return tatHelper(x, y, dir, i, j - 1);
		else if (dir == 8)
			return tatHelper(x, y, dir, i - 1, j - 1);
		else
			return 0;
	}

	public boolean isValid(int row, int col) {
		return row >= 0 && col >= 0 && row < board.length && col < board[row].length;
	}

	public boolean boardFilledUp() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == null) {
					return false;
				}
			}
		}
		return true;
	}

	public synchronized boolean legalMove(int x, int y, Player player) {
		if (player == currentPlayer && moveCount == 1 && (x != 7 || y != 7))
			return false;
		if (player == currentPlayer && moveCount == 3 && ((x <= 10 && x >= 4) && (y <= 10 && y >= 4)))
			return false;
		if (player == currentPlayer && board[x][y] == null) {
			board[x][y] = currentPlayer;
			currentPlayer = currentPlayer.opponent;
			currentPlayer.otherPlayerMoved(x, y);
			moveCount++;
			return true;
		}
		return false;
	}

	class Player extends Thread {
		char mark;
		Player opponent;
		Socket socket;
		BufferedReader input;
		PrintWriter output;

		public Player(Socket socket, char mark) {
			this.socket = socket;
			this.mark = mark;
			try {
				input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				output = new PrintWriter(socket.getOutputStream(), true);
				output.println("WELCOME " + mark);
				output.println("MESSAGE Waiting for opponent to connect");
			} catch (IOException e) {
				System.out.println("Player died: " + e);
			}
		}

		public void setOpponent(Player opponent) {
			this.opponent = opponent;
		}

		public void otherPlayerMoved(int x, int y) {
			output.println("OPPONENT_MOVED " + x + " " + y);
			int winner = hasWinner(x, y);

			if (winner == 0)
				output.println("");
			else if (winner == 1)
				output.println("END White wins.");
			else if (winner == 2)
				output.println("END White overline victory.");
			else if (winner == 3)
				output.println("END White three and three victory.");
			else if (winner == 4)
				output.println("END White four and four victory.");
			else if (winner == -1)
				output.println("END Black wins.");
			else if (boardFilledUp())
				output.println("END Tie.");
		}

		public void run() {
			try {
				// The thread is only started after everyone connects.
				output.println("MESSAGE All players connected");

				// Tell the first player that it is her turn.
				if (mark == 'X') {
					output.println("MESSAGE Your move");
				}

				// Repeatedly get commands from the client and process them.
				while (true) {
					String command = input.readLine();
					if (command.startsWith("MOVE")) {
						String[] commandArray = command.split(" ");
						int x = Integer.parseInt(commandArray[1]);
						int y = Integer.parseInt(commandArray[2]);
						if (legalMove(x, y, this)) {
							output.println("VALID_MOVE");
							int winner = hasWinner(x, y);
							if (winner == 0)
								output.println("");
							else if (winner == 1)
								output.println("END White wins.");
							else if (winner == 2)
								output.println("END White overline victory.");
							else if (winner == 3)
								output.println("END White three and three victory.");
							else if (winner == 4)
								output.println("END White four and four victory.");
							else if (winner == -1)
								output.println("END Black wins.");
							else if (boardFilledUp())
								output.println("END Tie.");
						} else {
							output.println("MESSAGE Invalid Move.");
						}
					} else if (command.startsWith("QUIT")) {
						return;
					}
				}
			} catch (IOException e) {
				System.out.println("Player died: " + e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}