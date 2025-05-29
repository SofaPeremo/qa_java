import com.example.Animal;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnimalGeneralTests {

    @Test
    public void testGetFamily() {
        Animal animal = new Animal();
        String expected = "Существует несколько семейств: заячьи, беличьи, мышиные, " +
                "кошачьи, псовые, медвежьи, куньи";
        assertEquals(expected, animal.getFamily());
    }
}
