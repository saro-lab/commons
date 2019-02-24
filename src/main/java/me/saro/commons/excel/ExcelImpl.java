package me.saro.commons.excel;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import me.saro.commons.Converter;
import me.saro.commons.NullOutputStream;
import me.saro.commons.function.ThrowableFunction;

/**
 * excel
 * @author      PARK Yong Seo
 * @since       2.0
 */
public class ExcelImpl extends ExcelStatic implements Closeable {
    
    final private Workbook book;
    final private boolean bulk;
    
    private int sheetIndex = -1;
    private int rowIndex = - 1;
    private int cellIndex = -1;
    
    private Sheet sheet;
    private Row row;
    private Cell cell;
    
    File file;
    
    private ExcelImpl(Workbook book, File file) {
        this.book = book;
        this.file = file;
        this.bulk = book.getClass().getName().equals(SXSSFWorkbook.class.getName());
        moveSheet(0).move(0, 0, false);
    }
    
    public static ExcelImpl createBulkExcel() {
        return new ExcelImpl(new SXSSFWorkbook(100), null);
    }
    
    public static ExcelImpl create() {
        return new ExcelImpl(new XSSFWorkbook(), null);
    }
    
    public static ExcelImpl open(File file, boolean overwrite) throws IOException, InvalidFormatException {
        return new ExcelImpl(new XSSFWorkbook(file), file);
    }
    
    /**
     * write horizontal list
     * @param startColumnName
     * @param values
     * @return
     */
    public <T> ExcelImpl writeHorizontalList(String startColumnName, Collection<T> values) {
        if (values != null) {
            move(startColumnName, true);
            int ci = this.cellIndex;
            for (T value : values) {
                moveCell(ci++, true);
                setCellValueAuto(this.cell, value);
            }
        }
        return this;
    }
    
    /**
     * write vertical list
     * @param startColumnName
     * @param values
     * @return
     */
    public <T> ExcelImpl writeVerticalList(String startColumnName, Collection<T> values) {
        if (values != null) {
            int[] rc = toRowCellIndex(startColumnName);
            int ri = rc[0];
            int ci = rc[1];
            for (T value : values) {
                move(ri++, ci, true);
                setCellValueAuto(this.cell, value);
            }
        }
        return this;
    }
    
    /**
     * write table by list class
     * @param startColumnName
     * @param columnNames
     * @param list
     * @return
     */
    public <T> ExcelImpl writeTable(String startColumnName, Collection<String> columnNames, List<T> list) {
        if (list != null && !list.isEmpty()) {
            int[] rc = toRowCellIndex(startColumnName);
            int ri = rc[0];
            int sci = rc[1];
            int ci = sci;
            for (T t : list) {
                Map<String, Object> map = Converter.toMapByClass(t);
                moveRow(ri++, true);
                ci = sci;
                for (String name : columnNames) {
                    moveCell(ci++, true);
                    setCellValueAuto(this.cell, map.get(name));
                }
            }
        }
        return this;
    }
    
    /**
     * write pivot table by list class
     * @param startColumnName
     * @param columnNames
     * @param list
     * @return
     */
    public <T> ExcelImpl writePivotTable(String startColumnName, Collection<String> columnNames, List<T> list) {
        if (list != null && !list.isEmpty()) {
            int[] rc = toRowCellIndex(startColumnName);
            int sri = rc[0];
            int ri = sri;
            int ci = rc[1];
            for (T t : list) {
                Map<String, Object> map = Converter.toMapByClass(t);
                ri = sri;
                for (String name : columnNames) {
                    move(ri++, ci, true);
                    setCellValueAuto(this.cell, map.get(name));
                }
                ci++;
            }
        }
        return this;
    }
    
    /**
     * read row
     * @param index
     * @return Row or null
     */
    public Row readRow(int index) {
        return sheet.getRow(index);
    }
    
    /**
     * read cell
     * @param rowIndex
     * @param cellIndex
     * @return Cell or null
     */
    public Cell readCell(int rowIndex, int cellIndex) {
        Row row = sheet.getRow(rowIndex);
        return row != null ? row.getCell(cellIndex) : null;
    }
    
    /**
     * read cell
     * @param startColumnName
     * @return Cell or null
     */
    public Cell readCell(String startColumnName) {
        int[] rc = toRowCellIndex(startColumnName);
        return readCell(rc[0], rc[1]);
    }
    
    /**
     * read table
     * @param startColumnName
     * @param columnCount
     * @param map (List<Cell or null>) : List[T]
     * @return
     */
    public <R> List<R> readTable(String startColumnName, int columnCount, ThrowableFunction<List<Cell>, R> map) {
        return readTable(startColumnName, columnCount, 2000000, map);
    }
    
    /**
     * read table
     * @param startColumnName
     * @param columnCount
     * @param limitRowCount
     * @param map (List<Cell or null>) : List[T]
     * @return
     */
    public <R> List<R> readTable(String startColumnName, int columnCount, int limitRowCount, ThrowableFunction<List<Cell>, R> map) {
        int[] rc = toRowCellIndex(startColumnName);
        int ri = rc[0];
        int eri = Math.min(ri + limitRowCount, sheet.getLastRowNum() + 1);
        int ci = rc[1];
        int eci = ci + columnCount;
        
        List<R> rv = new ArrayList<>();
        
        while (ri < eri) {
            try {
                Row row = readRow(ri++);
                if (row == null) {
                    continue;
                }
                List<Cell> list = new ArrayList<>(columnCount);
                for (int i = ci ; i < eci ; i++) {
                    list.add(row.getCell(i));
                }
                R r = map.apply(list);
                if (r != null) {
                    rv.add(r);
                }
            } catch (Exception e) {
                throw new RuntimeException("row["+toColumnNameByRowIndex(ri)+"] : " + e.getMessage(), e);
            }
        }
        
        return rv;
    }
    
    /**
     * read pivot table
     * @param startColumnName
     * @param columnCount
     * @param map (List<Cell or null>) : List[T]
     * @return
     */
    public <R> List<R> readPivotTable(String startColumnName, int columnCount, ThrowableFunction<List<Cell>, R> map) {
        return readPivotTable(startColumnName, columnCount, 10000, map);
    }
    
    /**
     * read pivot table
     * @param startColumnName
     * @param columnCount
     * @param limitRowCount
     * @param map (List<Cell or null>) : List[T]
     * @return
     */
    public <R> List<R> readPivotTable(String startColumnName, int columnCount, int limitRowCount, ThrowableFunction<List<Cell>, R> map) {
        
        int[] rc = toRowCellIndex(startColumnName);
        int ri = rc[0];
        int ci = rc[1];
        int eci = ci + limitRowCount;
        
        Row[] rows = new Row[columnCount];
        for (int i = 0 ; i < columnCount ; i++) {
            if ((rows[i] = readRow(ri + i)) == null) {
                throw new IllegalArgumentException(toColumnName(ri + i, ci) + " is does not exist");
            }
        }
        
        List<R> rv = new ArrayList<>();
        
        stop : while (ci < eci) {
            try {
                int nullCnt = 0;
                List<Cell> list = new ArrayList<>(columnCount);
                for (int i = 0 ; i < columnCount ; i++) {
                    Cell cell = rows[i].getCell(ci);
                    if (cell == null) {
                        nullCnt++;
                    }
                    list.add(cell);
                }
                if (columnCount == nullCnt) {
                    break stop;
                }
                ci++;
                R r = map.apply(list);
                if (r != null) {
                    rv.add(r);
                }
            } catch (Exception e) {
                throw new RuntimeException("column["+toColumnNameByCellIndex(ci)+"] : " + e.getMessage(), e);
            }
        }
        
        return rv;
    }
    
    /**
     * output excel
     * @param os
     * @throws IOException
     */
    public void output(OutputStream os) throws IOException {
        book.write(os);
        os.flush();
    }
    
    /**
     * save to file
     * @param file
     * @throws IOException
     */
    public void save(File file, boolean overwrite) throws IOException {
        if (file.exists()) {
            if (!overwrite) {
                throw new IOException("file exists : " + file.getAbsolutePath());
            }
            file.delete();
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            book.write(fos);
            fos.flush();
        }
    }

    /**
     * close
     */
    @Override
    public void close() throws IOException {
        try (OutputStream bos = new NullOutputStream()){
            book.write(bos);
            bos.flush();
        } catch (IOException e) {
        }
        try {
            book.close();
        } catch (IOException e) {
        }
    }
    
    /**
     * select sheet
     * @param index
     * @return
     */
    public ExcelImpl moveSheet(int index) {
        if (book.getNumberOfSheets() <= index) {
            int need = (index + 1) - book.getNumberOfSheets();
            for (int i = 0 ; i < need ; i++) {
                book.createSheet();
            }
        }
        sheet = book.getSheetAt((this.sheetIndex = index));
        return this;
    }
    
    /**
     * set sheet name
     * @param name
     * @return
     */
    public ExcelImpl setSheetName(String name) {
        book.setSheetName(sheetIndex, name);
        return this;
    }
    
    /**
     * size of sheet
     * @return
     */
    public int sheetsLength() {
        return book.getNumberOfSheets();
    }
    
    /**
     * size of length
     * @return
     */
    public int rowsLength() {
        return sheet.getLastRowNum() + 1;
    }
    
    /**
     * move row
     * @param index
     * @return
     */
    public ExcelImpl moveRow(int index, boolean forceCreate) {
        if (index != this.rowIndex || (this.row == null && forceCreate)) {
            this.row = sheet.getRow(index);
            this.rowIndex = index;
            if (forceCreate && this.row == null) {
                this.row = sheet.createRow(index);
            }
        }
        return this;
    }
    
    /**
     * move cell
     * @param index
     * @return
     */
    public ExcelImpl moveCell(int index, boolean forceCreate) {
        if (this.row != null) {
            cell = row.getCell(index);
        } else if (forceCreate) {
            moveRow(this.rowIndex, forceCreate);
            cell = row.getCell(index);
        } else {
            cell = null;
        }
        if (cell == null && forceCreate) {
            cell = row.createCell(index);
        }
        this.cellIndex = index;
        return this;
    }
    
    /**
     * move row and cell
     * @param rowIndex
     * @param cellIndex
     * @return
     */
    public ExcelImpl move(int rowIndex, int cellIndex, boolean forceCreate) {
        return moveRow(rowIndex, forceCreate).moveCell(cellIndex, forceCreate);
    }
    
    /**
     * move
     * @param rowIndex
     * @param cellIndex
     * @return cell or null
     */
    public ExcelImpl move(int rowIndex, int cellIndex) {
        return move(rowIndex, cellIndex, false);
    }
    
    /**
     * move row and cell by excel column name
     * @param columnName
     * @param forceCreate
     * @return
     */
    public ExcelImpl move(String columnName, boolean forceCreate) {
        int[] rc = toRowCellIndex(columnName);
        return move(rc[0], rc[1], forceCreate);
    }
    
    /**
     * move row and cell by excel column name
     * @param columnName
     * @return cell or null
     */
    public ExcelImpl move(String columnName) {
        int[] rc = toRowCellIndex(columnName);
        return move(rc[0], rc[1]);
    }
    
    /**
     * move next row
     * @param forceCreate
     * @return
     */
    public ExcelImpl moveNextRow(boolean forceCreate) {
        return moveRow(rowIndex + 1, forceCreate);
    }
    
    /**
     * move next cell
     * @param forceCreate
     * @return
     */
    public ExcelImpl moveNextCell(boolean forceCreate) {
        return moveCell(cellIndex + 1, forceCreate);
    }
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public int getInt(int defaultValue) {
        return toInt(cell, defaultValue);
    }
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public long getLong(long defaultValue) {
        return toLong(cell, defaultValue);
    }
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public float getFloat(float defaultValue) {
        return toFloat(cell, defaultValue);
    }
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public double getDouble(double defaultValue) {
        return toDouble(cell, defaultValue);
    }
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public Date getDate(Date defaultValue) {
        return toDate(cell, defaultValue);
    }
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public String toIntegerString(long defaultValue) {
        return toIntegerString(cell, defaultValue);
    }
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public String getString(String defaultValue) {
        return toString(cell, defaultValue);
    }
    
    /**
     * get cell value in the moved pos
     * @return
     */
    public String getString() {
        return toString(cell, null);
    }
    
    /**
     * set value
     * @param obj
     * @return
     */
    public ExcelImpl setValue(Object obj) {
        if (cell == null) {
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }
            cell = row.createCell(cellIndex);
        }
        setCellValueAuto(cell, obj);
        return this;
    }
    
    /**
     * get row index
     * @return
     */
    public int getRowIndex() {
        return this.rowIndex;
    }
    
    /**
     * get cell index
     * @return
     */
    public int getCellIndex() {
        return this.cellIndex;
    }
    
    /**
     * get column name<br>
     * ex) E5
     * @return
     */
    public String getColumnName() {
        return getColumnNameX() + getColumnNameY();
    }
    
    /**
     * column name X (cell)<br>
     * ex) AF
     * @return
     */
    public String getColumnNameX() {
        return toColumnNameByCellIndex(this.cellIndex);
    }
    
    /**
     * column name Y (row)<br>
     * ex) 3213
     * @return
     */
    public String getColumnNameY() {
        return toColumnNameByRowIndex(this.rowIndex);
    }
    
    /**
     * autoSizeColumn
     */
    public void autoSizeColumn() {
        if (bulk) {
            throw new RuntimeException("bulk mode does not support autoSizeColumn");
        }
        Set<Integer> set = new HashSet<>();
        for (Row row : sheet) {
            for (Cell cell : row) {
                set.add(cell.getColumnIndex());
            }
        }
        for (Integer i : set) {
            sheet.autoSizeColumn(i);
        }
    }
    
    /**
     * autoSizeColumn
     * @param cellIndex
     */
    public void autoSizeColumn(int cellIndex) {
        if (bulk) {
            throw new RuntimeException("bulk mode does not support autoSizeColumn");
        }
        sheet.autoSizeColumn(cellIndex);
    }
    
    /**
     * style
     * @param columnName
     * @return
     */
    public ExcelStyle style(String columnName) {
        if (bulk) {
            throw new RuntimeException("bulk mode does not support style");
        }
        int[] rc = toRowCellIndex(columnName);
        return new ExcelStyle((XSSFSheet) sheet, rc[0], rc[1], rc[0], rc[1]);
    }
    
    /**
     * style
     * @param columnName
     * @return
     */
    public ExcelStyle style(String startColumnName, String endColumnName) {
        if (bulk) {
            throw new RuntimeException("bulk mode does not support style");
        }
        int[] src = toRowCellIndex(startColumnName);
        int[] erc = toRowCellIndex(endColumnName);
        return new ExcelStyle((XSSFSheet) sheet, src[0], src[1], erc[0], erc[1]);
    }
}
