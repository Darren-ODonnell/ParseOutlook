package Models;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter @Setter @SuperBuilder
@AllArgsConstructor
public class Result implements Comparable<Result>{
    String studentNo = "";
    int filesSubmitted = 0;
    int attendance = 0;
    int emailSubmission = 0;
    int brightspaceSubmission = 0;
    int IncorrectFilesSubmitted = 0;

    @Override
    public int compareTo(Result o) {
        return this.getStudentNo().compareTo(o.getStudentNo());
    }

}
