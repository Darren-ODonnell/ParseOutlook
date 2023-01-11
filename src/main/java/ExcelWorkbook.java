
// Java program implementing Singleton class
// with using getInstance() method

import lombok.CustomLog;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static Models.Util.*;

@CustomLog
class ExcelWorkbook {

    public static ExcelWorkbook instance = null;
    public static Workbook wb;
    public static FileInputStream inputStream;

    private ExcelWorkbook() {
        wb = getWorkbook();
    }


    public static ExcelWorkbook getInstance()
    {
        instance = (instance == null) ? new ExcelWorkbook() : instance;
        return instance;
    }

    public Workbook getWorkbook() {
        setBasePath();
        try {
            File file = new File(RESULTS_FOLDER + RESULTS_WB); //creating a new file instance
            inputStream = new FileInputStream(file);
//            wb = WorkbookFactory.create(inputStream);
            wb = new XSSFWorkbook(inputStream);
        } catch (FileNotFoundException e) {
            log.severe("Severe: "+ e.getMessage());
            System.exit(1);

        } catch (IOException e) {
            log.severe(new MyString("Severe: File read error - probably already open by another application: ",RESULTS_FOLDER + RESULTS_WB).toString());
            System.exit(1);

        }
        // creating Workbook instance that refers to .xlsx file
        return wb;
    }

    static void closeWorkbook() {
        try {
            inputStream.close();
            wb.close();


        } catch (IOException e) {
            log.severe("Error: Closing Workbook");
            System.exit(1);
        }
    }

}
