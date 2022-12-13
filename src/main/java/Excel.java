import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.*;

import Excel.*;
import static Models.Util.*;

public class Excel  {

    public static void main(String[]args) {
        String basePath = getBasePath();

        List<ClasslistRow> classlist = getClasslist(basePath + TEST_FILE_PATH + CLASSLIST_WB, CLASSLIST_SHT, CLASSLIST_RANGE);
        classlist = addAttendance(classlist,basePath +  RESULT_FILE_PATH + RESULTS_WB, ATTENDANCE_SHT, ATTENDANCE_RANGE);

        // write to main results file for specific test sheet
    }

    private static List<ClasslistRow> addAttendance(List<ClasslistRow> classlist, String workbook, String worksheet, String range) {
        // read in columns from excel
        // merge these with classlist
        // done

//        NamedRange nRange = new NamedRange();

        try {
            File file = new File(workbook); //creating a new file instance
            FileInputStream fis = new FileInputStream(file);//obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheet( worksheet );
            FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();

            XSSFName namedRange = wb.getName(range);
            String formula =  namedRange.getRefersToFormula();
            RangeRef rr = new RangeRef(sheet, formula);

            // for each row
            // pick out studentno and attendance
            // update classlist

            int startRow = rr.getStart().getRowIndex()+1;
            int endRow = rr.getEnd().getRowIndex()+1;

            for (int row = startRow; row < endRow; row++ ) {

                if( checkCellValue(wb, sheet.getRow(row).getCell(STUDENTNO_ATTENDANCE)) ) {

                    Cell stdno = sheet.getRow(row).getCell(STUDENTNO_ATTENDANCE);
                    Cell att = sheet.getRow(row).getCell(SPSS_ATTENDANCE);
                    eval.evaluate(stdno);
                    eval.evaluate(att);

                    String studentNo = stdno.getStringCellValue();
                    int attendance = 999;
                    if(att != null)
                        attendance = (int) att.getNumericCellValue();

                    System.out.println(studentNo + " - " + attendance );
                } else {
                    System.out.println("Null found at: " + row);
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return classlist;


    }

    private static boolean checkCellValue( XSSFWorkbook wb, Cell cell) {
        // skip any null/empty values or error values
        FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();
        CellType type = eval.evaluateFormulaCell(cell);
        if( cell != null ) {
            switch (type) {
                case BOOLEAN:
                case NUMERIC:
                case STRING:
                case BLANK:
                    return true;
                case ERROR:
                    return false;
            }
        }
        return false;
    }

    public static List<ClasslistRow> getClasslist(String classlistWb, String classlistSht, String classlistRange) {


        List<ClasslistRow> classlist = new ArrayList<>();
        try {
            File file = new File(classlistWb);//creating a new file instance
            FileInputStream fis = new FileInputStream(file);//obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheet( classlistSht );

            XSSFName namedRange = wb.getName(classlistRange);

            String formula =  namedRange.getRefersToFormula();

            System.out.println("getSheetIndex:      " + namedRange.getSheetIndex());
            System.out.println("getNameName :       "+namedRange.getNameName());
            System.out.println("getSheetName:       "+namedRange.getSheetName());
            System.out.println("Formula:            "+namedRange.getRefersToFormula());
            System.out.println("getFunction:        "+namedRange.getFunction());
            System.out.println("getComment:         "+namedRange.getComment());
            System.out.println("getFunctionGroupId: "+namedRange.getFunctionGroupId());
            System.out.println("getClass:           "+namedRange.getClass());

            for (Row row : sheet) {
                classlist.add(getRow(row));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return classlist;
    }

    public static ClasslistRow getRow(Row row) {
        // get the 4 columns
        ClasslistRow listRow = new ClasslistRow();

        listRow.sno = row.getCell(0).getStringCellValue();
        // get rid of trailing period!
        listRow.surnameFirtsname = row.getCell(1).getStringCellValue().replace(".","");
        listRow.fullname = row.getCell(2).getStringCellValue();
        listRow.fullname1 = row.getCell(3).getStringCellValue();

        // tidyup surname/firstname values
        String[] parts = listRow.surnameFirtsname.split(",",2);
        String sn = parts[0].strip();
        String fn = parts[1].strip().replace(".","");

        listRow.surname = sn;
        listRow.firstname = fn;

        System.out.println(listRow);

        return listRow;

    }

    public String ReadCellData(int vRow,int vColumn) {
        String value = null; //variable for storing the cell value
        Workbook wb = null; //initialize Workbook null
        try {
            //reading data from a file in the form of bytes
            FileInputStream fis = new FileInputStream("C:\\demo\\EmployeeData.xlsx");
            //constructs an XSSFWorkbook object,by buffering the whole stream into the memory
            wb = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = null;
        if ((sheet = wb.getSheetAt(0)) != null) {     // getting the XSSFSheet object at given index
            Row row = sheet.getRow(vRow);         // returns the logical row
            Cell cell = row.getCell(vColumn);     // getting the cell representing the given column
            value = cell.getStringCellValue();    // getting cell value
                                     // returns the cell value
        }
        return value;
    }
}
