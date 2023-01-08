package Models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ExcelResult extends Result {

    int xlsxSubmitted = 0;
    int sno = 0;
    int xlsxSno = 0;

    @Override
    public String toString() {
        return "ExcelResult{" +
                "xlsxSubmitted=" + xlsxSubmitted +
                ", sno=" + sno +
                ", xlsxSno=" + xlsxSno +
                ", studentNo='" + studentNo + '\'' +
                ", filesSubmitted=" + filesSubmitted +
                ", attendance=" + attendance +
                ", emailSubmission=" + emailSubmission +
                ", brightspaceSubmission=" + brightspaceSubmission +
                ", IncorrectFilesSubmitted=" + IncorrectFilesSubmitted +
                '}';
    }
}



