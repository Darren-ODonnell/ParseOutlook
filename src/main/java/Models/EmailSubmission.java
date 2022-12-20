package Models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Time;
import java.sql.Date;
import java.util.List;

@SuperBuilder @Setter @Getter
public class EmailSubmission extends Submission implements Comparable<EmailSubmission>{
    private String studentName;
    private Date date;
    private Time time;

    @Builder.Default
    int qtyEmails        = 0;
    @Override
    public int compareTo(EmailSubmission o) {
        return this.getTime().compareTo(o.getTime());
    }

    @Override
    public String toString() {
        return "EmailSubmission{" +
                "studentName='" + studentName + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", qtyEmails=" + qtyEmails +
                ", xlsxSubmitted=" + xlsxSubmitted +
                ", savSubmitted=" + savSubmitted +
                ", spvSubmitted=" + spvSubmitted +
                ", snoXlsx=" + snoXlsx +
                ", snoSav=" + snoSav +
                ", snoSpv=" + snoSpv +
                ", qtyFiles=" + qtyFiles +
                '}';
    }
}
    