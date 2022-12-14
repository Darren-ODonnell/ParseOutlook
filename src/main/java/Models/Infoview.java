package Models;

import lombok.Builder;

@Builder
public class Infoview {

    // key fullname -> to be able to locate

    private String classRollNo; //0
    private String studentNo; //3
    private String address; //4
    private String email; //5
    private String surnameFirstname; //8
    private String surname; //9
    private String firstname; //10
    private String fullnameMin; //11
    private String fullnameMax; //12

    public String getClassRollNo() {
        return classRollNo;
    }
    public void setClassRollNo(String classRollNo) {
        this.classRollNo = classRollNo;
    }
    public String getStudentNo() {
        return studentNo;
    }
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSurnameFirstname() {
        return surnameFirstname;
    }
    public void setSurnameFirstname(String surnameFirstname) {
        this.surnameFirstname = surnameFirstname;
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
    public String getFullnameMin() {
        return fullnameMin;
    }
    public void setFullnameMin(String fullnameMin) {
        this.fullnameMin = fullnameMin;
    }
    public String getFullnameMax() {
        return fullnameMax;
    }
    public void setFullnameMax(String fullnameMax) {
        this.fullnameMax = fullnameMax;
    }

    @Override
    public String toString() {
        return "Infoview{" +
                "classRollNo='" + classRollNo + '\'' +
                ", studentNo='" + studentNo + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", surnameFirstname='" + surnameFirstname + '\'' +
                ", surname='" + surname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", fullnameMin='" + fullnameMin + '\'' +
                ", fullnameMax='" + fullnameMax + '\'' +
                '}';
    }
}
