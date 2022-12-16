package Models;

import lombok.Builder;

@Builder
public class SpssResult extends Result {

    int IncorrectFilesSubmitted = 0;    // 0/1
    int emailNoSubmission = 0;          // 1/0
    int brightspaceNoSubmission = 0;    // 1/0
    int savSubmitted = 0;               // 1/0
    int spvSubmitted = 0;               // 1/0
    int studentNumberMissing = 0;       // 1/.5/ 0
    int savSno = 0;                     // 1/0
    int spvSno = 0;                     // 1/0
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
    public int getSavSubmitted() {
        return savSubmitted;
    }
    public void setSavSubmitted(int savSubmitted) {
        this.savSubmitted = savSubmitted;
    }
    public int getSpvSubmitted() {
        return spvSubmitted;
    }
    public void setSpvSubmitted(int spvSubmitted) {
        this.spvSubmitted = spvSubmitted;
    }
    public int getStudentNumberMissing() {
        return studentNumberMissing;
    }
    public void setStudentNumberMissing(int studentNumberMissing) {
        this.studentNumberMissing = studentNumberMissing;
    }
    public int getSavSno() {
        return savSno;
    }
    public void setSavSno(int savSno) {
        this.savSno = savSno;
    }
    public int getSpvSno() {
        return spvSno;
    }
    public void setSpvSno(int spvSno) {
        this.spvSno = spvSno;
    }
    public int getAttendance() {
        return attendance;
    }
    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    @Override
    public String toString() {
        return "SpssResult{" +
                "IncorrectFilesSubmitted=" + IncorrectFilesSubmitted +
                ", emailNoSubmission=" + emailNoSubmission +
                ", brightspaceNoSubmission=" + brightspaceNoSubmission +
                ", savSubmitted=" + savSubmitted +
                ", spvSubmitted=" + spvSubmitted +
                ", studentNumberMissing=" + studentNumberMissing +
                ", savSno=" + savSno +
                ", spvSno=" + spvSno +
                ", attendance=" + attendance +
                '}';
    }
}
