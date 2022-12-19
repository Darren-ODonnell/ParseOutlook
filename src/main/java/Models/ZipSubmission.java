package Models;

import lombok.Builder;

import java.util.List;

@Builder
public class ZipSubmission {
    String studentNo;
    String header;
    String name;
    String date;
    String time;
    List<String> files;
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
    int qtyBsSubmissions = 0;

    public int getType() {        return type;    }
    public void setType(int type) {        this.type = type;    }
    public boolean isXlsxSubmitted() {        return xlsxSubmitted;    }
    public void setXlsxSubmitted(boolean xlsxSubmitted) {        this.xlsxSubmitted = xlsxSubmitted;    }
    public boolean isSavSubmitted() {        return savSubmitted;    }
    public void setSavSubmitted(boolean savSubmitted) {        this.savSubmitted = savSubmitted;    }
    public boolean isSpvSubmitted() {        return spvSubmitted;    }
    public void setSpvSubmitted(boolean spvSubmitted) {        this.spvSubmitted = spvSubmitted;    }
    public boolean isSnoXlsx() {        return snoXlsx;    }
    public void setSnoXlsx(boolean snoXlsx) {        this.snoXlsx = snoXlsx;    }
    public boolean isSnoSav() {        return snoSav;    }
    public void setSnoSav(boolean snoSav) {        this.snoSav = snoSav;    }
    public boolean isSnoSpv() {        return snoSpv;    }
    public void setSnoSpv(boolean snoSpv) {        this.snoSpv = snoSpv;    }
    public int getQtyFiles() {        return qtyFiles;    }
    public void setQtyFiles(int qtyFiles) {        this.qtyFiles = qtyFiles;    }
    public int getQtyBsSubmissions() {        return qtyBsSubmissions;    }
    public void setQtyBsSubmissions(int qtyBsSubmissions) {        this.qtyBsSubmissions = qtyBsSubmissions;    }

    public String getHeader()        {  return header;   }
    public void setHeader(String header) {  this.header    = header;    }
    public String getName()         {  return name;    }
    public void setName(String name) { this.name      = name;    }
    public String getDate()         {  return date;    }
    public void setDate(String date) { this.date      = date;    }
    public List<String> getFiles()  {  return files;    }
    public void setFiles(List<String> files) {  this.files     = files;    }
    public String getStudentNo()     { return studentNo.toLowerCase();  }
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    @Override
    public String toString() {
        return "ZipSubmission{" +
                "studentNo='" + studentNo + '\'' +
                ", header='" + header + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", files=" + files +
                ", type=" + type +
                ", xlsxSubmitted=" + xlsxSubmitted +
                ", savSubmitted=" + savSubmitted +
                ", spvSubmitted=" + spvSubmitted +
                ", snoXlsx=" + snoXlsx +
                ", snoSav=" + snoSav +
                ", snoSpv=" + snoSpv +
                ", qtyFiles=" + qtyFiles +
                ", qtyBsSubmissions=" + qtyBsSubmissions +
                '}';
    }
}
