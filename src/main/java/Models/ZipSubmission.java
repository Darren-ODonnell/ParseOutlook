package Models;

import lombok.Builder;

import java.util.List;

@Builder
public class ZipSubmission {
    String studentNo;
    String header;
    String name;
    String date;
    List<String> files;

    public String getHeader()        {  return header;   }
    public void setHeader(String header) {  this.header    = header;    }
    public String getName()         {  return name;    }
    public void setName(String name) { this.name      = name;    }
    public String getDate()         {  return date;    }
    public void setDate(String date) { this.date      = date;    }
    public List<String> getFiles()  {  return files;    }
    public void setFiles(List<String> files) {  this.files     = files;    }
    public String getStudentNo()     { return studentNo;  }
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
                ", files=" + files +
                '}';
    }
}
