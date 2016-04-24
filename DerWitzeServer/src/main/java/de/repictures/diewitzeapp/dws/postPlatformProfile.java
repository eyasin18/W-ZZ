package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import de.repictures.diewitzeapp.dws.database.GetRandomName;

public class postPlatformProfile {
    public postPlatformProfile(DatastoreService datastore, String user, String email,
                               String photoUrl, String coverUrl, String devise, Boolean isEmail,
                               int platform, int count, String lang, Boolean female, String about, HttpServletResponse resp)
            throws IOException {
        Key personKey = KeyFactory.createKey("email", email);
        Query query = new Query("profile", personKey);
        List<Entity> profiles = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        if (profiles.size() > 0){
            Entity e = profiles.get(0);
            resp.getWriter().println(e.getProperty("key"));
        } else {
            String[] spam = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"};
            List<String> strings = Arrays.asList(spam);

            Key userKey = KeyFactory.createKey("email", email);
            Entity userProfile = new Entity("profile", userKey);

            userProfile.setProperty("name", user);
            userProfile.setProperty("email", email);
            userProfile.setProperty("photoUrl", photoUrl);
            userProfile.setProperty("coverUrl", coverUrl);
            userProfile.setProperty("platform", platform);
            userProfile.setProperty("devise", "Hi <3");
            userProfile.setProperty("crazyValue", count);
            userProfile.setProperty("isEmail", isEmail);
            userProfile.setProperty("level", 0);
            new GetRandomName(userProfile, lang, female, datastore);
            if (!about.equals("null"))userProfile.setProperty("about", about);
            else userProfile.setProperty("about", null);
            if (!devise.equals("null"))userProfile.setProperty("devise", devise);
            else userProfile.setProperty("devise", null);
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
