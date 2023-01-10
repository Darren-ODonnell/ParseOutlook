import Models.SpssResult;
import Models.EmailSubmission;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import Models.*;
import lombok.CustomLog;

import static Models.Util.SIZE_STUDENT_NO;
import static Models.Util.type;

@CustomLog
public class Outlook {

    HashMap<String, List<EmailSubmission>> submissions = new HashMap<>();
    HashMap<String,SpssResult>  results = new HashMap<>();

    public static void main(String[] args) {
        // spss first
        String path = "C:\\Users\\liam.odonnell\\Desktop\\OneDrive - Technological University Dublin\\Tests Admin Automate\\";
        String spssT1file = "SPSS T1 email.pst";
        String spssT2file = "SPSS T2 email.pst";
        String excelT1file = "Excel T1 email.pst";
        String excelT2file = "Excel T2 email.pst";

        new Outlook(path + spssT2file);

    }

    public Outlook(String filename) {
            log.info(new MyString("Info: Reading Outlook file: " , filename).toString());
        try {
            PSTFile pstFile = new PSTFile(filename);
            processFolder(pstFile.getRootFolder());
            log.info(new MyString("Info: Submission read from Outlook files ","Count: " , submissions.size()).toString());
        } catch (Exception err) {
            log.severe(new MyString("Error: Outlook file cannot be opened: " , filename).toString());
            System.exit(1);
        }
    }


    public void processFolder(PSTFolder folder)  throws PSTException, java.io.IOException    {
        // go through the folders...
        if (folder.hasSubfolders()) {
            Vector<PSTFolder> childFolders = folder.getSubFolders();
            for (PSTFolder childFolder : childFolders) {
                processFolder(childFolder);
            }
        }

        // and now the emails for this folder
        if (folder.getContentCount() > 0) {
            PSTMessage email = (PSTMessage)folder.getNextChild();
            while (email != null) {
                addSubmission(email);
                email = (PSTMessage)folder.getNextChild();
            }
        }
        // need to process students with multiple submissions
        // Delete earlier submission If later submissions have same files.
        //
        for ( HashMap.Entry<String, List<EmailSubmission>> sub : submissions.entrySet() ) {
            String key = sub.getKey();
            List<EmailSubmission> subs = sub.getValue();

            if(subs.size() > 1) {
                subs = mergeSubmissions(subs);
            }
        }


    }

    private List<EmailSubmission> mergeSubmissions(List<EmailSubmission> subs) {
        // merge the files details between each submission into the first.
        for(EmailSubmission sub : subs) {
            subs.get(0).setSavSubmitted( subs.get(0).isSavSubmitted() || sub.isSavSubmitted());
            subs.get(0).setSpvSubmitted( subs.get(0).isSpvSubmitted() || sub.isSpvSubmitted());
            subs.get(0).setXlsxSubmitted( subs.get(0).isXlsxSubmitted() || sub.isXlsxSubmitted());
            subs.get(0).setSnoSav( subs.get(0).isSnoSav() || sub.isSnoSav());
            subs.get(0).setSnoSpv( subs.get(0).isSnoSpv() || sub.isSnoSpv());
            subs.get(0).setSnoXlsx( subs.get(0).isSnoXlsx() || sub.isSnoXlsx());
        }
        return subs;
    }

    private void addSubmission(PSTMessage email) {
        List<String> files = getFiles( email );
        String studentNo = email.getSenderName().substring(0,SIZE_STUDENT_NO).toLowerCase();

        boolean savSubmitted = Util.extnExists("sav", files);
        boolean spvSubmitted = Util.extnExists("spv", files);
        boolean xlsxSubmitted = Util.extnExists("xlsx", files);

        boolean snoSav = Util.snoExists("sav",studentNo, files);
        boolean snoSpv = Util.snoExists("spv",studentNo, files);
        boolean snoXlsx = Util.snoExists("xlsx",studentNo, files);

        String emailDate = String.valueOf(email.getClientSubmitTime());

        EmailSubmission submission = EmailSubmission.builder()
                .type( type ) // SPSS or Excel submission
                .files( files )
                .studentNo( studentNo )
                .studentName(email.getSenderName().substring(SIZE_STUDENT_NO + 1))
                .qtyFiles( files.size() )

                .savSubmitted( savSubmitted )
                .spvSubmitted( spvSubmitted )
                .xlsxSubmitted( xlsxSubmitted )

                .snoSpv( snoSpv )
                .snoSav( snoSav )
                .snoXlsx( snoXlsx )
                .qtyEmails(1)

                .date( getDate(emailDate) )
                .time( getTime(emailDate) )
                .build();

        // sno entry already exists
        if(submissions.containsKey(studentNo)) {
            // change this block
            // last submission is always the accepted submission.
            List<EmailSubmission> subm = submissions.get(studentNo);

            List<EmailSubmission> subs = submissions.get(studentNo);
            submission.setQtyEmails(submission.getQtyEmails()+1);
            subs.add(submission);
            submissions.replace(studentNo, subs);
        } else {
            List<EmailSubmission> subs = new ArrayList<>();
            subs.add(submission);
            submissions.put(studentNo, subs);
        }
    }

    private Date getDate(String date) {
        // Mon Oct 24 15:41:25 BST 2022

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd", Locale.UK);

        String[] parts = date.split(" ");
        String month = parts[1];
        String day   = parts[2];
        String year  = parts[5];

        String strDate = year + "-" + month + "-" + day;

        Date newDate;
        try {
            newDate = new Date( formatter.parse(strDate).getTime() );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return newDate;
    }

    private Time getTime(String strTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.UK);
        String[] parts = strTime.split(" ");
        String time = parts[3];
        Time newTime;
        try {
            newTime = new Time( formatter.parse(time).getTime() );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return newTime;
    }

    public List<String> getFiles(PSTMessage email) {
        List<String> files = new ArrayList<>();
        int fileCount = email.getNumberOfAttachments();
        for (int i = 0; i< fileCount; i++) {
            try {
                String file = email.getAttachment(i).getLongFilename();
                files.add(file.toLowerCase());
            } catch (PSTException | IOException e) {
                log.severe(new MyString("Error: Cannot read from Outlook file" ).toString());
                throw new RuntimeException(e);
            }
        }
        return files;
    }
}
