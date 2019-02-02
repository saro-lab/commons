package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Data;


public class ExcelTest {
    
    @Test
    public void toColumnNameByCellIndex() {
        assertEquals(Excel.toColumnNameByCellIndex(1 - 1), "A");
        assertEquals(Excel.toColumnNameByCellIndex(26 - 1), "Z");
        assertEquals(Excel.toColumnNameByCellIndex(27 - 1), "AA");
        assertEquals(Excel.toColumnNameByCellIndex(52 - 1), "AZ");
        assertEquals(Excel.toColumnNameByCellIndex(520 - 1), "SZ");
        assertEquals(Excel.toColumnNameByCellIndex(2600 - 1), "CUZ");
        assertEquals(Excel.toColumnNameByCellIndex(10000 - 1), "NTP");
    }
    
    @Test
    public void toCellIndex() {
        int idx = 0;
        assertEquals(Excel.toCellIndex(Excel.toColumnNameByCellIndex(idx)), idx);
        idx = 321;
        assertEquals(Excel.toCellIndex(Excel.toColumnNameByCellIndex(idx)), idx);
        idx = 3289;
        assertEquals(Excel.toCellIndex(Excel.toColumnNameByCellIndex(idx)), idx);
        idx = 10111;
        assertEquals(Excel.toCellIndex(Excel.toColumnNameByCellIndex(idx)), idx);
        idx = 457;
        assertEquals(Excel.toCellIndex(Excel.toColumnNameByCellIndex(idx)), idx);
    }
    
    @Test
    public void toRowIndex() {
        assertEquals(Excel.toRowIndex("1"), 0);
        assertEquals(Excel.toRowIndex("100"), 99);
        assertEquals(Excel.toRowIndex("2341"), 2340);
        assertEquals(Excel.toRowIndex("16000"), 15999);
    }
    
    @Test
    public void writeTableByListMap() throws IOException {
        try (Excel excel = Excel.create()) {
            
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(Converter.toMap("a", 1, "b", "AA"));
            list.add(Converter.toMap("a", 2, "b", "BB"));
            
            excel.writeTableByListMap("B2", List.of("a", "b"), list);
            
            assertEquals((int)excel.move("B2").getNumericCellValue(), 1);
            assertEquals((int)excel.move("B3").getNumericCellValue(), 2);
            
            assertEquals(excel.move("C2").getStringCellValue(), "AA");
            assertEquals(excel.move("C3").getStringCellValue(), "BB");
        }
    }
    
    @Test
    public void writePivotTableByListMap() throws IOException {
        try (Excel excel = Excel.create()) {
            
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(Converter.toMap("a", 1, "b", "AA"));
            list.add(Converter.toMap("a", 2, "b", "BB"));
            
            excel.writePivotTableByListMap("B2", List.of("a", "b"), list);
            
            assertEquals((int)excel.move("B2").getNumericCellValue(), 1);
            assertEquals((int)excel.move("C2").getNumericCellValue(), 2);
            
            assertEquals(excel.move("B3").getStringCellValue(), "AA");
            assertEquals(excel.move("C3").getStringCellValue(), "BB");
        }
    }
    
    @Test
    public void writeTableByListClass() throws IOException {
        try (Excel excel = Excel.create()) {
            
            @Data @AllArgsConstructor
            class TestObject {
                int a;
                String b;
            }
            
            List<TestObject> list = new ArrayList<>();
            list.add(new TestObject(11, "AAA"));
            list.add(new TestObject(22, "BBB"));
            
            excel.writeTableByListClass("B2", List.of("a", "b"), list);
            
            assertEquals((int)excel.move("B2").getNumericCellValue(), 11);
            assertEquals((int)excel.move("B3").getNumericCellValue(), 22);
            
            assertEquals(excel.move("C2").getStringCellValue(), "AAA");
            assertEquals(excel.move("C3").getStringCellValue(), "BBB");
        }
    }
    
    @Test
    public void writePivotTableByListClass() throws IOException {
        try (Excel excel = Excel.create()) {
            
            @Data @AllArgsConstructor
            class TestObject {
                int a;
                String b;
            }
            
            List<TestObject> list = new ArrayList<>();
            list.add(new TestObject(11, "AAA"));
            list.add(new TestObject(22, "BBB"));
            
            excel.writePivotTableByListClass("B2", List.of("a", "b"), list);
            
            assertEquals((int)excel.move("B2").getNumericCellValue(), 11);
            assertEquals((int)excel.move("C2").getNumericCellValue(), 22);
            
            assertEquals(excel.move("B3").getStringCellValue(), "AAA");
            assertEquals(excel.move("C3").getStringCellValue(), "BBB");
        }
    }
}
