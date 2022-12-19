public class ClasslistRow {

    private String sno;
    private String surnameFirtsname;
    private String fullname1;
    private String fullname;
    private String surname;
    private String firstname;
    private Integer attendance = 0;


    public String getSno() {
        return sno;
    }

    public void setSno(String sno) { this.sno = sno.toLowerCase(); }

    public String getSurnameFirtsname() {
        return surnameFirtsname;
    }

    public void setSurnameFirtsname(String surnameFirtsname) {
        this.surnameFirtsname = surnameFirtsname;
    }

    public String getFullname1() {
        return fullname1;
    }

    public void setFullname1(String fullname1) {
        this.fullname1 = fullname1;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Integer getAttendance() {
        return attendance;
    }

    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }

    @Override
    public String toString() {
        return "ClasslistRow{" +
                "sno='" + sno + '\'' +
                ", surnameFirtsname='" + surnameFirtsname + '\'' +
                ", fullname1='" + fullname1 + '\'' +
                ", fullname='" + fullname + '\'' +
                ", surname='" + surname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", attendance=" + attendance +
                '}';
    }
}
