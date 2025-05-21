import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Arrays;

public class SILab2Test {

    @Test
    void testNullItemList() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            SILab2.checkCart(null, "1234567890123456");
        });
        assertTrue(ex.getMessage().contains("allItems list can't be null"));
    }

    @Test
    void testInvalidItemName() {
        List<Item> items = List.of(new Item(null, 1, 100, 0));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            SILab2.checkCart(items, "1234567890123456");
        });
        assertTrue(ex.getMessage().contains("Invalid item"));
    }

    @Test
    void testInvalidCardNumberLength() {
        List<Item> items = List.of(new Item("Banana", 1, 100, 0));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            SILab2.checkCart(items, "12345678");
        });
        assertTrue(ex.getMessage().contains("Invalid card number"));
    }

    @Test
    void testInvalidCardCharacters() {
        List<Item> items = List.of(new Item("Banana", 1, 100, 0));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            SILab2.checkCart(items, "1234abcd5678efgh");
        });
        assertTrue(ex.getMessage().contains("Invalid character in card number"));
    }

    @Test
    void testValidItemNoDiscount() {
        Item item = new Item("Apple", 2, 100, 0);
        double result = SILab2.checkCart(List.of(item), "1234567890123456");
        assertEquals(200.0, result);
    }

    @Test
    void testValidItemWithDiscount() {
        Item item = new Item("Banana", 2, 150, 0.2); // discount = 20%, triggers -30
        // 150 * 0.8 = 120 * 2 = 240 → 240 - 30 = 210
        double result = SILab2.checkCart(List.of(item), "1234567890123456");
        assertEquals(210.0, result);
    }

    @Test
    void testTriggerPenaltyRule() {
        Item item = new Item("Laptop", 1, 400, 0);  // price > 300 → -30 penalty
        // 400 - 30 = 370
        double result = SILab2.checkCart(List.of(item), "1234567890123456");
        assertEquals(370.0, result);
    }

    @Test
    void testMultipleItems() {
        List<Item> items = Arrays.asList(
            new Item("A", 1, 100, 0),       // 100
            new Item("B", 2, 150, 0.2),     // triggers -30 → 150 * 0.8 = 120 * 2 = 240 → 240 - 30 = 210
            new Item("C", 5, 60, 0)         // 5 * 60 = 300
        );
        // Total: 100 + 210 + 300 = 610
        double result = SILab2.checkCart(items, "1234567890123456");
        assertEquals(610.0, result);
    }
}

