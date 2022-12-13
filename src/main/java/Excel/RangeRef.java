package Excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;


public class RangeRef {
    Cell start;
    Cell end;

    public RangeRef(XSSFSheet sheet, String formula) {
        String[] parts = formula.split("[!:?]");
        String startRef = "";
        String endRef = "";
        switch(parts.length) {
            case 1:
                // simple string no split found (no '!' or ':' found
                break;
            case 2:
                // no sheet found - "CellRef:CellRef"
                startRef = parts[0];
                endRef = parts[1];
                break;
            case 3:
                // full formula - sheet plus cell refs "Sheet|CellRef:CellRef"
                startRef = parts[1];
                endRef = parts[2];
                break;
            default:
                // string has too many parts
                break;
        }
        CellReference refStart = new CellReference(startRef);
        CellReference refEnd = new CellReference(endRef);

        Row row1 = sheet.createRow(refStart.getRow());
        Row row2 = sheet.createRow(refEnd.getRow());

        start = row1.createCell(refStart.getCol());
        end = row2.createCell(refEnd.getCol());
    }

    public RangeRef(Cell start, Cell end) {
        this.start = start;
        this.end = end;
    }

    public Cell getStart() {
        return start;
    }

    public void setStart(Cell start) {
        this.start = start;
    }

    public Cell getEnd() {
        return end;
    }

    public void setEnd(Cell end) {
        this.end = end;
    }
}
