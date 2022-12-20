package Models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.java.Log;

@SuperBuilder @Log @Setter @Getter
public class ZipSubmission extends Submission implements Comparable<ZipSubmission> {
    String header;
    String name;
    String date;
    String time;

    @Builder.Default
    int qtyBsSubmissions = 0;

    // sort by time so that the most recent submission appears first
    public int compareTo(ZipSubmission o) {
        return this.getTime().compareTo(o.getTime());
    }

    @Override
    public String toString() {
        return "ZipSubmission{" +
                "header='" + header + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", qtyBsSubmissions=" + qtyBsSubmissions +
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
