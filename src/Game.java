import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Game {

  private static List<String> wordUsedThisRound = new ArrayList<String>();

  static ArrayList<String> dictionary = new ArrayList<>();

  static List<BoggleCube> boggleCubes;

  public static void main(String[] args) {
    Game game = new Game();
    game.readWordsFromFile();
    Scanner scanner = new Scanner(System.in);
    char[][] allLetters = new char[16][6]; // 16 cubes with 6 letters per cube.


    // Prompt user to input letters for each cube
    System.out.println("Input 16 lines consisting of 6 letters for each cube: ");
    for (int i = 0; i < 16; i++) {
      allLetters[i] = scanner.nextLine().toCharArray();
    }
    boggleCubes = BoggleCube.makeCubes(allLetters); // Create Boggle cubes using input letters

    System.out.println("How many players ?");
    int numPlayers = scanner.nextInt();
    scanner.nextLine();

    BogglePlayer[] bogglePlayers = new BogglePlayer[numPlayers]; // Array to hold Player pbjects.

    String[] playerNames = new String[numPlayers]; //String array to store player names.
    for (int i = 0; i < numPlayers; i++) {
      System.out.println("Enter player " + (i + 1) + "'s name (no spaces): ");
      playerNames[i] = scanner.nextLine();
      bogglePlayers[i] = new BogglePlayer(
          playerNames[i]); // Assign each player to their corresponding name.
    }

    // Flag to check status of game.
    boolean gameOver = false;

    //Main game loop.
    while (!gameOver) {
      playRound(bogglePlayers); //Play a new round.
      calculatePlayerScore(bogglePlayers); //Calculate scores for each player
      displayCurrentScores(bogglePlayers); //Display current scores.
      removeFoundWords(bogglePlayers);    // Clear players foundWords list.
      gameOver = checkGameOver(bogglePlayers); //Check if game over condition has been met.


    }
    displayFinalScores(bogglePlayers); //Game is over, display final scores/winner.

  }

  /* Method to calculate score for each player based on unique words found.
     Loop through each word found by the current player.
     Check if the word is found in the list of words of other players
     If word is found in other player's list, mark it as not unique */
  private static void calculatePlayerScore(BogglePlayer[] bogglePlayers) {
    for (int i = bogglePlayers.length - 1; i >= 0; i--) {
      for (String word : bogglePlayers[i].getWords()) {
        boolean isUnique = true;
        for (int j = 0; j < bogglePlayers.length; j++) {
          // Ensure we're not comparing the same player with themselves
          if (i != j && bogglePlayers[j].getWords().contains(word)) {
            isUnique = false;
            break;
          }
        }
        // If the word is unique, calculate the words points and add to player's score.
        if (isUnique) {
          int wordPoints = calculatePoints(word);
          bogglePlayers[i].addPoints(wordPoints);
        }
      }
    }

  }

  /* Method to create the board ensuring each cube is used.
     Generate random index
    Repeat if cube at index is already used
     Mark cube at index as used
     Print letter from random cube */
  public static void createBoard() {
    boolean[] usedCubes = new boolean[16]; // Array to track used cubes
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        int randomIndex;
        do {
          randomIndex = (int) (Math.random() * 16);
        } while (usedCubes[randomIndex]);
        usedCubes[randomIndex] = true;
        System.out.print(boggleCubes.get(randomIndex).getRandomLetter() + " ");
      }
      System.out.println();
    }
  }

  /* Method to check if any player as reached
   a score of 100 or greater, if so they win.  */
  private static boolean checkGameOver(BogglePlayer[] bogglePlayers) {
    for (BogglePlayer player : bogglePlayers) {
      if (player.getScore() >= 100) {
        System.out.println(player.getName() + " has reached 100 points ! Game over.");
        return true;
      }
    }
    return false;
  }

  /* Method to display the final scores of
   each player one the game is over. */
  private static void displayFinalScores(BogglePlayer[] bogglePlayers) {
    for (BogglePlayer player : bogglePlayers) {
      System.out.println(player.getName() + "'s score: " + player.getScore());
    }
  }

  /* Method to play players current scores
  *  at the end of each round */
  private static void displayCurrentScores(BogglePlayer[] bogglePlayers) {
    for (BogglePlayer bogglePlayer : bogglePlayers) {
      System.out.println(bogglePlayer.getName() + ": " + bogglePlayer.getScore());
    }
  }

  /* Method to shuffle the cubes and start.
     a new round. */
  private static void playRound(BogglePlayer[] bogglePlayers) {
    Scanner scanner = new Scanner(System.in);
    rollCubes();
    createBoard();

    /* Clear the list at the beginning of each
       round to allow next player to enter the same word. */
    for (BogglePlayer bogglePlayer : bogglePlayers) {
      System.out.println("Enter " + bogglePlayer.getName()
          + "'s words in all CAPS. (type 'done' in all lowercase to move on to the next player):");
      String word;
      do {
        word = scanner.nextLine(); //
        if (!word.equals("done")) {
          if (!isValidWord(word)) {
            System.out.println("That is not a real word ! Try again.");
          } else if (!isThreeLetters(word)) {
            System.out.println("Word must contain atleast three letters !");
          } else if (wordUsedThisRound.contains(word)) {
            System.out.println("You already used that word this round ! Try again.");
          } else {
            bogglePlayer.addWord(word);
            wordUsedThisRound.add(word);
          }
        }
      } while (!word.equals("done"));
      wordUsedThisRound.clear();
    }
  }

  /* Method to clear foundWords list after
    calculating player score. */
  public static void removeFoundWords(BogglePlayer[] bogglePlayer) {
    for (BogglePlayer player : bogglePlayer) {
      player.clearWords();
    }
  }

  // Method to calculate words points.
  public static int calculatePoints(String word) {
    int length = word.length();
    return calculateFibonacci(length - 2);

  }

  // Method to calculate fibonacci of a given number n.
  private static int calculateFibonacci(int n) {
    if (n <= 1) {
      return n;
    } else {
      return calculateFibonacci(n - 1) + calculateFibonacci(n - 2);
    }
  }

  //Method to roll the all cubes.
  public static void rollCubes() {
    Collections.shuffle(boggleCubes);
  }


  // Method to ensure input is a real word.
  public static boolean isValidWord(String word) {
    return dictionary.contains(word);

  }
// Method to ensure input is at least 3 letters in length.
  public static boolean isThreeLetters(String word) {
    return word.length() >= 3;
  }


  // Method to read words from dictionary file.
  public void readWordsFromFile() {
    try {
      Scanner inFile = new Scanner(
          new File("C:\\Users\\kyril\\IdeaProjects\\project4.cpp\\src\\words.txt"));
      while (inFile.hasNextLine()) {
        String word = inFile.nextLine();
        dictionary.add(word);

      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    }
  }

}




