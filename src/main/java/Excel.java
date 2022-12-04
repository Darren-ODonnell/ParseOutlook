import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileInputStream;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel  {

    public static void main(String[]args) {
        String path = "C:\\Users\\liam\\OneDrive - Technological University Dublin\\Tests Admin Automate\\";
        String excelFile = "classlist.xlsx";
        List<ClasslistRow> classlist = getClasslist(excelFile);
    }

    public static List<ClasslistRow> getClasslist(String filename) {
        String path = "C:\\Users\\liam\\OneDrive - Technological University Dublin\\Tests Admin Automate\\";
        String excelFile = "classlist.xlsx";

        List<ClasslistRow> classlist = new ArrayList<>();
        try {
            File file = new File(path + excelFile);//creating a new file instance
            FileInputStream fis = new FileInputStream(file);//obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheet("classlist");
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

        Iterator<Cell> cellIterator = row.cellIterator();//iterating over each column
        Cell cell = cellIterator.next();
        listRow.sno = cell.getStringCellValue();
        // firct column
        cellIterator.hasNext();
        cell = cellIterator.next();
        String snfn = cell.getStringCellValue();
        listRow.surnameFirtsname = snfn.replace(".","");
        // second column
        cellIterator.hasNext();
        cell = cellIterator.next();
        listRow.fullname = cell.getStringCellValue();
        // third column
        cellIterator.hasNext();
        cell = cellIterator.next();
        listRow.fullname1 = cell.getStringCellValue();

        String[] parts = listRow.surnameFirtsname.split(",",2);
        String sn = parts[0];
        String fn = parts[1];

        sn = sn.strip();
        fn = fn.strip();

        fn = fn.replace(".","");

        listRow.surname = sn;
        listRow.firstname = fn;

        System.out.println(listRow.toString());

        return listRow;

    }



}