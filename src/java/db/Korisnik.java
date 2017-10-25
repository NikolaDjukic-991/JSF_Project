package db;
// Generated Feb 16, 2017 3:47:35 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Korisnik generated by hbm2java
 */
public class Korisnik  implements java.io.Serializable {


     private String uname;
     private String lozinka;
     private String ime;
     private String prezime;
     private String email;
     private String telefon;
     private int prestupi;
     private int tip;
     private int ban;
     private Date lastLogin;
     private Set<Rezervacija> rezervacijas = new HashSet<Rezervacija>(0);
     private Set<Komentar> komentars = new HashSet<Komentar>(0);
     private Set<Poruka> porukas = new HashSet<Poruka>(0);

    public Korisnik() {
    }

	
    public Korisnik(String uname, String lozinka, String ime, String prezime, String email, String telefon, int prestupi, int tip, int ban, Date lastLogin) {
        this.uname = uname;
        this.lozinka = lozinka;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.telefon = telefon;
        this.prestupi = prestupi;
        this.tip = tip;
        this.ban = ban;
        this.lastLogin = lastLogin;
    }
    public Korisnik(String uname, String lozinka, String ime, String prezime, String email, String telefon, int prestupi, int tip, int ban, Date lastLogin, Set<Rezervacija> rezervacijas, Set<Komentar> komentars, Set<Poruka> porukas) {
       this.uname = uname;
       this.lozinka = lozinka;
       this.ime = ime;
       this.prezime = prezime;
       this.email = email;
       this.telefon = telefon;
       this.prestupi = prestupi;
       this.tip = tip;
       this.ban = ban;
       this.lastLogin = lastLogin;
       this.rezervacijas = rezervacijas;
       this.komentars = komentars;
       this.porukas = porukas;
    }
   
    public String getUname() {
        return this.uname;
    }
    
    public void setUname(String uname) {
        this.uname = uname;
    }
    public String getLozinka() {
        return this.lozinka;
    }
    
    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
    public String getIme() {
        return this.ime;
    }
    
    public void setIme(String ime) {
        this.ime = ime;
    }
    public String getPrezime() {
        return this.prezime;
    }
    
    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefon() {
        return this.telefon;
    }
    
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    public int getPrestupi() {
        return this.prestupi;
    }
    
    public void setPrestupi(int prestupi) {
        this.prestupi = prestupi;
    }
    public int getTip() {
        return this.tip;
    }
    
    public void setTip(int tip) {
        this.tip = tip;
    }
    public int getBan() {
        return this.ban;
    }
    
    public void setBan(int ban) {
        this.ban = ban;
    }
    public Date getLastLogin() {
        return this.lastLogin;
    }
    
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
    public Set<Rezervacija> getRezervacijas() {
        return this.rezervacijas;
    }
    
    public void setRezervacijas(Set<Rezervacija> rezervacijas) {
        this.rezervacijas = rezervacijas;
    }
    public Set<Komentar> getKomentars() {
        return this.komentars;
    }
    
    public void setKomentars(Set<Komentar> komentars) {
        this.komentars = komentars;
    }
    public Set<Poruka> getPorukas() {
        return this.porukas;
    }
    
    public void setPorukas(Set<Poruka> porukas) {
        this.porukas = porukas;
    }




}

