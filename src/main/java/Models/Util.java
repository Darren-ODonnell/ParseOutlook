package Models;

import java.awt.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static final int STUDENTNO_ATTENDANCE = 3;
    public static final int SPSS_ATTENDANCE = 41;
    public static final int EXCEL_ATTENDANCE = SPSS_ATTENDANCE + 2;


    public static final int SIZE_STUDENT_NO = 9;
    public static final String HOME_BASE_ADDRESS = "192.168";




    // Module Constants
    public static final boolean HOME = true;
    public static final boolean WORK = false;

    // setup basepath for files

    public static final String HOME_TESTS_FOLDER = "c:\\Users\\Liam\\OneDrive - Technological University Dublin\\Tests Admin Automate\\";
    public static final String HOME_RESULTS_FOLDER = "c:\\Users\\Liam\\OneDrive - Technological University Dublin\\Coursework\\";

    public static final String WORK_TESTS_FOLDER = "c:\\Users\\Liam.ODonnell\\Desktop\\OneDrive - Technological University Dublin\\Tests Admin Automate\\";
    public static final String WORK_RESULTS_FOLDER = "c:\\Users\\Liam.ODonnell\\Desktop\\OneDrive - Technological University Dublin\\Coursework\\";

    public static String TESTS_FOLDER = "";
    public static String RESULTS_FOLDER = "";

    // Inforview Indices
    public static final int INFOVIEW_CLASS_ROLL_NO   =0;
    public static final int INFOVIEW_STUDENT_NO      =3;
    public static final int INFOVIEW_ADDRESS         =5;
    public static final int INFOVIEW_EMAIL           =6;

    public static final int INFOVIEW_SURNAME_FIRSTNAME =9;
    public static final int INFOVIEW_SURNAME         =11;
    public static final int INFOVIEW_FIRSTNAME       =12;
    public static final int INFOVIEW_FULLNAME_MIN    =14;
    public static final int INFOVIEW_FULLNAME_MAX    =13;

    public static final String USERS = "c:\\Users\\";
    public static final String ONEDRIVE = "OneDrive - Technological University Dublin\\";

    public static final String BASE_PATH_HOME = USERS + "liam\\";
    public static final String BASE_PATH_WORK = USERS + "liam.odonnell\\Desktop\\";
    public static final String TEST_FILE_PATH = ONEDRIVE + "Tests Admin Automate\\";
    public static final String RESULT_FILE_PATH = ONEDRIVE + "Coursework\\";

    // workbooks / sheets and / range to retrieve
    public static final String EXCEL_WB = "classlist.xlsx";
    public static final String CLASSLIST_WB = "classlist.xlsx";
    public static final String CLASSLIST_SHT = "classlist";
    public static final String CLASSLIST_RANGE = "subset";


    public static final String RESULTS_WB = "DT341-2 Version1 Sign-in details Anew2.xlsm";
    public static final String ATTENDANCE_SHT = "Attendance";
    public static final String ATTENDANCE_RANGE = "Attendance";
    public static final String INFOVIEW_SHT = "infoview";
    public static final String INFOVIEW_RANGE = "infoview";

    public static final String RESULTS_SNO_COLUMN = "";
    public static final String RESULTS_ATTENDANCE_COLUMN_SPSS = "";
    public static final String RESULTS_ATTENDANCE_COLUMN_EXCEL = ""; // or always SPSS + 2

    // regex strings
    public final static String MATCH_STUDENT_NO = ".*[c|d|C|D][0-9]{8}.*"; // student number
    public final static String CAPTURE_STUDENT_NO = "[c|d|C|D][0-9]{8}"; // student number

    // get ip to determine if running program from Home or Work.
    public static String getIpAddress() {
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            return datagramSocket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setBasePath() {

        String ip = Util.getIpAddress();
        boolean locn = ip.contains(HOME_BASE_ADDRESS);

        TESTS_FOLDER = (locn) ? HOME_TESTS_FOLDER : WORK_TESTS_FOLDER;
        RESULTS_FOLDER = (locn) ? HOME_RESULTS_FOLDER : WORK_RESULTS_FOLDER;

    }

    public static String[] split(String formula) {
        String[] cell = new String[2];
        String row = "";
        String col = "";
        String[] parts = formula.split(":");
        col = parts[1];
        String[] newparts = parts[0].split("!");

        row = (newparts.length > 1) ? parts[1] : parts[0];
        cell[0] = row;
        cell[1] = col;
        return cell;
    }

    public static String findStudentNo(String name, String filename) {

        if(filename.matches(MATCH_STUDENT_NO)) {
            Pattern p = Pattern.compile(CAPTURE_STUDENT_NO);
            Matcher m = p.matcher(filename);
            if (m.find()) {
                return m.group(0);
            }
        }
        return "";
    }






}
