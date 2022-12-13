import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.xssf.usermodel.*;

import Excel.*;
import static Models.Util.*;

public class Excel  {

    public static void main(String[]args) {
        String basePath = getBasePath();

        HashMap<String, ClasslistRow> classlist = getClasslist(basePath + TEST_FILE_PATH + CLASSLIST_WB, CLASSLIST_SHT, CLASSLIST_RANGE);
        classlist = addAttendance(classlist,basePath +  RESULT_FILE_PATH + RESULTS_WB, ATTENDANCE_SHT, ATTENDANCE_RANGE);

        classlist.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.toString());
        });

        // write to main results file for specific test sheet
    }

    private static HashMap<String, ClasslistRow> addAttendance(HashMap<String, ClasslistRow> classlist, String workbook, String worksheet, String range) {


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

            for ( int row = startRow; row < endRow; row++ ) {

                if( checkCellValue(wb, sheet.getRow( row ).getCell( STUDENTNO_ATTENDANCE )) ) {

                    Cell stdno = sheet.getRow( row ).getCell( STUDENTNO_ATTENDANCE );
                    Cell att = sheet.getRow( row ).getCell( SPSS_ATTENDANCE );
                    eval.evaluate( stdno );
                    eval.evaluate( att );

                    String studentNo = stdno.getStringCellValue();
                    Integer attendance = 999;
                    if( att != null ) {
                        attendance = (int) att.getNumericCellValue();
                    }

                    if(classlist.containsKey(studentNo.toLowerCase())) {
                        classlist.get(studentNo.toLowerCase()).setAttendance(attendance);
                    }
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

    public static HashMap<String, ClasslistRow> getClasslist(String classlistWb, String classlistSht, String classlistRange) {

        HashMap<String, ClasslistRow> classlist = new HashMap<>();

        try {
            File file = new File(classlistWb);//creating a new file instance
            FileInputStream fis = new FileInputStream(file);//obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx/xlsm file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheet( classlistSht );

            for (Row row : sheet) {
                classlist.put(getStudentNo(row).toLowerCase(), getRow(row));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return classlist;
    }

    private static String getStudentNo(Row row) {
        return row.getCell(0).getStringCellValue();
    }

    public static ClasslistRow getRow(Row row) {
        // get the 4 columns
        ClasslistRow listRow = new ClasslistRow();

        listRow.setSno(row.getCell(0).getStringCellValue());
        // get rid of trailing period!
        listRow.setSurnameFirtsname(row.getCell(1).getStringCellValue().replace(".",""));
        listRow.setFullname(row.getCell(2).getStringCellValue());
        listRow.setFullname1(row.getCell(3).getStringCellValue());

        // tidyup surname/firstname values
        String[] parts = listRow.getSurnameFirtsname().split(",",2);
        String sn = parts[0].strip();
        String fn = parts[1].strip().replace(".","");
        listRow.setSurname( sn );
        listRow.setFirstname( fn );
//        System.out.println(listRow);

        return listRow;

    }

}
