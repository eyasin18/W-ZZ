package de.repictures.diewitzeapp.dws.database;

import com.google.appengine.api.datastore.Entity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DE {

    private String name;
    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
    final java.util.Random rand = new java.util.Random();
    final Set<String> identifiers = new HashSet<String>();
    private Boolean female;

    public DE(Entity profile, Boolean female) {
        this.female = female;
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
        for (Boolean anUsed : used()) {
            if (!anUsed) return false;
        }
        return true;
    }

    private void selectName() {
        int idx = new Random().nextInt(used().length);
        if (!used()[idx]) {
            used()[idx] = true;
            name = names()[idx];
        }
        else selectName();
    }

    private String[] names(){
        if (female) return names_f;
        else return names_m;
    }

    private Boolean[] used(){
        if (female) return used_f;
        else return used_m;
    }

    public String[] names_f = {"Anna", "Laura", "Mila", "Julia", "Emilia", "Lea", "Vanessa", "Sarah", "Lena", "Amelie", "Elena", "Leonie", "Nina", "Lara", "Alina", "Lisa", "Mia", "Emma", "Sophie", "Jana", "Marie", "Luisa", "Jasmin", "Emily", "Juna", "Melina", "Selina", "Noah", "Hannah", "Ava", "Anouk", "Mina", "Valentina", "Franziska", "Johanna", "Michelle", "Jessica", "Melanie", "Sandra", "Mara", "Nele", "Sophia", "Katharina", "Pia", "Mira", "Isabella", "Luana", "Nora", "Antonia"};
    public String[] names_m = {"Liam", "Milan", "Elias", "Julian", "Jonas", "Linus", "Alexander", "Daniel", "Jan", "Samuel", "David", "Tim", "Lukas", "Andre", "Leon", "Thomas", "Marcel", "Patrick", "Emil", "Valentin", "Tobias", "Joris", "Moritz", "Simon", "Florian", "Andreas", "Paul", "Sebastian", "Finn", "Felix", "Damian", "Joshua", "Theo", "Ben", "Fabian", "Niklas", "Levin", "Max", "Stefan", "Oskar", "Benjamin", "Aaron", "Manuel", "Markus", "Raphael", "Christian", "Robin", "Philipp", "Timo"};
    public Boolean[] used_m = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    public Boolean[] used_f = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
}
