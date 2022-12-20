package Models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
@Data
@SuperBuilder
public class SpssResult extends Result {

    int savSubmitted = 0;
    int spvSubmitted = 0;
    int savSno = 0;
    int spvSno = 0;

    @Override
    public String toString() {
        return "SpssResult{" +
                "savSubmitted=" + savSubmitted +
                ", spvSubmitted=" + spvSubmitted +
                ", savSno=" + savSno +
                ", spvSno=" + spvSno +
                ", studentNo='" + studentNo + '\'' +
                ", filesSubmitted=" + filesSubmitted +
                ", attendance=" + attendance +
                ", emailSubmission=" + emailSubmission +
                ", brightspaceSubmission=" + brightspaceSubmission +
                ", IncorrectFilesSubmitted=" + IncorrectFilesSubmitted +
                '}';
    }
}
