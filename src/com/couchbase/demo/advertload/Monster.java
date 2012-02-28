/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.couchbase.demo.advertload;

import java.util.UUID;

/**
 *
 * @author ingenthr
 */
public class Monster {

    private String jsonType = "monster";
    private String name;
    private UUID uuid;
    private int hitpoints;
    private int experienceWhenKilled;
    private double itemProbability;

    public Monster(String monstername) {
	name = monstername;
	uuid = UUID.randomUUID();
	experienceWhenKilled = AdvertloadDriver.getRandom().random(10, 100);
	hitpoints = AdvertloadDriver.getRandom().random(10, 5000);
	itemProbability = AdvertloadDriver.getRandom().drandom(0.1d, 0.6d);
    }

    protected Monster() {
	// for GSON
    }

    String getName() {
	return name;
    }

    /**
     * @return the hitpoints
     */
    public int getHitpoints() {
	return hitpoints;
    }

    /**
     * @return the experienceWhenKilled
     */
    public int getExperienceWhenKilled() {
	return experienceWhenKilled;
    }

    /**
     * @return the itemProbability
     */
    public double getItemProbability() {
	return itemProbability;
    }

}
