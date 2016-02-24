package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class getRating {
    public getRating(DatastoreService datastore, String key, HttpServletResponse resp) {

        try {
            Entity rating = datastore.get(KeyFactory.stringToKey(key));
            resp.getWriter().println(rating.getProperty("Votes") + "~" + rating.getProperty("Reports"));
        } catch (EntityNotFoundException | IOException e) {
            e.printStackTrace();
        }

    }
}
