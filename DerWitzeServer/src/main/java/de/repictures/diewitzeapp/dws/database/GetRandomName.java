package de.repictures.diewitzeapp.dws.database;

import com.google.appengine.api.datastore.Entity;

import java.util.Locale;
import java.util.Objects;

public class GetRandomName {

    private final Entity profile;
    private final String lang;
    private Boolean female;

    public GetRandomName(Entity profile, String lang, Boolean female) {
        this.profile = profile;
        this.lang = lang;
        this.female = female;
        selector();
    }

    private void selector() {
        if (Objects.equals(lang, Locale.GERMAN.getDisplayLanguage())){
            new DE(profile, female);
        } else {
            new EN(profile, female);
        }
    }
}
