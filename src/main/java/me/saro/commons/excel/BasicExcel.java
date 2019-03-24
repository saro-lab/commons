package me.saro.commons.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import lombok.Getter;
import me.saro.commons.NullOutputStream;

/**
 * excel
 * @author      PARK Yong Seo
 * @since       3.0
 */
public class BasicExcel implements Excel {
    
    final private Workbook book;
    @Getter final private boolean bulk;
    
    //private int sheetIndex = -1;
    private int rowIndex = - 1;
    
    private Sheet sheet;
    private ExcelRow row;
    
    protected BasicExcel(Workbook book) {
        this.book = book;
        this.bulk = book.getClass().getName().equals(SXSSFWorkbook.class.getName());
        moveSheet(0);
    }
    
    @Override
    public Sheet getPoiSheet() {
        return sheet;
    }
    
    @Override
    public Excel moveSheet(int index) {
        if (book.getNumberOfSheets() <= index) {
            int need = (index + 1) - book.getNumberOfSheets();
            for (int i = 0 ; i < need ; i++) {
                book.createSheet();
            }
        }
        //sheet = book.getSheetAt((this.sheetIndex = index));
        sheet = book.getSheetAt(index);
        return this;
    }
    
    @Override
    public Excel moveNextRow() {
        row = new ExcelRow(this, ++rowIndex);
        return this;
    }
    
    @Override
    public ExcelRow getRow() {
        return row;
    }
    
    @Override
    public ExcelRow getRow(int rowIndex) {
        return new ExcelRow(this, rowIndex);
    }
    
    @Override
    public ExcelCell getCell(int rowIndex, int cellIndex) {
        return new ExcelCell(new ExcelRow(this, rowIndex), cellIndex);
    }
    
    @Override
    public Excel output(OutputStream os) throws IOException {
        book.write(os);
        os.flush();
        return this;
    }
    
    /**
     * autoSizeColumn
     */
    public Excel autoSizeColumn() {
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
        return this;
    }
    
    /**
     * autoSizeColumn
     * @param cellIndex
     */
    public Excel autoSizeColumn(int cellIndex) {
        if (bulk) {
            throw new RuntimeException("bulk mode does not support autoSizeColumn");
        }
        sheet.autoSizeColumn(cellIndex);
        return this;
    }
    
    @Override
    public Excel save(File file, boolean overwrite) throws IOException {
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
        return this;
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
}
