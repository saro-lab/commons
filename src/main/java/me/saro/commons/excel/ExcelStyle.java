package me.saro.commons.excel;

import java.util.function.Consumer;

import org.apache.poi.xssf.usermodel.XSSFCell;
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
    
    public ExcelStyle(XSSFSheet sheet, int startRowIndex, int startCellIndex, int endRowIndex, int endCellIndex) {
        this.sheet = sheet;
        this.startCellIndex = startCellIndex;
        this.endCellIndex = endCellIndex;
        this.startRowIndex = startRowIndex;
        this.endRowIndex = endRowIndex;
    }
    
    public void forEach(Consumer<XSSFCell> consumer) {
        for (int irow = startRowIndex ; irow <= endRowIndex ; irow++) {
            XSSFRow row = Utils.nvl(sheet.getRow(irow), sheet.createRow(irow));
            for (int icell = startCellIndex ; icell <= endCellIndex ; icell++) {
                consumer.accept(Utils.nvl(row.getCell(icell), row.createCell(icell)));
            }
        }
        
    }
}
