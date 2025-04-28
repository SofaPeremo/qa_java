package com.example;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CatTests {

    @Mock
    private com.example.Feline feline;
    private com.example.Cat cat;

    @Before
    public void setUp() {
        cat = new com.example.Cat(feline);
    }

    @Test
    public void testGetSound() {
        assertEquals("Мяу", cat.getSound());
    }

    @Test
    public void testGetFood() throws Exception {
        when(feline.eatMeat()).thenReturn(List.of("Животные", "Птицы", "Рыба"));
        List<String> food = cat.getFood();
        assertEquals(3, food.size());
        assertEquals("Рыба", food.get(2));
    }

    @Test(expected = Exception.class)
    public void testGetFoodThrowsException() throws Exception {
        when(feline.eatMeat()).thenThrow(new Exception("Ошибка в Feline"));
        cat.getFood();
    }
}