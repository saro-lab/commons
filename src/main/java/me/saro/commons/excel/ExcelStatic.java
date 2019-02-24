package me.saro.commons.excel;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

/**
 * excel static
 * @author      PARK Yong Seo
 * @since       2.2
 */
public abstract class ExcelStatic {
    
    final private static int CHAR_A = (int)'A';
    
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
