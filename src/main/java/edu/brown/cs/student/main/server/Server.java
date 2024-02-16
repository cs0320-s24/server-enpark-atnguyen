package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.ACSData.Caching.Cache;
import edu.brown.cs.student.main.ACSData.CensusAPI;
import edu.brown.cs.student.main.Handlers.BroadbandHandler;
import edu.brown.cs.student.main.Handlers.LoadHandler;
import edu.brown.cs.student.main.Handlers.SearchHandler;
import edu.brown.cs.student.main.Handlers.ViewHandler;
import edu.brown.cs.student.main.ACSData.Caching.BroadbandDatasource;
import edu.brown.cs.student.main.CSVData.CSVData;
import edu.brown.cs.student.main.CSVData.CSVDatasource;
import spark.Spark;

/** The Main class of our project. This is where execution begins. */
public final class Server {

  private final CSVDatasource CSVstate;
  private final BroadbandDatasource broadbandState;
  static final int port = 3233;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Server(new CSVData(), new Cache(new CensusAPI(), 50, 5));
  }

  /**
   * Constructor of Server that defines the shared state and calls on our port to start.
   *
   * @param CSVstate
   * @param broadbandState
   */
  private Server(CSVDatasource CSVstate, BroadbandDatasource broadbandState) {
    this.CSVstate = CSVstate;
    this.broadbandState = broadbandState;
    this.run();
  }

  /** A method that is called at executions and starts the port 3232. */
  private void run() {
    Spark.port(this.port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    Spark.get("broadband", new BroadbandHandler(this.broadbandState));
    Spark.get("load", new LoadHandler(this.CSVstate));
    Spark.get("search", new SearchHandler(this.CSVstate));
    Spark.get("view", new ViewHandler(this.CSVstate));
    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + this.port);
  }
}
