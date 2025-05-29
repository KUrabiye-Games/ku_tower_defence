package com.kurabiye.kutd.DynamicList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DynamicArrayList<T> extends ArrayList<T> {


    // This class extends ArrayList to create a dynamic array that can grow as needed
    // It can be used to store objects of any type with deferred operations during iteration
    
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
     * Returns an iterator that skips items marked for removal and doesn't show staged additions.
     * This provides a consistent view during iteration even when items are marked for operations.
     */
    @Override
    public Iterator<T> iterator() {
        return new DeferredOperationIterator();
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
