package me.saro.commons;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


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
    
    final static int CHAR_A = (int)'A';
    
    File file;
    
    // private
    private Excel(Workbook book, File file) {
        this.book = book;
        this.file = file;
        moveSheet(0).move(0, 0);
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
    public Excel writeHorizontalList(String startColumnName, Collection<String> values) {
        if (values != null) {
            move(startColumnName);
            int ci = this.cellIndex;
            for (String value : values) {
                moveCell(ci++).setCellValue(value);
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
    public Excel writeVerticalList(String startColumnName, Collection<String> values) {
        if (values != null) {
            int[] rc = toRowCellIndex(startColumnName);
            int ri = rc[0];
            int ci = rc[1];
            for (String value : values) {
                move(ri++, ci).setCellValue(value);
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
    public <T> Excel writeTableByListClass(String startColumnName, Collection<String> columnNames, List<T> list) {
        if (list != null && !list.isEmpty()) {
            int[] rc = toRowCellIndex(startColumnName);
            int ri = rc[0];
            int sci = rc[1];
            int ci = sci;
            for (T t : list) {
                Map<String, Object> map = Converter.toMapByClass(t);
                moveRow(ri++);
                ci = sci;
                for (String name : columnNames) {
                    setCellValueAuto(moveCell(ci++), map.get(name));
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
    public <T> Excel writePivotTableByListClass(String startColumnName, Collection<String> columnNames, List<T> list) {
        if (list != null && !list.isEmpty()) {
            int[] rc = toRowCellIndex(startColumnName);
            int sri = rc[0];
            int ri = sri;
            int ci = rc[1];
            for (T t : list) {
                Map<String, Object> map = Converter.toMapByClass(t);
                ri = sri;
                for (String name : columnNames) {
                    setCellValueAuto(move(ri++, ci), map.get(name));
                }
                ci++;
            }
        }
        return this;
    }
    
    /**
     * write table by list map
     * @param startColumnName
     * @param columnNames
     * @param listMap
     * @return
     */
    public <V> Excel writeTableByListMap(String startColumnName, Collection<String> columnNames, List<Map<String, V>> listMap) {
        if (listMap != null && !listMap.isEmpty()) {
            int[] rc = toRowCellIndex(startColumnName);
            int ri = rc[0];
            int sci = rc[1];
            int ci = sci;
            for (Map<String, V> map : listMap) {
                moveRow(ri++);
                ci = sci;
                for (String name : columnNames) {
                    setCellValueAuto(moveCell(ci++), map.get(name));
                }
            }
        }
        return this;
    }
    
    /**
     * write pivot table by list map
     * @param startColumnName
     * @param columnNames
     * @param listMap
     * @return
     */
    public <V> Excel writePivotTableByListMap(String startColumnName, Collection<String> columnNames, List<Map<String, V>> listMap) {
        if (listMap != null && !listMap.isEmpty()) {
            int[] rc = toRowCellIndex(startColumnName);
            int sri = rc[0];
            int ri = sri;
            int ci = rc[1];
            for (Map<String, V> map : listMap) {
                ri = sri;
                for (String name : columnNames) {
                    setCellValueAuto(move(ri++, ci), map.get(name));
                }
                ci++;
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
     * move row
     * @param index
     * @return
     */
    public Excel moveRow(int index) {
        move(index, 0);
        return this;
    }
    
    /**
     * move cell
     * @param index
     * @return
     */
    public Cell moveCell(int index) {
        Cell cell = this.row.getCell(index);
        if (cell == null) {
            cell = row.createCell(index);
        }
        this.cellIndex = index;
        return cell;
    }
    
    /**
     * move row and cell
     * @param rowIndex
     * @param cellIndex
     * @return
     */
    public Cell move(int rowIndex, int cellIndex) {
        if (rowIndex != this.rowIndex) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }
            this.row = row;
            this.rowIndex = rowIndex;
        }
        return moveCell(cellIndex);
    }
    
    /**
     * move row and cell by excel column name
     * @param columnName
     * @return
     */
    public Cell move(String columnName) {
        int[] rc = toRowCellIndex(columnName);
        return move(rc[0], rc[1]);
    }
    
    /**
     * move next row
     * @return
     */
    public Excel nextRow() {
        return moveRow(rowIndex + 1);
    }
    
    /**
     * move next cell
     * @return
     */
    public Cell nextCell() {
        return moveCell(cellIndex + 1);
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
     * set value
     * @param cell
     * @param obj
     * @return
     */
    public static Cell setCellValueAuto(Cell cell, Object obj) {
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
