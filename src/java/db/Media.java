package db;
// Generated Feb 16, 2017 3:47:35 PM by Hibernate Tools 4.3.1



/**
 * Media generated by hbm2java
 */
public class Media  implements java.io.Serializable {


     private MediaId id;
     private Festival festival;
     private String uname;
     private String path;
     private int tip;
     private int odobrena;

    public Media() {
    }

    public Media(MediaId id, Festival festival, String uname, String path, int tip, int odobrena) {
       this.id = id;
       this.festival = festival;
       this.uname = uname;
       this.path = path;
       this.tip = tip;
       this.odobrena = odobrena;
    }
   
    public MediaId getId() {
        return this.id;
    }
    
    public void setId(MediaId id) {
        this.id = id;
    }
    public Festival getFestival() {
        return this.festival;
    }
    
    public void setFestival(Festival festival) {
        this.festival = festival;
    }
    public String getUname() {
        return this.uname;
    }
    
    public void setUname(String uname) {
        this.uname = uname;
    }
    public String getPath() {
        return this.path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    public int getTip() {
        return this.tip;
    }
    
    public void setTip(int tip) {
        this.tip = tip;
    }
    public int getOdobrena() {
        return this.odobrena;
    }
    
    public void setOdobrena(int odobrena) {
        this.odobrena = odobrena;
    }




}


