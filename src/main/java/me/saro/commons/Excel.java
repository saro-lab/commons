package me.saro.commons;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;


/**
 * excel
 * @author      PARK Yong Seo
 * @since       not yet
 */
@Deprecated
public class Excel {
    
    final Workbook book;
    //Sheet
    
    Excel(Workbook book) {
        this.book = book;
    }
    
    public static Excel open(File file) throws IOException {
        return new Excel(new HSSFWorkbook(new POIFSFileSystem(file, false)));
    }
    
    public static Excel create(File file) throws IOException {
        return new Excel(new HSSFWorkbook(POIFSFileSystem.create(file)));
    }
    
    public int sheetLength() {
        return book.getNumberOfSheets();
    }
    
    public Excel setSheet(int index) {
        book.getSheetAt(index);
        return this;
    }
    
    public void write(OutputStream os) throws IOException {
        book.write(os);
    }
}
