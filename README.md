> **GETTING STARTED:** You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the `pom.xml`, `/src` folder, etc, are all at this base directory.

> **IMPORTANT NOTE**: In order to run the server, run `mvn package` in your terminal then `./run` (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at `edu/brown/cs/student/main/server/Server`. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

# Project Details
- Sprint 2: Server
- Team:
  - We worked together to make design choices on everything before writing code, as well as debugging various issues and testing.
  - enpark: mainly contributed to load, search, and view handlers and their shared states
  - atnguyen: mainly contributed to broadband handler and caching
- Estimated time: 20 hrs
- Repository: https://github.com/cs0320-s24/server-enpark-atnguyen.git

# Design Choices
The highest-level main class is the Server class, which starts the server and creates handlers for 
the load, search, view, and broadband endpoints. Our server has two primary components: one for CSV 
Data and one for ACS Data.

### CSV Data
To load, search, and view CSV data we have 3 handlers for each endpoint. We use dependency injection
in each of these handlers with a shared state class, called CSVData that implements the interface
CSVDatasource. This class determines how CSV data and headers should be set and retrieved. In our
load handler, the csv that the user enters is immediately parsed, which is an optimization we
included so that each call to search or view on the same CSV wouldn't have to be parsed
repeatedly. The parsed CSV is then stored in the shared state class, so search and view can call
a getter to retrieve it. The CSV headers are also parsed out separately, if they exist, so they can
be used appropriately in search/view. LoadHandler also checks if CSVs are in a valid data/ directory,
and that the next directory is not ../ to navigate into a parent directory.

For searching, we refactored our CSV Sprint searcher to no longer parse in Searcher and instead take
in an already parsed CSV. We also added a getMalformedRows() function to Searcher so the handler can
report malformed rows to the user.

Lastly, for view, we just serialize the parsed CSV and put it in the response map, adding in headers
if the CSV has one. In each of these handlers, we have extensive error checking that populates the 
response map with a corresponding error message.

### ACS Data
For the broadband endpoint, we first query to get the state code based on the user's passed in state.
This state to code information is stored in a HashMap in the CodeConverter class so this query does
not have to be made again in the future. Then, we use the state code and the county entered by the 
user to query for the county code. In CodeConverter, we keep a variable for the most recently queried
state and county map (HashMap that maps counties to their codes), which is an optimization we made
so that if a user queries about the same state repeatedly, we don't have to query for the state's county
codes repeatedly. Lastly, we use this state code and county code information to query for broadband
data of that county, which is returned into our response map in broadbandHandler. 

Broadband handler also has extensive error checking that populates the response map with corresponding
errors.

### Caching
To avoid repeated calls to the ACS API, we implemented a cache that stores Broadband data and 
automatically purges entries after a certain cache size or time limit. To allow developers to 
customize the cache settings, we made the Caching class take in parameters for size and time, so
developers can set these arguments according to their needs. These settings are automatically
set to 10 and 1 if an invalid specification is entered.

### Serialization/Deserialization
In our API, we had to serialize and deserialize our response maps and the data we received from ACS.
We created a class for each of these two functions, which uses moshi to serialize and deserialize a
HashMap<String,Object> to a JSON and a JSON to a List<List<String>>.

# Errors/Bugs
No known bugs

# Tests
We created integration tests and unit tests

# How to
To run the tests, each testing suite sets up and runs on its own. To run the program, we just run the main Server class with ./run or the play button.