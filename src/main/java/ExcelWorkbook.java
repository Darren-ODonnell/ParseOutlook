
// Java program implementing Singleton class
// with using getInstance() method

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static Models.Util.RESULTS_FOLDER;
import static Models.Util.RESULTS_WB;


class ExcelWorkbook {

    public static ExcelWorkbook instance = null;
    public XSSFWorkbook wb;

    private ExcelWorkbook() {
        wb = getWorkbook();
    }


    public static ExcelWorkbook getInstance()
    {
        instance = (instance == null) ? new ExcelWorkbook() : instance;
        return instance;
    }

    public XSSFWorkbook getWorkbook() {
        File file = new File(RESULTS_FOLDER + RESULTS_WB); //creating a new file instance
        FileInputStream fis = null;//obtaining bytes from the file
        try {
            fis = new FileInputStream(file);
            wb = new XSSFWorkbook(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // creating Workbook instance that refers to .xlsx file
        return wb;
    }

}
