package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.com.google.io.protocol.proto.RPC_ServiceDescriptor;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletResponse;

public class addName {
    public addName(String inputStr, DatastoreService datastore, int lang, HttpServletResponse resp) throws IOException {
        String[] input = URLDecoder.decode(inputStr, "UTF-8").split("<we/>");
        String[] names = input[0].split("~");
        Boolean gender = Boolean.parseBoolean(input[1]);
        Key key = KeyFactory.createKey("language", String.valueOf(lang));
        if (gender) {
            for (String nameStr : names){
                Entity name = new Entity("Female Names", key);
                name.setProperty("Name", nameStr);
                name.setProperty("Vergeben", false);
                name.setProperty("Sprache", getLang(lang));
                datastore.put(name);
                resp.getWriter().println("true");
            }
        } else {
            for (String nameStr : names){
                Entity name = new Entity("Male Names", key);
                name.setProperty("Name", nameStr);
                name.setProperty("Vergeben", false);
                name.setProperty("Sprache", getLang(lang));
                datastore.put(name);
                resp.getWriter().println("true");
            }
        }

    }

    private String getLang(int lang) {
        switch (lang){
            case 0:
                return "Deutsch";
            case 1:
                return "Englisch";
        }
        return "Fehler!";
    }
}
