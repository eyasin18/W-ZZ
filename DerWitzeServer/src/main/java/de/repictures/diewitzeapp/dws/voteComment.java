package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

public class voteComment {
    public voteComment(DatastoreService datastore, String key, String profileKeyStr, Boolean votedUp, HttpServletResponse resp) throws IOException {

        if (votedUp != null) {
            try {
                Entity parent = datastore.get(KeyFactory.stringToKey(key));
                long parentVotes = (long) parent.getProperty("votes");
                ArrayList<String> voters = (ArrayList<String>) parent.getProperty("Voters");
                if (votedUp){
                    parent.setProperty("votes", parentVotes + 1);
                    voters.add(profileKeyStr);
                    parent.setProperty("Voters", voters);
                } else {
                    parent.setProperty("votes", parentVotes - 1);
                    int i = voters.indexOf(profileKeyStr);
                    voters.remove(i);
                    parent.setProperty("Voters", voters);
                }
                datastore.put(parent);
                resp.getWriter().println("success");
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
}
