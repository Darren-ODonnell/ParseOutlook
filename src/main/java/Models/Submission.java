package Models;

import lombok.Builder;
import java.sql.Time;
import java.sql.Date;
import java.util.List;

@Builder
public class Submission implements Comparable<Submission> {
    List<String> files;
    String studentName;
    String studentNo;
    int type       ;
    @Builder.Default
    boolean xlsxSubmitted = false;
    @Builder.Default
    boolean savSubmitted = false;
    @Builder.Default
    boolean spvSubmitted = false;
    @Builder.Default
    boolean snoXlsx      = false;
    @Builder.Default
    boolean snoSav       = false;
    @Builder.Default
    boolean snoSpv       = false;
    @Builder.Default
    int qtyFiles         = 0;
    @Builder.Default
    int qtyEmails        = 0;

    Date date;
    Time time;

    public List<String> getFiles() {
        return files;
    }
    public void setFiles(List<String> files) {
        this.files = files;
    }
    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String getStudentNo() {
        return studentNo;
    }
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public boolean isXlsxSubmitted() {
        return xlsxSubmitted;
    }
    public void setXlsxSubmitted(boolean xlsxSubmitted) {
        this.xlsxSubmitted = xlsxSubmitted;
    }
    public boolean isSavSubmitted() {
        return savSubmitted;
    }
    public void setSavSubmitted(boolean savSubmitted) {
        this.savSubmitted = savSubmitted;
    }
    public boolean isSpvSubmitted() {
        return spvSubmitted;
    }
    public void setSpvSubmitted(boolean spvSubmitted) {
        this.spvSubmitted = spvSubmitted;
    }
    public boolean isSnoXlsx() {
        return snoXlsx;
    }
    public void setSnoXlsx(boolean snoXlsx) {
        this.snoXlsx = snoXlsx;
    }
    public boolean isSnoSav() {
        return snoSav;
    }
    public void setSnoSav(boolean snoSav) {
        this.snoSav = snoSav;
    }
    public boolean isSnoSpv() {
        return snoSpv;
    }
    public void setSnoSpv(boolean snoSpv) {
        this.snoSpv = snoSpv;
    }
    public int getQtyFiles() {
        return qtyFiles;
    }
    public void setQtyFiles(int qtyFiles) {
        this.qtyFiles = qtyFiles;
    }
    public int getQtyEmails() {
        return qtyEmails;
    }
    public void setQtyEmails(int qtyEmails) {
        this.qtyEmails = qtyEmails;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Time getTime() {
        return time;
    }
    public void setTime(Time time) {
        this.time = time;
    }

   @Override
   public String toString() {
        return "Submission{" +
                "files=" + files +
                ", studentName='" + studentName + '\'' +
                ", studentNo='" + studentNo + '\'' +
                ", type=" + type +
                ", xlsxSubmitted=" + xlsxSubmitted +
                ", savSubmitted=" + savSubmitted +
                ", spvSubmitted=" + spvSubmitted +
                ", snoXlsx=" + snoXlsx +
                ", snoSav=" + snoSav +
                ", snoSpv=" + snoSpv +
                ", qtyFiles=" + qtyFiles +
                ", qtyEmails=" + qtyEmails +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

    @Override
    public int compareTo(Submission o) {
        return this.getTime().compareTo(o.getTime());
    }
}
    