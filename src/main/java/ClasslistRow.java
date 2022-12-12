public class ClasslistRow {

    String sno;
    String surnameFirtsname;
    String fullname1;
    String fullname;
    String surname;
    String firstname;
    boolean attendance = false;

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
