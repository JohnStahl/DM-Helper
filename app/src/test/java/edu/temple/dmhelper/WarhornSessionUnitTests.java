package edu.temple.dmhelper;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import edu.temple.dmhelper.Warhorn.Session;

import static org.junit.Assert.*;

public class WarhornSessionUnitTests {
    SessionsQuery.Node sessionNode, nullSessionNode, invalidTimeSessionNode;

    @Before
    public void setup(){
        SessionsQuery.Campaign campaign = new SessionsQuery.Campaign("Campaign",
                "Campaign Name");
        SessionsQuery.Scenario scenario = new SessionsQuery.Scenario("Scenario",
                "Scenario Name", campaign, "Blurb", "http://www.art.com");
        SessionsQuery.ScenarioOffering scenarioOffering = new SessionsQuery.ScenarioOffering(
                "Scenario Offering", scenario);
        SessionsQuery.Slot slot = new SessionsQuery.Slot("Slot",
                "2016-09-03T14:00:00-07:00", "2016-09-03T18:00:00-07:00");
        sessionNode = new SessionsQuery.Node("Session", 1,
                10, "notes", scenarioOffering,
                "http://www.signup.com", slot);

        SessionsQuery.Scenario nullScenario = new SessionsQuery.Scenario("Scenario",
                null, null, "Blurb", "http://www.art.com");
        SessionsQuery.ScenarioOffering nullScenarioOffering = new SessionsQuery.ScenarioOffering(
                "Scenario Offering", nullScenario);
        nullSessionNode = new SessionsQuery.Node("Session", 1,
                10, null, nullScenarioOffering,
                "http://www.signup.com", slot);


        SessionsQuery.Slot invalidSlot = new SessionsQuery.Slot("Slot",
                "Invalid time", "Another invalid time");
        invalidTimeSessionNode = new SessionsQuery.Node("Session", 1,
                1, null, nullScenarioOffering,
                "http://www.signup.com", invalidSlot);
    }

    @Test
    public void TestTimeFormatting(){
        DateFormat ISO8601dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        boolean ISOFormat;

        try{
            ISO8601dateFormat.parse(sessionNode.slot.startsAt.toString());
            ISOFormat = true;
        }catch(ParseException e){
            ISOFormat = false;
        }

        assertTrue("Session slot not in correct format", ISOFormat);

        Session testSession = new Session(sessionNode);
        DateFormat desiredFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        boolean DesiredFormat;

        try{
            desiredFormat.parse(testSession.time);
            DesiredFormat = true;
        }catch(ParseException e){
            DesiredFormat = false;
        }

        assertTrue("Time not formatted correclty", DesiredFormat);
    }

    @Test
    public void TestNullEntries(){
        Session testNullSession = new Session(nullSessionNode);
        assertEquals("Null campaign name not caught",
                "No campaign name given", testNullSession.campaign);
        assertEquals("Null scenario name not caught",
                "No scenario name given", testNullSession.scenario);
    }

    @Test
    public void TestInvalidTime(){
        Session invalidTimeSession = new Session(invalidTimeSessionNode);
        assertEquals("Invalid times not caught correctly",
                "Invalid time given", invalidTimeSession.time);
    }
}