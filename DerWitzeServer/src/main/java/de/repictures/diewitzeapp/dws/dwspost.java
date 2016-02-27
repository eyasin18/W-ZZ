package de.repictures.diewitzeapp.dws;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class dwspost extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String numberStr = req.getParameter("number");
        String getCountStr = req.getParameter("count");
        String katego = req.getParameter("katego");
        String datumStr = req.getParameter("datum");
        String user = req.getParameter("user");
        String email = req.getParameter("email");
        String inhalt = req.getParameter("inhalt");
        String key = req.getParameter("key");
        String profileKey = req.getParameter("profileKey");
        String votedUpStr = req.getParameter("votedUp");
        String sortVotesStr = req.getParameter("sortVotes");
        String isEmailStr = req.getParameter("isEmail");
        String photoUrl = req.getParameter("photoUrl");
        String coverUrl = req.getParameter("coverUrl");
        String voteCheckerStr = req.getParameter("voteChecker");
        String devise = req.getParameter("devise");
        String platformStr = req.getParameter("platform");

        int platform;
        if (platformStr != null) platform = Integer.parseInt(platformStr);
        else platform = 0;
        Boolean isEmail;
        if (isEmailStr != null) isEmail = Boolean.parseBoolean(isEmailStr);
        else isEmail = null;
        Boolean sortVotes;
        if (sortVotesStr != null) sortVotes = Boolean.parseBoolean(sortVotesStr);
        else sortVotes = null;
        Boolean votedUp;
        if (votedUpStr != null) votedUp = Boolean.parseBoolean(votedUpStr);
        else votedUp = null;
        Boolean voteChecker;
        if (voteCheckerStr != null) voteChecker = Boolean.parseBoolean(voteCheckerStr);
        else voteChecker = null;
        int number = 0;
        if (numberStr != null) number = Integer.parseInt(numberStr);
        int count = 0;
        if (getCountStr != null) count = Integer.parseInt(getCountStr);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        switch (number) {
            case 0: //Witz nach Kategorie ausgeben
                Key postKey = KeyFactory.createKey("katego", katego);
                if (sortVotes != null && sortVotes){
                    try {
                        new WitzeNachKategoBeliebteste(count, datastore, postKey, profileKey, resp);
                    } catch (EntityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (sortVotes != null && !sortVotes){
                    try {
                        new WitzeNachKategoNew(count, datastore, postKey, profileKey, resp);
                    } catch (EntityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        new WitzeNachKategoHeute(count, datastore, postKey, profileKey, resp);
                    } catch (EntityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1: //Witz nach Email ausgeben
                Key emailKey = KeyFactory.createKey("user", user);
                try {
                    new WitzeNachUser(user, datastore, emailKey, resp);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 2: //Witz hinzufügen & voten
                try {
                    new postWitz(datastore, email, user,  katego, inhalt, datumStr,
                            photoUrl, devise, resp);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 3: //Kommentare ausgeben
                Key commentKey = KeyFactory.stringToKey(key);
                try {
                    new getKommentare(datastore, commentKey, profileKey, resp);
                } catch (EntityNotFoundException e) {
                    resp.getWriter().println(e);
                    e.printStackTrace();
                }
                break;
            case 4: //Kommentar hinzufügen & voten
                if (key != null) new postKommentar(datastore, key, profileKey, inhalt, votedUp, voteChecker, resp);
                break;
            case 5: //Unterkommentar hinzufügen
                break;
            case 6: //Profil ausgeben
                new getProfile(datastore, key, email, resp);
                break;
            case 7: //Profil hinzufügen
                new postPlatformProfile(datastore, user, email, photoUrl, coverUrl, devise, isEmail, platform, count, resp);
                break;
            case 8: //Witz melden
                new postReport(datastore, key, profileKey, inhalt, resp);
                break;
            case 9: //Witz liken
                new likeJoke(datastore, key, profileKey, katego, votedUp, voteChecker, resp);
                break;
            case 10: //Rating ausgeben
                new getRating(datastore, key, resp);
                break;
            case 11: //Profil aktualisieren
                new updateProfile(datastore, key, user, email, photoUrl, coverUrl, devise, resp);
                break;
            case 12: //Profil + Witze ausgeben
                try {
                    new getProfileExtended(datastore, user, profileKey, resp);
                } catch (EntityNotFoundException e) {
                    resp.getWriter().println(e);
                    e.printStackTrace();
                }
                break;
            case 13: //Neuer Witzeausgeber
                Key postKey1 = KeyFactory.createKey("katego", katego);
                try {
                    new WitzeNachKategoAlle(datastore, postKey1, profileKey, resp);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 14: //Kommentar liken
                new voteComment(datastore, key, profileKey, votedUp, resp);
                break;
            case 15: //Abbonieren
                try {
                    new abo(datastore, profileKey, key, votedUp, resp);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 16:
                try {
                    new wizzByKey(datastore, key, resp);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 17:
                Key personKey = KeyFactory.createKey("email", email);
                Query query = new Query("profile", personKey);
                List<Entity> profiles = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
                if (profiles.size() > 0){
                    Entity e = profiles.get(0);
                    resp.getWriter().println(e.getProperty("key"));
                } else {
                    resp.getWriter().println("false");
                }
                break;
        }
    }
}
