package me.saro.commons.excel;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import me.saro.commons.function.ThrowableFunction;

public interface Excel extends Closeable {
    
    public static Excel createBulkExcel() {
        return new BasicExcel(new SXSSFWorkbook(100), null);
    }
    
    public static Excel create() {
        return new BasicExcel(new XSSFWorkbook(), null);
    }
    
    public static Excel open(File file, boolean overwrite) throws IOException, InvalidFormatException {
        return new BasicExcel(new XSSFWorkbook(file), file);
    }
    
    /**
     * write horizontal list
     * @param startColumnName
     * @param values
     * @return
     */
    public <T> Excel writeHorizontalList(String startColumnName, Collection<T> values);
    
    /**
     * write vertical list
     * @param startColumnName
     * @param values
     * @return
     */
    public <T> Excel writeVerticalList(String startColumnName, Collection<T> values);
    
    /**
     * write table by list class
     * @param startColumnName
     * @param columnNames
     * @param list
     * @return
     */
    public <T> Excel writeTable(String startColumnName, Collection<String> columnNames, List<T> list);
    
    
    /**
     * write pivot table by list class
     * @param startColumnName
     * @param columnNames
     * @param list
     * @return
     */
    public <T> Excel writePivotTable(String startColumnName, Collection<String> columnNames, List<T> list);
    
    /**
     * read row
     * @param index
     * @return Row or null
     */
    public Row readRow(int index);
    
    /**
     * read cell
     * @param rowIndex
     * @param cellIndex
     * @return Cell or null
     */
    public Cell readCell(int rowIndex, int cellIndex);
    
    /**
     * read cell
     * @param startColumnName
     * @return Cell or null
     */
    public Cell readCell(String startColumnName);
    
    /**
     * read table
     * @param startColumnName
     * @param columnCount
     * @param map (List<Cell or null>) : List[T]
     * @return
     */
    public <R> List<R> readTable(String startColumnName, int columnCount, ThrowableFunction<List<Cell>, R> map);
    
    /**
     * read table
     * @param startColumnName
     * @param columnCount
     * @param limitRowCount
     * @param map (List<Cell or null>) : List[T]
     * @return
     */
    public <R> List<R> readTable(String startColumnName, int columnCount, int limitRowCount, ThrowableFunction<List<Cell>, R> map);
    
    /**
     * read pivot table
     * @param startColumnName
     * @param columnCount
     * @param map (List<Cell or null>) : List[T]
     * @return
     */
    public <R> List<R> readPivotTable(String startColumnName, int columnCount, ThrowableFunction<List<Cell>, R> map);
    
    /**
     * read pivot table
     * @param startColumnName
     * @param columnCount
     * @param limitRowCount
     * @param map (List<Cell or null>) : List[T]
     * @return
     */
    public <R> List<R> readPivotTable(String startColumnName, int columnCount, int limitRowCount, ThrowableFunction<List<Cell>, R> map);
    
    /**
     * output excel
     * @param os
     * @throws IOException
     */
    public void output(OutputStream os) throws IOException;
    
    /**
     * save to file
     * @param file
     * @throws IOException
     */
    public void save(File file, boolean overwrite) throws IOException;
    
    /**
     * select sheet
     * @param index
     * @return
     */
    public Excel moveSheet(int index);
    
    /**
     * set sheet name
     * @param name
     * @return
     */
    public Excel setSheetName(String name);
    
    /**
     * size of sheet
     * @return
     */
    public int sheetsLength();
    
    /**
     * size of length
     * @return
     */
    public int rowsLength();
    
    /**
     * move row
     * @param index
     * @return
     */
    public Excel moveRow(int index, boolean forceCreate);
    
    /**
     * move cell
     * @param index
     * @return
     */
    public Excel moveCell(int index, boolean forceCreate);
    
    /**
     * move row and cell
     * @param rowIndex
     * @param cellIndex
     * @return
     */
    public Excel move(int rowIndex, int cellIndex, boolean forceCreate);
    
    /**
     * move
     * @param rowIndex
     * @param cellIndex
     * @return cell or null
     */
    public Excel move(int rowIndex, int cellIndex);
    
    /**
     * move row and cell by excel column name
     * @param columnName
     * @param forceCreate
     * @return
     */
    public Excel move(String columnName, boolean forceCreate);
    
    /**
     * move row and cell by excel column name
     * @param columnName
     * @return cell or null
     */
    public Excel move(String columnName);
    
    /**
     * move next row
     * @param forceCreate
     * @return
     */
    public Excel moveNextRow(boolean forceCreate);
    
    /**
     * move next cell
     * @param forceCreate
     * @return
     */
    public Excel moveNextCell(boolean forceCreate);
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public int getInt(int defaultValue);
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public long getLong(long defaultValue);
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public float getFloat(float defaultValue);
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public double getDouble(double defaultValue);
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public Date getDate(Date defaultValue);
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public String toIntegerString(long defaultValue);
    
    /**
     * get cell value in the moved pos
     * @param defaultValue
     * @return
     */
    public String getString(String defaultValue);
    
    /**
     * get cell value in the moved pos
     * @return
     */
    public String getString();
    
    /**
     * set value
     * @param obj
     * @return
     */
    public Excel setValue(Object obj);
    
    /**
     * get row index
     * @return
     */
    public int getRowIndex();
    
    /**
     * get cell index
     * @return
     */
    public int getCellIndex();
    
    /**
     * get column name<br>
     * ex) E5
     * @return
     */
    public String getColumnName();
    
    /**
     * column name X (cell)<br>
     * ex) AF
     * @return
     */
    public String getColumnNameX();
    
    /**
     * column name Y (row)<br>
     * ex) 3213
     * @return
     */
    public String getColumnNameY();
    
    /**
     * autoSizeColumn
     */
    public void autoSizeColumn();
    
    /**
     * autoSizeColumn
     * @param cellIndex
     */
    public void autoSizeColumn(int cellIndex);
    
    /**
     * style
     * @param columnName
     * @return
     */
    public ExcelStyle style(String columnName);
    
    /**
     * style
     * @param columnName
     * @return
     */
    public ExcelStyle style(String startColumnName, String endColumnName);
    
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
            rv[--offset] = (char)(65 + ((idx) % 26));
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
            case 3 : rv += ((ca[pos++] - 65) + 1) * 26 * 26;
            case 2 : rv += ((ca[pos++] - 65) + 1) * 26;
            case 1 : rv += (ca[pos++] - 65);
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
