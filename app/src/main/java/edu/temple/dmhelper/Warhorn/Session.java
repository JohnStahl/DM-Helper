package edu.temple.dmhelper.Warhorn;

import java.io.Serializable;

import edu.temple.dmhelper.SessionsQuery;

public class Session implements Serializable {
    protected String campaign, scenario, playerSeats, gmSeats, notes, blurb, time, pictureURL, signupURL;

    public Session(SessionsQuery.Node session){
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
            time = session.slot().startsAt().toString() + "-" + session.slot().endsAt().toString();
        }else{ time = "No time given"; }

        //Following fields will never be null
        playerSeats = "Player seats available: " + session.availablePlayerSeats();
        gmSeats = "GM seats available: " + session.availableGmSeats();
        pictureURL = session.scenarioOffering().scenario().coverArtUrl();
        notes = session.notes();
        blurb = session.scenarioOffering().scenario().blurb();
        signupURL = session.signupUrl();
    }
}
