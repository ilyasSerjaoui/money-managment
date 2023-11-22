package atlas.app.moneyfine;

public class Datatype {
    String c1, c2, c3, email;

    private Datatype(){}

    public Datatype(String c1, String c2, String c3, String email) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public String getC3() {
        return c3;
    }

    public void setC3(String c3) {
        this.c3 = c3;
    }
}
