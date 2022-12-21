import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import org.apache.poi.xssf.usermodel.*;
import static Models.Util.*;

import Models.Infoview;
import Excel.*;
import Models.Attendance;

public class Excel  {

    public static void main(String[]args) {
        setBasePath();

        HashMap<String, ClasslistRow> classlist = getClasslist(TESTS_FOLDER + CLASSLIST_WB, CLASSLIST_SHT, CLASSLIST_RANGE);
        classlist = addAttendance(classlist,RESULTS_FOLDER + RESULTS_WB, ATTENDANCE_SHT, ATTENDANCE_RANGE);

        classlist.entrySet().forEach( entry -> System.out.println(entry.getKey() + " " + entry) );
    }

    public static HashMap<String, Attendance> getAttendance(int attendanceCell, String workbook, String worksheet, String range) {
        HashMap<String, Attendance> attendance = new HashMap<>();

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
            int endRow = rr.getEnd().getRowIndex();

            for ( int row = startRow; row < endRow; row++ ) {
                Row currentRow = sheet.getRow(row);

                Cell snoAtt = currentRow.getCell(STUDENT_NO_ATTENDANCE);
                Cell sNameAtt = currentRow.getCell(STUDENT_NAME_ATTENDANCE);

                if( checkCellValue(wb, snoAtt) && checkCellValue(wb, sNameAtt)) {

                    Cell stdno = currentRow.getCell( STUDENT_NO_ATTENDANCE );
                    Cell stdname = currentRow.getCell( STUDENT_NAME_ATTENDANCE );
                    Cell att = currentRow.getCell( attendanceCell );

                    eval.evaluate( stdno );
                    eval.evaluate( att );
                    eval.evaluate( stdname );

                    String studentNo = getValue(wb,stdno); // return empty string if error
                    String studentName = getValue(wb,stdname); // return empty string if error
                    if(!studentNo.equals("")) { // skip over cells with no student number
                        int test_attendance = 999;
                        if (att != null) {
                            test_attendance = (int) att.getNumericCellValue();
                        }
                        Attendance attend = Attendance.builder()
                                .studentNo(studentNo.toLowerCase())
                                .studentName(studentName)
                                .attendance(test_attendance)
                                .build();

                        attendance.put(studentNo.toLowerCase(), attend);
                    } else {
                        System.out.println("Empty Student Row cell at: " + row);
                    }
                } else {
                    System.out.println("Null Cell found at: " + row);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return attendance;
    }

    private static String getValue(XSSFWorkbook wb, Cell stdno) {
        FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();
        CellValue res = eval.evaluate(stdno);

        if( res != null ) {
            switch (res.getCellType()) {
                case FORMULA:
                    return res.getStringValue();
                default:
                    return "";
            }
        }
        return "";
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

                if( checkCellValue(wb, sheet.getRow( row ).getCell(STUDENT_NO_ATTENDANCE)) ) {

                    Cell stdno = sheet.getRow( row ).getCell(STUDENT_NO_ATTENDANCE);
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

    private static boolean checkCellValue(XSSFWorkbook wb, Cell cell) {
        // skip any null/empty values or error values

        FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();
        CellType type = cell.getCellType();

        if( cell != null ) {
            switch (type) {
                case FORMULA:
                    return eval.evaluate(cell).getStringValue().length()>0;
                case BOOLEAN:
                case NUMERIC:
                case STRING:
                case BLANK:
                case ERROR:
                case _NONE:
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
            XSSFSheet sheet = new XSSFWorkbook( fis ).getSheet( classlistSht );
            sheet.forEach( row -> classlist.put(getStudentNo( row).toLowerCase(), getClasslistRow( row )) );

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

        return listRow;
    }

    private static String getStudentNo(Row row) {
        return row.getCell( 0 ).getStringCellValue().toLowerCase();
    }

    private static String getName(Row row) {
        return row.getCell(INFOVIEW_FULLNAME_MIN).getStringCellValue().toLowerCase();
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
            int endRow = rr.getEnd().getRowIndex();

            for(int vRow = startRow; vRow < endRow-1; vRow++) {

                if((sheet.getRow(vRow) != null) && sheet.getRow(vRow).getCell(INFOVIEW_STUDENT_NO).getStringCellValue().length()>0) {

                    Infoview info = getInfoviewRow(wb, sheet.getRow(vRow));
                    String name = info.getFullnameMax().toLowerCase();
                    classlistByName.put( name  , info );
                }
            }
            classlistByName.entrySet().forEach ( key -> System.out.println(key + " -> " + classlistByName.get(key)) );

        } catch(Exception e) {
            e.printStackTrace();
        }
        return classlistByName;
    }

    public static Infoview getInfoviewRow( XSSFWorkbook wb, Row row) {
        FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();

        Infoview listRow = Infoview.builder()
                .address(       eval.evaluate(  row.getCell(INFOVIEW_ADDRESS)).getStringValue())
                .classRollNo(   eval.evaluate(  row.getCell(INFOVIEW_CLASS_ROLL_NO)).getStringValue())
                .studentNo(     eval.evaluate(  row.getCell(INFOVIEW_STUDENT_NO)).getStringValue().toLowerCase())
                .firstname(     eval.evaluate(  row.getCell(INFOVIEW_FIRSTNAME)).getStringValue())
                .surname(       eval.evaluate(  row.getCell(INFOVIEW_SURNAME)).getStringValue())
                .fullnameMin(   eval.evaluate(  row.getCell(INFOVIEW_FULLNAME_MIN)).getStringValue())
                .fullnameMax(   eval.evaluate(  row.getCell(INFOVIEW_FULLNAME_MAX)).getStringValue())
                .surnameFirstname(eval.evaluate(row.getCell(INFOVIEW_SURNAME_FIRSTNAME)).getStringValue())
                .email(         eval.evaluate(  row.getCell(INFOVIEW_EMAIL)).getStringValue())
                .build();

        return listRow;
    }

}
