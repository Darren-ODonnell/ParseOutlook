package Models;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static final int SPSS_T1_ATT        = 20;
    public static final int EXCEL_T1_ATT       = SPSS_T1_ATT + 2;
    public static final int SPSS_T2_ATT        = 41;
    public static final int EXCEL_T2_ATT       = SPSS_T2_ATT + 2;


    public static final String SPSS_T1_EMAIL_PST        = "SPSS T1 Email.pst";
    public static final String SPSS_T1_BRIGHTSPACE_ZIP  = "SPSS T1 BS.zip";
    public static final String EXCEL_T1_EMAIL_PST       = "EXCEL T1 Email.pst";
    public static final String EXCEL_T1_BRIGHTSPACE_ZIP = "EXCEL T1 BS.zip";

    public static final String SPSS_T2_EMAIL_PST        = "SPSS T2 Email.pst";
    public static final String SPSS_T2_BRIGHTSPACE_ZIP  = "SPSS T2 BS.zip";
    public static final String EXCEL_T2_EMAIL_PST       = "EXCEL T2 Email.pst";
    public static final String EXCEL_T2_BRIGHTSPACE_ZIP = "EXCEL T2 BS.zip";

    public static final int STUDENT_NO_ATTENDANCE = 3;
    public static final int STUDENT_NAME_ATTENDANCE = 4;

//    public static final int SPSS_ATTENDANCE = 41;
//    public static final int EXCEL_ATTENDANCE = SPSS_ATTENDANCE + 2;

    public static final String ATTENDANCE_TESTS_SHEET = "Attendance Tests";
//    public static final int ATTENDANCE_TESTS_START_ROW = 6;
    public static final int ATTENDANCE_TESTS_STUDENT_NO_COL = 3;

    public static final String ATTENDANCE_TESTS_RANGE =  "attendancetests";

    // column positions in 'Attendance Sheet'
    public static final int SPSS1_ATT_TEST_EM_SUB = 8;
    public static final int SPSS1_ATT_TEST_QTY_FILES = SPSS1_ATT_TEST_EM_SUB + 1 ;
    public static final int SPSS1_ATT_TEST_SNO      = SPSS1_ATT_TEST_EM_SUB + 2;
    public static final int SPSS1_ATT_TEST_SAV      = SPSS1_ATT_TEST_EM_SUB + 3;
    public static final int SPSS1_ATT_TEST_SPV      = SPSS1_ATT_TEST_EM_SUB + 4;
    public static final int SPSS1_ATT_TEST_BS_SUB   = SPSS1_ATT_TEST_EM_SUB + 5;

    public static final int EXCEL1_ATT_TEST_EM_SUB = 17;
    public static final int EXCEL1_ATT_TEST_QTY_FILES = EXCEL1_ATT_TEST_EM_SUB + 1 ;
    public static final int EXCEL1_ATT_TEST_SNO = EXCEL1_ATT_TEST_EM_SUB + 2;
    public static final int EXCEL1_ATT_TEST_BS_SUB = EXCEL1_ATT_TEST_EM_SUB + 3;

    public static final int SPSS2_ATT_TEST_EM_SUB = 24;

    public static final int SPSS2_ATT_TEST_QTY_FILES = SPSS2_ATT_TEST_EM_SUB + 1 ;
    public static final int SPSS2_ATT_TEST_SNO = SPSS2_ATT_TEST_EM_SUB + 2;
    public static final int SPSS2_ATT_TEST_SAV = SPSS2_ATT_TEST_EM_SUB + 3;
    public static final int SPSS2_ATT_TEST_SPV = SPSS2_ATT_TEST_EM_SUB + 4;
    public static final int SPSS2_ATT_TEST_BS_SUB = SPSS2_ATT_TEST_EM_SUB + 5;

    public static final int EXCEL2_ATT_TEST_EM_SUB = 33;
    public static final int EXCEL2_ATT_TEST_QTY_FILES = EXCEL2_ATT_TEST_EM_SUB + 1 ;
    public static final int EXCEL2_ATT_TEST_SNO = EXCEL2_ATT_TEST_EM_SUB + 2;
    public static final int EXCEL2_ATT_TEST_BS_SUB = EXCEL2_ATT_TEST_EM_SUB + 3;


    public static final int SIZE_STUDENT_NO = 9;
    public static final String HOME_BASE_ADDRESS = "192.168";

    // Module Constants
//    public static final boolean HOME = true;
//    public static final boolean WORK = false;
    public static final int SPSS = 1;
    public static final int EXCEL = 2;
    public static int type = 0;
    public final static int FIRST_TEST = 1;
    public final static int SECOND_TEST = 2;

    // setup basepath for files

    public static final String HOME_TESTS_FOLDER = "c:\\Users\\Liam\\OneDrive - Technological University Dublin\\Tests Admin Automate\\";
    public static final String HOME_RESULTS_FOLDER = "c:\\Users\\Liam\\OneDrive - Technological University Dublin\\Coursework\\";

    public static final String WORK_TESTS_FOLDER = "c:\\Users\\Liam.ODonnell\\Desktop\\OneDrive - Technological University Dublin\\Tests Admin Automate\\";
    public static final String WORK_RESULTS_FOLDER = "c:\\Users\\Liam.ODonnell\\Desktop\\OneDrive - Technological University Dublin\\Coursework\\";

    public static String TESTS_FOLDER = "";
    public static String RESULTS_FOLDER = "";

    // Infoview Indices

    public static final int INFOVIEW_CLASS_ROLL_NO   =0;
    public static final int INFOVIEW_STUDENT_NO      =3;
    public static final int INFOVIEW_ADDRESS         =5;
    public static final int INFOVIEW_EMAIL           =6;

    public static final int INFOVIEW_SURNAME_FIRSTNAME =9;
    public static final int INFOVIEW_SURNAME         =11;
    public static final int INFOVIEW_FIRSTNAME       =12;
    public static final int INFOVIEW_FULLNAME_MIN    =14;
    public static final int INFOVIEW_FULLNAME_MAX    =13;

//    public static final String USERS = "c:\\Users\\";
//    public static final String ONEDRIVE = "OneDrive - Technological University Dublin\\";

//    public static final String BASE_PATH_HOME = USERS + "liam\\";
//    public static final String BASE_PATH_WORK = USERS + "liam.odonnell\\Desktop\\";
//    public static final String TEST_FILE_PATH = ONEDRIVE + "Tests Admin Automate\\";
//    public static final String RESULT_FILE_PATH = ONEDRIVE + "Coursework\\";

//    public static final String RESULTS_WB = "DT341-2 Version1 Sign-in details Anew2.xlsm";
    public static final String RESULTS_WB = "TU922 Coursebook 2022.xlsm";

    public static final String ATTENDANCE_SHT = "Attendance";
    public static final String ATTENDANCE_RANGE = "Attendance";
    public static final String INFOVIEW_SHT = "Infoview";
    public static final String INFOVIEW_RANGE = "infoview";

//    public static final String RESULTS_SNO_COLUMN = "";
//    public static final String RESULTS_ATTENDANCE_COLUMN_SPSS = "";
//    public static final String RESULTS_ATTENDANCE_COLUMN_EXCEL = ""; // or always SPSS + 2

    // regex strings
    public final static String MATCH_STUDENT_NO = ".*[cdCD][0-9]{8}.*"; // student number
    public final static String CAPTURE_STUDENT_NO = "[cdCD][0-9]{8}"; // student number

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

    public static String findStudentNo(HashMap<String, Infoview> classlist, String name, String filename) {

        // find student number based on the students name
        String studentNo;
        if(classlist.containsKey(name.toLowerCase())) {
            studentNo = classlist.get(name.toLowerCase()).getStudentNo();
            return studentNo.toLowerCase();
        }

        if(filename.matches(MATCH_STUDENT_NO)) {
            Pattern p = Pattern.compile(CAPTURE_STUDENT_NO);
            Matcher m = p.matcher(filename);
            if (m.find()) {
                return m.group(0);
            }
        }
        return "";
    }

    public static boolean extnExists(String extension, List<String> files) {
        extension = extension.toLowerCase();
        for(String file : files) {
            file = file.toLowerCase();
            if (file.length() > 4) {
                String endOfFile3 = file.substring(file.length() - 3);
                String endOfFile4 = file.substring(file.length() - 4);
                switch (extension.toLowerCase()) {
                    case "sav":
                        if (endOfFile3.equalsIgnoreCase(extension))
                            return true;
                    case "spv":
                        if (endOfFile3.equalsIgnoreCase(extension))
                            return true;
                    case "xlsx":
                        if (endOfFile4.equalsIgnoreCase(extension))
                            return true;
                }
            }
        }
        return false;
    }

    public static boolean snoExists(String extn, String studentNo, List<String> files) {
        String sno = studentNo.toLowerCase();
        String extension = extn.toLowerCase();

        for (String file : files) {
//            file = file.toLowerCase();
            if(file.length()>10) {
                String endOfFile = file.substring(file.length() - 3).toLowerCase();
                switch (extension) {
                    case "xlsx":
                        if (file.substring(file.length() - 4).equals(extension))
                            if (file.contains(sno))
                                return true;
                        break;
                    case "sav":
                    case "spv":
                        if (endOfFile.equals(extension))
                            if (file.contains(sno))
                                return true;
                        break;
                }
            }
        }
        return false;
    }

}
