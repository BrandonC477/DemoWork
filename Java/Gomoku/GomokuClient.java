import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GomokuClient {

	private JFrame frame = new JFrame("Gomoku");
	private JLabel messageLabel = new JLabel("");
	private ImageIcon icon;
	private ImageIcon opponentIcon;

	private Square[][] board = new Square[15][15];
	private Square currentSquare;

	private static int PORT = 8901;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	public GomokuClient(String serverAddress) throws Exception {

		// Setup networking
		socket = new Socket(serverAddress, PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		// Layout GUI
		messageLabel.setBackground(Color.lightGray);
		frame.getContentPane().add(messageLabel, "South");

		JPanel boardPanel = new JPanel();
		boardPanel.setBackground(Color.black);
		boardPanel.setLayout(new GridLayout(15, 15, 1, 1));
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				final int x = i;
				final int y = j;
				board[i][j] = new Square(i, j);
				board[i][j].addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						currentSquare = board[x][y];
						out.println("MOVE " + x + " " + y);
					}
				});
				boardPanel.add(board[i][j]);
			}
		}
		frame.getContentPane().add(boardPanel, "Center");
	}
	public void play() throws Exception {
		String response;
		try {
			response = in.readLine();
			if (response.startsWith("WELCOME")) {
				char mark = response.charAt(8);
				icon = new ImageIcon(mark == 'B' ? "gomoku_black.png" : "gomoku_white.jpg");
				opponentIcon = new ImageIcon(mark == 'B' ? "gomoku_white.jpg" : "gomoku_black.png");
				if(mark == 'B')
					frame.setTitle("Gomoku - Black Player");
				else
					frame.setTitle("Gomoku - White Player");
			}
			String rules = "How to play: \n";
			rules += "To win the game either black or white must make a row of five stones.\n";
			rules += "Black must make there first move in the direct center of the board.\n";
			rules += "On blacks second turn they are not allowed to play within the marked 7x7 grid around the center tile.\n";
			rules += "If black makes a line longer than 5 stones this is considered an Overline and white will win.\n";
			rules += "If black places a stone in a spot that will make two lines of three or two lines of four, they automatically lose.\n";
			rules += "Black is allowed to place a stone in the situation described in the previous rule if white is blocking any end of the ";
			rules += "lines of three or lines of four";
			JOptionPane.showMessageDialog(frame,rules);
			while (true) {
				response = in.readLine();
				if (response.startsWith("VALID_MOVE")) {
					messageLabel.setText("Valid move, please wait");
					currentSquare.setIcon(icon);
					currentSquare.repaint();
				} else if (response.startsWith("OPPONENT_MOVED")) {
					String[] responseArray = response.split(" ");
					int x = Integer.parseInt(responseArray[1]);
					int y = Integer.parseInt(responseArray[2]);
					board[x][y].setIcon(opponentIcon);
					board[x][y].repaint();
					messageLabel.setText("Opponent moved, your turn");
				} else if (response.startsWith("END")) {
					messageLabel.setText(response.substring(4));
					break;
				} else if (response.startsWith("MESSAGE")) {
					messageLabel.setText(response.substring(8));
				}
			}
			out.println("QUIT");
		} finally {
			socket.close();
		}
	}

	private boolean wantsToPlayAgain() {
		int response = JOptionPane.showConfirmDialog(frame, "Want to play again?", "Again?",
				JOptionPane.YES_NO_OPTION);
		frame.dispose();
		return response == JOptionPane.YES_OPTION;
	}

	static class Square extends JLabel {
		public Square(int i, int j) throws IOException {
			//setBackground(Color.black);
			setOpaque(true);
			BufferedImage image;
			if((i == 7 && j == 7) || (i == 4 && j == 4) ||
			   (i == 10 && j == 10) || (i == 10 && j == 4) || (i == 4 && j == 10))
				image = ImageIO.read(new File("background_mark.jpg"));
			else
				image = ImageIO.read(new File("background.jpg"));
			Image newImage = image.getScaledInstance(36, 36, Image.SCALE_SMOOTH);
			setIcon(new ImageIcon(newImage));
			repaint();
		}
	}

	public static void main(String[] args) throws Exception {
		while (true) {
			String serverAddress = (args.length == 0) ? "localhost" : args[0];
			GomokuClient client = new GomokuClient(serverAddress);
			client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			client.frame.setBounds(0, 0, 550, 550);
			client.frame.setVisible(true);
			client.frame.setResizable(false);
			client.play();
			if (!client.wantsToPlayAgain()) {
				break;
			}
		}
	}
}