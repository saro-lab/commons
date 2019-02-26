package me.saro.commons.excel;

import java.util.function.Consumer;

import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import me.saro.commons.Utils;

/**
 * excel style
 * @author      PARK Yong Seo
 * @since       2.3
 */
public class ExcelStyle {
    
    final XSSFSheet sheet;
    final int startRowIndex;
    final int startCellIndex;
    final int endRowIndex;
    final int endCellIndex;
    final StylesTable styleTable;
    
    public ExcelStyle(XSSFSheet sheet, int startRowIndex, int startCellIndex, int endRowIndex, int endCellIndex, StylesTable styleTable) {
        this.sheet = sheet;
        this.startCellIndex = startCellIndex;
        this.endCellIndex = endCellIndex;
        this.startRowIndex = startRowIndex;
        this.endRowIndex = endRowIndex;
        this.styleTable = styleTable;
    }
    
    public void forEach(Consumer<XSSFCellStyle> consumer) {
        for (int irow = startRowIndex ; irow <= endRowIndex ; irow++) {
            XSSFRow row = Utils.nvl(sheet.getRow(irow), sheet.createRow(irow));
            for (int icell = startCellIndex ; icell <= endCellIndex ; icell++) {
                XSSFCell cell = Utils.nvl(row.getCell(icell), row.createCell(icell));
                consumer.accept(cell.getCellStyle());
            }
        }
    }
    
    public void fontSize(short size) {
        forEach(e -> e.getFont().setFontHeight(size));
    }
    
    public void fontColor(int r, int g, int b) {
        IndexedColorMap icm = styleTable.getIndexedColors();
        XSSFColor grey = new XSSFColor(new java.awt.Color(r, g, b), icm);
        forEach(e -> e.getFont().setColor(grey));
    }
}
