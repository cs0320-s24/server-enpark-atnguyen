package edu.brown.cs.student.main.CreatorFromRowClasses;

import java.util.ArrayList;
import java.util.List;

/** A class that implements CreatorFromRow and creates an ArrayList out of a list of Strings. */
public class ArrayListCreator implements CreatorFromRow<ArrayList<String>> {

  /**
   * A method that creates an ArrayList out of a List of Strings.
   *
   * @param row the passed in List of Strings.
   * @return the created ArrayList.
   */
  @Override
  public ArrayList<String> create(List<String> row) {
    ArrayList<String> list = new ArrayList<>();
    for (String currRow : row) {
      list.add(currRow);
    }
    return list;
  }
}
