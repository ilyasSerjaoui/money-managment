package atlas.app.moneyfine;

public class Account {
    String fname, lname, email, pass, db, mb, yb;

    private Account(){}

    public Account(String fname, String lname, String email, String pass, String db, String mb, String yb) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.pass = pass;
        this.db = db;
        this.mb = mb;
        this.yb = yb;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getMb() {
        return mb;
    }

    public void setMb(String mb) {
        this.mb = mb;
    }

    public String getYb() {
        return yb;
    }

    public void setYb(String yb) {
        this.yb = yb;
    }
}
