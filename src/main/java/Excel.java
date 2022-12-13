import Models.Util;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.util.List;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
            File file = new File(workbook);//creating a new file instance
            FileInputStream fis = new FileInputStream(file);//obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheet( worksheet );

            XSSFName namedRange = wb.getName(range);
            String formula =  namedRange.getRefersToFormula();


            RangeRef rr = new RangeRef(sheet, formula);

            // for each row
            // pick out studentno and attendance
            // update classlist
            int noPosn = 4;
            int attPosn = 30;

            for (int r = rr.getStart().getRowIndex(); r < rr.getEnd().getRowIndex(); r++ ) {
                String stdno = String.valueOf(sheet.getRow(r).getCell(noPosn));
//                int att = sheet.getRow(r).getCell(attPosn);
                System.out.println(stdno);

            }



            for (Row row : sheet) {
                classlist.add(getRow(row));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return classlist;


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

//            System.out.println("SheetName: " + sheet.getSheetName());
//            System.out.println("RelationId: requires param");
//            System.out.println("ActiveCell: " + sheet.getActiveCell());
//            System.out.println("CellComment - requires parameter: "); // + sheet.getCellComment("test"));
//            System.out.println("columnBreaks: " + sheet.getColumnBreaks());
//            System.out.println("CTWorksheet: " + sheet.getCTWorksheet());
//            System.out.println("Dimension: " + sheet.getDimension());
//            System.out.println("LastRowNumber: " + sheet.getLastRowNum());
//            System.out.println("LeftCol: " + sheet.getLeftCol());
//            System.out.println("PhysicalNumberOfRows: " + sheet.getPhysicalNumberOfRows());
//            System.out.println("TopRow: " + sheet.getTopRow());

            System.out.println("getSheetIndex:      " + namedRange.getSheetIndex());
            System.out.println("getNameName :       "+namedRange.getNameName());
            System.out.println("getSheetName:       "+namedRange.getSheetName());
            System.out.println("Formula:            "+namedRange.getRefersToFormula());
            System.out.println("getFunction:        "+namedRange.getFunction());
            System.out.println("getComment:         "+namedRange.getComment());
            System.out.println("getFunctionGroupId: "+namedRange.getFunctionGroupId());
            System.out.println("getClass:           "+namedRange.getClass());

//            getSheetIndex:      -1
//            getNameName :       studentlist
//            getSheetName:       Classlist
//            getRefersToFormula: Classlist!$A$1:$D$105
//            getFunction:        false
//            getComment:         null
//            getFunctionGroupId: 0
//            getClass:           class org.apache.poi.xssf.usermodel.XSSFName

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

    public String ReadCellData(int vRow,int vColumn)  {
        String value = null; //variable for storing the cell value
        Workbook wb = null; //initialize Workbook null
        try {
            //reading data from a file in the form of bytes
            FileInputStream fis = new FileInputStream("C:\\demo\\EmployeeData.xlsx");
            //constructs an XSSFWorkbook object,by buffering the whole stream into the memory
            wb = new XSSFWorkbook(fis);
        } catch(IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = wb.getSheetAt(0);     // getting the XSSFSheet object at given index
        Row row = sheet.getRow(vRow);         // returns the logical row
        Cell cell = row.getCell(vColumn);     // getting the cell representing the given column
        value = cell.getStringCellValue();    // getting cell value
        return value;                         // returns the cell value
    }


}
