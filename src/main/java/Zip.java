import Models.Infoview;
import Models.ZipSubmission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.stream.Collectors;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

import static Models.Util.*;

public class Zip {

    public static void main(String[] args) {

        // set paths based on Work or Home
        setBasePath();

        String spssT1A = "SPSS T1A.zip";
        String spssT1B = "SPSS T1B.zip";
        String spssT2A = "SPSS T2A.zip";
        String spssT2B = "SPSS T2B.zip";
        String excelT1A = "Excel T1A.zip";
        String excelT2A = "Excel T2A.zip";
        String excelT1B = "Excel T1B.zip";
        String excelT2B = "Excel T2B.zip";

        HashMap<String, Infoview> classlist = Excel.getInfoviewList();
//        classlist.forEach((key, value) ->  System.out.println(key + " -> " + value.toString()) );

//        HashMap<String, ZipSubmission> zipSubmissions = getZipSubmissions(classlist, TESTS_FOLDER + spssT1A);
    }

    private static HashMap<String, ZipSubmission> getZipSubmissions(HashMap<String, Infoview> classlist, String filename) {

        List<String> fileContent = readZip(filename);
        HashMap<String, ZipSubmission> zipSubmissions = new HashMap<>();

        for(String student : fileContent) {
            String[] parts = student.split("[-/?]");
            String header = "";
            String name = "";
            String date = "";
            String file = "";

            switch(parts.length) {
                case 1:
                    // simple string no split found (no '-' or ':' found
                case 2:
                    // only one split found  (no '-' or ':' found
                case 3:
                    break;
                case 5:
                    file = parts[4].trim();
                case 4:
                    // two splits found - no file
                    header = parts[0]+"-"+parts[1].trim();
                    name = parts[2].trim();
                    date = parts[3].trim();
                    break;
                default:
                    // string has too many parts
                    break;
            }

            List<String> files = new ArrayList<>();

            String studentNo = findStudentNo(classlist, name, file);

            if(zipSubmissions.containsKey(studentNo.toLowerCase())) {
                files = zipSubmissions.get(studentNo.toLowerCase()).getFiles();
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

        zipSubmissions.forEach((key,value) -> {
            System.out.println(key + "-> "+value);
        });

        return zipSubmissions;
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
