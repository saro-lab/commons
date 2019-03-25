package me.saro.commons.excel;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import me.saro.commons.Utils;

public class ExcelCell {
    
    final ExcelRow row;
    final int cellIndex;
    private Cell pCell;
    
    ExcelCell(ExcelRow row, int cellIndex) {
        this.row = row;
        this.cellIndex = cellIndex;
    }
    
    public Cell getPoiCell(boolean force) {
        Row pRow = row.getPoiRow(force);
        if (pRow != null) {
            pCell = pRow.getCell(cellIndex);
            if (pCell == null && force) {
                pCell = pRow.createCell(cellIndex);
            }
        }
        return pCell;
    }
    
    public boolean isEmpty() {
        return getPoiCell(false) == null;
    }
    
    public ExcelCell getNextCell() {
        return new ExcelCell(row, cellIndex + 1);
    }
    
    public ExcelCell getNextRowCell() {
        return new ExcelCell(row.getNextRow(), cellIndex);
    }
    
    public ExcelCell set(Object val) {
        setCellValueAuto(getPoiCell(true), val);
        return this;
    }
    
    public int getIntValue(int defaultValue) {
        return (int)toDouble(getPoiCell(false), defaultValue);
    }
    
    public long getLongValue(long defaultValue) {
        return (long)toDouble(getPoiCell(false), defaultValue);
    }
    
    public float getFloatValue(float defaultValue) {
        return (float)toDouble(getPoiCell(false), defaultValue);
    }
    
    public double getDoubleValue(double defaultValue) {
        return toDouble(getPoiCell(false), defaultValue);
    }
    
    public int getIntValue() {
        return (int)toDouble(getPoiCell(false));
    }
    
    public long getLongValue() {
        return (long)toDouble(getPoiCell(false));
    }
    
    public float getFloatValue() {
        return (float)toDouble(getPoiCell(false));
    }
    
    public double getDoubleValue() {
        return toDouble(getPoiCell(false));
    }
    
    public Date getDateValue(Date defaultValue) {
        return toDate(getPoiCell(false), defaultValue);
    }
    
    public String getIntegerStringValue(long defaultValue) {
        return Long.toString(getLongValue(defaultValue));
    }
    
    public String getStringValue(String defaultValue) {
        return toString(getPoiCell(false), defaultValue);
    }
    
    public String getStringValue() {
        return toString(getPoiCell(false), null);
    }
    
    /**
     * toDouble by cell
     * @param cell
     * @param defaultValue
     * @return
     */
    public static double toDouble(Cell cell, double defaultValue) {
        try {
            return toDouble(cell);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    /**
     * toDouble by cell
     * @param cell
     * @param defaultValue
     * @return
     */
    public static double toDouble(Cell cell) {
        if (cell != null) {
            String tmp;
            switch (cell.getCellType()) {
                case BOOLEAN:
                    return cell.getBooleanCellValue() ? 1 : 0;
                case NUMERIC:
                    return cell.getNumericCellValue();
                case FORMULA:
                    if ((tmp = cell.getCellFormula()) != null && !tmp.isEmpty()) {
                        return Utils.parseDouble(tmp);
                    }
                case STRING:
                    if ((tmp = cell.getStringCellValue()) != null && !tmp.isEmpty()) {
                        return Utils.parseDouble(tmp);
                    }
                case _NONE: case ERROR: case BLANK: default:
            }
        }
        throw new IllegalArgumentException(cell + " is not number");
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
    static Cell setCellValueAuto(Cell cell, Object obj) {
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
        return toColumnNameByCellIndex(cellIndex) + ExcelRow.toColumnNameByRowIndex(rowIndex); 
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
        return new int[] { ExcelRow.toRowIndex(columnName.replaceFirst("[A-Z]+", "")), toCellIndex(columnName.replaceFirst("[\\d]+", "")) };
    }
}
