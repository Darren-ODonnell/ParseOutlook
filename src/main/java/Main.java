import Models.Submission;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;
import java.io.IOException;
import java.sql.Time;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Main {
    public static final int SPSS = 1;
    public static final int EXCEL = 2;
    public static int type = 0;
    HashMap<String, List<Submission>> submissions = new HashMap<>();

    public static void main(String[] args)
    {
        // spss first
        type = SPSS;
        String path = "C:\\Users\\liam\\OneDrive - Technological University Dublin\\Tests Admin Automate\\";
        String spssT1file = "SPSS T1 email.pst";
        String spssT2file = "SPSS T2 email.pst";
        String excelT1file = "Excel T1 email.pst";
        String excelT2file = "Excel T2 email.pst";

//        new Main(path + spssT1file);

        // now excel
        type = EXCEL;
        new Main(path + excelT1file);
        System.out.println("Here");
    }

    public Main(String filename) {
        // create class to accept results
        // get Classlist
        //

//        try {
//            PSTFile pstFile = new PSTFile(filename);
//            System.out.println(pstFile.getMessageStore().getDisplayName());
//            processFolder(pstFile.getRootFolder());
//        } catch (Exception err) {
//            err.printStackTrace();
//        }
    }

}