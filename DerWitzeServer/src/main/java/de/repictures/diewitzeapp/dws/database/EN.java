package de.repictures.diewitzeapp.dws.database;

import com.google.appengine.api.datastore.Entity;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EN {
    private final Boolean female;
    private String name;
    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
    final java.util.Random rand = new java.util.Random();
    final Set<String> identifiers = new HashSet<String>();

    public EN(Entity profile, Boolean female) {
        this.female = female;
        if (nothingLeft()) profile.setProperty("Visible Name", "Object Nr. " + computerName());
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

    public String[] names_f = {"Amelia", "Olivia", "Emily", "Ava", "Isla", "Jessica", "Poppy", "Isabella", "Sophie", "Mia", "Ruby", "Lily", "Grace", "Evie", "Sophia", "Ella", "Scarlett", "Chloe", "Isabelle", "Freya", "Charlotte", "Sienna", "Daisy", "Phoebe", "Millie", "Eva", "Alice", "Lucy", "Florence", "Sofia", "Layla", "Lola", "Holly", "Imogen", "Molly", "Matilda", "Lilly", "Rosie", "Elizabeth", "Erin", "Maisie", "Lexi", "Ellie", "Hannah", "Evelyn", "Abigail", "Elsie", "Summer", "Megan", "Jasmine", "Maya", "Amelie", "Lacey", "Willow", "Emma", "Bella", "Eleanor", "Esme", "Eliza", "Georgia", "Harriet", "Gracie", "Annabelle", "Emilia", "Amber", "Ivy", "Brooke", "Rose", "Anna", "Zara", "Leah", "Mollie", "Martha", "Faith", "Hollie", "Amy", "Bethany", "Violet", "Katie", "Maryam", "Francesca", "Julia", "Maria", "Darcey", "Isabel", "Tilly", "Maddison", "Victoria", "Isobel", "Niamh", "Skye", "Madison", "Darcy", "Aisha", "Beatrice", "Sarah", "Zoe", "Paige", "Lydia", "Sara"};
    public String[] names_m = {"Oliver", "Jack", "Harry", "Jacob", "Charlie", "Thomas", "Oscar", "William", "James", "George", "Alfie", "Joshua", "Noah", "Ethan", "Muhammad", "Alexander", "Max", "Lucas", "Mason", "Logan", "Isaac", "Benjamin", "Dylan", "Jake", "Edward", "Finley", "Freddie", "Harrison", "Tyler", "Sebastian", "Zachary", "Adam", "Theo", "Jayden", "Arthur", "Toby", "Luke", "Lewis", "Matthew", "Harvey", "Harley", "David", "Ryan", "Tommy", "Michael", "Reuben", "Nathan", "Blake", "Mohammad", "Jenson", "Bobby", "Luca", "Charles", "Frankie", "Dexter", "Kai", "Alex", "Connor", "Liam", "Jamie", "Elijah", "Stanley", "Louie", "Jude", "Callum", "Hugo", "Leon", "Elliot", "Louis", "Theodore", "Gabriel", "Ollie", "Aaron", "Frederick", "Evan", "Elliott", "Owen", "Teddy", "Finlay", "Caleb", "Ibrahim", "Ronnie", "Felix", "Aiden", "Cameron", "Austin", "Kian", "Rory", "Seth", "Robert", "Albert", "Sonny"};
    public Boolean[] used_f = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    public Boolean[] used_m = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
}
