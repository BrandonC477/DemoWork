import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//Not fully finished. But working well enough to play the game

public class MineSweeper extends Application{
	int[][] game;
	Button gameBoardButton[][];
	ImageView smiley;
	ImageView gameTile;
	int nonMineTilesUncovered;
	int rows = 8;
	int columns = 8;
	int mines = 10;
	boolean firstClick = true;
	public static void main(String[] args){
		launch(args);
	}
	public void start(Stage theStage){
		theStage.setScene(new Scene(createGameBoard(theStage)));
		theStage.setTitle("Mine Sweeper");
		theStage.show();
	}
	public Pane createGameBoard(Stage theStage){
		VBox menu = createMenu(theStage);
		game = generateMines();
		nonMineTilesUncovered = 0;
		BorderPane fullGamePane = new BorderPane();
		BorderPane pane = new BorderPane();
		GridPane gameBoard = new GridPane();
		NewGameButton smiley = new NewGameButton();	
		smiley.setOnAction(e -> {
			start(theStage);
		});
		pane.setPadding(new Insets(0,0,0,8));
		pane.setLeft(new Label(Integer.toString(mines)));
		pane.setCenter(smiley);
		pane.setRight(new Label("000"));
		createButtons(gameBoard,smiley);
		fullGamePane.setTop(menu);
		fullGamePane.setCenter(pane);
		fullGamePane.setBottom(gameBoard);
		return fullGamePane;
	}
	public void disableButtons(){
		for(int i = 0; i < gameBoardButton.length; i++){
			for(int j = 0; j < gameBoardButton[i].length;j++){
				gameBoardButton[i][j].setOnAction(null);
			}
		}
	}
	public void displayMines(){
		for(int i = 0; i < game.length; i++){
			for(int j = 0; j < game[i].length;j++){
				if(game[i][j] == -1){
					gameBoardButton[i][j].setGraphic(new ImageView(new Image("/res/mine-grey.png")));
				}
			}
		}
	}
	class NewGameButton extends Button{
		public NewGameButton(){
			double size = 50;
			setMinWidth(size);
			setMaxWidth(size);
			setMinHeight(size);
			setMaxHeight(size);
			smiley = new ImageView(new Image("/res/face-smile.png"));
			setGraphic(smiley);
		}
	}
	class gameBoardButton extends Button{
		public gameBoardButton(){
			double size = 30;
			setMinWidth(size);
			setMaxWidth(size);
			setMinHeight(size);
			setMaxHeight(size);
			gameTile= new ImageView(new Image("/res/cover.png"));
			setGraphic(gameTile);
		}
	}
	public int[][] generateMines(){
		int[][] game = new int[rows][columns];
		int mineCount = 0;
		while(mineCount < mines){
			int randomX = (int)(Math.random() * rows);
			int randomY = (int)(Math.random() * columns);
			if(game[randomX][randomY] == -1){
				continue;
			}else{
				game[randomX][randomY] = -1;
				mineCount++;
			}
		}
		//game[1][1] = -1;
		//game[2][3] = -1;
		//game[3][3] = -1;
		int count;
		for(int i = 0; i < game.length;i++){
			for(int j = 0; j < game[i].length;j++){
				count = 0;
				if(game[i][j] != -1){
					if((j + 1) < game[i].length && (game[i][j+1] == -1)){
						count++;
					}
					if((i + 1) < game.length && (game[i+1][j] == -1)){
						count++;
					}
					if((j-1) >= 0 && (game[i][j-1] == -1)){
						count++;
					}
					if((i-1) >= 0 && (game[i-1][j] == -1)){
						count++;
					}
					if((i+1) < game.length && (j+1) < game[i].length && (game[i+1][j+1] == -1)){
						count++;
					}
					if((i-1) >= 0 && (j-1) >= 0 && (game[i-1][j-1] == -1)){
						count++;
					}
					if((i-1) >= 0 && (j+1) < game[i].length && (game[i-1][j+1] == -1)){
						count++;
					}
					if((i+1) < game.length && (j-1) >= 0 && (game[i+1][j-1] == -1)){
						count++;
					}
					game[i][j] = count;
				}
			}
		}
		return game;
	}
	public boolean checkWin(){
		if(nonMineTilesUncovered == ((rows * columns) - mines)){
			return true;
		}else{
			return false;
		}
	}
	public void applyWin(NewGameButton smiley){
		smiley.setGraphic(new ImageView(new Image("/res/face-win.png")));
		disableButtons();
		displayMines();
	}
	public void createButtons(GridPane gameBoard, NewGameButton smiley){
		gameBoardButton = new gameBoardButton[rows][columns];
		for(int i = 0; i < gameBoardButton.length; i++){
			for(int j = 0; j < gameBoardButton[i].length; j++){
				gameBoardButton[i][j] = new gameBoardButton();
				Button currentButton = gameBoardButton[i][j];
				int test = game[i][j];
				currentButton.setOnAction(e -> {
					if((test < 9) && (test > -1)){
						currentButton.setGraphic(new ImageView(new Image("/res/" + test + ".png")));
						nonMineTilesUncovered++;
						if(checkWin())
							applyWin(smiley);
					}else{
						currentButton.setGraphic(new ImageView(new Image("/res/mine-red.png")));
						smiley.setGraphic(new ImageView(new Image("/res/face-dead.png")));
						disableButtons();
					}
					currentButton.setOnAction(null);
				});
				gameBoard.add(currentButton,j,i);
			}
		}
	}
	public VBox createMenu(Stage theStage){
		VBox gameMenu = new VBox();
		MenuBar menuBar = new MenuBar();
		Menu Difficulties = new Menu("Difficulties");
		MenuItem Beginner = new MenuItem("Beginner");
		MenuItem Intermediate = new MenuItem("Intermediate");
		MenuItem Expert = new MenuItem("Expert");
		Beginner.setOnAction(e ->{
			rows = 8;
			columns = 8;
			mines = 10;
			start(theStage);
		});
		Intermediate.setOnAction(e -> {
			rows = 16;
			columns = 16;
			mines = 40;
			start(theStage);
		});
		Expert.setOnAction(e -> {
			rows = 16;
			columns = 32;
			mines = 99;
			start(theStage);
		});
		gameMenu.getChildren().add(menuBar);
		menuBar.getMenus().add(Difficulties);
		Difficulties.getItems().addAll(Beginner,Intermediate,Expert);
		return gameMenu;
	}
}
