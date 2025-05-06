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
    public TestType testType;

    private com.example.Lion lion;

    enum TestType {
        POSITIVE, NEGATIVE
    }

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] data() {
        return new Object[][]{
                {"Самец", true, TestType.POSITIVE},
                {"Неизвестно", false, TestType.NEGATIVE}
        };
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(feline.getFood("Хищник")).thenReturn(Arrays.asList("Мясо", "Рыба"));
        when(feline.getKittens()).thenReturn(1);

        if (testType == TestType.POSITIVE) {
            lion = new com.example.Lion(sex, feline);
        }
    }

    @Test
    public void testDoesHaveMane() throws Exception {
        if (testType == TestType.NEGATIVE) {
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
    public void testPositiveCases() throws Exception {
        if (testType != TestType.POSITIVE) return;

        assertEquals(expectedHasMane, lion.doesHaveMane());
        assertEquals(1, lion.getKittens());

        List<String> food = lion.getFood();
        assertTrue(food.contains("Мясо"));
        assertTrue(food.contains("Рыба"));
    }
}