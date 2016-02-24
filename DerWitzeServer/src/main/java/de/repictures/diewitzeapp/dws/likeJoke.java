package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

public class likeJoke {
    public likeJoke(DatastoreService datastore, String key, String profileKeyStr, String katego, Boolean votedUp,
                    Boolean voteChecker, HttpServletResponse resp) throws IOException {

        Key ratingKey = KeyFactory.stringToKey(key);
        if (votedUp != null && !voteChecker) {
            try {
                Entity rating = datastore.get(ratingKey);
                long votes = (long) rating.getProperty("Votes");
                Key parentKey = KeyFactory.stringToKey((String) rating.getProperty("ParentKey"));
                Entity parent = datastore.get(parentKey);
                Entity user = datastore.get(KeyFactory.stringToKey(profileKeyStr));
                long crazyValueUser = (long) user.getProperty("crazyValue");
                long crazyValueParent = (long) parent.getProperty("crazyValue");
                long parentVotes = (long) parent.getProperty("votes");
                ArrayList<String> voters = (ArrayList<String>) rating.getProperty("Voters");
                Logger log = Logger.getLogger(dwspost.class.getName());
                if (votedUp){
                    parent.setProperty("votes", parentVotes + 1);
                    rating.setProperty("Votes", votes + 1);
                    voters.add(profileKeyStr);
                    rating.setProperty("Voters", voters);
                    user.setProperty("crazyValue", getNewCrazyValue(crazyValueUser, crazyValueParent, true));
                    log.log(Level.INFO, crazyValueParent + " " + crazyValueUser + " " + getNewCrazyValue(crazyValueUser, crazyValueParent, true));
                } else {
                    parent.setProperty("votes", parentVotes - 1);
                    rating.setProperty("Votes", votes - 1);
                    int i = voters.indexOf(profileKeyStr);
                    voters.remove(i);
                    rating.setProperty("Voters", voters);
                    user.setProperty("crazyValue", getNewCrazyValue(crazyValueUser, crazyValueParent, false));
                    log.log(Level.INFO, crazyValueParent + " " + crazyValueUser + " " + getNewCrazyValue(crazyValueUser, crazyValueParent, false));
                }
                datastore.put(parent);
                datastore.put(rating);
                datastore.put(user);
                resp.getWriter().println("success");
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        } else if (voteChecker) {
            Key profileKey = KeyFactory.stringToKey(profileKeyStr);
            try {
                Entity profile = datastore.get(profileKey);
                resp.getWriter().println(profile.getProperty("votedJokes" + katego));
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private long getNewCrazyValue(long crazyValueUser, long crazyValueParent, boolean votedUp) {
        long crazyDiff = crazyValueParent - crazyValueUser;
        if (votedUp){
            return crazyValueUser + (crazyDiff/4);
        } else {
            return crazyValueUser - (crazyDiff/3);
        }
    }
}
