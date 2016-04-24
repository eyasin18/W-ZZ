package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

public class WitzeNachKategoAlle {
    private final DatastoreService datastore;
    private final Key postKey;
    private final String profileKey;
    Logger log = Logger.getLogger(dwspost.class.getName());

    public WitzeNachKategoAlle(DatastoreService datastore, Key postKey,String profileKey, HttpServletResponse resp)
            throws IOException, EntityNotFoundException {
        this.datastore = datastore;
        this.postKey = postKey;
        this.profileKey = profileKey;

        resp.getWriter().println(URLEncoder.encode(getNeu() + "</~>" + getHeute() + "</~>" + getBeliebteste(), "UTF-8"));
    }

    private String getNeu() throws EntityNotFoundException {
        Query query;
        query = new Query("posting", postKey);
        query.addSort("datum", Query.SortDirection.DESCENDING);
        List<Entity> posts = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        List<Entity> outputlist;
        try {
            outputlist = posts.subList(0, 15);
        } catch (IndexOutOfBoundsException e){
            outputlist = posts;
        }
        String output = "";
        for(Entity e : outputlist) {
            String ratingKeyStr = (String) e.getProperty("VotingKey");
            Entity rating = datastore.get(KeyFactory.stringToKey(ratingKeyStr));
            String profileKeyStr = (String) e.getProperty("user");
            Entity user = datastore.get(KeyFactory.stringToKey(profileKeyStr));
            Boolean voted = false, reported = false;
            ArrayList<String> voters = (ArrayList<String>) rating.getProperty("Voters");
            if (voters.contains(profileKey)) voted = true;
            ArrayList<String> reporters = (ArrayList<String>) rating.getProperty("Reporters");
            if (reporters.contains(profileKey)) reported = true;
            output += false + "~" + user.getProperty("Visible Name") + "~" + e.getProperty("inhalt")+ "~"
                    + rating.getProperty("Votes") + "~" + rating.getProperty("Reports") + "~"
                    + e.getProperty("key") + "~" + user.getProperty("photoUrl") + "~" + user.getProperty("devise") + "~"
                    + voted + "~" + reported + "~" + e.getProperty("VotingKey") + "~" + e.getProperty("user") + "</we>";
        }
        output += true + "</we>";
        return output.replace("\n", "<br />");
    }

    private String getHeute() throws EntityNotFoundException {
        Query query;
        query = new Query("posting", postKey);
        query.addSort("votes", Query.SortDirection.DESCENDING);
        List<Entity> preposts = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        ArrayList<Entity> posts = new ArrayList<>();

        for (int c = 0; c < preposts.size(); c++){
            Entity e = preposts.get(c);
            if (getTimePosted(e, 3)) {
                posts.add(preposts.get(c));
            }
        }
        List<Entity> outputlist;
        try {
            outputlist = posts.subList(0, 15);
        } catch (IndexOutOfBoundsException e){
            outputlist = posts;
        }
        String output = "";
        for(Entity e : outputlist) {
            String ratingKeyStr = (String) e.getProperty("VotingKey");
            Entity rating = datastore.get(KeyFactory.stringToKey(ratingKeyStr));
            String profileKeyStr = (String) e.getProperty("user");
            Entity user = datastore.get(KeyFactory.stringToKey(profileKeyStr));
            Boolean voted = false, reported = false;
            ArrayList<String> voters = (ArrayList<String>) rating.getProperty("Voters");
            if (voters.contains(profileKey)) voted = true;
            ArrayList<String> reporters = (ArrayList<String>) rating.getProperty("Reporters");
            if (reporters.contains(profileKey)) reported = true;
            output += false + "~" + user.getProperty("Visible Name") + "~" + e.getProperty("inhalt") + "~"
                    + rating.getProperty("Votes") + "~" + rating.getProperty("Reports") + "~"
                    + e.getProperty("key") + "~" + user.getProperty("photoUrl") + "~" + user.getProperty("devise") + "~"
                    + voted + "~" + reported + "~" + e.getProperty("VotingKey") + "~" + e.getProperty("user") + "</we>";
        }
        output += true + "</we>";
        return output.replace("\n", "<br />");
    }

    private String getBeliebteste() throws EntityNotFoundException {
        Query query;
        query = new Query("posting", postKey);
        query.addSort("votes", Query.SortDirection.DESCENDING);
        List<Entity> preposts = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        List<Entity> outputlist;
        List<Entity> posts = new ArrayList<>();

        for (int c = 0; c < preposts.size(); c++){
            Entity e = preposts.get(c);
            if (compareCrazyValues(e) && getTimePosted(e , 5)) {
                posts.add(preposts.get(c));
            }
        }
        try {
            outputlist = posts.subList(0, 15);
        } catch (IndexOutOfBoundsException e){
            outputlist = posts;
        }
        String output = "";
        for(Entity e : outputlist) {
            String ratingKeyStr = (String) e.getProperty("VotingKey");
            Entity rating = datastore.get(KeyFactory.stringToKey(ratingKeyStr));
            String profileKeyStr = (String) e.getProperty("user");
            Entity user = datastore.get(KeyFactory.stringToKey(profileKeyStr));
            Boolean voted = false, reported = false;
            ArrayList<String> voters = (ArrayList<String>) rating.getProperty("Voters");
            if (voters.contains(profileKey)) voted = true;
            ArrayList<String> reporters = (ArrayList<String>) rating.getProperty("Reporters");
            if (reporters.contains(profileKey)) reported = true;
            output += false + "~" + user.getProperty("Visible Name") + "~" + e.getProperty("inhalt") + "~"
                    + rating.getProperty("Votes") + "~" + rating.getProperty("Reports") + "~"
                    + e.getProperty("key") + "~" + user.getProperty("photoUrl") + "~" + user.getProperty("devise") + "~"
                    + voted + "~" + reported + "~" + e.getProperty("VotingKey") + "~" + e.getProperty("user") + "</we>";
        }
        output += true + "</we>";
        return output.replace("\n", "<br />");
    }

    private Boolean getTimePosted(Entity e, int time) {
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DAY_OF_YEAR, -time - 1); // time Tage vorher
        Date h = c1.getTime();

        Date d = (Date) e.getProperty("datum"); // your date

        return !d.before(h);
    }

    private boolean compareCrazyValues(Entity witz) throws EntityNotFoundException {
        Entity user = datastore.get(KeyFactory.stringToKey(profileKey));
        long userCrazyValue = (long) user.getProperty("crazyValue");
        long witzCrazyValue = (long) witz.getProperty("crazyValue");
        Boolean isInRange = (userCrazyValue - 10) <= witzCrazyValue && witzCrazyValue <= (userCrazyValue + 10);
        log.info("User crazy value = " + userCrazyValue + " Witz crazy value = " + witzCrazyValue + " Is in range = " + isInRange);
        return isInRange;
    }
}
