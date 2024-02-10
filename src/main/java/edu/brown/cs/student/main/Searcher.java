package edu.brown.cs.student.main;

import edu.brown.cs.student.main.CreatorFromRowClasses.FactoryFailureException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Searcher class that handles the searching of a CSV for a word. */
public class Searcher {

  private CSVParser parser;

  /**
   * Constructor for the Searcher class that defines the parser field.
   *
   * @param parser - a CSVParser that the searcher uses to parse a CSV
   */
  public Searcher(CSVParser parser) {
    this.parser = parser;
  }

  /**
   * Method that searches a CSV for the String toFind. This search method has no column specified
   * and searches all the CSV rows.
   *
   * @param toFind - the String to look for.
   * @param hasHeaders - whether the CSV has headers.
   * @return - a List<ArrayList<String>> of rows with the desired word.
   * @throws FactoryFailureException
   * @throws IOException
   */
  public List<ArrayList<String>> search(String toFind, boolean hasHeaders)
      throws FactoryFailureException, IOException {
    List<ArrayList<String>> rowsWithVal = new ArrayList<>();
    List<ArrayList<String>> parsedCSV = this.parser.parse(hasHeaders);
    int numCols = 0;
    // find the number of expected columns based on the number of headers or the number of items in
    // the first row
    if (hasHeaders) {
      numCols = findNumCols(this.parser.getCSVHeaders());
    } else {
      numCols = findNumCols(parsedCSV.get(0));
    }

    for (ArrayList<String> row : parsedCSV) {
      // statement to warn users that their CSV may be malformed, but it is still searchable
      if (numCols != this.findNumCols(row)) {
        System.out.println(
            "Warning: Row " + parsedCSV.indexOf(row) + " has an incorrect number of columns");
      }
      for (String word : row) { // check each column
        if (word.toLowerCase().equals(toFind.toLowerCase())) {
          rowsWithVal.add(row);
          break;
        }
      }
    }
    return rowsWithVal;
  }

  /**
   * Method that searches a CSV for the String toFind. This search method has a column specified and
   * searches within that column.
   *
   * @param toFind - the String to look for.
   * @param column - the column to look in.
   * @param hasHeaders - whether the CSV has headers.
   * @return - a List<ArrayList<String>> of rows with the desired word.
   * @throws FactoryFailureException
   * @throws IOException
   */
  public List<ArrayList<String>> search(String toFind, String column, boolean hasHeaders)
      throws FactoryFailureException, IOException, IllegalArgumentException {

    List<ArrayList<String>> rowsWithVal = new ArrayList<>();
    // want to keep the headers in to determine the header's column index
    List<ArrayList<String>> parsedCSV = this.parser.parse(hasHeaders);

    int numCols = 0;
    List<String> headers = new ArrayList<>();
    if (hasHeaders) {
      headers = this.parser.getCSVHeaders();
      numCols = findNumCols(headers);
    } else {
      numCols = findNumCols(parsedCSV.get(0));
    }

    int colIndex = -1;
    // determine String header column index

    for (int i = 0; i < numCols; i++) {
      // for header names
      if (hasHeaders) {
        if (headers.get(i).toLowerCase().equals(column.toLowerCase())) {
          colIndex = i;
          break;
        }
      }
      // for column indexes
      if (Integer.toString(i).equals(column)) {
        colIndex = i;
        break;
      }
    }

    // header was not found or index is invalid
    if (colIndex < 0) {
      throw new IllegalArgumentException("Invalid column");
    }

    // start at the second row if there's headers
    for (int i = 0; i < parsedCSV.size(); i++) { // for each row
      ArrayList<String> row = parsedCSV.get(i);
      if (numCols != this.findNumCols(row)) {
        System.out.println(
            "Warning: Row " + parsedCSV.indexOf(row) + " has an incorrect number of columns");
      }
      try {
        if (row.get(colIndex).toLowerCase().equals(toFind.toLowerCase())) {
          rowsWithVal.add(row);
        }
      } catch (IndexOutOfBoundsException e) {
        continue; //  skip the row and continue searching if this value doesn't exist
      }
    }
    return rowsWithVal;
  }

  /**
   * Helper method that determines how many columns are in a row.
   *
   * @param row - the row to count in.
   * @return - the number of columns found.
   */
  public int findNumCols(List<String> row) {
    int numCols = 0;
    for (String word : row) {
      numCols++;
    }
    return numCols;
  }
}
