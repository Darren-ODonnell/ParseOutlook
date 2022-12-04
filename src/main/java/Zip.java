import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Enumeration;

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

        readZip(path + spssT1A);


    }

    public static void readZip(String fileName) {
        ZipFile zipFile = null;
        File file = new File(fileName);
        try {
            zipFile = new ZipFile(file);
            List<String> fileContent = zipFile.stream().map(ZipEntry::getName).collect(Collectors.toList());
            System.out.println("ZipFile contents - " + fileContent);
            zipFile.close();
        }
        catch (IOException ioException) {
            System.out.println("Error opening zip file" + ioException);
        }
    }

}
