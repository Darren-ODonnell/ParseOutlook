import Models.*;
import lombok.CustomLog;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.HashMap;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import static Models.Util.*;
import static org.apache.poi.ss.usermodel.CellType.ERROR;

import Excel.*;

@CustomLog
public class Excel  {
    int padding = 50;
    ExcelWorkbook instance = ExcelWorkbook.getInstance();
    public static XSSFWorkbook wb = ExcelWorkbook.getInstance().wb;
    public static FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();

    public Excel() {
    }

    public HashMap<String, Attendance> getAttendance(int attendanceCell) {
        log.info(new MyString("Start loading Attendance: ").toString());

        HashMap<String, Attendance> attendance = new HashMap<>();

        try {

            XSSFSheet sheet = wb.getSheet( ATTENDANCE_SHT );

            int startRow = getStartRow(sheet, ATTENDANCE_RANGE);
            int endRow = getEndRow(sheet, ATTENDANCE_RANGE);

            for ( int row = startRow-1; row < endRow; row++ ) {
                Row currentRow = sheet.getRow(row);
                String studentNo = getStringValue( currentRow, STUDENT_NO_ATTENDANCE);

                if( studentNo.length() > 0 ) {
                    String studentName = getStringValue( currentRow, STUDENT_NAME_ATTENDANCE);
                    int test_attendance = getIntValue( currentRow, attendanceCell);

                    Attendance attend = Attendance.builder()
                            .studentNo(studentNo.toLowerCase())
                            .studentName(studentName)
                            .attendance(test_attendance)
                            .build();

                    attendance.put(studentNo.toLowerCase(), attend);
                } else { // skip over cells with no student number
                    log.warning("Null Cell found at: " + row);
                }
            }
        } catch(Exception e) {
            log.severe(new MyString("Error: opening worksheet : ", ATTENDANCE_SHT).toString());
            System.exit(1);
        }
        log.info(new MyString("Attendance : ", "Size : ", attendance.size()).toString());

        return attendance;
    }

    public  String getCellValue(Cell cell) {

        CellValue abc = eval.evaluate(cell);
        CellType cType = cell.getCellType();
        switch(abc.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return "" + cell.getNumericCellValue();
            case ERROR:
                return "";
        }

        return "";
    }

    private  String getStringValue(Row row, int column) {
        Cell cell = row.getCell( column );
        CellValue res = eval.evaluate( cell );

        if( res != null ) {
            switch (res.getCellType()) {
                case FORMULA:
                    return "" + eval.evaluate(cell);
                case STRING:
                    return res.getStringValue();
                default:
                    return "";
            }
        }
        return "";
    }

    private static int getIntValue(Row row, int column) {
        Cell cell = row.getCell( column );
        CellValue res = eval.evaluate(cell);

        if( res != null ) {
            switch (res.getCellType()) {
                case FORMULA:
                case NUMERIC:
                    return (int) res.getNumberValue();
                default:
                    return 999;
            }
        }
        return 999;
    }

    private static String checkCellValue(Row row, int column) {

        CellType cellType = row.getCell(column).getCellType();

        if( cellType != null ) {
            switch (cellType) {
                case FORMULA:
                    return eval.evaluate(row.getCell(column)).getStringValue();
                case BOOLEAN:
                case NUMERIC:
                case STRING:
                case BLANK:
                case ERROR:
                case _NONE:
                    return "";
            }
        }
        return "";
    }

    public  HashMap<String, ClasslistRow> getClasslist(String classlistWb, String classlistSht, String classlistRange) {

        HashMap<String, ClasslistRow> classlist = new HashMap<>();

        try {
            File file = new File(classlistWb);//creating a new file instance
            FileInputStream fis = new FileInputStream( file );//obtaining bytes from the file
            XSSFSheet sheet = new XSSFWorkbook( fis ).getSheet( classlistSht );
            sheet.forEach( row -> classlist.put(getStudentNo( row).toLowerCase(), getClasslistRow( row )) );

        } catch(Exception e) {
            log.severe("Error opening workbook : " + classlistWb);
            System.exit(1);
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

    private String getStudentNo(Row row) {
        return row.getCell( 0 ).getStringCellValue().toLowerCase();
    }

    private String getName(Row row) {
        return row.getCell(INFOVIEW_FULLNAME_MIN).getStringCellValue().toLowerCase();
    }

    public HashMap<String, Infoview> getInfoviewList() {
        log.info(new MyString("Start loading Infoview : ").toString());
        HashMap<String, Infoview> classlistByName = new HashMap<>();
        HashMap<String, Infoview> classlistBySno = new HashMap<>();

        try {
            XSSFSheet sheet = wb.getSheet( INFOVIEW_SHT );

            int startRow = getStartRow(sheet, INFOVIEW_RANGE);
            int endRow = getEndRow(sheet, INFOVIEW_RANGE);

            for(int vRow = startRow; vRow < endRow-1; vRow++) {
                if(sheet.getRow(vRow) != null) {
                    CellValue cellContent = eval.evaluate(sheet.getRow(vRow).getCell(INFOVIEW_STUDENT_NO) );
                    if (cellContent !=null && cellContent.getCellType() != ERROR) {
                        if (cellContent.getStringValue().length() > 0) {

                            Infoview info = getInfoviewRow(wb, sheet.getRow(vRow));
                            String name = info.getFullnameMax().toLowerCase();
                            classlistByName.put(name, info);
                        } else {
                            log.warning(vRow + " - Invalid Cell contents found: " + cellContent.getStringValue());
                            continue;
                        }
                    } else {
                        log.warning(vRow + " - Cell Error - Sno not recognised ");
                        continue;
                    }
                } else {
                    log.warning(vRow + " - Row is null: ");
                    continue;
                }

            }


        } catch(Exception e) {

            log.severe(new MyString("Error: Cannot open worksheet: " , INFOVIEW_SHT).toString());
            System.exit(1);
        }
        log.info(new MyString("Start loading Infoview : ", "Size : ", classlistByName.size()).toString());

        return classlistByName;
    }

    public Infoview getInfoviewRow( XSSFWorkbook wb, Row row) {

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

    public XSSFWorkbook getWorkbook() {
        XSSFWorkbook wb = null;
        File file = new File(RESULTS_FOLDER + RESULTS_WB); //creating a new file instance
        FileInputStream fis = null;//obtaining bytes from the file
        try {
            fis = new FileInputStream(file);
            wb = new XSSFWorkbook(fis);
        } catch (FileNotFoundException e) {
            log.severe("Error: Workbook not found: " + RESULTS_FOLDER + RESULTS_WB);
            System.exit(1);
        } catch (IOException e) {
            log.severe("Error: Workbook readin error: " + RESULTS_FOLDER + RESULTS_WB);
            System.exit(1);
        }
        // creating Workbook instance that refers to .xlsx file
        return wb;
    }

    public void postSpssResults(HashMap<String, SpssResult> results, int test) {

        log.info("Info: Posting SPSS results to results Workbook: =====================");
        try {
            XSSFSheet sheet = wb.getSheet( ATTENDANCE_TESTS_SHEET );

            int startRow = getStartRow(sheet, ATTENDANCE_TESTS_RANGE);
            int endRow = getEndRow(sheet, ATTENDANCE_TESTS_RANGE);

            int studentNoCol = ATTENDANCE_TESTS_STUDENT_NO_COL;

            int emSubCol = (test == 1) ? SPSS1_ATT_TEST_EM_SUB : SPSS2_ATT_TEST_EM_SUB;
            int qtyFilesCol = (test == 1) ? SPSS1_ATT_TEST_QTY_FILES : SPSS2_ATT_TEST_QTY_FILES;
            int snoCol = (test == 1) ? SPSS1_ATT_TEST_SNO : SPSS2_ATT_TEST_SNO;
            int savCol = (test == 1) ? SPSS1_ATT_TEST_SAV : SPSS2_ATT_TEST_SAV;
            int spvCol = (test == 1) ? SPSS1_ATT_TEST_SPV : SPSS2_ATT_TEST_SPV;
            int bsSubCol =  (test == 1) ? SPSS1_ATT_TEST_BS_SUB : SPSS2_ATT_TEST_BS_SUB;

            for ( int row = startRow-1; row < endRow; row++ ) {
                Row currentRow = sheet.getRow(row);
                String studentNo = currentRow.getCell(ATTENDANCE_TESTS_STUDENT_NO_COL).getStringCellValue().toLowerCase();

                if(results.containsKey(studentNo)) {
                    SpssResult result = results.get(studentNo);
                    currentRow.getCell(emSubCol).setCellValue(result.getEmailSubmission());
                    currentRow.getCell(qtyFilesCol).setCellValue(result.getFilesSubmitted());
                    currentRow.getCell(snoCol).setCellValue((result.getSno()));
                    currentRow.getCell(savCol).setCellValue(result.getSavSubmitted());
                    currentRow.getCell(spvCol).setCellValue(result.getSpvSubmitted());
                    currentRow.getCell(bsSubCol).setCellValue(result.getBrightspaceSubmission());
                } else {
                    log.warning("Warn: StudentNo not found in results : " + studentNo);
                }
                break;
            }
        } catch(Exception e) {
            log.severe("Error: Accessing worksheet: " + ATTENDANCE_TESTS_SHEET);
            System.exit(1);

        }

        if(wb != null) {
            writeExcelFile(wb);
            log.info("Info: Posting SPSS results to results Workbook: =====================");
        } else {
            log.severe("Error: Workbook wb is null");
            System.exit(1);
        }

    }

    private int getEndRow(XSSFSheet sheet, String rangeName) {

        XSSFName namedRange = wb.getName( rangeName );
        String formula =  namedRange.getRefersToFormula();
        String[] parts1 = formula.split("!",2);
        CellRangeAddress cra = createCorrectCellRangeAddress(parts1[1]);

        return cra.getLastRow();
    }

    private int getStartRow(XSSFSheet sheet, String rangeName) {

        XSSFName namedRange = wb.getName( rangeName );
        String formula =  namedRange.getRefersToFormula();
        String[] parts1 = formula.split("!",2);
        CellRangeAddress cra = createCorrectCellRangeAddress(parts1[1]);

        return cra.getFirstRow();
    }

    public void postExcelResults(HashMap<String, ExcelResult> results, int test) {
        log.info("Info: Posting Excel results to results Workbook: =====================");
        try {
            XSSFSheet sheet = wb.getSheet( ATTENDANCE_TESTS_SHEET );


            int startRow = getStartRow(sheet, ATTENDANCE_TESTS_RANGE);
            int endRow = getEndRow(sheet, ATTENDANCE_TESTS_RANGE);

            int studentNoCol = ATTENDANCE_TESTS_STUDENT_NO_COL;

            int emSubCol = (test == 1) ? SPSS1_ATT_TEST_EM_SUB : SPSS2_ATT_TEST_EM_SUB;
            int qtyFilesCol = (test == 1) ? SPSS1_ATT_TEST_QTY_FILES : SPSS2_ATT_TEST_QTY_FILES;
            int snoCol = (test == 1) ? SPSS1_ATT_TEST_SNO : SPSS2_ATT_TEST_SNO;
            int savCol = (test == 1) ? SPSS1_ATT_TEST_SAV : SPSS2_ATT_TEST_SAV;
            int spvCol = (test == 1) ? SPSS1_ATT_TEST_SPV : SPSS2_ATT_TEST_SPV;
            int bsSubCol =  (test == 1) ? SPSS1_ATT_TEST_BS_SUB : SPSS2_ATT_TEST_BS_SUB;

            for ( int row = startRow-1; row < endRow; row++ ) {
                Row currentRow = sheet.getRow(row);
                String studentNo = currentRow.getCell(ATTENDANCE_TESTS_STUDENT_NO_COL).getStringCellValue().toLowerCase();

                if(results.containsKey(studentNo)) {
                    ExcelResult result = results.get(studentNo);
                    currentRow.getCell(emSubCol).setCellValue(result.getEmailSubmission());
                    currentRow.getCell(qtyFilesCol).setCellValue(result.getFilesSubmitted());
                    currentRow.getCell(snoCol).setCellValue((result.getSno()));
                    currentRow.getCell(bsSubCol).setCellValue(result.getBrightspaceSubmission());
                }
                break;
            }
        } catch(Exception e) {
            log.severe("Error: Accessing worksheet: " + ATTENDANCE_TESTS_SHEET);
            System.exit(1);
        }

        if(wb != null) {
            writeExcelFile(wb);
            log.info("Info: Posting Excel results to results Workbook: =====================");
        } else {
            log.severe("Error: Workbook wb is null");
            System.exit(1);
        }

    }

    public void writeExcelFile(XSSFWorkbook wb) {

        try {
            FileOutputStream outputStream = new FileOutputStream(RESULTS_FOLDER + RESULTS_WB);
            wb.write(outputStream);
        } catch (FileNotFoundException e) {
            log.severe("Error: File not found Accessing worksheet: " + RESULTS_FOLDER + RESULTS_WB);
            System.exit(1);
        } catch (IOException e) {
            log.severe("Error: Writing to file: " + RESULTS_FOLDER + RESULTS_WB);
            System.exit(1);
        }
    }

    protected CellRangeAddress createCorrectCellRangeAddress(String addressString) {
        final String[] split = addressString.split(":");
        final CellReference cr1 = new CellReference(split[0]);
        final CellReference cr2 = new CellReference(split[1]);
        int r1 = cr1.getRow() > cr2.getRow() ? cr2.getRow() : cr1.getRow();
        int r2 = cr1.getRow() > cr2.getRow() ? cr1.getRow() : cr2.getRow();
        int c1 = cr1.getCol() > cr2.getCol() ? cr2.getCol() : cr1.getCol();
        int c2 = cr1.getCol() > cr2.getCol() ? cr1.getCol() : cr2.getCol();
        return new CellRangeAddress(r1, r2, c1, c2);
    }
}
