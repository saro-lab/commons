package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.junit.jupiter.api.Test;
import org.w3c.dom.css.RGBColor;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.saro.commons.bytes.Bytes;


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
    public void readTable() throws IOException {
        try (Excel excel = Excel.create()) {
            
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(Converter.toMap("a", 1, "b", "AA"));
            list.add(Converter.toMap("a", 2, "b", "BB"));
            
            excel.writeTable("B2", Arrays.asList("a", "b"), list);
            
            List<List<String>> rv = excel.readTable("B2", 2, e -> Arrays.asList(
                Excel.toIntegerString(e.get(0), -1),
                Excel.toString(e.get(1), null)
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
            
            List<Map<String, String>> list = new ArrayList<>();
            list.add(Converter.toMap("a", "1", "b", "AA"));
            list.add(Converter.toMap("a", "2", "b", "BB"));
            
            excel.writeTable("B2", Arrays.asList("a", "b"), list);
            
            List<List<String>> rv = excel.readPivotTable("B2", 2, e -> Arrays.asList( e.get(0).toString(), e.get(1).toString() ));
            
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
            
            assertEquals(excel.move("B2").getInt(-1), 1);
            assertEquals(excel.move("B3").getInt(-1), 2);
            
            assertEquals(excel.move("C2").getString(), "AA");
            assertEquals(excel.move("C3").getString(), "BB");
        }
    }
    
    @Test
    public void writePivotTableByListMap() throws IOException {
        try (Excel excel = Excel.create()) {
            
            List<Map<String, Object>> list = new ArrayList<>();
            list.add(Converter.toMap("a", 1, "b", "AA"));
            list.add(Converter.toMap("a", 2, "b", "BB"));
            
            excel.writePivotTable("B2", Arrays.asList("a", "b"), list);
            
            assertEquals(excel.move("B2").getInt(-1), 1);
            assertEquals(excel.move("C2").getInt(-1), 2);
            
            assertEquals(excel.move("B3").getString(), "AA");
            assertEquals(excel.move("C3").getString(), "BB");
        }
    }
    
    @Test
    public void writeHorizontalList() throws IOException {
        try (Excel excel = Excel.create()) {
            excel.writeHorizontalList("A1", Arrays.asList("1", "2", "3"));
            
            assertEquals(excel.move("A1").getString(), "1");
            assertEquals(excel.move("B1").getString(), "2");
            assertEquals(excel.move("C1").getString(), "3");
        }
    }
    
    @Test
    public void writeVerticalList() throws IOException {
        try (Excel excel = Excel.create()) {
            excel.writeVerticalList("A1", Arrays.asList("1", "2", "3"));
            
            assertEquals(excel.move("A1").getString(), "1");
            assertEquals(excel.move("A2").getString(), "2");
            assertEquals(excel.move("A3").getString(), "3");
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
            
            assertEquals(excel.move("B2").getInt(-1), 11);
            assertEquals(excel.move("B3").getInt(-1), 22);
            
            assertEquals(excel.move("C2").getString(), "AAA");
            assertEquals(excel.move("C3").getString(), "BBB");
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
            
            assertEquals(excel.move("B2").getInt(-1), 11);
            assertEquals(excel.move("C2").getInt(-1), 22);
            
            assertEquals(excel.move("B3").getString(), "AAA");
            assertEquals(excel.move("C3").getString(), "BBB");
        }
    }
    
    @Test
    public void style() throws IOException, InvalidFormatException {
        try (Excel excel = Excel.createClone(new File("C:/Users/SARO/Desktop/bbb.xlsx"), new File("C:/Users/SARO/Desktop/abc.xlsx"), true)) {
            
            @Data @AllArgsConstructor
            class TestObject {
                int a;
                String b;
            }
            
            List<TestObject> list = new ArrayList<>();
            list.add(new TestObject(11, "Aasdfasdfsadfjklsadfj;lasdfjasd;lkfjasd;lfkjsadfA"));
            list.add(new TestObject(22, "2394992394BBB"));
            
            excel.writePivotTable("B1", Arrays.asList("a", "b"), list);
            
            
            excel.autoSizeColumn("A2", "E2");
            
            
            //excel.save(, true);
            
            
        }
    }
}
