import Models.*;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static Models.Util.*;

@Slf4j @Log
public class Main {
    public static HashMap<String, Infoview> classlist;
    public static HashMap<String, Attendance> attendance;
    public static HashMap<String, ExcelResult> excelResults;
    public static HashMap<String, SpssResult> spssResults;

    public static void main(String[] args) {
        // util.java has all the filename definitions

        HashMap<String, List<EmailSubmission>> submissions;
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
//                attendance = Excel.getAttendance(SPSS_T2_ATT, RESULTS_FOLDER + RESULTS_WB, ATTENDANCE_SHT, ATTENDANCE_RANGE);
//                Outlook spssOutlookT2 = new Outlook(TESTS_FOLDER + SPSS_T2_EMAIL_PST);
//                submissions = spssOutlookT2.submissions;
//                Zip spssZipT2 = new Zip(TESTS_FOLDER + SPSS_T2_BRIGHTSPACE_ZIP, classlist);
//                zipSubmissions = spssZipT2.zipSubmissions;
//                spssResults = getSpssResults(submissions, zipSubmissions, attendance, classlist, type);
//                postResults(type, test);

                type = EXCEL;
                attendance = Excel.getAttendance(EXCEL_T2_ATT, RESULTS_FOLDER + RESULTS_WB, ATTENDANCE_SHT, ATTENDANCE_RANGE);
                Outlook excelOutlookT2 = new Outlook(TESTS_FOLDER + EXCEL_T2_EMAIL_PST);
                submissions = excelOutlookT2.submissions;
                Zip excelZipT2 = new Zip(TESTS_FOLDER + EXCEL_T2_BRIGHTSPACE_ZIP, classlist);
                zipSubmissions = excelZipT2.zipSubmissions;
                excelResults = getExcelResults(submissions, zipSubmissions, attendance, classlist, type);
                printResults(excelResults);
//                postResults(type, test);

                break;
        }

    }

    private static void postResults(int type, int test) {

        switch(test) {
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

    private static  HashMap<String, ExcelResult> getExcelResults(HashMap<String, List<EmailSubmission>> submissions,
                                               HashMap<String, ZipSubmission> zipSubmissions,
                                               HashMap<String, Attendance> attendance,
                                               HashMap<String, Infoview> classlist, int type) {

        HashMap<String, ExcelResult> results = new HashMap<>();

        for (String name : classlist.keySet()) {
            String studentNo = classlist.get(name).getStudentNo();
            ExcelResult excelResult = null;
            int attend = 0;
            int bsSubmissions = 0;
            int filesSubmitted = 0;
            int xlsxSno = 0;
            int xlsxSubmitted = 0;
            int emailSubmission = 0;

            attend =  (attendance.containsKey(studentNo)) ? attendance.get(studentNo).getAttendance() : 0;
            bsSubmissions = (submissions.containsKey(studentNo)) ? submissions.get(studentNo).size() : 0;

            if(submissions.containsKey(studentNo)) {
                EmailSubmission sub = submissions.get(studentNo).get(0);
                filesSubmitted = sub.getQtyFiles();
                xlsxSno = sub.isSnoXlsx() ? 1 : 0;
                xlsxSubmitted = sub.isXlsxSubmitted() ? 1 : 0;
                emailSubmission = (sub.getQtyEmails() > 0) ? 1 : 0;

            }

            if (zipSubmissions.containsKey(studentNo.toLowerCase()))
                bsSubmissions = (zipSubmissions.get(studentNo).getQtyBsSubmissions() == 0) ? 0 : 1;


            if (zipSubmissions.containsKey(studentNo.toLowerCase())) {
                excelResult = ExcelResult.builder()
                        .studentNo(studentNo)
                        .attendance(attend)
                        .filesSubmitted(filesSubmitted)
                        .brightspaceSubmission(bsSubmissions)
                        .IncorrectFilesSubmitted(filesSubmitted)
                        .emailSubmission(emailSubmission)
                        .xlsxSno(xlsxSno)
                        .xlsxSubmitted(xlsxSubmitted)
                        .build();
            } else {
                excelResult = ExcelResult.builder()
                        .studentNo(studentNo)
                        .attendance(attendance.get(studentNo).getAttendance())
                        .brightspaceSubmission(0)
                        .emailSubmission(0)
                        .xlsxSno(0)
                        .xlsxSubmitted(0)
                        .build();
            }
            results.put(studentNo.toLowerCase(), excelResult);
        }

        return results;
    }

    private static void printResults(HashMap<String, ExcelResult> results) {

        List<String> sortedkeys = new ArrayList<>(results.keySet());
        Collections.sort(sortedkeys);

        for(Object key : sortedkeys) {
            System.out.println(key.toString() + " -> " +results.get(key).toString());
        }
    }

    private static HashMap<String, SpssResult> getSpssResults(HashMap<String, List<EmailSubmission>> submissions,
                                                              HashMap<String, ZipSubmission> zipSubmissions,
                                                              HashMap<String, Attendance> attendance,
                                                              HashMap<String, Infoview> classlist, int type) {
        HashMap<String, SpssResult> results = new HashMap<>();
        for (Map.Entry<String, Infoview> studentNo : classlist.entrySet()) {
            EmailSubmission sub = submissions.get(studentNo).get(0);
            ZipSubmission zSub = zipSubmissions.get(studentNo);

            SpssResult spssResult = SpssResult.builder()
                    .attendance(attendance.get(studentNo).getAttendance())
                    .brightspaceSubmission((zSub.getQtyBsSubmissions() == 0) ? 0 : 1)
                    .emailSubmission((sub.getQtyEmails() == 0) ? 0 : 1)
                    .savSubmitted((sub.isSavSubmitted()) ? 1 : 0)
                    .spvSubmitted((sub.isSpvSubmitted()) ? 1 : 0)
                    .savSno((sub.isSnoSav()) ? 1 : 0)
                    .spvSno((sub.isSnoSpv()) ? 1 : 0)
                    .IncorrectFilesSubmitted((sub.getQtyFiles()==2) ? 1 : 0)
                    .build();

            results.put(String.valueOf(studentNo), spssResult);

        }
        return results;
    }
}
