package Models;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Util {

    public static final int SIZE_STUDENT_NO = 9;
    public static final String HOME_BASE_ADDRESS = "192.168";




    // Module Constants
    public static final boolean HOME = true;
    public static final boolean WORK = false;

    // setup basepath for files

    public static final String USERS = "\\Users\\";
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

    public static final String RESULTS_SNO_COLUMN = "";
    public static final String RESULTS_ATTENDANCE_COLUMN_SPSS = "";
    public static final String RESULTS_ATTENDANCE_COLUMN_EXCEL = ""; // or always SPSS + 2


    // get ip to determine if running program from Home or Work.
    public static String getIpAddress() {
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            return datagramSocket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getBasePath() {

        String ip = Util.getIpAddress();
        boolean locn = ip.contains(HOME_BASE_ADDRESS);

        return (locn) ? BASE_PATH_HOME : BASE_PATH_WORK;
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
}
