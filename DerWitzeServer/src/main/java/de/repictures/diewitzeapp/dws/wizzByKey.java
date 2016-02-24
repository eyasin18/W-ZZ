package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

public class wizzByKey {
    public wizzByKey(DatastoreService datastore, String key, HttpServletResponse resp) throws EntityNotFoundException, IOException {
        Entity e = datastore.get(KeyFactory.stringToKey(key));
        String ratingKeyStr = (String) e.getProperty("VotingKey");
        Entity rating = datastore.get(KeyFactory.stringToKey(ratingKeyStr));
        String profileKeyStr = (String) e.getProperty("user");
        Entity user = datastore.get(KeyFactory.stringToKey(profileKeyStr));
        String output = user.getProperty("photoUrl") + "~" + user.getProperty("name")
                + "~" + e.getProperty("inhalt") + "~" + user.getProperty("devise") + "~" + rating.getProperty("Votes") + "~"
                + rating.getProperty("Reports") + "~" + e.getProperty("VotingKey") + "~" + e.getProperty("user");
        resp.getWriter().println(URLEncoder.encode(output.replace("\n", "<br />"), "UTF-8"));
    }
}
