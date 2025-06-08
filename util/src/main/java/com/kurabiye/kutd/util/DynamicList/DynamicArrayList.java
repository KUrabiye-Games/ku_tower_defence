package com.kurabiye.kutd.util.DynamicList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/*
 * Representation Invariant:
 *  - pendingRemovals != null
 *  - pendingAdditions != null
 *  - Elements in pendingRemovals must be elements already in the base list
 *  - Elements in pendingAdditions must NOT be in the base list
 *  - No element should exist in both pendingRemovals and pendingAdditions
 */

public class DynamicArrayList<T> extends ArrayList<T> {
    /** OVERVIEW: This class extends ArrayList to create a dynamic array that can grow as needed
    * It can be used to store objects of any type with deferred operations during iteration
    * This class allows deferred modifications (additions/removals), providing a consistent view
    * during iteration and ensuring safety when modifying the list concurrently with iteration.
    */

     /**
     * Abstraction Function:
     * AF(this) = a dynamic list of elements of type T such that:
     *   - The visible state of the list is: elements in 'this' minus those in pendingRemovals
     *   - Items in pendingAdditions are not yet part of the list, but will be after addCommit()
     *   - Iteration through filteredIterator() reflects the abstract list:
     *       this - pendingRemovals
     *   - Iteration through unfilteredIterator() reflects the raw internal list:
     *       this
     */
    
    private Set<T> pendingRemovals = new HashSet<>();
    private ArrayList<T> pendingAdditions = new ArrayList<>();

    public DynamicArrayList() {
        super(); // Call the constructor of ArrayList
    }    /**
     * Regular add method - adds item immediately to the list
     */
    @Override
    public boolean add(T item) {
        return super.add(item);
    }
    
    /**
     * Regular remove method - removes item immediately from the list
     */
    @Override
    public boolean remove(Object item) {
        boolean result = super.remove(item);
        // Also remove from pending operations if it was there
        pendingRemovals.remove(item);
        pendingAdditions.remove(item);
        return result;
    }
    
    /**
     * Marks an item for removal. The actual removal happens when removeCommit() is called.
     * This allows safe removal during iteration.
     * @param item The item to mark for removal
     */
    public void removeLater(T item) {
        pendingRemovals.add(item);
    }
    
    /**
     * Commits all pending removals, actually removing the marked items from the list.
     */
    public void removeCommit() {
        for (T item : pendingRemovals) {
            super.remove(item);
        }
        pendingRemovals.clear();
    }
    
    /**
     * Stages an item for addition. The actual addition happens when commitAdd() is called.
     * @param item The item to stage for addition
     */
    public void addLater(T item) {
        pendingAdditions.add(item);
    }
    
    /**
     * Commits all pending additions, actually adding the staged items to the list.
     */
    public void addCommit() {
        for (T item : pendingAdditions) {
            super.add(item);
        }
        pendingAdditions.clear();
    }

    /**
     * Commit all pending operations (both removals and additions).
     * This method is provided for convenience to apply all changes at once.
     */

    public void commitAll() {
        removeCommit();
        addCommit();
    }
    
    /**
     * Returns an iterator that shows all items currently in the list,
     * regardless of pending operations. This allows normal iteration
     * over the actual current state of the list.
     * @return Iterator that shows all current items
     */
    public Iterator<T> unfilteredIterator() {
        return super.iterator();
    }
    
    /**
     * Returns an iterator that skips items marked for removal and doesn't show staged additions.
     * This provides a consistent view during iteration even when items are marked for operations.
     * This is the default iterator behavior.
     * @return Iterator that filters out items marked for removal
     */
    public Iterator<T> filteredIterator() {
        return new DeferredOperationIterator();
    }
    
    /**
     * Returns the filtered iterator by default (skips items marked for removal).
     * Use unfilteredIterator() if you want to see all current items.
     */
    @Override
    public Iterator<T> iterator() {
        return unfilteredIterator();
    }
    
    /**
     * Gets the number of pending removals
     * @return Number of items marked for removal
     */
    public int getPendingRemovalCount() {
        return pendingRemovals.size();
    }
    
    /**
     * Gets the number of pending additions
     * @return Number of items staged for addition
     */
    public int getPendingAdditionCount() {
        return pendingAdditions.size();
    }
      /**
     * Checks if an item is marked for removal
     * @param item The item to check
     * @return true if the item is marked for removal
     */
    public boolean isMarkedForRemoval(T item) {
        return pendingRemovals.contains(item);
    }
    
    /**
     * Clears all pending operations without committing them
     */
    public void clearPendingOperations() {
        pendingRemovals.clear();
        pendingAdditions.clear();
    }
    

    public boolean repOk() {
        // 1. Check for null fields
        if (pendingRemovals == null || pendingAdditions == null) return false;

        // 2. All elements in pendingRemovals must already exist in the base list
        for (T item : pendingRemovals) {
            if (!super.contains(item)) return false;
        }

        // 3. All elements in pendingAdditions must not already exist in the base list
        for (T item : pendingAdditions) {
            if (super.contains(item)) return false;
        }

        /*
        // 4. No element should exist in both pendingAdditions and pendingRemovals
        for (T item : pendingAdditions) {
            if (pendingRemovals.contains(item)) return false;
        }
        */
    return true;
    }




    /**
     * Custom iterator that provides a consistent view during iteration by skipping
     * items marked for removal
     */
    private class DeferredOperationIterator implements Iterator<T> {
        private Iterator<T> baseIterator;
        private T nextItem;
        private boolean hasNext;
        
        public DeferredOperationIterator() {
            baseIterator = DynamicArrayList.super.iterator();
            advance();
        }
        
        private void advance() {
            hasNext = false;
            while (baseIterator.hasNext()) {
                T item = baseIterator.next();
                if (!pendingRemovals.contains(item)) {
                    nextItem = item;
                    hasNext = true;
                    break;
                }
            }
        }
        
        @Override
        public boolean hasNext() {
            return hasNext;
        }
        
        @Override
        public T next() {
            if (!hasNext) {
                throw new java.util.NoSuchElementException();
            }
            T current = nextItem;
            advance();
            return current;
        }
          @Override
        public void remove() {
            // Allow normal iterator removal - it will work with the underlying ArrayList
            baseIterator.remove();
        }
    }

}
