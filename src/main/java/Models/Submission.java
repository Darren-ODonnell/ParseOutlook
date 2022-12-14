package Models;

import lombok.Builder;
import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Builder
public class Submission {
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
}
    