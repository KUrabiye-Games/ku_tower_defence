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

        assertFalse(dynamicList.contains("Item6"), "Item6 should not be in the list before commit");        
        
        dynamicList.addCommit();    

        assertTrue(dynamicList.contains("Item6"), "Item6 should be in the list after addCommit");

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

        assertTrue(dynamicList.contains("Item3"), "Item3 should still be in the list before commit");

        dynamicList.removeCommit();

        assertFalse(dynamicList.contains("Item3"), "Item3 should be removed from the list after removeCommit");       
        
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
        assertEquals(1, dynamicList.getPendingRemovalCount(), "Should have 1 pending removal");
        assertEquals(2, dynamicList.getPendingAdditionCount(), "Should have 2 pending additions");
        assertTrue(dynamicList.contains("B"), "Item B should still be in the list before commit");
        assertFalse(dynamicList.contains("D"), "Item D should not yet be added before commit");
        assertFalse(dynamicList.contains("E"), "Item E should not yet be added before commit");
        
        // Test commitAll()
        dynamicList.commitAll();
          // Verify all operations were applied
        assertEquals(0, dynamicList.getPendingRemovalCount(), "Should have 0 pending removals after commitAll");
        assertEquals(0, dynamicList.getPendingAdditionCount(), "Should have 0 pending additions after commitAll");
        assertFalse(dynamicList.contains("B"), "Item B should be removed after commitAll");
        assertTrue(dynamicList.contains("D"), "Item D should be added after commitAll");
        assertTrue(dynamicList.contains("E"), "Item E should be added after commitAll");
        assertTrue(dynamicList.contains("A"), "Item A should still be there after commitAll");
        assertTrue(dynamicList.contains("C"), "Item C should still be there after commitAll");
          // Test clearPendingOperations()
        dynamicList.removeLater("A");
        dynamicList.addLater("F");
        assertEquals(1, dynamicList.getPendingRemovalCount(), "Should have 1 pending removal before clear");
        assertEquals(1, dynamicList.getPendingAdditionCount(), "Should have 1 pending addition before clear");
        
        dynamicList.clearPendingOperations();
        assertEquals(0, dynamicList.getPendingRemovalCount(), "Should have 0 pending removals after clearPendingOperations");
        assertEquals(0, dynamicList.getPendingAdditionCount(), "Should have 0 pending additions after clearPendingOperations");
        assertTrue(dynamicList.contains("A"), "Item A should still be there since clear didn't commit");
        assertFalse(dynamicList.contains("F"), "Item F should not be added since clear didn't commit");
        
        dynamicList.clear();
    }


    @Test
    public void iteratorTest() {
        dynamicList.add("Item1");
        dynamicList.add("Item2");
        dynamicList.add("Item3");

        Iterator<String> iterator = dynamicList.iterator();
        int count = 0;        while (iterator.hasNext()) {
            String item = iterator.next();
            assertTrue(dynamicList.contains(item), "Iterator should only return items that exist in the list");
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

        assertEquals(3, count, "Iterator should have iterated over 3 items");

        // Commit the changes made during iteration

        dynamicList.addCommit();
        dynamicList.removeCommit();        assertTrue(dynamicList.contains("Item1"), "Item1 should still be present");
        assertFalse(dynamicList.contains("Item2"), "Item2 should have been removed");
 
        assertTrue(dynamicList.contains("Item3"), "Item3 should still be present");
        assertTrue(dynamicList.contains("Item4"), "Item4 should have been added");

        assertEquals(3, dynamicList.size(), "List should contain exactly 3 items after operations");
        
        dynamicList.clear();
    }

    



}
