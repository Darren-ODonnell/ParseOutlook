package Excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellReferenceType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;

public class NamedRange {
    String rangeStr="";
    Cell start;
    Cell end;
    int rowIndex;
    int columnIndex;

    public NamedRange(String range) {
        String[] parts = range.split("!");
//        Cell startRange = getStartRange(parts[1]);
//        Cell endRange = getEndRange(parts[1]);
    }
    public NamedRange(Cell start, Cell end) {
        this.start = start;
        this.end = end;
    }



}
