package Models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Setter @Getter @SuperBuilder
public class Submission  {
    private List<String> files;
    private String studentNo;
    private int type       ;

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

}
    