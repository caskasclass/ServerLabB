package UserManager;

import SQLBuilder.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import pkg.User;

/**
 *
 * @author lorenzo
 */
public class UserManager implements UserManagerInterface {

    private SQLFinder sqlfinder;
    private SQLInserter sqlinserter;
    private String[] searchCriteria;
    private static final int KEY = 5;

    public UserManager(String strAccess, String psw) {
        this.sqlfinder = new SQLFinder();
        this.sqlinserter = null;
        this.searchCriteria = new String[2];
        this.searchCriteria[0] = strAccess;
        this.searchCriteria[1] = psw;
    }

    public UserManager() {
        this.sqlfinder = null;
        //this.sqlinserter = new SQLInserter();
        this.searchCriteria = null;
    }

    private static String encrypt(String s, int key) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            if (!Character.isLetter(current)) {
                res.append(current);
                continue;
            }
            char cChar = (char) (current + key);
            if (Character.isLowerCase(current) && cChar > 'z') {
                cChar = (char) (cChar - 26);
            } else if (Character.isUpperCase(current) && cChar > 'Z') {
                cChar = (char) (cChar - 26);
            }
            res.append(cChar);
        }
        return res.toString();
    }

    @Override
    public void registration(User u) {
        this.sqlinserter.renewQuery();
        ArrayList<String> column = new ArrayList<String>();
        column.add("userid");
        column.add("nome");
        column.add("cognome");
        column.add("cf");
        column.add("indirizzo");
        column.add("cap");
        column.add("città");
        column.add("mail");
        column.add("password");
        ArrayList<String> values = new ArrayList<String>();
        values.add(u.getUserid());
        values.add(u.getNome());
        values.add(u.getCognome());
        values.add(u.getCf());
        values.add(u.getIndirizzo());
        values.add("" + u.getCap());
        values.add(u.getCittà());
        values.add(u.getMail());
        values.add(encrypt(u.getPsw(), KEY));
        //this.sqlinserter.setQuery("utenti_registrati", column, values);
        this.sqlinserter.executeQuery();
    }

    @Override
    public User access() {
        this.sqlfinder.renewQuery();
        this.sqlfinder.renewResultSet();
        User u = null;
        if (this.searchCriteria[0].contains("@")) {
            this.sqlfinder.setQuery("*", "utenti_registrati", "mail = '" + this.searchCriteria[0] + "'");
        } else {
            this.sqlfinder.setQuery("*", "utenti_registrati", "userid = '" + this.searchCriteria[0] + "'");
        }
        this.sqlfinder.executeQuery();
        try {
            while (this.sqlfinder.getRes().next()) {
                String cPassword = this.sqlfinder.getRes().getString("password");
                if (this.searchCriteria[1].equals(encrypt(cPassword, 26 - KEY))) {
                    String userid = this.sqlfinder.getRes().getString("userid");
                    String nome = this.sqlfinder.getRes().getString("nome");
                    String cognome = this.sqlfinder.getRes().getString("cognome");
                    String cf = this.sqlfinder.getRes().getString("cf");
                    String indirizzo = this.sqlfinder.getRes().getString("indirizzo");
                    int cap = this.sqlfinder.getRes().getInt("cap");
                    String città = this.sqlfinder.getRes().getString("città");
                    String mail = this.sqlfinder.getRes().getString("mail");
                    u = new User(userid, nome, cognome, cf, indirizzo, cap, città, mail, this.searchCriteria[1]); 
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return u;
    }
    
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
