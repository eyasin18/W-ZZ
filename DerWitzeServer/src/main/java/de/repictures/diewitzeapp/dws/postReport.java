package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

public class postReport {
    public postReport(DatastoreService datastore, String key, String inhalt, String profileKeyStr, HttpServletResponse resp) throws IOException {
        Key ratingKey = KeyFactory.stringToKey(key);
        try {
            Entity rating = datastore.get(ratingKey);
            long reports = (long) rating.getProperty("Reports");
            ArrayList<String> reporters = (ArrayList<String>) rating.getProperty("Reporters");
            ArrayList<String> reasons = (ArrayList<String>) rating.getProperty("Reasons");
            if (!reporters.contains(profileKeyStr)){
                rating.setProperty("Reports", reports + 1);
                reporters.add(profileKeyStr);
                rating.setProperty("Reporters", reporters);
                reasons.add(inhalt);
                rating.setProperty("Reasons", reasons);
                datastore.put(rating);
                resp.getWriter().println("success");
            }
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
