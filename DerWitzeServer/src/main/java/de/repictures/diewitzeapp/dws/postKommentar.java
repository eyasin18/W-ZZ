package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class postKommentar {
    public postKommentar(DatastoreService datastore, String key, String profileKey,
                         String inhalt, Boolean votedUp, Boolean voteChecker, HttpServletResponse resp) {

        Key commentKey = KeyFactory.stringToKey(key);
        if (profileKey != null && inhalt != null){
            try {
                Entity comment = new Entity("comment", commentKey);
                comment.setProperty("comment", inhalt);
                comment.setProperty("user", profileKey);
                comment.setProperty("key", key);
                comment.setProperty("votes", 0);
                List<String> voters = new ArrayList<>();
                voters.add("blub");
                comment.setProperty("Voters", voters);
                datastore.put(comment);
                Key commentKey2 = comment.getKey();
                comment = datastore.get(commentKey2);
                String commentKeyStr = KeyFactory.keyToString(commentKey2);
                comment.setProperty("commentKey", commentKeyStr);
                datastore.put(comment);
                resp.getWriter().println("success");
            } catch (IOException | EntityNotFoundException e) {
                e.printStackTrace();
            }
        } else if (votedUp != null && !voteChecker) {
            try {
                Entity comment = datastore.get(commentKey);
                long NVotes = (long) comment.getProperty("votes");
                if (votedUp) {
                    NVotes = NVotes + 1;
                } else {
                    NVotes = NVotes - 1;
                }
                comment.setProperty("votes", NVotes);
                datastore.put(comment);
                resp.getWriter().println(NVotes);
            } catch (EntityNotFoundException | IOException e){
                e.printStackTrace();
            }
        }
    }
}
