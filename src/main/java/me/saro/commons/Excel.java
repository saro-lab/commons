package me.saro.commons;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import me.saro.commons.function.ThrowableFunction;


/**
 * excel
 * @author      PARK Yong Seo
 * @since       2.0
 */
public class Excel implements Closeable {
    
    final private Workbook book;
    
    private int sheetIndex = -1;
    private int rowIndex = - 1;
    private int cellIndex = -1;
    
    private Sheet sheet;
    private Row row;
    private Cell cell;
    
    final private static int CHAR_A = (int)'A';
    
    File file;
    
    // private
    private Excel(Workbook book, File file) {
        this.book = book;
        this.file = file;
        moveSheet(0).move(0, 0, false);
    }
    
    /**
     * create excel .xls<br>
     * does not recommend .xls file
     * @see createXlsx
     * @return
     */
    @Deprecated
    public static Excel createXls() {
        return new Excel(new HSSFWorkbook(), null);
    }
    
    /**
     * create excel file .xls<br>
     * does not recommend .xls file
     * @param file
     * @param overwrite
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    @Deprecated
    public static Excel createXls(File file, boolean overwrite) throws IOException, InvalidFormatException {
        try (Excel excel = createXls()){
            excel.save(file, overwrite);
        }
        return open(file);
    }
    
    /**
     * open excel file<br>
     * does not recommend .xls file
     * @param file
     * @return
     * @throws IOException
     * @throws InvalidFormatException 
     */
    @Deprecated
    public static Excel openXls(File file) throws IOException, InvalidFormatException {
        return new Excel(new HSSFWorkbook(POIFSFileSystem.create(file)), file);
    }
    
    /**
     * clone excel file to file<br>
     * does not recommend .xls file
     * @param openFile
     * @param saveFile
     * @return
     * @throws IOException
     * @throws InvalidF
     * ormatException 
     */
    @Deprecated
    public static Excel createCloneXls(File openFile, File saveFile, boolean overwrite) throws IOException, InvalidFormatException {
        if (saveFile.exists()) {
            if (!overwrite) {
                throw new IOException("file exists : " + saveFile.getAbsolutePath());
            }
            saveFile.delete();
        }
        Files.copy(openFile.toPath(), saveFile.toPath());
        return openXls(saveFile);
    }
    
    /**
     * create excel .xlsx<br>
     * keep just 100 row in memory : new SXSSFWorkbook(rowAccessWindowSize:100)
     * @see org.apache.poi.xssf.streaming.SXSSFWorkbook.SXSSFWorkbook
     * @return
     */
    public static Excel create() {
        return new Excel(new SXSSFWorkbook(100), null);
    }
    
    /**
     * create excel file .xlsx
     * @param file
     * @param overwrite
     * @return
     * @throws IOException
     * @throws InvalidFormatException 
     */
    public static Excel create(File file, boolean overwrite) throws IOException, InvalidFormatException {
        try (Excel excel = create()){
            excel.save(file, overwrite);
        }
        return open(file);
    }
    
    /**
     * open excel file
     * @param file
     * @return
     * @throws IOException
     * @throws InvalidFormatException 
     */
    public static Excel open(File file) throws IOException, InvalidFormatException {
        return new Excel(new XSSFWorkbook(file), file);
    }
    
    /**
     * clone excel file to file
     * @param openFile
     * @param saveFile
     * @return
     * @throws IOException
     * @throws InvalidFormatException 
     */
    public static Excel createClone(File openFile, File saveFile, boolean overwrite) throws IOException, InvalidFormatException {
        if (saveFile.exists()) {
            if (!overwrite) {
                throw new IOException("file exists : " + saveFile.getAbsolutePath());
            }
            saveFile.delete();
        }
        Files.copy(openFile.toPath(), saveFile.toPath());
        return open(saveFile);
    }
    
    /**
     * write horizontal list
     * @param startColumnName
     * @param values
     * @return
     */
    public <T> Excel writeHorizontalList(String startColumnName, Collection<T> values) {
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
    public <T> Excel writeVerticalList(String startColumnName, Collection<T> values) {
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
    public <T> Excel writeTable(String startColumnName, Collection<String> columnNames, List<T> list) {
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
    public <T> Excel writePivotTable(String startColumnName, Collection<String> columnNames, List<T> list) {
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
     * @param rowIndex
     * @param cellIndex
     * @param forceCreate if cell is not exist then create cell
     * @return
     */
    public Cell readCell(int rowIndex, int cellIndex, boolean forceCreate) {
        if (!forceCreate) {
            return readCell(rowIndex, cellIndex);
        }
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(cellIndex);
        if (cell == null) {
            cell = row.createCell(cellIndex);
        }
        return cell;
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
     * read cell
     * @param columnName
     * @param forceCreate if cell is not exist then create cell
     * @return
     */
    public Cell readCell(String columnName, boolean forceCreate) {
        int[] rc = toRowCellIndex(columnName);
        return readCell(rc[0], rc[1], forceCreate);
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
     * get cell style
     * @param startColumnName
     * @param forceCell if cell is not exist then create cell
     * @return
     */
    public CellStyle getCellStyle(String columnName, boolean forceCell) {
        Cell cell = readCell(columnName, forceCell);
        return cell != null ? cell.getCellStyle() : null;
    }
    
    /**
     * autoSizeColumn
     * @param startColumnName
     * @param endColumnName
     * @return
     */
    public Excel autoSizeColumn(String startColumnName, String endColumnName) {
        int[] src = toRowCellIndex(startColumnName);
        int[] erc = toRowCellIndex(endColumnName);
        int sc = src[1];
        int ec = erc[1];
        System.out.println(sc+"/"+ec);
        if (sc > ec) {
            throw new IllegalArgumentException(startColumnName + " is bigger than " + endColumnName);
        }
        
        for ( ; sc <= ec ; sc++) {
            System.out.println(sc);
            try {
                sheet.autoSizeColumn(sc, true);
                System.out.println("통과");
            } catch (IllegalStateException ise) {
                System.out.println("에러");
            }
            
        }
        return this;
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
    public Excel moveSheet(int index) {
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
    public Excel setSheetName(String name) {
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
    public Excel moveRow(int index, boolean forceCreate) {
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
    public Excel moveCell(int index, boolean forceCreate) {
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
    public Excel move(int rowIndex, int cellIndex, boolean forceCreate) {
        return moveRow(rowIndex, forceCreate).moveCell(cellIndex, forceCreate);
    }
    
    /**
     * move
     * @param rowIndex
     * @param cellIndex
     * @return cell or null
     */
    public Excel move(int rowIndex, int cellIndex) {
        return move(rowIndex, cellIndex, false);
    }
    
    /**
     * move row and cell by excel column name
     * @param columnName
     * @param forceCreate
     * @return
     */
    public Excel move(String columnName, boolean forceCreate) {
        int[] rc = toRowCellIndex(columnName);
        return move(rc[0], rc[1], forceCreate);
    }
    
    /**
     * move row and cell by excel column name
     * @param columnName
     * @return cell or null
     */
    public Excel move(String columnName) {
        int[] rc = toRowCellIndex(columnName);
        return move(rc[0], rc[1]);
    }
    
    /**
     * move next row
     * @param forceCreate
     * @return
     */
    public Excel moveNextRow(boolean forceCreate) {
        return moveRow(rowIndex + 1, forceCreate);
    }
    
    /**
     * move next cell
     * @param forceCreate
     * @return
     */
    public Excel moveNextCell(boolean forceCreate) {
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
    public Excel setValue(Object obj) {
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
     * to column name by row index
     * @param rowIndex
     * @return
     */
    public static String toColumnNameByRowIndex(int rowIndex) {
        return Integer.toString(rowIndex + 1);
    }
    
    /**
     * to column name by cell index
     * @param cellIndex
     * @return
     */
    public static String toColumnNameByCellIndex(int cellIndex) {
        int offset = 4;
        int max = 4;
        int idx = cellIndex;
        char[] rv = new char[max];
        
        do {
            rv[--offset] = (char)(CHAR_A + ((idx) % 26));
        } while ((idx = ((idx / 26) - 1)) >= 0);
        
        return new String(rv, offset, max - offset);
    }
    
    /**
     * to column name
     * @param rowIndex
     * @param cellIndex
     * @return
     */
    public static String toColumnName(int rowIndex, int cellIndex) {
        return toColumnNameByCellIndex(cellIndex) + toColumnNameByRowIndex(rowIndex); 
    }
    
    /**
     * to row index
     * @param rowColumnName
     * @return
     */
    public static int toRowIndex(String rowColumnName) {
        return Integer.parseInt(rowColumnName) - 1;
    }

    /**
     * to cell index
     * @param cellColumnName
     * @return
     */
    public static int toCellIndex(String cellColumnName) {
        if (!cellColumnName.matches("[A-Z]{1,3}")) {
            throw new IllegalArgumentException(cellColumnName + " is not cellColumnName : ex) AF");
        }
        char[] ca = cellColumnName.toCharArray();
        int rv = 0;
        int pos = 0;
        switch (ca.length) {
            case 3 : rv += ((ca[pos++] - CHAR_A) + 1) * 26 * 26;
            case 2 : rv += ((ca[pos++] - CHAR_A) + 1) * 26;
            case 1 : rv += (ca[pos++] - CHAR_A);
        }
        return rv;
    }
    
    /**
     * to row cell index
     * @param columnName
     * @return int[]{rowIndex, cellIndex}
     */
    public static int[] toRowCellIndex(String columnName) {
        if (!columnName.matches("[A-Z]+[\\d]+")) {
            throw new IllegalArgumentException(columnName + " is not columnName : ex) E3");
        }
        return new int[] { toRowIndex(columnName.replaceFirst("[A-Z]+", "")), toCellIndex(columnName.replaceFirst("[\\d]+", "")) };
    }
    
    /**
     * toInt by cell
     * @param cell
     * @param defaultValue
     * @return
     */
    public static int toInt(Cell cell, int defaultValue) {
        return (int)toDouble(cell, defaultValue);
    }
    
    /**
     * toLong by cell
     * @param cell
     * @param defaultValue
     * @return
     */
    public static long toLong(Cell cell, long defaultValue) {
        return (long)toDouble(cell, defaultValue);
    }
    
    /**
     * toFloat by cell
     * @param cell
     * @param defaultValue
     * @return
     */
    public static float toFloat(Cell cell, float defaultValue) {
        return (float)toDouble(cell, defaultValue);
    }
    
    /**
     * toDouble by cell
     * @param cell
     * @param defaultValue
     * @return
     */
    public static double toDouble(Cell cell, double defaultValue) {
        if (cell != null) {
            String tmp;
            switch (cell.getCellType()) {
                case BOOLEAN:
                    return cell.getBooleanCellValue() ? 1 : 0;
                case NUMERIC:
                    return cell.getNumericCellValue();
                case FORMULA:
                    if ((tmp = cell.getCellFormula()) != null && !tmp.isEmpty()) {
                        return Double.parseDouble(tmp);
                    }
                case STRING:
                    if ((tmp = cell.getStringCellValue()) != null && !tmp.isEmpty()) {
                        return Double.parseDouble(tmp);
                    }
                case _NONE: case ERROR: case BLANK: default:
            }
        }
        return defaultValue;
    }
    
    /**
     * toDate by cell
     * @param cell
     * @param defaultValue
     * @return
     */
    public static Date toDate(Cell cell, Date defaultValue) {
        if (cell.getCellType() == CellType.STRING) {
            try {
                return cell.getDateCellValue();
            } catch (Exception e) {}
        }
        return defaultValue;
    }
    
    /**
     * toIntegerString by cell<br>
     * Integer String = Long.toString((long)doubleValue)
     * @param cell
     * @param defaultValue
     * @return
     */
    public static String toIntegerString(Cell cell, long defaultValue) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return Long.toString((long)cell.getNumericCellValue());
        }
        String val = toString(cell, null);
        if (val == null) {
            return Long.toString(defaultValue);
        }
        if ((val = val.trim()).matches("[\\d]+\\.[\\d]+")) {
            val = val.substring(0, val.indexOf('.'));
        }
        return val.matches("[\\d]+") ? val : Long.toString(defaultValue);
    }
    
    /**
     * toString by cell
     * @param cell
     * @param defaultValue
     * @return
     * BOOLEAN [1, 0]<br>
     * FORMULA -> String<br>
     * NUMERIC -> Double.toString<br>
     * STRING -> String<br>
     * ETC -> defaultValue
     */
    public static String toString(Cell cell, String defaultValue) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case BOOLEAN:
                    return cell.getBooleanCellValue() ? "1" : "0";
                case FORMULA:
                    return cell.getCellFormula();
                case NUMERIC:
                    return Double.toString(cell.getNumericCellValue());
                case STRING:
                    return cell.getStringCellValue();
                case _NONE: case ERROR: case BLANK: default:
            }
        }
        return defaultValue;
    }
    
    /**
     * set value
     * @param cell
     * @param obj
     * @return
     */
    public static Cell setCellValueAuto(Cell cell, Object obj) {
        if (obj == null) {
            cell.setCellValue((String)null);
            return cell;
        }
        set : switch (obj.getClass().getName()) {
            case "int" : case "java.lang.Integer" :
                cell.setCellValue((double)(int)obj);
                break set;
            case "long" : case "java.lang.Long" : 
                cell.setCellValue((double)(long)obj);
                break set;
            case "float" : case "java.lang.Float" :
                cell.setCellValue((double)(float)obj);
                break set;
            case "double" : case "java.lang.Double" :
                cell.setCellValue((double)obj);
                break set;
            case "Date" :
                cell.setCellValue((Date)obj);
                break set;
            default :
                cell.setCellValue(obj.toString());
        }
        return cell;
    }
}
