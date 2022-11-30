package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void changeList(){
        List<String> items = new ArrayList<>();
        change(items);
        change(items);
        assertEquals(2, items.size());
    }

    private void change(List<String> items){
        items.add("1");
    }
}
