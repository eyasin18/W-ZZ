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
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class getProfileExtended {
    public getProfileExtended(DatastoreService datastore, String user, String userKey, HttpServletResponse resp) throws IOException, EntityNotFoundException {

        Key profileKey = KeyFactory.createKey("user", user);
        Entity profile = datastore.get(KeyFactory.stringToKey(user));
        Query query = new Query("userPosting", profileKey);
        query.addSort("datum", Query.SortDirection.DESCENDING);
        List<Entity> userPosts = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        String profileStr = profile.getProperty("Visible Name") + "~" + profile.getProperty("name") + "~"
                + profile.getProperty("photoUrl") + "~" + profile.getProperty("email") + "~"
                + profile.getProperty("platform") + "~" + profile.getProperty("coverUrl") + "~"
                + profile.getProperty("devise") + "~" + profile.getProperty("about") + "~";
        String output = "";
        for (Entity e : userPosts){
            String ratingKeyStr = (String) e.getProperty("VotingKey");
            Entity rating = datastore.get(KeyFactory.stringToKey(ratingKeyStr));
            Boolean voted = false, reported = false;
            ArrayList<String> voters = (ArrayList<String>) rating.getProperty("Voters");
            if (voters.contains(userKey)) voted = true;
            ArrayList<String> reporters = (ArrayList<String>) rating.getProperty("Reporters");
            if (reporters.contains(userKey)) reported = true;
            output += e.getProperty("inhalt") + "~" + rating.getProperty("Votes") + "~" +
                    rating.getProperty("Reports") + "~" + e.getProperty("key") + "~" +
                    voted + "~" + reported + "~" + e.getProperty("VotingKey") + "~";
        }
        output += "</we>";
        ArrayList<String> abolist;
        try {
            abolist = (ArrayList<String>) profile.getProperty("abos");
            for (int i = 0; i < abolist.size(); i++){
                Entity abo = datastore.get(KeyFactory.stringToKey(abolist.get(i)));
                output += abolist.get(i) + "~" + abo.getProperty("name") + "~" + abo.getProperty("photoUrl") + "</~>";
            }
        } catch (NullPointerException ignored){}
        resp.getWriter().println(URLEncoder.encode(profileStr + output.replace("\n", "<br />"), "UTF-8"));
    }
}
