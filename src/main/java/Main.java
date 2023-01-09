import Models.*;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import Excel.*;
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
        setBasePath();
        Excel excel = new Excel();

        HashMap<String, List<EmailSubmission>> submissions;
        HashMap<String, ZipSubmission> zipSubmissions;

        ExcelWorkbook instance = ExcelWorkbook.getInstance();

        int test = 2;

        setBasePath();

        System.out.println("Read in from Infoview");
        classlist = excel.getInfoviewList();
        System.out.println("Read in from Infoview Complete - Records found: " + classlist.size());
        switch(test) {
            case FIRST_TEST :
                type = SPSS;
                attendance = excel.getAttendance(SPSS_T1_ATT);
                Outlook spssOutlookT1 = new Outlook(TESTS_FOLDER + SPSS_T1_EMAIL_PST);
                submissions = spssOutlookT1.submissions;
                Zip spssZipT1 = new Zip(TESTS_FOLDER + SPSS_T1_BRIGHTSPACE_ZIP, classlist);
                zipSubmissions = spssZipT1.zipSubmissions;
                spssResults = getSpssResults(submissions, zipSubmissions, attendance, classlist, type);
                excel.postSpssResults(spssResults, FIRST_TEST);

                type = EXCEL;
                attendance = excel.getAttendance(EXCEL_T1_ATT);
                Outlook excelOutlookT1 = new Outlook(TESTS_FOLDER + EXCEL_T1_EMAIL_PST);
                submissions = excelOutlookT1.submissions;
                Zip excelZipT1 = new Zip(TESTS_FOLDER + EXCEL_T1_BRIGHTSPACE_ZIP, classlist);
                zipSubmissions = excelZipT1.zipSubmissions;
                excelResults = getExcelResults(submissions, zipSubmissions, attendance, classlist, type);
//                excel.postResults(excelResults, FIRST_TEST);

                break;
            case SECOND_TEST:
                type = SPSS;
                System.out.println("Read Attendance");
                attendance = excel.getAttendance(SPSS_T2_ATT);
                System.out.println("Attendance Rad - Present: " + attendance.size());

//                Outlook spssOutlookT2 = new Outlook(TESTS_FOLDER + SPSS_T2_EMAIL_PST);
//                submissions = spssOutlookT2.submissions;
//                Zip spssZipT2 = new Zip(TESTS_FOLDER + SPSS_T2_BRIGHTSPACE_ZIP, classlist);
//                zipSubmissions = spssZipT2.zipSubmissions;
//                spssResults = getSpssResults(submissions, zipSubmissions, attendance, classlist, type);
//                excel.postSpssResults(spssResults, SECOND_TEST);

//                type = EXCEL;
//                attendance = excel.getAttendance(EXCEL_T2_ATT);
//                Outlook excelOutlookT2 = new Outlook(TESTS_FOLDER + EXCEL_T2_EMAIL_PST);
//                submissions = excelOutlookT2.submissions;
//                Zip excelZipT2 = new Zip(TESTS_FOLDER + EXCEL_T2_BRIGHTSPACE_ZIP, classlist);
//                zipSubmissions = excelZipT2.zipSubmissions;
//                excelResults = getExcelResults(submissions, zipSubmissions, attendance, classlist, type);
//                printResults(excelResults);
//                excel.postResults(excelResults, SECOND_TEST);

                break;
        }

    }

//    private static void postResults(int type, int test) {
//
//        switch(test) {
//            case FIRST_TEST:
//                switch(type) {
//                    case SPSS:
//
//                        break;
//                    case EXCEL:
//                        break;
//
//                }
//            case SECOND_TEST:
//                switch(type) {
//                    case SPSS:
//                        System.out.println(spssResults);
//                        break;
//                    case EXCEL:
//                        break;
//
//                }
//
//        }
//
//    }

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

        for(String key : sortedkeys) {
            System.out.println(key + " -> " +results.get(key).toString());
        }
    }

    private static HashMap<String, SpssResult> getSpssResults(HashMap<String, List<EmailSubmission>> submissions,
                                                              HashMap<String, ZipSubmission> zipSubmissions,
                                                              HashMap<String, Attendance> attendance,
                                                              HashMap<String, Infoview> classlist, int type) {
        HashMap<String, SpssResult> results = new HashMap<>();
        for (Map.Entry<String, Infoview>  student : classlist.entrySet()) {
            int bsSub = 0;
            int emSub = 0;
            EmailSubmission sub = null;
            ZipSubmission zSub = null;
            boolean savSubE = false;
            boolean spvSubE = false;
            boolean savSnoE = false;
            boolean spvSnoE = false;
            boolean savSubB = false;
            boolean spvSubB = false;
            boolean savSnoB = false;
            boolean spvSnoB = false;
            int bsFiles = 0;
            int emFiles = 0;
            String studentNo = student.getValue().getStudentNo();

            // check brightspace submission
            if(! submissions.containsKey(studentNo) ) continue; // skip over where no submissions exist
            else
                if(submissions.get(studentNo).size() == 0) {
                    emFiles = 0;
                    emSub = 0;

                } else {
                    sub = submissions.get(studentNo).get(0);
                    emSub = (submissions.get(studentNo).get(0) .getQtyFiles()>0) ? 1 : 0;

                    savSubE = sub.isSavSubmitted();
                    savSnoE = sub.isSnoSav();
                    spvSubE = sub.isSpvSubmitted();
                    spvSnoE = sub.isSnoSpv();
                    emFiles = sub.getQtyFiles();

                }
            // check emailSubmission
            if(zipSubmissions.containsKey(studentNo))     {
                bsSub = (zipSubmissions.get(studentNo) != null) ? 1 : 0;
                zSub = zipSubmissions.get(studentNo);
                savSubB = zSub.isSavSubmitted();
                savSnoB = zSub.isSnoSav();
                spvSubB = zSub.isSpvSubmitted();
                spvSnoB = zSub.isSnoSpv();
                bsFiles = zSub.getQtyFiles();

            }

            System.out.println(studentNo);
            SpssResult spssResult = SpssResult.builder()
                    .attendance(attendance.get(studentNo).getAttendance())
                    .brightspaceSubmission(bsSub)
                    .emailSubmission(emSub)
                    .savSubmitted( (savSubE || savSubB) ? 1 : 0)
                    .spvSubmitted( (spvSubE || spvSubB) ? 1 : 0)
                    .sno(savSnoE && spvSnoB ? 1 : 0)
                    .savSno( (savSnoE || savSnoB) ? 1 : 0)
                    .spvSno( (spvSnoE || spvSnoB) ? 1 : 0)
                    .IncorrectFilesSubmitted((emFiles == 2) || (bsFiles == 2) ? 1 : 0)
                    .build();

            results.put(String.valueOf(studentNo), spssResult);

        }
        return results;
    }



}
