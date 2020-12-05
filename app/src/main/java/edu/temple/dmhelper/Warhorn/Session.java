package edu.temple.dmhelper.Warhorn;

import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import edu.temple.dmhelper.SessionsQuery;

//Class converts a session query into a collection of strings that will be displayed to the user
public class Session implements Serializable {
    protected String campaign, scenario, playerSeats, gmSeats, notes, blurb, time, pictureURL, signupURL;

    public Session(SessionsQuery.Node session) {
        //Following fields may be null; need to be checked before assignment
        if(session.scenarioOffering() != null && session.scenarioOffering().scenario() != null &&
                session.scenarioOffering().scenario().campaign() != null &&
                session.scenarioOffering().scenario().campaign().name() != null) {
            campaign = "Campaign: " + session.scenarioOffering().scenario().campaign().name();
        }else { campaign = "No campaign name given"; }

        if(session.scenarioOffering().scenario().name() != null) {
            scenario = "Scenario: " + session.scenarioOffering().scenario().name();
        }else { scenario = "No Scenario name given"; }
        if(session.slot() != null) {
            try {
                time = formatTime(session.slot().startsAt().toString(), session.slot().endsAt().toString());
            } catch (ParseException e) {
                time = "No time given";
            }
        }else{ time = "No time given"; }

        //Following fields will never be null
        playerSeats = "Player seats available: " + session.availablePlayerSeats();
        gmSeats = "GM seats available: " + session.availableGmSeats();
        pictureURL = session.scenarioOffering().scenario().coverArtUrl();
        notes = session.notes();
        blurb = session.scenarioOffering().scenario().blurb();
        signupURL = session.signupUrl();
    }

    private String formatTime(String startsAt, String endsAt) throws ParseException {
        DateFormat ISO8601dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        DateFormat desiredFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        Date start = ISO8601dateFormat.parse(startsAt);
        Date end = ISO8601dateFormat.parse(endsAt);
        return desiredFormat.format(start) + " - " + desiredFormat.format(end);
    }
}
