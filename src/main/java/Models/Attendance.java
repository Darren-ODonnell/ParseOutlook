package Models;

import lombok.Builder;

@Builder
public class Attendance implements Comparable<Attendance>{
    String studentNo;
    String studentName;
    int attendance;

    public String getStudentNo() {
        return studentNo;
    }
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }
    public int getAttendance() {
        return attendance;
    }
    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }
    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "studentNo='" + studentNo + '\'' +
                ", studentName='" + studentName + '\'' +
                ", attendance=" + attendance +
                '}';
    }

    @Override
    public int compareTo(Attendance o) {
        return this.getStudentName().compareTo(o.getStudentName());
    }
}
