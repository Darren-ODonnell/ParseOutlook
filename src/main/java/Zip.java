import Models.Infoview;
import Models.ZipSubmission;
import lombok.CustomLog;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.stream.Collectors;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

import static Models.Util.*;

@CustomLog
public class Zip {

    HashMap<String, ZipSubmission> zipSubmissions;

    public Zip(String filename,  HashMap<String, Infoview> classlist) {
        log.info(new MyString("Info: Reading in Brightspace Zip file: " , filename).toString());
        zipSubmissions = getZipSubmissions(classlist, filename);
        log.info(new MyString("Info: Brightspace Zip file Complete: ","Count: " , zipSubmissions.size()).toString());

    }

    private static HashMap<String, ZipSubmission> getZipSubmissions(HashMap<String, Infoview> classlist, String filename) {

        List<String> fileContent = readZip(filename);
        HashMap<String, ZipSubmission> zipSubmissions = new HashMap<>();

        for(String student : fileContent) {

            String[] parts = student.split("[-/?]",5);

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
                    file = parts[4].trim().toLowerCase();
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
            String studentNo = findStudentNo(classlist, name, file).toLowerCase();
            if(zipSubmissions.containsKey(studentNo.toLowerCase())) {
                files = zipSubmissions.get(studentNo.toLowerCase()).getFiles();
            }
            files.add(file.toLowerCase());

            ZipSubmission zipSubmission = ZipSubmission.builder()
                    .savSubmitted( extnExists("sav",files))
                    .spvSubmitted( extnExists("spv",files))
                    .xlsxSubmitted(extnExists("xlsx",files))
                    .snoSav( snoExists("sav", studentNo, files))
                    .snoSpv( snoExists("spv", studentNo, files))
                    .snoXlsx( snoExists("xlsx", studentNo, files))
                    .type( type )
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

            zipFile.close();
        }
        catch (IOException ioException) {
            log.severe(new MyString("Error: Opening zip file: " , filename + "\n" + ioException).toString());
            System.exit(1);
        }
        return fileContent;
    }

}
