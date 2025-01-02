import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BogglePlayer {

  private String name;
  private int score;

  private ArrayList foundWords;


  public BogglePlayer(String name) {
    this.name = name;
    this.score = 0;
    foundWords = new ArrayList<>();
  }

  // Getter for player name
  public String getName() {
    return name;
  }


  // Method to increment score if all checks pass.
  public void addPoints(int points) {
    score += points;
  }

  // Getter for player score.
  public int getScore() {
    return score;
  }

  public void addWord(String word) {
    foundWords.add(word);
  }

  // Method to get player founds words.
  public List<String> getWords() {
    return this.foundWords;
  }

  // Method to clear player foundWords list.
  public void clearWords() {
    foundWords.clear();

  }

}


