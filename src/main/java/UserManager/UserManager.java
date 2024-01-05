package UserManager;

import SQLBuilder.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import jars.User;

/**
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 * 
 * Classe che modella oggetti in grado di inserire e leggere dalla tabella
 * registrated_users.
 * 
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri , matricola 748695, VA
 * @author Filippo Storti , matricola 749195, VA
 * @author Nazar Viytyuk, matricola 748964, VA
 * @version 1.0
 */
public class UserManager implements UserManagerInterface {

    private SQLFinder sqlfinder; // Oggetto in grado di cercare nella tabella
    private SQLInserter sqlinserter; // Oggetto in grado di inserire nella tabella
    private String[] searchCriteria; // Criteri di ricerca
    private static final int KEY = 5; // Chiave per la crittazione della password

    /**
     * Costruttore per l'accesso.
     * 
     * @param strAccess Nome utente o indirizzo email per l'accesso
     * @param psw       Password per l'accesso
     */
    public UserManager(String strAccess, String psw) {
        this.sqlfinder = new SQLFinder();
        this.sqlinserter = null;
        this.searchCriteria = new String[2]; // Array con due elementi che contiene username o mail e password
        this.searchCriteria[0] = strAccess;
        this.searchCriteria[1] = psw;
    }

    /**
     * Costruttore per la registrazione.
     */
    public UserManager() {
        this.sqlfinder = new SQLFinder();
        this.sqlinserter = new SQLInserter();
        this.searchCriteria = null;
    }

    /**
     * Metodo per crittazione e decrittazione della password.
     * 
     * @param s   Password da crittare o decrittare
     * @param key Chiave per la crittazione
     * @return Stringa crittata o decrittata
     */
    private static String encrypt(String s, int key) {
        StringBuilder res = new StringBuilder();
        // Ciclo che critta lettera per lettera
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            if (!Character.isLetter(current)) { // Se non è una lettera viene saltata
                res.append(current);
                continue;
            }
            char cChar = (char) (current + key); // Crittazione
            // Se si ha fatto il ciclo completo dell'alfabeto, si torna alla A
            if (Character.isLowerCase(current) && cChar > 'z') {
                cChar = (char) (cChar - 26);
            } else if (Character.isUpperCase(current) && cChar > 'Z') {
                cChar = (char) (cChar - 26);
            }
            res.append(cChar); // Aggiunta del carattere crittato
        }
        return res.toString(); // Ritorno del risultato
    }

    /**
     * Metodo per la registrazione di un utente.
     * 
     * @param u Oggetto User contenente le informazioni dell'utente da registrare
     */
    @Override
    public void registration(User u) {
        // Controllo che lo userid non sia già stato preso
        this.sqlinserter.renewQuery();
        this.sqlfinder.renewResultSet();
        // Costruzione delle liste che contengono i nomi delle colonne ed i loro valori
        ArrayList<String> column = new ArrayList<String>();
        System.out.println("funziona2");
        column.add("userid");
        column.add("name");
        column.add("surname");
        column.add("cf");
        column.add("address");
        column.add("cap");
        column.add("city");
        column.add("mail");
        column.add("password");
        System.out.println("funziona3");
        ArrayList<String> values = new ArrayList<String>();
        System.out.println("funziona4");
        values.add(u.getUserid());
        values.add(u.getName());
        values.add(u.getSurname());
        values.add(u.getCf());
        values.add(u.getAddress());
        values.add("" + u.getCap());
        values.add(u.getCity());
        values.add(u.getMail());
        values.add(encrypt(u.getPsw(), KEY)); // La password viene crittata prima di essere aggiunta
        // Settaggio delle colonne e dei valori
        this.sqlinserter.setColums(column);
        System.out.println("funziona6");
        this.sqlinserter.setValues(values);
        this.sqlinserter.setQuery("registrated_users"); // Settaggio della query
        this.sqlinserter.executeQuery(); // Esecuzione della query
    }

    /**
     * Metodo per l'accesso di un utente.
     * 
     * @return Oggetto User contenente le informazioni dell'utente se l'accesso è
     *         riuscito, altrimenti null
     */
    @Override
    public User access() {
        this.sqlfinder.renewQuery(); // Rinnovamento della query
        this.sqlfinder.renewResultSet(); // Rinnovamento dei risultati
        User u = null;
        // Se la stringa di accesso non contiene la @ si cerca per username, altrimenti
        // per mail
        if (this.searchCriteria[0].contains("@")) {
            // Costruzione della query
            this.sqlfinder.setQuery("*", "registrated_users", "mail = '" + this.searchCriteria[0] + "'");
        } else {
            // Costruzione della query
            this.sqlfinder.setQuery("*", "registrated_users", "userid = '" + this.searchCriteria[0] + "'");
        }
        this.sqlfinder.executeQuery(); // Esecuzione della query
        try {
            while (this.sqlfinder.getRes().next()) { // Cicla finchè ci sono risultati
                String cPassword = this.sqlfinder.getRes().getString("password"); // Presa della password
                // Se la password inserita corrisponde a quella trovata decrittata, si procede
                // con la presa degli altri valori
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

    /**
     * Metodo di utilità per la creazione di una password casuale.
     * 
     * @param length Lunghezza della password
     * @return Stringa contenente la password generata
     */
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

    /**
     * Metodo usato in fase di registrazione per controllare se l'utente è già
     * esistente.
     * 
     * @return ArrayList contenente gli userid degli utenti esistenti
     */
    @Override
    public ArrayList<String> findexExistingUsers() {
        try {
            this.sqlfinder.renewQuery(); // Rinnovamento della query
            this.sqlfinder.renewResultSet(); // Rinnovamento del resultset
            this.sqlfinder.setQuery("userid", "registrated_users");
            this.sqlfinder.executeQuery();
            ArrayList<String> res = new ArrayList<String>();
            while (this.sqlfinder.getRes().next()) {
                res.add(this.sqlfinder.getRes().getString("userid"));
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

}

