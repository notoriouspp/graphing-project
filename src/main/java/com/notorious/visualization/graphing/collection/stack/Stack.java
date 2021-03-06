/******************************************************************************
 *  Compilation:  javac Stack.java
 *  Execution:    java Stack < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 *  A generic stack, implemented using a singly-linked list.
 *  Each stack element is of type Item.
 *
 *  This version uses a static nested class Node (to save 8 bytes per
 *  Node), whereas the version in the textbook uses a non-static nested
 *  class (for simplicity).
 *  
 *  % more tobe.txt 
 *  to be or not to - be - - that - - - is
 *
 *  % java Stack < tobe.txt
 *  to be not that or be (2 left on stack)
 *
 ******************************************************************************/

package com.notorious.visualization.graphing.collection.stack;

import com.notorious.visualization.graphing.util.StdIn;
import com.notorious.visualization.graphing.util.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * A modern adaptation of the EdgeWeightedGraph.java written by Robert Sedgewick and Kevin Wayne.
 * Following a much stricter OOP structure, adding encapsulation, removing literal null
 * values, and introducing some of the features given by Java 8.
 *
 * <p>
 * ORIGINAL DOCUMENTATION:
 * -------------------------------------------------------------------------------
 *  The {@code Stack} class represents a last-in-first-out (LIFO) stack of generic items.
 *  It supports the usual <em>push</em> and <em>pop</em> operations, along with methods
 *  for peeking at the top item, testing if the stack is empty, and iterating through
 *  the items in LIFO order.
 *  <p>
 *  This implementation uses a singly-linked list with a static nested class for
 *  linked-list nodes. See {@link LinkedStack} for the version from the
 *  textbook that uses a non-static nested class.
 *  See {@link ResizingArrayStack} for a version that uses a resizing array.
 *  The <em>push</em>, <em>pop</em>, <em>peek</em>, <em>size</em>, and <em>is-empty</em>
 *  operations all take constant time in the worst case.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/13stacks">Section 1.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *-------------------------------------------------------------------------------
 * @author Notorious
 * @version 0.0.1
 * @since 3/12/2017
 *
 * @param <T> the generic type of an item in this stack
 */
public class Stack<T> implements Iterable<T> {

    private static final String UNDERFLOW_ERROR_MESSAGE = "Stack underflow occurred during operation!";

    private Node<T> head;     // top of stack
    private int nSize;            // size of the stack

    // helper linked list class
    private static class Node<T> {
        private final boolean dead;
        private T item;
        private Node<T> next;

        public Node() {
            dead = true;
        }

        Node(T item, Node<T> next) {
            this.item = item;
            this.next = next;
            dead = false;
        }

        /**
         * Gets item stored within this node.
         *
         * @return the item
         */
        public T getItem() {
            return item;
        }

        /**
         * Gets next node in chain.
         *
         * @return the next node
         */
        public Node<T> getNext() {
            return next;
        }

        boolean exists() {
            return !dead;
        }
    }

    /**
     * Initializes an empty stack.
     */
    public Stack() {
        //default
        head = new Node<>(); //empty node
        nSize = 0; //empties are not counted
    }

    /**
     * Returns true if this stack is empty.
     *
     * @return true if this stack is empty; false otherwise
     */
    public boolean isEmpty() {
        return !head.exists();
    }

    /**
     * Returns the number of items in this stack.
     *
     * @return the number of items in this stack
     */
    public int size() {
        return nSize;
    }

    /**
     * Adds the item to this stack.
     *
     * @param  item the item to add
     */
    public void push(T item) {
        head = new Node<>(item, head);
        nSize++;
    }

    /**
     * Removes and returns the item most recently added to this stack.
     *
     * @return the item most recently added
     * @throws NoSuchElementException if this stack is empty
     */
    public T pop() {
        if (isEmpty()) throw new NoSuchElementException(UNDERFLOW_ERROR_MESSAGE);
        T item = head.getItem();        // save item to return
        head = head.getNext();            // delete first node
        nSize--;
        return item;                   // return the saved item
    }


    /**
     * Returns (but does not remove) the item most recently added to this stack.
     *
     * @return the item most recently added to this stack
     * @throws NoSuchElementException if this stack is empty
     */
    public T peek() {
        if (isEmpty()) throw new NoSuchElementException(UNDERFLOW_ERROR_MESSAGE);
        return head.getItem();
    }

    /**
     * Returns a string representation of this stack.
     *
     * @return the sequence of items in this stack in LIFO order, separated by spaces
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (T item : this) {
            s.append(item);
            s.append(' ');
        }
        return s.toString();
    }
       

    /**
     * Returns an iterator to this stack that iterates through the items in LIFO order.
     *
     * @return an iterator to this stack that iterates through the items in LIFO order
     */
    public Iterator<T> iterator() {
        return new ListIterator<>(head);
    }

    // an iterator, doesn't implement remove() since it's optional
    private static class ListIterator<T> implements Iterator<T> {
        private Node<T> current;

        ListIterator(Node<T> first) {
            current = first;
        }

        public boolean hasNext() {
            return current.exists();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T item = current.getItem();
            current = current.getNext();
            return item;
        }
    }


    /**
     * Unit tests the {@code Stack} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Stack<String> stack = new Stack<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                stack.push(item);
            } else if (!stack.isEmpty()) {
                StdOut.print(stack.pop() + " ");
            }
        }
        StdOut.println("(" + stack.size() + " left on stack)");
    }
}


/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/