package me.saro.commons.excel;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import me.saro.commons.Converter;
import me.saro.commons.function.ThrowableFunction;

/**
 * excel
 * @author      PARK Yong Seo
 * @since       3.0
 */
public interface Excel extends Closeable {
    
    static Excel createBulkExcel() {
        return new BasicExcel(new SXSSFWorkbook(100));
    }
    
    static Excel create() {
        return new BasicExcel(new XSSFWorkbook());
    }
    
    static Excel open(File file) throws IOException, InvalidFormatException {
        return new BasicExcel(new XSSFWorkbook(file));
    }
    
    boolean isBulk();
    
    Sheet getPoiSheet();
    
    Excel moveSheet(int index);
    
    Excel moveNextRow();
    
    ExcelRow getRow();
    
    ExcelRow getRow(int rowIndex);
    
    ExcelCell getCell(int rowIndex, int cellIndex);
    
    Excel autoSizeColumn();
    
    Excel autoSizeColumn(int cellIndex);
    
    Excel output(OutputStream os) throws IOException;
    
    Excel save(File file, boolean overwrite) throws IOException;
    
    default ExcelCell getCell(String columnName) {
        int[] rc = ExcelCell.toRowCellIndex(columnName);
        return getCell(rc[0], rc[1]);
    }
    
    default Excel writeHorizontalList(String startColumnName, Collection<Object> values) {
        if (values != null) {
            ExcelCell cell = getCell(startColumnName);
            for (Object value : values) {
                cell = cell.set(value).getNextCell();
            }
        }
        return this;
    }
    
    default Excel writeVerticalList(String startColumnName, Collection<Object> values) {
        if (values != null) {
            ExcelCell cell = getCell(startColumnName);
            for (Object value : values) {
                cell = cell.set(value).getNextRowCell();
            }
        }
        return this;
    }
    
    default public <T> Excel writeTable(String startColumnName, Collection<String> columnNames, List<T> list) {
        if (list != null) {
            int[] rc = ExcelCell.toRowCellIndex(startColumnName);
            ExcelRow row = getRow(rc[0]);
            for (T t : list) {
                ExcelCell cell = row.getCell(rc[1]);
                Map<String, Object> map = Converter.toMapByClass(t);
                for (String name : columnNames) {
                    cell = cell.set(map.get(name)).getNextCell();
                }
                row = row.getNextRow();
            }
        }
        return this;
    }
    
    default public <T> Excel writePivotTable(String startColumnName, Collection<String> columnNames, List<T> list) {
        if (list != null) {
            int[] rc = ExcelCell.toRowCellIndex(startColumnName);
            int ri = rc[0];
            int ci = rc[1];
            
            for (T t : list) {
                ExcelCell cell = getCell(ri, ci++);
                Map<String, Object> map = Converter.toMapByClass(t);
                for (String name : columnNames) {
                    cell = cell.set(map.get(name)).getNextRowCell();
                }
            }
        }
        return this;
    }

    default <R> List<R> readTable(String startColumnName, int columnCount, ThrowableFunction<List<ExcelCell>, R> map) {
        return readTable(startColumnName, columnCount, 2000000, map);
    }
    
    default <R> List<R> readTable(String startColumnName, int columnCount, int limitRowCount, ThrowableFunction<List<ExcelCell>, R> map) {
        int[] rc = ExcelCell.toRowCellIndex(startColumnName);
        int ri = rc[0];
        int eri = Math.min(ri + limitRowCount, getPoiSheet().getLastRowNum() + 1);
        int ci = rc[1];
        int eci = ci + columnCount;
        
        List<R> rv = new ArrayList<>();
        
        while (ri < eri) {
            try {
                ExcelRow row = getRow(ri++);
                if (row.isEmpty()) {
                    continue;
                }
                List<ExcelCell> list = new ArrayList<>(columnCount);
                for (int i = ci ; i < eci ; i++) {
                    list.add(row.getCell(i));
                }
                R r = map.apply(list);
                if (r != null) {
                    rv.add(r);
                }
            } catch (Exception e) {
                throw new RuntimeException("row["+ExcelRow.toColumnNameByRowIndex(ri)+"] : " + e.getMessage(), e);
            }
        }
        
        return rv;
    }
    
    default <R> List<R> readPivotTable(String startColumnName, int columnCount, ThrowableFunction<List<ExcelCell>, R> map) {
        return readPivotTable(startColumnName, columnCount, 10000, map);
    }
    
    default <R> List<R> readPivotTable(String startColumnName, int columnCount, int limitRowCount, ThrowableFunction<List<ExcelCell>, R> map) {
        
        int[] rc = ExcelCell.toRowCellIndex(startColumnName);
        int ri = rc[0];
        int ci = rc[1];
        int eci = ci + limitRowCount;
        
        ExcelRow[] rows = new ExcelRow[columnCount];
        for (int i = 0 ; i < columnCount ; i++) {
            if ((rows[i] = getRow(ri + i)).isEmpty()) {
                throw new IllegalArgumentException(ExcelCell.toColumnName(ri + i, ci) + " is does not exist");
            }
        }
        
        List<R> rv = new ArrayList<>();
        
        stop : while (ci < eci) {
            try {
                int nullCnt = 0;
                List<ExcelCell> list = new ArrayList<>(columnCount);
                for (int i = 0 ; i < columnCount ; i++) {
                    ExcelCell cell = rows[i].getCell(ci);
                    if (cell.isEmpty()) {
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
                throw new RuntimeException("column["+ExcelCell.toColumnNameByCellIndex(ci)+"] : " + e.getMessage(), e);
            }
        }
        
        return rv;
    }
}
