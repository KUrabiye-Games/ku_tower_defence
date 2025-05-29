package com.kurabiye.kutd.DynamicArrayList;

import org.junit.jupiter.api.Test;

import com.kurabiye.kutd.DynamicList.DynamicArrayList;

import java.util.Iterator;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;



public class DynamicArrayListTest {

    private static DynamicArrayList<String> dynamicList;


    @BeforeAll
    public static void setup() {
        dynamicList = new DynamicArrayList<>();
    }

    @Test
    public void addLaterTest() {
        dynamicList.add("Item1");
        dynamicList.add("Item2");
        dynamicList.add("Item3");
        dynamicList.add("Item4");
        dynamicList.add("Item5");

        dynamicList.addLater(new String("Item6"));

        assertFalse(dynamicList.contains("Item6"));        
        
        dynamicList.addCommit();    

        assertTrue(dynamicList.contains("Item6"));

        dynamicList.clear();
        
    }

    @Test
    public void removeLaterTest() {
        dynamicList.add("Item1");
        dynamicList.add("Item2");
        dynamicList.add("Item3");
        dynamicList.add("Item4");
        dynamicList.add("Item5");

        dynamicList.removeLater("Item3");

        assertTrue(dynamicList.contains("Item3"));

        dynamicList.removeCommit();

        assertFalse(dynamicList.contains("Item3"));       
        
        dynamicList.clear();
    }      
    
    

    @Test
    public void commitAllAndClearPendingOperationsTest() {
        // Test commitAll() method and clearPendingOperations()
        dynamicList.add("A");
        dynamicList.add("B");
        dynamicList.add("C");
        
        // Stage some operations
        dynamicList.removeLater("B");
        dynamicList.addLater("D");
        dynamicList.addLater("E");
        
        // Verify pending operations exist
        assertEquals(1, dynamicList.getPendingRemovalCount());
        assertEquals(2, dynamicList.getPendingAdditionCount());
        assertTrue(dynamicList.contains("B")); // Still there before commit
        assertFalse(dynamicList.contains("D")); // Not yet added
        assertFalse(dynamicList.contains("E")); // Not yet added
        
        // Test commitAll()
        dynamicList.commitAll();
        
        // Verify all operations were applied
        assertEquals(0, dynamicList.getPendingRemovalCount());
        assertEquals(0, dynamicList.getPendingAdditionCount());
        assertFalse(dynamicList.contains("B")); // Should be removed
        assertTrue(dynamicList.contains("D")); // Should be added
        assertTrue(dynamicList.contains("E")); // Should be added
        assertTrue(dynamicList.contains("A")); // Should still be there
        assertTrue(dynamicList.contains("C")); // Should still be there
        
        // Test clearPendingOperations()
        dynamicList.removeLater("A");
        dynamicList.addLater("F");
        assertEquals(1, dynamicList.getPendingRemovalCount());
        assertEquals(1, dynamicList.getPendingAdditionCount());
        
        dynamicList.clearPendingOperations();
        assertEquals(0, dynamicList.getPendingRemovalCount());
        assertEquals(0, dynamicList.getPendingAdditionCount());
        assertTrue(dynamicList.contains("A")); // Should still be there since clear didn't commit
        assertFalse(dynamicList.contains("F")); // Should not be added since clear didn't commit
        
        dynamicList.clear();
    }


    @Test
    public void iteratorTest() {
        dynamicList.add("Item1");
        dynamicList.add("Item2");
        dynamicList.add("Item3");

        Iterator<String> iterator = dynamicList.iterator();
        int count = 0;

        while (iterator.hasNext()) {
            String item = iterator.next();
            assertTrue(dynamicList.contains(item));
            // add a new item during iteration
            if (count == 1) {
                dynamicList.addLater("Item4");
            }
            // remove an item during iteration

            if (count == 2) {
                dynamicList.removeLater("Item2");
            }
            count++;
        }

        assertEquals(3, count);

        // Commit the changes made during iteration

        dynamicList.addCommit();
        dynamicList.removeCommit();

        assertTrue(dynamicList.contains("Item1"));
        assertFalse(dynamicList.contains("Item2"));

        assertTrue(dynamicList.contains("Item3"));
        assertTrue(dynamicList.contains("Item4")); // Item4 should be added

        assertEquals(3, dynamicList.size());
        
        dynamicList.clear();
    }

    






}
