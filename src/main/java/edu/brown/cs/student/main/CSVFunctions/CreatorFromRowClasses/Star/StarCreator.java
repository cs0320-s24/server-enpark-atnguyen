package edu.brown.cs.student.main.CSVFunctions.CreatorFromRowClasses.Star;

import edu.brown.cs.student.main.CSVFunctions.CreatorFromRowClasses.CreatorFromRow;
import java.util.List;

/** A StarCreator class that implements CreatorFromRow. It creates a Star object. */
public class StarCreator implements CreatorFromRow<Star> {

  /**
   * A method that creates a new Star with properties defined in its passed in List.
   *
   * @param row the List of strings that have its properties
   * @return Star object
   */
  @Override
  public Star create(List<String> row) {
    return new Star(
        Float.parseFloat(row.get(0)),
        row.get(1),
        Float.parseFloat(row.get(2)),
        Float.parseFloat(row.get(3)),
        Float.parseFloat(row.get(4)));
  }
}
