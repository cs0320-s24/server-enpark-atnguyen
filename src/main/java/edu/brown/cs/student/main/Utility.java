package edu.brown.cs.student.main;

import edu.brown.cs.student.main.CreatorFromRowClasses.ArrayListCreator;
import edu.brown.cs.student.main.CreatorFromRowClasses.CreatorFromRow;
import edu.brown.cs.student.main.CreatorFromRowClasses.FactoryFailureException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Utility class that manages the overall search utility. Instantiates Searcher and Parser and
 * calls on them.
 */
public class Utility {

  private String file;
  private boolean hasHeaders;

  /**
   * Constructor for the utility class that ensures file safety by checking to make sure data/ is at
   * the beginning of the file.
   *
   * @param file - the CSV file to search within
   * @param hasHeaders - whether the CSV file has headers
   */
  public Utility(String file, boolean hasHeaders) {
    if (file.substring(0, 5).equals("data/")) {
      this.file = file;
    } else {
      this.file = "data/" + file;
    }
    this.hasHeaders = hasHeaders;
  }

  /**
   * Method that runs the search function and prints out the columns that contain the desired word.
   *
   * @param toFind - the word to look for.
   * @param column - the column (if any) to search within.
   * @throws FactoryFailureException
   * @throws IOException
   */
  public void run(String toFind, String column) throws FactoryFailureException, IOException {
    CreatorFromRow creator = new ArrayListCreator();
    CSVParser parser = new CSVParser(new FileReader(this.file), creator);
    Searcher searcher = new Searcher(parser);

    List<ArrayList<String>> rowsWithTarget = new ArrayList<>();
    if (column.equals("")
        || column.equals("all")) { // search all columns if the user just presses return
      rowsWithTarget = searcher.search(toFind.toLowerCase(), this.hasHeaders);
    } else {
      rowsWithTarget = searcher.search(toFind.toLowerCase(), column.toLowerCase(), this.hasHeaders);
    }
    System.out.println("Rows that contain '" + toFind + "':");
    for (ArrayList<String> row : rowsWithTarget) {
      System.out.println("\t" + row);
    }
  }
}
