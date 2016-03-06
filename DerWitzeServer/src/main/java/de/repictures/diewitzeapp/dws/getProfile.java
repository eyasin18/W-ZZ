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
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

public class getProfile {
    private DatastoreService datastore;
    private String key;

    public getProfile(DatastoreService datastore, String key, String email, HttpServletResponse resp) throws IOException {
        this.datastore = datastore;
        this.key = key;

        Logger log = Logger.getLogger(dwspost.class.getName());
        if (key != null){
            Key personKey = KeyFactory.stringToKey(key);
            Entity userProfile;
            try {
                userProfile = datastore.get(personKey);
                String subs = "";
                ArrayList<String> subsList;
                try {
                    subsList = (ArrayList<String>) userProfile.getProperty("subs");
                    for (int i = 0; i < subsList.size(); i++){
                        subs += subsList.get(i);
                        subs += "~";
                    }
                } catch (NullPointerException ignored){}
                String abojokes = "";
                try {
                    ArrayList<String> abojokeslist = (ArrayList<String>) userProfile.getProperty("AboBox");
                    for (int i = 0; i < abojokeslist.size(); i++){
                        abojokes += getJokeData(abojokeslist.get(i));
                    }
                } catch (NullPointerException ignore){}
                String output = userProfile.getProperty("name") + "</we>" + userProfile.getProperty("photoUrl") + "</we>"
                        + userProfile.getProperty("email") + "</we>" + userProfile.getProperty("platform") + "</we>"
                        + userProfile.getProperty("coverUrl") + "</we>" + userProfile.getProperty("devise") + "</we>"
                        + subs + "</we>" + abojokes + "</we>" + userProfile.getProperty("about") + "</we>"
                        + userProfile.getProperty("Visible Name");
                log.info(output);
                resp.getWriter().println(URLEncoder.encode(output, "UTF-8"));
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }  else if (email != null){
            Key personKey = KeyFactory.createKey("email", email);
            Query query = new Query("profile", personKey);
            List<Entity> profiles = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
            if (profiles.size() > 0){
                for (Entity e : profiles){
                    resp.getWriter().println(e.getProperty("key") + "~" + e.getProperty("password"));
                }
            } else {
                resp.getWriter().println("null");
            }
        }
    }

    private String getJokeData(String aboKey) throws EntityNotFoundException {
        Entity e = datastore.get(KeyFactory.stringToKey(aboKey));
        String ratingKeyStr = (String) e.getProperty("VotingKey");
        Entity rating = datastore.get(KeyFactory.stringToKey(ratingKeyStr));
        String profileKeyStr = (String) e.getProperty("user");
        Entity user = datastore.get(KeyFactory.stringToKey(profileKeyStr));
        Boolean voted = false, reported = false;
        ArrayList<String> voters = (ArrayList<String>) rating.getProperty("Voters");
        if (voters.contains(key)) voted = true;
        ArrayList<String> reporters = (ArrayList<String>) rating.getProperty("Reporters");
        if (reporters.contains(key)) reported = true;
        String output = "";
        output += false + "~" + user.getProperty("name") + "~" + e.getProperty("inhalt") + "~"
                + rating.getProperty("Votes") + "~" + rating.getProperty("Reports") + "~"
                + e.getProperty("key") + "~" + user.getProperty("photoUrl") + "~" + user.getProperty("devise") + "~"
                + voted + "~" + reported + "~" + e.getProperty("VotingKey") + "~" + e.getProperty("user") + "<br />";
        return output;
    }
}
