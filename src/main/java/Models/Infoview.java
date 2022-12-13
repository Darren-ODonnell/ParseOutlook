package Models;

import lombok.Builder;

@Builder
public class Infoview {

    private String classRollNo;
    private String studentNo;
    private String address;
    private String email;
    private String regStatus;
    private String surnameFirstname;
    private String surname;
    private String firstname;
    private String fullnameMin;
    private String fullnameMax;

    public Infoview() {
    }

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

    public String getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(String regStatus) {
        this.regStatus = regStatus;
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
}
