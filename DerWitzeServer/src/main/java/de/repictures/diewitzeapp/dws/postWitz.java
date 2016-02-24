package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

public class postWitz {
    public postWitz(DatastoreService datastore, String email,
                    String userkey, String katego, String inhalt, String datumStr, String profilePic,
                    String devise, HttpServletResponse resp) throws IOException, EntityNotFoundException {

        Entity posting;
        Entity userPosting;

        if (inhalt != null) {
            String postKeyStr;
            Date date = new Date();
            try {
                date = (new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMANY)).parse(datumStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Entity user = datastore.get(KeyFactory.stringToKey(userkey));
            long crazyValue = (long) user.getProperty("crazyValue");

            Key postKey = KeyFactory.createKey("katego", katego);
            Key emailKey = KeyFactory.createKey("user", userkey);
            posting = new Entity("posting", postKey);
            userPosting = new Entity("userPosting", emailKey);

            posting.setProperty("datum", date);
            posting.setProperty("user", userkey);
            posting.setProperty("katego", katego);
            posting.setProperty("inhalt", inhalt);
            posting.setProperty("crazyValue", crazyValue);
            posting.setProperty("votes", 0);
            datastore.put(posting);
            Key personKey2 = posting.getKey();
            postKeyStr = KeyFactory.keyToString(personKey2);

            userPosting.setProperty("datum", date);
            userPosting.setProperty("user", userkey);
            userPosting.setProperty("katego", katego);
            userPosting.setProperty("inhalt", inhalt);
            datastore.put(userPosting);
            Key userKey2 = userPosting.getKey();
            String userKeyStr = KeyFactory.keyToString(userKey2);
            createVoting(postKeyStr, userKeyStr, posting, userPosting, "Post", datastore);

            userPosting = datastore.get(userKey2);
            posting = datastore.get(personKey2);
            posting.setProperty("key", postKeyStr);
            userPosting.setProperty("key", postKeyStr);
            datastore.put(posting);
            datastore.put(userPosting);

            Entity userE = datastore.get(KeyFactory.stringToKey(userkey));
            ArrayList<String> abos = (ArrayList<String>) userE.getProperty("abos");
            try {
                for (int i = 0; i < abos.size(); i++) {
                    Entity abbonnent = datastore.get(KeyFactory.stringToKey(abos.get(i)));
                    ArrayList<String> abobox = (ArrayList<String>) abbonnent.getProperty("AboBox");
                    try {
                        abobox.add(0, postKeyStr);
                    } catch (NullPointerException ignore) {
                        abobox = new ArrayList<>();
                        abobox.add(0, postKeyStr);
                    }
                    abbonnent.setProperty("AboBox", abobox);
                    datastore.put(abbonnent);
                }
            } catch (NullPointerException ignore){}
            resp.getWriter().println("success");
        }
    }

    private void createVoting(String postKeyStr, String userPostKeyStr, Entity entity, Entity userEntity,
                              String post, DatastoreService datastore) {
        Key voteKey = KeyFactory.createKey("voting", postKeyStr);
        Entity voteEntity = new Entity(post + "Voting", voteKey);
        voteEntity.setProperty("ParentKey", postKeyStr);
        voteEntity.setProperty("UserParentKey", userPostKeyStr);
        voteEntity.setProperty("Votes", 0);
        voteEntity.setProperty("Reports", 0);
        ArrayList<String> reasonsList = new ArrayList<>();
        reasonsList.add("Start");
        voteEntity.setProperty("Reasons", reasonsList);
        ArrayList<String> postingReporters = new ArrayList<>();
        postingReporters.add("Start");
        ArrayList<String> postingVoters = new ArrayList<>();
        postingVoters.add("Start");
        voteEntity.setProperty("Voters", postingVoters);
        voteEntity.setProperty("Reporters", postingReporters);
        datastore.put(voteEntity);
        Key voteKey2 = voteEntity.getKey();
        String voteKeyStr = KeyFactory.keyToString(voteKey2);
        try {
            voteEntity = datastore.get(voteKey2);
            voteEntity.setProperty("Key", voteKeyStr);
            entity.setProperty("VotingKey", voteKeyStr);
            userEntity.setProperty("VotingKey", voteKeyStr);
            datastore.put(voteEntity);
            datastore.put(entity);
            datastore.put(userEntity);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
