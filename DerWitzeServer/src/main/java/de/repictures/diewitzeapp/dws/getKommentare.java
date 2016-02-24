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

public class getKommentare {

    public getKommentare(DatastoreService datastore, Key commentKey, String profileKey, HttpServletResponse resp) throws IOException, EntityNotFoundException {
        Boolean voted = false;
        Query query = new Query("comment", commentKey);
        query.addSort("votes", Query.SortDirection.DESCENDING);
        List<Entity> comments = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        for (Entity e : comments) {
            Key userKey = KeyFactory.stringToKey((String) e.getProperty("user"));
            Entity user = datastore.get(userKey);
            ArrayList<String> voters = (ArrayList<String>) e.getProperty("Voters");
            if (voters.contains(profileKey)) voted = true;
            resp.getWriter().println(URLEncoder.encode(e.getProperty("comment") + "~" + user.getProperty("name") + "~"
                    + e.getProperty("votes") + "~" + user.getProperty("photoUrl") + "~"
                    + user.getProperty("key") + "~" + voted + "~" + e.getProperty("commentKey") + "~", "UTF-8"));
        }
    }
}
