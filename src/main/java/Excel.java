import lombok.val;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import org.apache.poi.xssf.usermodel.*;

import static Models.Util.*;

import Models.Infoview;
import Excel.*;

public class Excel  {

    public static void main(String[]args) {
        setBasePath();

        HashMap<String, ClasslistRow> classlist = getClasslist(TESTS_FOLDER + CLASSLIST_WB, CLASSLIST_SHT, CLASSLIST_RANGE);
        classlist = addAttendance(classlist,RESULTS_FOLDER + RESULTS_WB, ATTENDANCE_SHT, ATTENDANCE_RANGE);

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
            FileInputStream fis = new FileInputStream( file );//obtaining bytes from the file
            XSSFSheet sheet = new XSSFWorkbook( fis ).getSheet( classlistSht );;
            sheet.forEach( row -> classlist.put(getStudentNo( row ).toLowerCase(), getClasslistRow( row )) );

        } catch(Exception e) {
            e.printStackTrace();
        }
        return classlist;
    }



    public static ClasslistRow getClasslistRow(Row row) {
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

    private static String getStudentNo(Row row) {
        return row.getCell(0).getStringCellValue();
    }
    private static String getName(XSSFRow row) {
        return row.getCell(INFOVIEW_FULLNAME_MIN).getStringCellValue();
    }
    public static HashMap<String, Infoview> getInfoviewList() {

        HashMap<String, Infoview> classlistByName = new HashMap<>();
        HashMap<String, Infoview> classlistBySno = new HashMap<>();

        try {
            File file = new File(RESULTS_FOLDER + RESULTS_WB);//creating a new file instance
            FileInputStream fis = new FileInputStream(file);//obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx/xlsm file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheet( INFOVIEW_SHT );
            FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();

            XSSFName namedRange = wb.getName(INFOVIEW_RANGE);
            String formula =  namedRange.getRefersToFormula();
            RangeRef rr = new RangeRef(sheet, formula);

            int startRow = rr.getStart().getRowIndex()+1;
            int endRow = rr.getEnd().getRowIndex()+1;

            for(int row = startRow; row< endRow-1; row++) {

                // update row data if non empty
                if(!sheet.getRow(row).getCell(INFOVIEW_STUDENT_NO).getStringCellValue().isEmpty() ) {

                    classlistBySno.put(getStudentNo(sheet.getRow(row)).toLowerCase(), getInfoviewRow(wb, sheet.getRow(row)));
                    classlistByName.put(getName(sheet.getRow(row)).toLowerCase(), getInfoviewRow(wb, sheet.getRow(row)));
                }
            }
//            sheet.forEach(row -> );
            classlistByName.forEach( (key,value) -> System.out.println(key + " -> " + value.toString()) );

        } catch(Exception e) {
            e.printStackTrace();
        }
        return classlistByName;



    }

    public static Infoview getInfoviewRow( XSSFWorkbook wb, Row row) {
        // get the 4 columns
        FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();

        // check for empty rows

        Infoview listRow = Infoview.builder()
                .address(eval.evaluate(row.getCell(INFOVIEW_ADDRESS)).getStringValue())
                .classRollNo(eval.evaluate(row.getCell(INFOVIEW_CLASS_ROLL_NO)).getStringValue())
                .studentNo(eval.evaluate(row.getCell(INFOVIEW_STUDENT_NO)).getStringValue())
                .firstname(eval.evaluate(row.getCell(INFOVIEW_FIRSTNAME)).getStringValue())
                .surname(eval.evaluate(row.getCell(INFOVIEW_SURNAME)).getStringValue())
                .fullnameMax(eval.evaluate(row.getCell(INFOVIEW_FULLNAME_MIN)).getStringValue())
                .fullnameMin(eval.evaluate(row.getCell(INFOVIEW_FULLNAME_MAX)).getStringValue())
                .surnameFirstname(eval.evaluate(row.getCell(INFOVIEW_SURNAME_FIRSTNAME)).getStringValue())
                .email(eval.evaluate(row.getCell(INFOVIEW_EMAIL)).getStringValue())
                .build();

        return listRow;
    }
}
