import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BoggleCube {

  private final char[] letters; // List to store the selected letters on each cube.

  private BoggleCube(char[] letters) {
    this.letters = letters; // Array of size 6 for each cube position
  }

  // Getter for a random letter on a cube.
  public char getRandomLetter(){
    int randomIndex = (int) (Math.random() * 6);
    return this.letters[randomIndex];
  }

  // Method to make cubes.
  public static List<BoggleCube> makeCubes(char[][] allLetters) {
    ArrayList <BoggleCube> cubeList = new ArrayList<>(16);
    for (int i = 0; i < 16; i++) {
      cubeList.add(new BoggleCube(allLetters[i]));
    }
    return cubeList;
  }

}