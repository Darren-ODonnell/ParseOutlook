import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel  {

    public static void main(String[]args) {

        String path = "C:\\Users\\liam.odonnell\\Desktop\\OneDrive - Technological University Dublin\\Tests Admin Automate\\";

        String resultsFile = "C:\\Users\\liam.odonnell\\Desktop\\OneDrive - Technological University Dublin\\Coursework\\DT341-2 Version1 Sign-in details Anew2.xlsm";
        String resultsFileSheet = "Attendance";
        String resultsSnoColumn = "";
        String resultsAttendanceColumnSPSS = "";
        String resultsAttendanceColumnEXCEL = ""; // or always SPSS + 2

        String excelFile = "classlist.xlsx";
        List<ClasslistRow> classlist = getClasslist(excelFile);
        classlist = getAttendance(classlist, excelFile);

    }

    private static List<ClasslistRow> getAttendance(List<ClasslistRow> classlist, String excelFile) {
        // read in columns from excel
        // merge these with classlist
        // done


        return classlist;
    }

    public static List<ClasslistRow> getClasslist(String filename) {
        String path = "C:\\Users\\liam.odonnell\\Desktop\\OneDrive - Technological University Dublin\\Tests Admin Automate\\";
        String excelFile = "classlist.xlsx";

        List<ClasslistRow> classlist = new ArrayList<>();
        try {
            File file = new File(path + excelFile);//creating a new file instance
            FileInputStream fis = new FileInputStream(file);//obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheet("classlist");

            String name = "subset";

            XSSFName namedRange = wb.getName(name);

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


    public Cell getStartCell(String cellStr) {
        Cell cell = null;
        return cell;
    }

    public Cell getEndStart(String cellSAtr) {
        Cell cell = null;
        return cell;
    }
    class Attendance {
        String studentNo = "";
        boolean attendance = false;
    }

    class Range {
        String rangeStr="";
        Cell start;
        Cell end;
        int rowIndex;
        int columnIndex;

        public Range (String range) {
            String[] parts = range.split("!");
            Cell startRange = getStartRange(parts[1]);
            Cell endRange = getEndRange(parts[1]);
        }
        public Range (Cell start, Cell end) {
            this.start = start;
            this.end = end;
        }

        private Cell getEndRange(String part) {
            String[] parts = part.split(":");
            String start = parts[0];
            Cell cell = null;
            return cell;
        }

        private Cell getStartRange(String part) {
            String[] parts = part.split(":");
            String end = parts[1];
            Cell cell = null;
            return cell;
        }

        public Cell getEnd() {
            return end;
        }

        public void setEnd(Cell end) {
            this.end = end;
        }

        public int getRow(Cell cell) {
            return cell.getRow().getRowNum();

        }
        public int getCol(Cell cell) {
            return cell.getColumnIndex();
        }

    }

}
