//Brandon Chesley 300262641

import java.io.*;
import java.util.*;
public class Hangman{
	public static void main(String[] args){
		int guesses = 7;
		int score = 0;
		Scanner input = new Scanner(System.in);
		ArrayList<String> availableWords = new ArrayList<String>();
		try{
			inputFile("WordFile.txt", availableWords);
		}catch(FileNotFoundException ex){
			System.out.println(ex);
		}
		while(guesses > 0){
			ArrayList<Character> incorrectGuesses = new ArrayList<Character>();
			ArrayList<Character> allGuesses = new ArrayList<Character>();
			String word = chooseWord(availableWords);
			char[] gameWord = new char[word.length()];
			constructPlayerGuess(gameWord);
			while(finishedWord(word,gameWord) == false && guesses > 0){
				handleOutput(gameWord,incorrectGuesses,guesses,score);
				char guess = ' ';
				guess = input.next().charAt(0);
				while(!Character.isLetter(guess)){
					if(!Character.isLetter(guess)){
						System.out.println("Please enter a letter");
						guess = input.next().charAt(0);
					}
				}
				while(goodGuess(guess,allGuesses) == false){
					System.out.println("Please enter a letter you haven't used before");
					guess = input.next().charAt(0);
				}
				allGuesses.add(guess);
				System.out.println();
				if(incorrectGuess(word,guess) == true){
					System.out.println("Sorry, there were no " + guess + "'s");
					incorrectGuesses.add(guess);
					guesses--;
				}else{
					score = correctGuess(guess,word,score,gameWord);
				}
			}
			System.out.println("Your word was " + word);
			if(finishedWord(word,gameWord) == true){
				score = finishedWordScore(score,guesses);
				guesses = 7;
			}
		}
		System.out.println("Your final score was " + score);
		handleHighScore(score);
	}
	public static void inputFile(String filename, ArrayList<String> availableWords) throws FileNotFoundException{
		File file = new File(filename);
		Scanner input = new Scanner(file);
		String word = "";
		while(input.hasNext()){
			word = input.nextLine();
			availableWords.add(word);
		}
		input.close();
	}
	public static String chooseWord(ArrayList<String> availableWords){
		String word = availableWords.get(((int)(Math.random() * 127142) + 1));
		return word;
	}
	public static void constructPlayerGuess(char[] gameWord){
		for(int i = 0; i < gameWord.length; i++){
			gameWord[i] = '-';
		}
	}
	public static boolean goodGuess(char guess, ArrayList<Character> allGuesses ){
		for(int i = 0; i < allGuesses.size();i++){
			if(guess == (allGuesses.get(i))){
				return false;
			}
		}
		return true;
	}
	public static void handleOutput(char[] gameWord, ArrayList<Character> incorrectGuesses, int guesses, int score ){
		System.out.print("Hidden Word: ");
		for(int i = 0; i < gameWord.length; i++){
			System.out.print(gameWord[i]);
		}
		System.out.println();
		System.out.print("Incorrect Guesses: ");
		Collections.sort(incorrectGuesses);
		for(int i = 0; i < incorrectGuesses.size();i++){
			System.out.print(incorrectGuesses.get(i) + ", ");
		}
		System.out.println();
		System.out.println("Guesses Left: " + guesses);
		System.out.println("Score: " + score);
		System.out.print("Enter next guess: ");
	}
	public static boolean finishedWord(String word, char[] gameWord){
		String testWord = "";
		for(int i = 0; i < word.length(); i++){
			testWord += gameWord[i];
		}
		if(testWord.equals(word)){
			return true;
		}else{
			return false;
		}
	}
	public static boolean incorrectGuess(String word, char guess){
		char check = ' ';
		for(int i = 0; i < word.length();i++){
			check = word.charAt(i);
			if(guess == check){
				return false;
			}
		}
		return true;
	}
	public static int correctGuess(char guess, String word, int score, char[] gameWord){
		char check = ' ';
		for(int i = 0; i < word.length();i++){
			check = word.charAt(i);
			if(guess == check){
				gameWord[i] = check;
				score += 10;
			}
		}
		return score;
	}
	public static int finishedWordScore(int score, int guesses){
		for(int i = 0; i < guesses;i++){
			score += 30;
		}
		score += 100;
		return score;
	}
	public static void handleHighScore(int score){
		ArrayList<Integer> scores = new ArrayList<Integer>();
		ArrayList<String> names = new ArrayList<String>();
		File file = new File("High Scores.txt");
		try{
			Scanner input = new Scanner(file);
			while(input.hasNext()){
				names.add(input.next());
				scores.add(input.nextInt());
			}
			input.close();
			Scanner userInput = new Scanner(System.in);
			for(int i = 0; i < 5;i++){
				if(score < scores.get(i)){
					continue;
				}else if(score > scores.get(i) || i == (scores.size() - 1)){
					System.out.println("You've made the highscores. Please enter your name.");
					String name = userInput.next();
					names.add(i, name);
					scores.add(i, score);
					scores.remove((scores.size() -1));
					names.remove((names.size() - 1));
					break;
				}
			}
			userInput.close();
		}catch(FileNotFoundException ex){
			System.out.println(ex);
		}
		try{
			PrintWriter output = new PrintWriter(file);
			for(int i = 0; i < names.size();i++){
				output.print(names.get(i) + " ");
				output.println(scores.get(i));
			}
			output.close();
			for(int i = 0; i < scores.size();i++){
				System.out.print(names.get(i) + " ");
				System.out.println(scores.get(i));
			}
		}catch(FileNotFoundException ex){
			System.out.println(ex);
		}

	}
}