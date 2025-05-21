import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SILab2Test {

    @Test
    public void testNullAllItems() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> SILab2.checkCart(null, "1234567890123456"));
        assertEquals("allItems list can't be null!", ex.getMessage());
    }

    @Test
    public void testItemWithNullName() {
        List<Item> items = List.of(new Item(null, 1, 100, 0));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> SILab2.checkCart(items, "1234567890123456"));
        assertEquals("Invalid item!", ex.getMessage());
    }

    @Test
    public void testInvalidCardLength() {
        List<Item> items = List.of(new Item("Apple", 1, 100, 0));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> SILab2.checkCart(items, "1234"));
        assertEquals("Invalid card number!", ex.getMessage());
    }

    @Test
    public void testInvalidCardCharacter() {
        List<Item> items = List.of(new Item("Apple", 1, 100, 0));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> SILab2.checkCart(items, "123456789012345X"));
        assertEquals("Invalid character in card number!", ex.getMessage());
    }

    @Test
    public void testValidItemNoDiscountNoPenalty() {
        List<Item> items = List.of(new Item("Apple", 1, 100, 0));
        double result = SILab2.checkCart(items, "1234567890123456");
        assertEquals(100.0, result);
    }

    @Test
    public void testValidItemWithDiscount() {
        List<Item> items = List.of(new Item("Banana", 2, 100, 0.1));
        double result = SILab2.checkCart(items, "1234567890123456");
        assertEquals(180.0, result); // 100 * 0.9 * 2
    }

    @Test
    public void testPenaltyAppliedDueToPrice() {
        List<Item> items = List.of(new Item("TV", 1, 400, 0));
        double result = SILab2.checkCart(items, "1234567890123456");
        assertEquals(370.0, result); // 400 - 30
    }

    @Test
    public void testPenaltyAppliedDueToQuantity() {
        List<Item> items = List.of(new Item("Water", 11, 10, 0));
        double result = SILab2.checkCart(items, "1234567890123456");
        assertEquals(80.0, result); // (11*10)-30
    }

    @Test
    public void testAllPenaltyConditions() {
        List<Item> items = List.of(new Item("Bundle", 11, 400, 0.1));
        double result = SILab2.checkCart(items, "1234567890123456");
        double expected = -30 + (400 * 0.9 * 11); // penalty + discounted price
        assertEquals(expected, result);
    }

    @Test
    public void testMultipleItemsMixed() {
        List<Item> items = List.of(
            new Item("Apple", 1, 100, 0),
            new Item("TV", 1, 400, 0),
            new Item("Water", 11, 10, 0.1)
        );
        double result = SILab2.checkCart(items, "1234567890123456");

        double expected =
            100 +        // Apple
            (-30 + 400) + // TV
            (-30 + 11 * 10 * 0.9); // Water

        assertEquals(expected, result);
    }
}
