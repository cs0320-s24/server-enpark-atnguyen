package edu.brown.cs.student.main.SearchCSV;

import static spark.Spark.after;

import edu.brown.cs.student.main.Handlers.BroadbandHandler;
import edu.brown.cs.student.main.Handlers.LoadHandler;
import edu.brown.cs.student.main.Handlers.SearchHandler;
import edu.brown.cs.student.main.Handlers.ViewHandler;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import spark.Spark;

/** The Main class of our project. This is where execution begins. */
public final class Server {

  private String[] args;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    int port = 3232;
    Spark.port(port);


    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    Spark.get("broadband", new BroadbandHandler());
    Spark.get("load", new LoadHandler());
    Spark.get("search", new SearchHandler());
    Spark.get("view", new ViewHandler());
    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);

  }


  /**
   * Starts the REPL that this Main class handles. Runs repeatedly until a user specifies 'quit' or
   * an error occurs.
   */
  private void run() {
    boolean hasHeaders = false;
    if (this.args[1].equals("true")) {
      hasHeaders = true;
    }
//    Utility utility = new Utility(this.args[0], hasHeaders);
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.println(
        "Welcome! Please enter your target word's column index or header, or 'all' to "
            + "search all columns."
            + "\n(Type 'quit' to exit at any time)");
    try {
      String line = reader.readLine();
      while (line != null) {
        String column = line;
        if (line.toLowerCase().equals("quit")) {
          break;
        }
        System.out.println("Enter a word or phrase to search for:");
        String toFind = reader.readLine();
        if (toFind.toLowerCase().equals("quit")) {
          break;
        }
        try {
//          utility.run(toFind, column);
        } catch (IllegalArgumentException e) {
          // goes to the next repl iteration if invalid column
          System.err.println(
              "Sorry, that column does not exist. Would you like to search all columns?");
          line = reader.readLine();
          if (line.toLowerCase().equals("yes")) {
//            utility.run(toFind, "all");
          }
        }
        System.out.println(
            "Enter your next word's column's index or header, or 'all' to search all columns:");
        line = reader.readLine();
      }
    } catch (FileNotFoundException e) {
      System.err.println(
          "File does not exist within the data/ folder. Please re-run with the "
              + "correct file path! \nRemember to include your file's sub-folders, or enter the "
              + "file path from content root.");
      System.exit(1);
//    } catch (FactoryFailureException e) {
//      System.err.println("Error creating row object");
//      System.exit(1);
    } catch (IOException e) {
      System.err.println("Error reading input");
      System.exit(1);
    }
    try {
      reader.close();
    } catch (IOException e) {
      System.err.println("Error closing file");
      System.exit(1);
    }
  }
}
