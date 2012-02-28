package com.couchbase.demo.advertload;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author ingenthr
 */
public class Event {

    private String type  = "event";
    private String viewed;
    private UUID uuid;
    private String triggeredBy;
    private Date eventDate;
    private Boolean clicked;

    private static final String[] thingsToBuy = { "womens_shoes", "mens_shoes", "womans_dress",
        "digital_camera", "womans_coat", "laptop_computer", "tv", "video_game", "handbag", "perfume"
    };

    public Event(String eventViewer) {
	triggeredBy = eventViewer;
	uuid = UUID.randomUUID();
        eventDate = new Date();
        viewed = thingsToBuy[AdvertloadDriver.getRandom().random(0, thingsToBuy.length-1)];
        if (AdvertloadDriver.getRandom().random(0, 99) > 74) {
            clicked = true;
        } else {
            clicked = false;
        }

    }

    protected Event() {
	// for GSON
    }

    /**
     * @return the uuid
     */
    public UUID getUuid() {
	return uuid;
    }

    public String getItemViewed() {
	return viewed;
    }

    public String getEventName() {
        return triggeredBy + getUuid();

    }
}
