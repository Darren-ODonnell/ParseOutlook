import Models.Infoview;
import Models.Util;
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

    static Util util = new Util();
    public static int type = 0;

    public static void main(String[] args) {


        // set paths based on Work or Home
        setBasePath();

//        String spssT1A = "SPSS T1A.zip";   type = SPSS;
//        String spssT1B = "SPSS T1B.zip";   type = SPSS;
        String spssT2A = "SPSS T2.zip";   type = SPSS;
//        String spssT2B = "SPSS T2B.zip";   type = SPSS;
//        String excelT1A = "Excel T1A.zip"; type = EXCEL;
//        String excelT2A = "Excel T2A.zip"; type = EXCEL;
//        String excelT1B = "Excel T1B.zip"; type = EXCEL;
//        String excelT2B = "Excel T2B.zip"; type = EXCEL;

        HashMap<String, Infoview> classlist = Excel.getInfoviewList();
        classlist.forEach((key, value) ->  System.out.println(key + " -> " + value.toString()) );

        HashMap<String, ZipSubmission> zipSubmissions = getZipSubmissions(classlist, TESTS_FOLDER + spssT2A);

        zipSubmissions.forEach((key, value) -> System.out.println(key + " -> " + value.toString()));

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
                case 2:
                case 3:
                    // format incorrect
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
            System.out.println(studentNo);
            ZipSubmission zipSubmission = ZipSubmission.builder()
                    .savSubmitted( getFor("sav",files))
                    .spvSubmitted( getFor("spv",files))
                    .xlsxSubmitted(getFor("xlsx",files))
                    .snoSav( snoExists("sav", studentNo, files))
                    .snoSpv( snoExists("spv", studentNo, files))
                    .snoXlsx( snoExists("xlsx", studentNo, files))
                    .type( type)
                    .qtyFiles(files.size())
                    .studentNo(studentNo.toLowerCase())
                    .header(header)
                    .name(name)
                    .date(date)
                    .files(files)
                    .build();

            if(zipSubmissions.containsKey(studentNo) && (zipSubmissions.get(studentNo.toLowerCase()) != null)) { // update
                ZipSubmission updatedSubmission =  combineSubmissions(zipSubmission, zipSubmissions);
                zipSubmissions.replace(studentNo.toLowerCase(), updatedSubmission);
            } else { // add
                zipSubmissions.put(studentNo.toLowerCase(), zipSubmission);
            }
        }

        return zipSubmissions;
    }

    private static ZipSubmission combineSubmissions(ZipSubmission newSubm, HashMap<String, ZipSubmission> zipSubmissions) {
        ZipSubmission existSubm = zipSubmissions.get(newSubm.getStudentNo().toLowerCase());

        ZipSubmission update = ZipSubmission.builder()
                .savSubmitted( existSubm.isSavSubmitted() || newSubm.isSavSubmitted() )
                .spvSubmitted( existSubm.isSpvSubmitted() || newSubm.isSpvSubmitted() )
                .xlsxSubmitted( existSubm.isXlsxSubmitted() || newSubm.isXlsxSubmitted() )
                .snoSav( existSubm.isSnoSav() || newSubm.isSnoSav() )
                .snoSpv( existSubm.isSnoSav() || newSubm.isSnoSpv() )
                .snoXlsx( existSubm.isSnoSav() || newSubm.isSnoXlsx() )
                .type(type)
                .qtyFiles( Math.max(existSubm.getQtyFiles(), newSubm.getQtyFiles()) )
                .studentNo( newSubm.getStudentNo().toLowerCase() )
                .header( newSubm.getHeader() )
                .name( newSubm.getName() )
                .date( newSubm.getDate() )
                .files( newSubm.getFiles() )
                .qtyBsSubmissions( existSubm.getQtyBsSubmissions()+1 )
                .build();

        return update;
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
