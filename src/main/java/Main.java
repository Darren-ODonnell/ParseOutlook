import Models.*;

import java.util.HashMap;
import java.util.List;

import static Models.Util.*;

public class Main {
    public static HashMap<String, Infoview> classlist;
    public static HashMap<String, Attendance> attendance;
    public static HashMap<String, ExcelResult> excelResults;
    public static HashMap<String, SpssResult> spssResults;

    public static void main(String[] args) {
        // util.java has all the filename definitions

        HashMap<String, List<Submission>> submissions;
        HashMap<String, ZipSubmission> zipSubmissions;

        int test = 2;

        setBasePath();
        classlist = Excel.getInfoviewList();

        switch(test) {
            case FIRST_TEST :
                type = SPSS;
                attendance = Excel.getAttendance(SPSS_T1_ATT, RESULTS_FOLDER + RESULTS_WB, ATTENDANCE_SHT, ATTENDANCE_RANGE);
                Outlook spssOutlookT1 = new Outlook(TESTS_FOLDER + SPSS_T1_EMAIL_PST);
                submissions = spssOutlookT1.submissions;
                Zip spssZipT1 = new Zip(TESTS_FOLDER + SPSS_T1_BRIGHTSPACE_ZIP, classlist);
                zipSubmissions = spssZipT1.zipSubmissions;
                spssResults = getSpssResults(submissions, zipSubmissions, attendance, classlist, type);
                postResults(type, test);

                type = EXCEL;
                attendance = Excel.getAttendance(EXCEL_T1_ATT, RESULTS_FOLDER + RESULTS_WB, ATTENDANCE_SHT, ATTENDANCE_RANGE);
                Outlook excelOutlookT1 = new Outlook(TESTS_FOLDER + EXCEL_T1_EMAIL_PST);
                submissions = excelOutlookT1.submissions;
                Zip excelZipT1 = new Zip(TESTS_FOLDER + EXCEL_T1_BRIGHTSPACE_ZIP, classlist);
                zipSubmissions = excelZipT1.zipSubmissions;
                excelResults = getExcelResults(submissions, zipSubmissions, attendance, classlist, type);
                postResults(type, test);

                break;
            case SECOND_TEST:
                type = SPSS;
                attendance = Excel.getAttendance(SPSS_T2_ATT, RESULTS_FOLDER + RESULTS_WB, ATTENDANCE_SHT, ATTENDANCE_RANGE);
                Outlook spssOutlookT2 = new Outlook(TESTS_FOLDER + SPSS_T2_EMAIL_PST);
                submissions = spssOutlookT2.submissions;
                Zip spssZipT2 = new Zip(TESTS_FOLDER + SPSS_T2_BRIGHTSPACE_ZIP, classlist);
                zipSubmissions = spssZipT2.zipSubmissions;
                spssResults = getSpssResults(submissions, zipSubmissions, attendance, classlist, type);
                postResults(type, test);

                type = EXCEL;
                attendance = Excel.getAttendance(EXCEL_T2_ATT, RESULTS_FOLDER + RESULTS_WB, ATTENDANCE_SHT, ATTENDANCE_RANGE);
                Outlook excelOutlookT2 = new Outlook(TESTS_FOLDER + EXCEL_T2_EMAIL_PST);
                submissions = excelOutlookT2.submissions;
                Zip excelZipT2 = new Zip(TESTS_FOLDER + EXCEL_T2_BRIGHTSPACE_ZIP, classlist);
                zipSubmissions = excelZipT2.zipSubmissions;
                excelResults = getExcelResults(submissions, zipSubmissions, attendance, classlist, type);
                postResults(type, test);

                break;
        }

    }



    private static void postResults(int type, int test) {

        switch(type) {
            case FIRST_TEST:
                switch(type) {
                    case SPSS:

                        break;
                    case EXCEL:
                        break;

                }
            case SECOND_TEST:
                switch(type) {
                    case SPSS:
                        break;
                    case EXCEL:
                        break;

                }

        }

    }

    private static HashMap<String, ExcelResult> getExcelResults(HashMap<String, List<Submission>> submissions, HashMap<String, ZipSubmission> zipSubmissions, HashMap<String, Attendance> attendance, HashMap<String, Infoview> classlist, int type) {
        return null;
    }

    private static HashMap<String, SpssResult> getSpssResults(HashMap<String, List<Submission>> submissions, HashMap<String, ZipSubmission> zipSubmissions, HashMap<String, Attendance> attendance, HashMap<String, Infoview> classlist, int type) {
        return null;
    }


}
