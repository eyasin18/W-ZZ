package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class WitzeNachUser {
    public WitzeNachUser(String userStr, DatastoreService datastore,
                         Key userKey, HttpServletResponse resp) throws IOException, EntityNotFoundException {

        Query query = new Query("userPosting", userKey);
        query.addSort("datum", Query.SortDirection.DESCENDING);
        List<Entity> userPosts = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        Entity user = datastore.get(KeyFactory.stringToKey(userStr));
        String profileStr = user.getProperty("name") + "~" + user.getProperty("photoUrl") + "~"
                + user.getProperty("email") + "~" + user.getProperty("platform") + "~"
                + user.getProperty("coverUrl") + "~" + user.getProperty("devise") + "~";
        String output = "";
        for (Entity e : userPosts){
            String ratingKeyStr = (String) e.getProperty("VotingKey");
            Entity rating = datastore.get(KeyFactory.stringToKey(ratingKeyStr));
            String profileKeyStr = (String) e.getProperty("user");
            Entity userE = datastore.get(KeyFactory.stringToKey(profileKeyStr));
            Boolean voted = false, reported = false;
            ArrayList<String> voters = (ArrayList<String>) rating.getProperty("Voters");
            if (voters.contains(profileKeyStr)) voted = true;
            ArrayList<String> reporters = (ArrayList<String>) rating.getProperty("Reporters");
            if (reporters.contains(profileKeyStr)) reported = true;
            output += userE.getProperty("name") + "~" + e.getProperty("inhalt")+ "~"
                    + rating.getProperty("Votes") + "~" + rating.getProperty("Reports") + "~"
                    + e.getProperty("key") + "~" + userE.getProperty("photoUrl") + "~" + userE.getProperty("devise") + "~"
                    + voted + "~" + reported + "~" + e.getProperty("VotingKey") + "~" + e.getProperty("user") + "~";
        }
        output += "</we>";
        ArrayList<String> abolist;
        try {
            abolist = (ArrayList<String>) user.getProperty("abos");
            for (int i = 0; i < abolist.size(); i++){
                Entity abo = datastore.get(KeyFactory.stringToKey(abolist.get(i)));
                output += abolist.get(i) + "~" + abo.getProperty("name") + "~" + abo.getProperty("photoUrl") + "</~>";
            }
        } catch (NullPointerException ignored){}
        resp.getWriter().println(URLEncoder.encode(profileStr, "UTF-8") + URLEncoder.encode(output.replace("\n", "<br />"), "UTF-8"));
    }
}
