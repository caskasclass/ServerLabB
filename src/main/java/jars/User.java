package jars;

import java.io.Serializable;

/**
 *
 * @author lorenzo
 */
public class User implements Serializable {
    private String userid;
    private String name;
    private String surname;
    private String cf;
    private String address;
    private int cap;
    private String city;
    private String mail;
    private String psw;

    public User(String userid, String name, String surname, String cf, String address, int cap, String city, String mail, String psw) {
        this.userid = userid;
        this.name = name;
        this.surname = surname;
        this.cf = cf;
        this.address = address;
        this.cap = cap;
        this.city = city;
        this.mail = mail;
        this.psw = psw;
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCf() {
        return cf;
    }

    public String getAddress() {
        return address;
    }

    public int getCap() {
        return cap;
    }

    public String getCity() {
        return city;
    }

    public String getMail() {
        return mail;
    }

    public String getPsw() {
        return psw;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
    
    
    @Override
    public String toString() {
        String res = userid + "<%SEP>" + name + "<%SEP>" + surname + "<%SEP>" + cf + "<%SEP>" + address + "<%SEP>" + cap + "<%SEP>" + city + "<%SEP>" + mail + "<%SEP>" + psw + "<%SEP>";
        return res;
    }
}
