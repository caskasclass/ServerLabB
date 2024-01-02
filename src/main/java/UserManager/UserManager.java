package UserManager;

import SQLBuilder.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import jars.User;

/**
 *
 * @author lorenzo
 */

/*
 * Classe che modella oggetti in grado di inserire e leggere dalla tabella registrated_users
 */
public class UserManager implements UserManagerInterface {

    private SQLFinder sqlfinder; //oggetto in grado di cercare nella tabella
    private SQLInserter sqlinserter; //oggetto in grado inserire nella tabella
    private String[] searchCriteria; //criteri di ricerca
    private static final int KEY = 5; //chiave per la crittazione della password

    //costruttore in caso di accesso
    public UserManager(String strAccess, String psw) {
        this.sqlfinder = new SQLFinder();
        this.sqlinserter = null;
        this.searchCriteria = new String[2]; //array con due elementi che contiene username o mail e password
        this.searchCriteria[0] = strAccess;
        this.searchCriteria[1] = psw;
    }

    //costruttore nel caso di registrazione
    public UserManager() {
        this.sqlfinder = null;
        this.sqlinserter = new SQLInserter();
        this.searchCriteria = null;
    }

    //metodo per crittazione e decrittazione della password
    private static String encrypt(String s, int key) { //riceve password e chiave
        StringBuilder res = new StringBuilder();
        //ciclo che critta lettera per lettera
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            if (!Character.isLetter(current)) { //se non è una lettera viene saltata
                res.append(current);
                continue;
            }
            char cChar = (char) (current + key); //crittazione
            //se si ha fatto il ciclo completo dell'alfabeto, si torna alla A
            if (Character.isLowerCase(current) && cChar > 'z') {
                cChar = (char) (cChar - 26);
            } else if (Character.isUpperCase(current) && cChar > 'Z') {
                cChar = (char) (cChar - 26);
            }
            res.append(cChar); //aggiunta del carattere crittato
        }
        return res.toString(); //ritorno del risultato
    }

    //metodo per la registrazione
    @Override
    public void registration(User u) { //viene passato un utente
        this.sqlinserter.renewQuery();
        //costruzione delle liste che contengono i nomi delle colonne ed i loro valori
        ArrayList<String> column = new ArrayList<String>();
        column.add("userid");
        column.add("name");
        column.add("surname");
        column.add("cf");
        column.add("address");
        column.add("cap");
        column.add("city");
        column.add("mail");
        column.add("password");
        ArrayList<String> values = new ArrayList<String>();
        values.add(u.getUserid());
        values.add(u.getName());
        values.add(u.getSurname());
        values.add(u.getCf());
        values.add(u.getAddress());
        values.add("" + u.getCap());
        values.add(u.getCity());
        values.add(u.getMail());
        values.add(encrypt(u.getPsw(), KEY)); //la password viene crittata prima di essere aggiunta
        //settaggio delle colonne e dei valori
        this.sqlinserter.setColums(column);
        this.sqlinserter.setValues(values);
        this.sqlinserter.setQuery("registrated_users"); //settaggio della query
        this.sqlinserter.executeQuery(); //esecuzione della query
    }

    //metodo per l'accesso di un utente
    @Override
    public User access() {
        this.sqlfinder.renewQuery(); //rinnovamento della query
        this.sqlfinder.renewResultSet(); //rinnovamento dei risultati
        User u = null;
        //se la stringa di accesso non contiene la @ si cerca per username, altrimenti per mail
        if (this.searchCriteria[0].contains("@")) {
            //costruzione della query
            this.sqlfinder.setQuery("*", "registrated_users", "mail = '" + this.searchCriteria[0] + "'");
        } else {
            //costruzione della query
            this.sqlfinder.setQuery("*", "registrated_users", "userid = '" + this.searchCriteria[0] + "'");
        }
        this.sqlfinder.executeQuery(); //esecuzione della query
        try {
            while (this.sqlfinder.getRes().next()) { //cicla finchè ci sono risultati
                String cPassword = this.sqlfinder.getRes().getString("password"); //presa della password
                //se la password inserita corrisponde a quella trovata decrittata, si procede con la presa degli altri valori
                if (this.searchCriteria[1].equals(encrypt(cPassword, 26 - KEY))) {
                    String userid = this.sqlfinder.getRes().getString("userid");
                    String nome = this.sqlfinder.getRes().getString("name");
                    String cognome = this.sqlfinder.getRes().getString("surname");
                    String cf = this.sqlfinder.getRes().getString("cf");
                    String indirizzo = this.sqlfinder.getRes().getString("address");
                    int cap = this.sqlfinder.getRes().getInt("cap");
                    String citta = this.sqlfinder.getRes().getString("city");
                    String mail = this.sqlfinder.getRes().getString("mail");
                    u = new User(userid, nome, cognome, cf, indirizzo, cap, citta, mail, this.searchCriteria[1]); 
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return u;
    }
    
    //metodo di utilità per la creazione di una password casuale
    public static String pswGenerator(int length) {
        StringBuilder res = new StringBuilder();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        alphabet += alphabet.toUpperCase();
        alphabet += "0123456789";
        Random rn = new Random();
        for (int i = 0; i < length; i++) {
            int randomInt = rn.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(randomInt);
            res.append(randomChar);
        }
        return res.toString();
    } 
}
