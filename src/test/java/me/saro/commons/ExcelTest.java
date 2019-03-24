package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.saro.commons.excel.Excel;
import me.saro.commons.excel.ExcelCell;
import me.saro.commons.excel.ExcelRow;


public class ExcelTest {
    
    @Test
    public void toColumnNameByCellIndex() {
        assertEquals(ExcelCell.toColumnNameByCellIndex(1 - 1), "A");
        assertEquals(ExcelCell.toColumnNameByCellIndex(26 - 1), "Z");
        assertEquals(ExcelCell.toColumnNameByCellIndex(27 - 1), "AA");
        assertEquals(ExcelCell.toColumnNameByCellIndex(52 - 1), "AZ");
        assertEquals(ExcelCell.toColumnNameByCellIndex(520 - 1), "SZ");
        assertEquals(ExcelCell.toColumnNameByCellIndex(2600 - 1), "CUZ");
        assertEquals(ExcelCell.toColumnNameByCellIndex(10000 - 1), "NTP");
    }
    
    @Test
    public void toCellIndex() {
        int idx = 0;
        assertEquals(ExcelCell.toCellIndex(ExcelCell.toColumnNameByCellIndex(idx)), idx);
        idx = 321;
        assertEquals(ExcelCell.toCellIndex(ExcelCell.toColumnNameByCellIndex(idx)), idx);
        idx = 3289;
        assertEquals(ExcelCell.toCellIndex(ExcelCell.toColumnNameByCellIndex(idx)), idx);
        idx = 10111;
        assertEquals(ExcelCell.toCellIndex(ExcelCell.toColumnNameByCellIndex(idx)), idx);
        idx = 457;
        assertEquals(ExcelCell.toCellIndex(ExcelCell.toColumnNameByCellIndex(idx)), idx);
    }
    
    @Test
    public void toRowIndex() {
        assertEquals(ExcelRow.toRowIndex("1"), 0);
        assertEquals(ExcelRow.toRowIndex("100"), 99);
        assertEquals(ExcelRow.toRowIndex("2341"), 2340);
        assertEquals(ExcelRow.toRowIndex("16000"), 15999);
    }
    
    @Test
    public void readTable() throws IOException {
        try (Excel excel = Excel.create()) {
            
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(Converter.toMap("a", 1, "b", "AA"));
            list.add(Converter.toMap("a", 2, "b", "BB"));
            
            excel.writeTable("B2", Arrays.asList("a", "b"), list);
            
            List<List<String>> rv = excel.readTable("B2", 2, e -> Arrays.asList(
                e.get(0).getIntegerStringValue(-1),
                e.get(1).getStringValue(null)
            )); 
            
            assertEquals(rv.get(0).get(0), "1");
            assertEquals(rv.get(0).get(1), "AA");
            
            assertEquals(rv.get(1).get(0), "2");
            assertEquals(rv.get(1).get(1), "BB");
        }
    }
    
    @Test
    public void readPivotTable() throws IOException {
        try (Excel excel = Excel.create()) {
            
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(Converter.toMap("a", "1", "b", "AA"));
            list.add(Converter.toMap("a", "2", "b", "BB"));
            
            excel.writeTable("B2", Arrays.asList("a", "b"), list);
            
            List<List<String>> rv = excel.readPivotTable("B2", 2, e -> Arrays.asList(
                e.get(0).getStringValue(null),
                e.get(1).getStringValue(null)
            ));
            
            assertEquals(rv.get(0).get(0), "1");
            assertEquals(rv.get(0).get(1), "2");
            
            assertEquals(rv.get(1).get(0), "AA");
            assertEquals(rv.get(1).get(1), "BB");
        }
    }
    
    @Test
    public void writeTableByListMap() throws IOException {
        try (Excel excel = Excel.create()) {
            
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(Converter.toMap("a", 1, "b", "AA"));
            list.add(Converter.toMap("a", 2, "b", "BB"));
            
            excel.writeTable("B2", Arrays.asList("a", "b"), list);
            
            assertEquals(excel.getCell("B2").getIntValue(-1), 1);
            assertEquals(excel.getCell("B3").getIntValue(-1), 2);
            
            assertEquals(excel.getCell("C2").getStringValue(null), "AA");
            assertEquals(excel.getCell("C3").getStringValue(null), "BB");
        }
    }
    
    @Test
    public void writePivotTableByListMap() throws IOException {
        try (Excel excel = Excel.create()) {
            
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(Converter.toMap("a", 1, "b", "AA"));
            list.add(Converter.toMap("a", 2, "b", "BB"));
            
            excel.writePivotTable("B2", Arrays.asList("a", "b"), list);
            
            assertEquals(excel.getCell("B2").getIntValue(-1), 1);
            assertEquals(excel.getCell("C2").getIntValue(-1), 2);
            
            assertEquals(excel.getCell("B3").getStringValue(), "AA");
            assertEquals(excel.getCell("C3").getStringValue(), "BB");
        }
    }
    
    @Test
    public void writeHorizontalList() throws IOException {
        try (Excel excel = Excel.create()) {
            excel.writeHorizontalList("A1", Arrays.asList("1", "2", "3"));
            
            assertEquals(excel.getCell("A1").getStringValue(), "1");
            assertEquals(excel.getCell("B1").getStringValue(), "2");
            assertEquals(excel.getCell("C1").getStringValue(), "3");
        }
    }
    
    @Test
    public void writeVerticalList() throws IOException {
        try (Excel excel = Excel.create()) {
            excel.writeVerticalList("A1", Arrays.asList("1", "2", "3"));
            
            assertEquals(excel.getCell("A1").getStringValue(), "1");
            assertEquals(excel.getCell("A2").getStringValue(), "2");
            assertEquals(excel.getCell("A3").getStringValue(), "3");
        }
    }
    
    @Test
    public void writeTable() throws IOException {
        try (Excel excel = Excel.create()) {
            
            @Data @AllArgsConstructor
            class TestObject {
                int a;
                String b;
            }
            
            List<TestObject> list = new ArrayList<>();
            list.add(new TestObject(11, "AAA"));
            list.add(new TestObject(22, "BBB"));
            
            excel.writeTable("B2", Arrays.asList("a", "b"), list);
            
            assertEquals(excel.getCell("B2").getIntValue(-1), 11);
            assertEquals(excel.getCell("B3").getIntValue(-1), 22);
            
            assertEquals(excel.getCell("C2").getStringValue(), "AAA");
            assertEquals(excel.getCell("C3").getStringValue(), "BBB");
        }
    }
    
    @Test
    public void writePivotTable() throws IOException {
        try (Excel excel = Excel.create()) {
            
            @Data @AllArgsConstructor
            class TestObject {
                int a;
                String b;
            }
            
            List<TestObject> list = new ArrayList<>();
            list.add(new TestObject(11, "AAA"));
            list.add(new TestObject(22, "BBB"));
            
            excel.writePivotTable("B2", Arrays.asList("a", "b"), list);
            
            assertEquals(excel.getCell("B2").getIntValue(-1), 11);
            assertEquals(excel.getCell("C2").getIntValue(-1), 22);
            
            assertEquals(excel.getCell("B3").getStringValue(), "AAA");
            assertEquals(excel.getCell("C3").getStringValue(), "BBB");
        }
    }
    
    //@Test
    public void file() throws Exception {
        try (Excel excel = Excel.open(new File("C:\\Users\\SARO\\Desktop\\aaa.xlsx"))) {
            System.out.println(excel.getCell("A1").getDoubleValue(-1));
            System.out.println(excel.getCell("A2").getIntValue(-1));
            System.out.println(excel.getCell("A3").getIntValue(-1));
            System.out.println(excel.getCell("A4").getIntValue(-1));
            System.out.println(excel.getCell("A5").getIntValue(-1));
            System.out.println(excel.getCell("A6").getIntValue(-1));
            
            System.out.println(excel.getCell("A1").getIntegerStringValue(-1));
            System.out.println(excel.getCell("A2").getIntegerStringValue(-1));
            System.out.println(excel.getCell("A3").getIntegerStringValue(-1));
            System.out.println(excel.getCell("A4").getIntegerStringValue(-1));
            System.out.println(excel.getCell("A5").getIntegerStringValue(-1));
            System.out.println(excel.getCell("A6").getIntegerStringValue(-1));
        }
    }
}
