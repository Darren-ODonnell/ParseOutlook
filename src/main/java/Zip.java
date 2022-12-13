import Models.Infoview;
import Models.ZipSubmission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

public class Zip {

    public static void main(String[] args) {

        // filenames and location
        String path = "C:\\Users\\liam\\OneDrive - Technological University Dublin\\Tests Admin Automate\\";
        String spssT1A = "SPSS T1A.zip";
        String spssT1B = "SPSS T1B.zip";
        String spssT2A = "SPSS T2A.zip";
        String spssT2B = "SPSS T2B.zip";
        String excelT1A = "Excel T1A.zip";
        String excelT2A = "Excel T2A.zip";
        String excelT1B = "Excel T1B.zip";
        String excelT2B = "Excel T2B.zip";

        HashMap<String, Infoview> classlist = new HashMap<>();

        HashMap<String, ZipSubmission> zipSubmissions = getZipSubmissions(path + spssT1A);

    }

    private static HashMap<String, ZipSubmission> getZipSubmissions(String filename) {

        List<String> fileContent = readZip(filename);
        HashMap<String, ZipSubmission> zipSubmissions = new HashMap<>();

        for(String student : fileContent) {

            String[] parts = student.split(" - ");
            String header = parts[0];
            String name = parts[1];

            String[] parts2 = parts[2].split("/");
            String date = parts2[0];
            String file = parts2[1];

            List<String> files = new ArrayList<>();

            String studentNo = findStudentNo(name, file);

            if(zipSubmissions.containsKey(studentNo.toLowerCase())) {
                files = zipSubmissions.get(studentNo).getFiles();
            }
            files.add(file);

            ZipSubmission zipSubmission = ZipSubmission.builder()
                    .studentNo(studentNo)
                    .header(header)
                    .name(name)
                    .date(date)
                    .files(files)
                    .build();

            zipSubmissions.put(studentNo, zipSubmission);
        }
        return zipSubmissions;
    }

    public static String matchStudentNo = "[c|d|C|D][0-9]{8}";

    private static String findStudentNo(String name, String filename) {
        // search for sno in filename
        // read classlist with same format of name
        // check that nom two names exist that are the same.



        if(filename.matches(matchStudentNo)) {
            Pattern p = Pattern.compile(matchStudentNo);
            Matcher m = p.matcher(filename);
            System.out.println(m.group(0));
            return m.group(0);
        } else {
            return null;
        }


    }

    public static List<String> readZip(String filename) {

        List<String> fileContent = new ArrayList<>();
        try {
            File file = new File(filename);
            ZipFile zipFile = new ZipFile(file);
            fileContent = zipFile.stream().map(ZipEntry::getName).collect(Collectors.toList());

            fileContent.forEach(System.out::println);
            zipFile.close();
        }
        catch (IOException ioException) {
            System.out.println("Error opening zip file: " + filename + "\n" + ioException);
        }
        return fileContent;
    }

}
