package com.example;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class AnimalTests {

    private final String animalKind;
    private final List<String> expectedFood;
    private final String expectedExceptionMessage;

    public AnimalTests(String animalKind, List<String> expectedFood, String expectedExceptionMessage) {
        this.animalKind = animalKind;
        this.expectedFood = expectedFood;
        this.expectedExceptionMessage = expectedExceptionMessage;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {"Травоядное", List.of("Трава", "Различные растения"), null},
                {"Хищник", List.of("Животные", "Птицы", "Рыба"), null},

                {"Всеядное", null, "Неизвестный вид животного, используйте значение Травоядное или Хищник"},
                {null, null, "Неизвестный вид животного, используйте значение Травоядное или Хищник"}
        };
    }

    @Test
    public void testGetFood() throws Exception {
        com.example.Animal animal = new com.example.Animal();

        if (expectedExceptionMessage != null) {
            Exception exception = assertThrows(Exception.class, () -> animal.getFood(animalKind));
            assertEquals(expectedExceptionMessage, exception.getMessage());
        } else {
            assertEquals(expectedFood, animal.getFood(animalKind));
        }
    }
}