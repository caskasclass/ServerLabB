package Server;

/**
 *
 * @author lorenzo
 */
public class User {
    private String userid;
    private String nome;
    private String cognome;
    private String cf;
    private String indirizzo;
    private int cap;
    private String città;
    private String mail;
    private String psw;

    public User(String userid, String nome, String cognome, String cf, String indirizzo, int cap, String città, String mail, String psw) {
        this.userid = userid;
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.indirizzo = indirizzo;
        this.cap = cap;
        this.città = città;
        this.mail = mail;
        this.psw = psw;
    }

    public String getUserid() {
        return userid;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCf() {
        return cf;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public int getCap() {
        return cap;
    }

    public String getCittà() {
        return città;
    }

    public String getMail() {
        return mail;
    }

    public String getPsw() {
        return psw;
    }
    
    public String toString() {
        String res = userid + "<SEP>" + nome + "<SEP>" + cognome + "<SEP>" + cf + "<SEP>" + indirizzo + "<SEP>" + cap + "<SEP>" + città + "<SEP>" + mail + "<SEP>" + psw + "<SEP>";
        return res;
    }
}
