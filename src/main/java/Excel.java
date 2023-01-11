import Models.*;
import lombok.CustomLog;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.HashMap;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import static Models.Util.*;
import static Models.Util.EXCEL2_ATT_TEST_BS_SUB;
import static org.apache.poi.ss.usermodel.CellType.*;


@CustomLog
public class Excel  {
    public static Workbook wb = ExcelWorkbook.getInstance().wb;
    public static FormulaEvaluator eval = wb.getCreationHelper().createFormulaEvaluator();

    public Excel() {
    }

    public HashMap<String, Attendance> getAttendance(int attendanceCell) {
        log.info(new MyString("Start loading Attendance: ").toString());

        HashMap<String, Attendance> attendance = new HashMap<>();

        try {

            Sheet sheet = wb.getSheet( ATTENDANCE_SHT );

            int startRow = getStartRow( ATTENDANCE_RANGE);
            int endRow = getEndRow( ATTENDANCE_RANGE);

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
            log.severe(new MyString("Error: opening worksheet: ", ATTENDANCE_SHT).toString());
            System.exit(1);
        }
        log.info(new MyString("Attendance : ", "Size: ", attendance.size() ).toString());

        return attendance;
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

    public HashMap<String, Infoview> getInfoviewList() {
        log.info(new MyString("Start loading Infoview : ").toString());
        HashMap<String, Infoview> classlistByName = new HashMap<>();

        try {
            Sheet sheet = wb.getSheet( INFOVIEW_SHT );

            int startRow = getStartRow( INFOVIEW_RANGE);
            int endRow = getEndRow( INFOVIEW_RANGE);

            for(int vRow = startRow; vRow < endRow-1; vRow++) {
                if (rowValid(sheet, vRow)) {
                    Infoview info = getInfoviewRow(sheet.getRow(vRow));
                    String name = info.getFullnameMax().toLowerCase();
                    classlistByName.put(name, info);
                }
            }

        } catch(Exception e) {

            log.severe(new MyString("Error: Cannot open worksheet: " , INFOVIEW_SHT).toString());
            System.exit(1);
        }
        log.info(new MyString("Infoview Loaded : ", "Size: ", classlistByName.size() ).toString());

        return classlistByName;
    }

    private static boolean rowValid(Sheet sheet, int row) {
        if(sheet.getRow(row) != null) {
            CellValue cellContent = eval.evaluate(sheet.getRow(row).getCell(INFOVIEW_STUDENT_NO) );
            if (cellContent != null) {
                if (cellContent.getCellType() == STRING) {
                    if (cellContent.getStringValue().length() > 0) {
                        return true;
                    } else {
                        log.warning(row + " - StudentNo not valid: " + cellContent.getStringValue());
                    }
                } else {
                    log.warning(row + " - Numeric value found - should be student no: " + cellContent.getStringValue());
                }
            } else {
                log.warning(row + " - Cell Error - Sno not recognised ");
            }
        } else {
            log.warning(row + " - Row is null: ");
        }
        return false;
    }

    public Infoview getInfoviewRow( Row row) {

       return Infoview.builder()
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
    }

    public void postSpssResults(HashMap<String, SpssResult> results, int test) {

        log.info(new MyString("Info: Posting SPSS results to results Workbook: ").toString());
        try {
            Sheet sheet = wb.getSheet( ATTENDANCE_TESTS_SHEET );

            int startRow = getStartRow( ATTENDANCE_TESTS_RANGE);
            int endRow = getEndRow( ATTENDANCE_TESTS_RANGE);

            int emSubCol = (test == 1) ? SPSS1_ATT_TEST_EM_SUB : SPSS2_ATT_TEST_EM_SUB;
            int qtyFilesCol = (test == 1) ? SPSS1_ATT_TEST_QTY_FILES : SPSS2_ATT_TEST_QTY_FILES;
            int snoCol = (test == 1) ? SPSS1_ATT_TEST_SNO : SPSS2_ATT_TEST_SNO;
            int savCol = (test == 1) ? SPSS1_ATT_TEST_SAV : SPSS2_ATT_TEST_SAV;
            int spvCol = (test == 1) ? SPSS1_ATT_TEST_SPV : SPSS2_ATT_TEST_SPV;
            int bsSubCol =  (test == 1) ? SPSS1_ATT_TEST_BS_SUB : SPSS2_ATT_TEST_BS_SUB;

            for ( int row = startRow; row < endRow; row ++ ) {
                String studentNo = "";
                Row currentRow = sheet.getRow(row);
                System.out.println(row);
                if(rowValid(sheet, row)) {
                    studentNo = currentRow.getCell(ATTENDANCE_TESTS_STUDENT_NO_COL).getStringCellValue().toLowerCase();
                    if(results.containsKey(studentNo)) {
                        SpssResult result = results.get(studentNo);
                        currentRow.getCell(emSubCol).setCellValue(result.getEmailSubmission());
                        currentRow.getCell(qtyFilesCol).setCellValue(result.getIncorrectFilesSubmitted());
                        currentRow.getCell(snoCol).setCellValue((result.getSno()));
                        currentRow.getCell(savCol).setCellValue(result.getSavSubmitted());
                        currentRow.getCell(spvCol).setCellValue(result.getSpvSubmitted());
                        currentRow.getCell(bsSubCol).setCellValue(result.getBrightspaceSubmission());

                    } else log.warning(new MyString("Warning: StudentNo not in results list: ",studentNo).toString());

                } else log.warning(new MyString("Warning: StudentNo not found in results : " , studentNo).toString());
            }
        } catch(Exception e) {
            log.severe("Error: Accessing worksheet: " + ATTENDANCE_TESTS_SHEET);
            System.exit(1);
        }

        if(wb != null) {
            writeExcelFile(wb);
            log.info(new MyString("Info: Posting SPSS results to results Workbook: ").toString());
        } else {
            log.severe(new MyString("Error: Workbook wb is null").toString());
            System.exit(1);
        }

    }

    private int getEndRow(String rangeName) {

        Name namedRange = wb.getName( rangeName );
        String formula =  namedRange.getRefersToFormula();
        String[] parts1 = formula.split("!",2);
        CellRangeAddress cra = getCellRangeAddress(parts1[1]);

        return cra.getLastRow();
    }

    private int getStartRow( String rangeName) {

        Name namedRange = wb.getName( rangeName );
        String formula =  namedRange.getRefersToFormula();
        String[] parts1 = formula.split("!",2);
        CellRangeAddress cra = getCellRangeAddress(parts1[1]);

        return cra.getFirstRow();
    }

    public void postExcelResults(HashMap<String, ExcelResult> results, int test) {
        log.info(new MyString("Info: Posting Excel results to results Workbook: ").toString());
        try {
            Sheet sheet = wb.getSheet( ATTENDANCE_TESTS_SHEET );

            int startRow = getStartRow( ATTENDANCE_TESTS_RANGE);
            int endRow = getEndRow( ATTENDANCE_TESTS_RANGE);

            int emSubCol = (test == 1) ? EXCEL1_ATT_TEST_EM_SUB : EXCEL2_ATT_TEST_EM_SUB;
            int qtyFilesCol = (test == 1) ? EXCEL1_ATT_TEST_QTY_FILES : EXCEL2_ATT_TEST_QTY_FILES;
            int snoCol = (test == 1) ? EXCEL1_ATT_TEST_SNO : EXCEL2_ATT_TEST_SNO;
            int bsSubCol =  (test == 1) ? EXCEL1_ATT_TEST_BS_SUB : EXCEL2_ATT_TEST_BS_SUB;

            for ( int row = startRow-1; row < endRow; row++ ) {
                String studentNo = "";
                Row currentRow = sheet.getRow(row);
                System.out.println(row);
                if(rowValid(sheet, row)) {
                    studentNo = currentRow.getCell(ATTENDANCE_TESTS_STUDENT_NO_COL).getStringCellValue().toLowerCase();
                    if (results.containsKey(studentNo)) {
                        ExcelResult result = results.get(studentNo);
                        currentRow.getCell(emSubCol).setCellValue(result.getEmailSubmission());
                        currentRow.getCell(qtyFilesCol).setCellValue(result.getIncorrectFilesSubmitted());
                        currentRow.getCell(snoCol).setCellValue((result.getXlsxSno()));
                        currentRow.getCell(bsSubCol).setCellValue(result.getBrightspaceSubmission());

                    } else log.warning(new MyString("Warning: StudentNo not in results list: ",studentNo).toString());

                } else log.warning(new MyString("Warning: StudentNo not found in results : " , studentNo).toString());
            }
        } catch(Exception e) {
            log.severe(new MyString("Error: Accessing worksheet: " ,ATTENDANCE_TESTS_SHEET).toString());
            System.exit(1);
        }

        if(wb != null) {
            writeExcelFile(wb);
            log.info(new MyString("Info: Posting Excel results to results Workbook: ").toString());
        } else {
            log.severe(new MyString("Error: Workbook wb is null").toString());
            System.exit(1);
        }

    }

    public void writeExcelFile(Workbook wb) {

        try {
            FileOutputStream outputStream = new FileOutputStream(RESULTS_FOLDER + RESULTS_WB );
            wb.write(outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            log.severe(new MyString("Error: File or Worksheet not found: " , RESULTS_FOLDER + RESULTS_WB).toString());
            System.exit(1);
        } catch (IOException e) {
            log.severe(new MyString("Error: Writing to file: " , RESULTS_FOLDER + RESULTS_WB).toString());
            System.exit(1);
        }
    }

    protected CellRangeAddress getCellRangeAddress(String addressString) {
        final String[] split = addressString.split(":");
        final CellReference cr1 = new CellReference(split[0]);
        final CellReference cr2 = new CellReference(split[1]);
        int r1 = Math.min(cr1.getRow(), cr2.getRow());
        int r2 = Math.max(cr1.getRow(), cr2.getRow());
        int c1 = cr1.getCol() > cr2.getCol() ? cr2.getCol() : cr1.getCol();
        int c2 = cr1.getCol() > cr2.getCol() ? cr1.getCol() : cr2.getCol();
        return new CellRangeAddress(r1, r2, c1, c2);
    }

    void closeWorkbook() {
        try {
            Excel.wb.close();

        } catch (IOException e) {
            log.severe("Error: Closing Workbook");
            System.exit(1);
        }
    }
}
