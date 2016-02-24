package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class postProfile {
    public postProfile(DatastoreService datastore, String user, String email,
                       String id, String devise, String password, int platform, HttpServletResponse resp) throws IOException {
        if (user != null && email != null && id != null){

            Key personKey = KeyFactory.createKey("email", email);
            Query query = new Query("profile", personKey);
            List<Entity> profiles = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
            if (profiles.size() > 0){
                resp.getWriter().println("null");
            } else {
                String[] spam = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
                List<String> strings = Arrays.asList(spam);

                Key userKey = KeyFactory.createKey("email", email);
                Entity userProfile = new Entity("profile", userKey);

                userProfile.setProperty("name", user);
                userProfile.setProperty("email", email);
                userProfile.setProperty("password", password);
                userProfile.setProperty("id", id);
                userProfile.setProperty("platform", platform);
                userProfile.setProperty("devise", devise);
                ArrayList<String> voted = new ArrayList<>();
                voted.add("blub");

                for (int i = 0; i < strings.size(); i += 1) {
                    userProfile.setProperty("votedJokes" + strings.get(i), voted);
                }
                datastore.put(userProfile);

                Key userKey2 = userProfile.getKey();
                String postKeyStr = KeyFactory.keyToString(userKey2);
                try {
                    userProfile = datastore.get(userKey2);
                    userProfile.setProperty("key", postKeyStr);
                    datastore.put(userProfile);
                    try {
                        resp.getWriter().println(postKeyStr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
