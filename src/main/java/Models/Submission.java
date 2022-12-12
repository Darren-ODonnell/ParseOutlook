package Models;

import lombok.Builder;
import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Builder
public class Submission {
    List<String> files   = new ArrayList<>();
    String studentName   = "";
    String studentNo     = "";
    int type             = 0;
    boolean xlsxSubmitted = false;
    boolean savSubmitted = false;
    boolean spvSubmitted = false;

    boolean snoXlsx      = false;
    boolean snoSav       = false;
    boolean snoSpv       = false;

    int qtyFiles         = 0;
    int qtyEmails        = 0;

    Date date;
    Time time;
}
    