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
                         String photoUrl, String coverUrl, String devise, String inhalt, String katego, HttpServletResponse resp) throws IOException {

        Key profileKey = KeyFactory.stringToKey(key);
        try {
            Entity profile = datastore.get(profileKey);
            profile.setProperty("name", URLDecoder.decode(user, "UTF-8"));
            profile.setProperty("email", URLDecoder.decode(email, "UTF-8"));
            profile.setProperty("photoUrl", photoUrl);
            profile.setProperty("coverUrl", coverUrl);
            if (inhalt.length() > 4)profile.setProperty("about", URLDecoder.decode(inhalt, "UTF-8"));
            if (devise.length() > 4)profile.setProperty("devise", URLDecoder.decode(devise, "UTF-8"));
            if (katego.length() > 4)profile.setProperty("Visible Name", URLDecoder.decode(katego, "UTF-8"));
            datastore.put(profile);
            resp.getWriter().println("success");
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

    }
}
