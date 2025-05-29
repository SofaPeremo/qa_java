package com.example;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LionTests {

    @Mock
    private com.example.Feline feline;
    private com.example.Lion lion;

    @Test
    public void testMaleLionConstructor() throws Exception {
        lion = new com.example.Lion("Самец", feline);
        assertTrue(lion.doesHaveMane());
    }

    @Test
    public void testFemaleLionConstructor() throws Exception {
        lion = new com.example.Lion("Самка", feline);
        assertFalse(lion.doesHaveMane());
    }

    @Test(expected = Exception.class)
    public void testInvalidSexConstructor() throws Exception {
        new com.example.Lion("Неизвестный пол", feline);
    }

    @Test
    public void testGetKittens() throws Exception {
        when(feline.getKittens()).thenReturn(3);
        lion = new com.example.Lion("Самец", feline);
        assertEquals(3, lion.getKittens());
        verify(feline, times(1)).getKittens();
    }

    @Test
    public void testGetFood() throws Exception {
        List<String> expectedFood = List.of("Животные", "Птицы", "Рыба");
        when(feline.getFood("Хищник")).thenReturn(expectedFood);
        lion = new com.example.Lion("Самка", feline);
        assertEquals(expectedFood, lion.getFood());
        verify(feline).getFood("Хищник");
    }

    @Test(expected = Exception.class)
    public void testGetFoodThrowsException() throws Exception {
        when(feline.getFood("Хищник")).thenThrow(new Exception("Ошибка в Feline"));
        lion = new com.example.Lion("Самец", feline);
        lion.getFood();
    }
}