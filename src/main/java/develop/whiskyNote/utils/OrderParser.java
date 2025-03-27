
package develop.whiskyNote.utils;

import develop.whiskyNote.enums.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderParser {
    public static Order parse(String orderStr) {
        if (orderStr == null) return null;
        try {
            return Order.valueOf(orderStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // 또는 default: Order.DESC
        }
    }
}