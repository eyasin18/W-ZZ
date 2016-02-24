package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

public class updateProfile {
    public updateProfile(DatastoreService datastore, String key, String user, String email,
                         String photoUrl, String coverUrl, String devise, HttpServletResponse resp) throws IOException {

        Key profileKey = KeyFactory.stringToKey(key);
        try {
            Entity profile = datastore.get(profileKey);
            profile.setProperty("name", URLDecoder.decode(user, "UTF-8"));
            profile.setProperty("email", URLDecoder.decode(email, "UTF-8"));
            profile.setProperty("photoUrl", photoUrl);
            profile.setProperty("coverUrl", coverUrl);
            if (devise != null)profile.setProperty("devise", URLDecoder.decode(devise, "UTF-8"));
            datastore.put(profile);
            resp.getWriter().println("success");
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

    }
}
