/*
 * This benchmark has been based on the FTP101 driver
 */
package com.couchbase.demo.advertload;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.CouchbaseConnectionFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.faban.driver.*;
import com.sun.faban.driver.util.Random;
import com.sun.faban.harness.EndRun;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import sun.net.ftp.FtpClient;

import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.logging.Logger;

@BenchmarkDefinition(name = "Advertising Workload",
version = "0.1",
configPrecedence = true)
@BenchmarkDriver(name = "AdvertloadDriver",
threadPerScale = (float) 1)
@MatrixMix(operations = {"Login", "Logout", "Event"},
mix = {
    @Row({0, 20, 80}),
    @Row({100, 0, 0}),
    @Row({0, 50, 50})
})
@NegativeExponential(cycleType = CycleType.CYCLETIME,
cycleMean = 200,
cycleDeviation = 5)
public class AdvertloadDriver {

    /** The driver context for this instance. */
    private DriverContext ctx;
    Logger logger;
    private static Random random;
    FtpClient ftpClient;
    int fileCount;
    String host;
    int port = -1;
    int threadId;
    int putSequence = 1;
    String localFileName;
    String uploadPrefix;
    String user;
    Gson gson;
    GsonBuilder gsonBuilder;
    String playerName;
    String password;
    AdViewer player;
    private final String bucketname;
    private String bucketpass;
    private static CouchbaseClient advertloadStore;
    private int actorsSpecified;
    private final int ACTORMULT = 1000 / adViewers.length;
    private static final String[] adViewers = {"Matt", "Steve", "Dustin",
	"James", "Trond", "Melinda",
	"Bob", "Perry", "Sharon",
	"Leila", "Tony", "Damien", "Jan", "JChris",
	"Volker", "Dale", "Aaron", "Aliaksey", "Frank",
	"Mike", "Claire", "Benjamin", "Tony", "Keith",
	"Bin", "Chiyoung", "Jens", "Srini", "Rags", "John", "Ali"
    };

    /**
     * Constructs an instance of a user session on the game simulator.
     * @throws XPathExpressionException An XPath error occurred
     * @throws IOException I/O error creating the driver instance
     */
    public AdvertloadDriver() throws XPathExpressionException, IOException {
	ctx = DriverContext.getContext();

	// set up gson for serialization
	gsonBuilder = new GsonBuilder();
	gson = gsonBuilder.create();

	logger = ctx.getLogger();

	random = ctx.getRandom();
	threadId = ctx.getThreadId();
	uploadPrefix = "up" + threadId + '_';
	localFileName = "/tmp/ftp" + threadId;
	host =
		ctx.getXPathValue("/advertloadBenchmark/serverConfig/host").trim();
	String portNum =
		ctx.getXPathValue("/advertloadBenchmark/serverConfig/port").trim();
	user = ctx.getProperty("user");
	password = ctx.getProperty("password");
        actorsSpecified = Integer.parseInt(ctx.getProperty("numactors"));

	URI server;
	try {
	    // Create a basic client
	    server = new URI(ctx.getXPathValue("/advertloadBenchmark/serverConfig/host").trim());
	} catch (URISyntaxException ex) {
	    Logger.getLogger(AdvertloadDriver.class.getName()).log(Level.SEVERE, null, ex);
	    throw new IOException("Could not deal with URI.");
	}
	bucketname = ctx.getXPathValue("/advertloadBenchmark/serverConfig/bucket").trim();
	bucketpass = ctx.getXPathValue("/advertloadBenchmark/serverConfig/bucketpw").trim();
	if (bucketpass == null) {
	    bucketpass = "";
	}
	ArrayList<URI> servers = new ArrayList<URI>();
	servers.add(server);
	// @todo fix up this singletonness of the clients
	if (advertloadStore == null) {
            CouchbaseConnectionFactoryBuilder cfb = new CouchbaseConnectionFactoryBuilder();
            cfb.setOpTimeout(10000);  // wait up to 10 seconds for an operation to succeed
            cfb.setOpQueueMaxBlockTime(5000); // wait up to 5 seconds when trying to enqueue an operation
	    advertloadStore = new CouchbaseClient(cfb.buildCouchbaseConnection(servers, bucketname, bucketname, bucketpass));
	}
    }

    @OnceBefore
    public void setup() throws InterruptedException {
	logger.info("The creator is setting up a consumer oriented world.");
	populateViewers(ACTORMULT);
	logger.info("The creator will now rest; we're ready for the holiday season.");


    }

    private void populateViewers(int number) {
	logger.log(Level.INFO, "Creating {0} advertising viewers to buy our stuff.", number);
	for (int i = 0; i < number; i++) {
	    for (String aplayer : adViewers) {
		AdViewer newPlayer = new AdViewer(aplayer + i);
		advertloadStore.add(newPlayer.getName(), 0, gson.toJson(newPlayer));
	    }
	}
    }

    /**
     * Operation to simulate a login.
     * @throws IOException Error connecting to server
     */
    @BenchmarkOperation(name = "Login",
    max90th = 2,
    timing = Timing.MANUAL)
    public void doLogin() throws IOException, InterruptedException, ExecutionException {

	if (player != null) {
	    return;
	}
	ctx.recordTime();
	playerName = getRandomViewer();
	String playerJsonRepresentation = (String) advertloadStore.get(stripBlanks(playerName));
	logger.log(Level.FINE, "Player JSON:\n {0}", playerJsonRepresentation);
	if (playerJsonRepresentation == null) {
	    logger.log(Level.FINE, "Player JSON:\n {0}", playerJsonRepresentation);
	    player = new AdViewer(playerName);
	} else {
	    player = gson.fromJson(playerJsonRepresentation, AdViewer.class);
	}
        //Only write to the store if the status changed
        if (!player.isLoggedIn()){
            player.logIn();
            storePlayer();
        }
	ctx.recordTime();
    }

    /**
     * Operation to simulate a login.
     * @throws IOException Error connecting to server
     */
    @BenchmarkOperation(name = "Logout",
    max90th = 2,
    timing = Timing.MANUAL)
    public void doLogout() throws IOException, InterruptedException, ExecutionException {
	if (player == null) {
	    return; // can't log out when logged out
	}
	ctx.recordTime();
        //Only write to the store if the status changed
        if (player.isLoggedIn()){
            player.logOut();
            storePlayer();
        }
	player = null;
	ctx.recordTime();
    }

    private void storePlayer() throws InterruptedException, ExecutionException {
	Future<Boolean> setRes = advertloadStore.set(stripBlanks(player.getName()), 0, gson.toJson(player));
	setRes.get();
    }

    /**
     * Operation to simulate an advertising event.
     * @throws IOException Error connecting to server
     */
    @BenchmarkOperation(name = "Eat",
    max90th = 2,
    timing = Timing.MANUAL)
    public void doEat() throws IOException, InterruptedException, ExecutionException {
	doLogin();
	int foodEnergy = random.random(10, 100);
	ctx.recordTime();
	player.feed(foodEnergy);
	ctx.recordTime();
    }


    /**
     * Operation to simulate eating.
     * @throws IOException Error connecting to server
     */
    @BenchmarkOperation(name = "Event",
    max90th = 2,
    timing = Timing.MANUAL)
    public void doEvent() throws IOException, InterruptedException, ExecutionException {
	doLogin();
        Event aView = new Event(getRandomViewer());
	ctx.recordTime();
        advertloadStore.set(aView.getEventName(), 0, gson.toJson(aView));
	ctx.recordTime();
    }

    @EndRun
    public void cleanup() {
	//gamesimStore.flush();
	advertloadStore.shutdown();
    }

    public static String stripBlanks(String s) {
	return s.replaceAll("\\s", "");
    }

    private String getRandomViewerName() {
	int i = random.random(0, adViewers.length - 1);
	return adViewers[i];
    }

    private String getRandomViewer() {
	return getRandomViewerName() + random.random(0, ACTORMULT - 1);
    }

    /**
     * @return the random
     */
    public static Random getRandom() {
	return random;
    }
}
