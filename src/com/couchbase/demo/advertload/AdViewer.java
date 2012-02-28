package com.couchbase.demo.advertload;

import java.util.UUID;

import java.util.ArrayList;

/**
 * This class represents a player in the game simulator.
 * @author ingenthr
 */
public class AdViewer {
    private String type = "viewer";
    private UUID uuid;
    private String name;
    private boolean loggedIn;
    private ArrayList<String> playerItems;
    private String serverSideCookie;

    public AdViewer(String playerName) {
	name = playerName;
	uuid = UUID.randomUUID();
        serverSideCookie = AdvertloadDriver.getRandom().makeAString(6*1024, 100*1024);
    }

    protected AdViewer() {
	// for GSON
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void feed(int foodEnergy) {
	return;
    }

    String getName() {
	return this.name;
    }

    public void logIn() {
	loggedIn = true;
    }

    public void logOut() {
	loggedIn = false;
    }

    UUID getUuid() {
	return uuid;
    }

}
