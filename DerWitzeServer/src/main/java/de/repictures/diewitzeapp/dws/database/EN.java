package de.repictures.diewitzeapp.dws.database;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EN {
    private String name;
    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
    final java.util.Random rand = new java.util.Random();
    final Set<String> identifiers = new HashSet<String>();
    private DatastoreService datastore;
    List<Entity> names;

    public EN(Entity profile, Boolean female, DatastoreService datastore) {
        Key key = KeyFactory.createKey("language", "1");
        Query query;
        if (female) query = new Query("Female Names", key);
        else query = new Query("Male Names", key);
        names = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
        this.datastore = datastore;
        if (nothingLeft()) profile.setProperty("Visible Name", "Objekt Nr. " + computerName());
        else{
            selectName();
            profile.setProperty("Visible Name", name);
        }
    }

    private String computerName() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(4)+4;
            for(int i = 0; i < length; i++)
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            if(identifiers.contains(builder.toString()))
                builder = new StringBuilder();
        }
        return builder.toString();
    }



    private boolean nothingLeft() {
        for (Entity name : names){
            if (!(Boolean) name.getProperty("Vergeben")){
                return false;
            }
        }
        return true;
    }

    private void selectName() {
        int idx = new Random().nextInt(names.size());
        Entity name = names.get(idx);
        if (!(Boolean) name.getProperty("Vergeben")) {
            name.setProperty("Vergeben", true);
            datastore.put(name);
            this.name = (String) name.getProperty("Name");
        }
        else selectName();
    }
}
