package com.example;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class LionParamsTests {

    @Mock
    com.example.Feline feline;

    @Parameterized.Parameter
    public String sex;

    @Parameterized.Parameter(1)
    public boolean expectedHasMane;

    @Parameterized.Parameter(2)
    public boolean shouldThrowException;

    private com.example.Lion lion;

    @Parameterized.Parameters(name = "Проверка льва с полом: {0}")
    public static Object[][] data() {
        return new Object[][]{
                {"Самец", true, false},
                {"Самка", false, false},
                {"Неизвестно", false, true},
                {null, false, true}
        };
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(feline.getFood("Хищник")).thenReturn(Arrays.asList("Мясо", "Рыба"));
        when(feline.getKittens()).thenReturn(1);

        if (!shouldThrowException) {
            lion = new com.example.Lion(sex, feline);
        }
    }

    @Test
    public void testDoesHaveMane() throws Exception {
        if (shouldThrowException) {
            try {
                new com.example.Lion(sex, feline);
                fail("Ожидалось исключение для недопустимого пола: " + sex);
            } catch (Exception e) {
                assertTrue(e.getMessage().contains("допустимые значения пола"));
            }
        } else {
            assertEquals(expectedHasMane, lion.doesHaveMane());
        }
    }

    @Test
    public void testGetKittens() throws Exception {
        if (!shouldThrowException) {
            assertEquals(1, lion.getKittens());
        }
    }

    @Test
    public void testGetFood() throws Exception {
        if (!shouldThrowException) {
            List<String> food = lion.getFood();
            assertTrue(food.contains("Мясо"));
            assertTrue(food.contains("Рыба"));
        }
    }
}