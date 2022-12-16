package Models;

import lombok.Builder;

@Builder
public class ExcelResult extends Result {

    int IncorrectFilesSubmitted = 0;    // 1/0
    int emailNoSubmission = 0;          // 1/0
    int brightspaceNoSubmission = 0;    // 1/0
    int xlsxSubmitted = 0;              // 1/0
    int xlsxSno = 0;                    // 1/0
    int attendance = 0;                 // 1/0

    public int getIncorrectFilesSubmitted() {
        return IncorrectFilesSubmitted;
    }
    public void setIncorrectFilesSubmitted(int incorrectFilesSubmitted) {
        IncorrectFilesSubmitted = incorrectFilesSubmitted;
    }
    public int getEmailNoSubmission() {
        return emailNoSubmission;
    }
    public void setEmailNoSubmission(int emailNoSubmission) {
        this.emailNoSubmission = emailNoSubmission;
    }
    public int getBrightspaceNoSubmission() {
        return brightspaceNoSubmission;
    }
    public void setBrightspaceNoSubmission(int brightspaceNoSubmission) {
        this.brightspaceNoSubmission = brightspaceNoSubmission;
    }
    public int getXlsxSubmitted() {
        return xlsxSubmitted;
    }
    public void setXlsxSubmitted(int xlsxSubmitted) {
        this.xlsxSubmitted = xlsxSubmitted;
    }
    public int getXlsxSno() {
        return xlsxSno;
    }
    public void setXlsxSno(int xlsxSno) {
        this.xlsxSno = xlsxSno;
    }
    public int getAttendance() {
        return attendance;
    }
    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    @Override
    public String toString() {
        return "ExcelResult{" +
                "IncorrectFilesSubmitted=" + IncorrectFilesSubmitted +
                ", emailNoSubmission=" + emailNoSubmission +
                ", brightspaceNoSubmission=" + brightspaceNoSubmission +
                ", xlsxSubmitted=" + xlsxSubmitted +
                ", xlsxSno=" + xlsxSno +
                ", attendance=" + attendance +
                '}';
    }
}



