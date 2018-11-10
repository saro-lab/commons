package me.saro.commons;

import java.io.File;

import org.junit.jupiter.api.Test;

public class FilesTest {
    @Test
    public void test() {
        
    }
    
    
    public void remove() {
        
        // 현재로부터 24시간 이전
        long before24hour = DateFormat.now().addHours(-24).getTimeInMillis();
        
        Files
            /// 경로에 모든 파일을 가져옴
            .streamFiles("/testpath")
            // 24시간 보다 오래된 데이터만 가져옴
            .filter(Files.attributesFilter(attr -> attr.creationTime().toMillis() < before24hour))
            // 삭제
            .forEach(File::delete);
    }
}
