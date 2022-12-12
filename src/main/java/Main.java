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
        try {
            PSTFile pstFile = new PSTFile(filename);
            System.out.println(pstFile.getMessageStore().getDisplayName());
            processFolder(pstFile.getRootFolder());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    int depth = -1;
    public void processFolder(PSTFolder folder)  throws PSTException, java.io.IOException    {
        depth++;
        // the root folder doesn't have a display name
        if (depth > 0) {
            printDepth();
            System.out.println(folder.getDisplayName());
        }

        // go through the folders...
        if (folder.hasSubfolders()) {
            Vector<PSTFolder> childFolders = folder.getSubFolders();
            for (PSTFolder childFolder : childFolders) {
                processFolder(childFolder);
            }
        }

        // and now the emails for this folder
        if (folder.getContentCount() > 0) {
            depth++;
            PSTMessage email = (PSTMessage)folder.getNextChild();
            while (email != null) {
                printDepth();

                System.out.println("Email: "+email.getNumberOfAttachments());
                addSubmission(email);

                if(email.getNumberOfAttachments() == 2) {
                    System.out.println("Email: "+email.getAttachment(0).getLongFilename());
                    System.out.println("Email: "+email.getAttachment(1).getLongFilename());
                }

                email = (PSTMessage)folder.getNextChild();
            }
            depth--;
        }
        depth--;
    }

    private void addSubmission(PSTMessage email) {
        List<String> files = new ArrayList<>();

        boolean sav = false;
        boolean spv = false;
        boolean xlsx = false;

        boolean snoSav = false;
        boolean snoSpv = false;
        boolean snoXlsx = false;

        String studentNo = email.getSenderName().substring(0,9).toLowerCase();
        String studentName = email.getSenderName().substring(10);

        String emailDate = String.valueOf(email.getClientSubmitTime());
        Date newDate = getDate(emailDate);
        Time newTime = getTime(emailDate);

        if (email.getNumberOfAttachments() > 0) {
            files = getFiles(email);
            if (Main.type == Main.SPSS) {
                sav = getFor("sav", files);
                spv = getFor("spv", files);
                snoSav = snoExists("sav",studentNo, files);
                snoSpv = snoExists("spv",studentNo, files);
            } else { // must be Excel
                xlsx = getFor("xlsx", files);
                snoXlsx = snoExists("xlsx",studentNo, files);
            }
        }

        Submission submission = Submission.builder()
                .type(Main.type) // SPSS or Excel submission
                .files(files)
                .studentNo(studentNo)
                .studentName(studentName)
                .qtyFiles(email.getNumberOfAttachments())

                .savSubmitted(sav)
                .spvSubmitted(spv)
                .xlsxSubmitted(xlsx)

                .snoSpv(snoSpv)
                .snoSav(snoSav)
                .snoXlsx(snoXlsx)

                .date(newDate)
                .time(newTime)
                .build();


        // sno entry already exists
        if(submissions.containsKey(studentNo)) {
            List<Submission> subs = submissions.get(studentNo);
            subs.add(submission);
            submissions.replace(studentNo, subs);


        } else {
            List<Submission> subs = new ArrayList<>();
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

    private boolean snoExists(String extn, String studentNo, List<String> files) {
        String sno = studentNo.toLowerCase();
        String extension = extn.toLowerCase();

        for (String file : files) {
//            file = file.toLowerCase();
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
        return false;
    }

    private boolean getFor(String extension, List<String> files) {
        extension = extension.toLowerCase();
        for(String file : files) {
            file = file.toLowerCase();
            String endOfFile = file.substring(file.length() -3);
            switch (extension) {
                case "xlsx":
                    if(file.substring(file.length() -4).equals(extension))
                        return true;
                case "sav":
                    if(endOfFile.equals(extension))
                        return true;
                case "spv":
                    if(endOfFile.equals(extension))
                        return true;
            }
        }
        return false;
    }

    public List<String> getFiles(PSTMessage email) {
        List<String> files = new ArrayList<>();
        int fileCount = email.getNumberOfAttachments();
        for (int i = 0; i< fileCount; i++) {
            try {
                String file = email.getAttachment(i).getLongFilename();
                files.add(file);
            } catch (PSTException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return files;
    }

    public void printDepth() {
        for (int x = 0; x < depth-1; x++) {
            System.out.print(" | ");
        }
        System.out.print(" |- ");
    }
}