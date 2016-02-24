package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

public class abo {
    public abo(DatastoreService datastore, String userKeyStr, String aboKeyStr, Boolean abo, HttpServletResponse resp) throws EntityNotFoundException, IOException {
        Entity aboUser = datastore.get(KeyFactory.stringToKey(aboKeyStr));
        Entity user = datastore.get(KeyFactory.stringToKey(userKeyStr));
        ArrayList<String> abos;
        ArrayList<String> subs;
        if (abo){
            abos = (ArrayList<String>) user.getProperty("abos");
            try {
                abos.add(aboKeyStr);
            } catch (NullPointerException e){
                abos = new ArrayList<>();
                abos.add(aboKeyStr);
            }
            user.setProperty("abos", abos);
            subs = (ArrayList<String>) aboUser.getProperty("subs");
            try {
                subs.add(userKeyStr);
            } catch (NullPointerException e){
                subs = new ArrayList<>();
                subs.add(userKeyStr);
            }
            aboUser.setProperty("subs", subs);
        } else {
            try {
                abos = (ArrayList<String>) user.getProperty("abos");
                abos.remove(abos.indexOf(aboKeyStr));
                user.setProperty("abos", abos);
                subs = (ArrayList<String>) aboUser.getProperty("subs");
                subs.remove(subs.indexOf(userKeyStr));
                aboUser.setProperty("subs", subs);
            } catch (NullPointerException e){
                resp.getWriter().println("error");
            }
        }
        datastore.put(user);
        datastore.put(aboUser);
        resp.getWriter().println("success");
    }
}
