package develop.whiskyNote.utils;

import org.springframework.stereotype.Component;

@Component
public class TranslatorUtils {
    public boolean isKorean(String language) {
        for (char c : language.toCharArray()) {
            if (c >= '가' && c <= '힣') {
                return true;
            }
        }
        return false;
    }

}
