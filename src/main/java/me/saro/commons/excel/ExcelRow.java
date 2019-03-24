package me.saro.commons.excel;

import org.apache.poi.ss.usermodel.Row;

public class ExcelRow {
    
    final Excel excel;
    final int rowIndex;
    private int cellIndex = -1;
    private Row pRow;
    
    ExcelCell cell;
    
    ExcelRow(Excel excel, int rowIndex) {
        this.excel = excel;
        this.rowIndex = rowIndex;
    }
    
    public ExcelRow getNextRow() {
        return new ExcelRow(excel, rowIndex + 1);
    }
    
    public ExcelRow moveNextCell() {
        cell = new ExcelCell(this, ++cellIndex);
        return this;
    }
    
    public ExcelCell getCell() {
        return cell;
    }
    
    public ExcelCell getCell(int cellIndex) {
        return new ExcelCell(this, cellIndex);
    }
    
    public boolean isEmpty() {
        return getPoiRow(false) == null;
    }
    
    public Row getPoiRow(boolean force) {
        if (pRow == null) {
            pRow = excel.getPoiSheet().getRow(rowIndex);
            if (pRow == null && force) {
                pRow = excel.getPoiSheet().createRow(rowIndex);
            }
        }
        return pRow;
    }
    
    public static int toRowIndex(String rowColumnName) {
        return Integer.parseInt(rowColumnName) - 1;
    }
    
    public static String toColumnNameByRowIndex(int rowIndex) {
        return Integer.toString(rowIndex + 1);
    }
}
