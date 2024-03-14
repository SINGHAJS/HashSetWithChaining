package HashSetWithChaining;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.callback.TextInputCallback;

/**
 * @author Ajit Singh
 * 
 */

public class HashSetWithChaining<E> implements Set<E> {
    private int numElements;
    private Node<E>[] table;
    private double loadFactor;
    private int defaultInitialCapacity = 20;

    /**
     * default constructor
     */
    public HashSetWithChaining() {
        this.numElements = 0;
        this.loadFactor = 0.75;
        this.table = new Node[defaultInitialCapacity];
        System.out.println("[HasSetWithChaining Created]");
        System.out.println("\tDefaultInitial Capacity=" + defaultInitialCapacity
                + "\n\tDefault Load Factor:" + loadFactor + "\n");
    }

    /**
     * custom constructor, takes in a input of capacity and load factor defined by
     * the user. sets the input values to their respectful variables.
     * 
     * @param customCapacity
     * @param customLoadFactor
     */
    public HashSetWithChaining(int customCapacity, double customLoadFactor) {
        this.loadFactor = customLoadFactor;
        int customInitialCapacity = (customCapacity <= 0) ? defaultInitialCapacity : customCapacity;
        this.table = new Node[customInitialCapacity];

        System.out.println("[HasSetWithChaining Created]");
        System.out.println("\tCustom Initial Capacity=" + customInitialCapacity
                + "\n\tCustom Load Factor:" + customLoadFactor + "\n");
    }

    /**
     * checks the number of the elements in the table and returns those elements.
     * 
     * @return int, the size of number of elements in the table
     */
    @Override
    public int size() {
        return this.numElements;
    }

    /**
     * checks if the table is empty
     * 
     * @return boolean
     *         false if the table size is not zero and true if the table size zero
     */
    @Override
    public boolean isEmpty() {
        return this.numElements == 0;
    }

    /**
     * given an element e, it adds the element to the table[] array
     * 
     * @param e type E
     * @return Boolean, false if the element is not added or true if and only if the
     *         element is added to the table
     */
    @Override
    public boolean add(E e) {
        Node<E> newNode = new Node<E>(e);

        // not adding duplicates
        // and checking if input obj is null
        if (contains(e) || e == null) {
            return false;
        }

        // checking table capacity
        if ((this.numElements / table.length) > loadFactor) {
            expandTable();
        }

        // calculating the index of the element
        int index = newNode.element.hashCode() % this.table.length;

        // adding the elements to the calculated index
        if (table[index] != null) {
            // linking the nodes if elements have the same index
            newNode.nextNode = table[index];
            table[index] = newNode;
            newNode.hasNext = true;
            this.numElements++;
            return true;
        }

        // if the value at index is null, it is added without a link
        table[index] = newNode;
        this.numElements++;
        return true;
    }

    /**
     * given an object, it will check if the object exists in the table. If it
     * exists in the table it will remove that out of the table. Otherwise, it will
     * not remove the object and return false.
     * 
     * @param obj type Object
     * @return Boolean, false if the obj was not removed from the table. True if and
     *         only if the object existed and was removed from the table.
     */
    @Override
    public boolean remove(Object obj) {
        // checking if input obj is null
        if (obj == null)
            return false;

        int index = obj.hashCode() % this.table.length;
        Node<E> node = table[index];

        // checking if the location based on the given object is null, if so return

        // false as the element at that index does not exist
        if (table[index] == null)
            return false;

        // comparing the two object values
        if (node.element.equals(obj)) {
            node.element = null;
            numElements--;
            return true;
        } else if (node.hasNext) { // executes if the node has links to the next nodes
            // iterates over the linked nodes and compares those values
            Node<E> nextNode = node.nextNode;
            while (nextNode != null) {
                if (nextNode.element.equals(obj)) {
                    node.nextNode = nextNode.nextNode;
                    this.numElements--;
                    return true;
                }
                nextNode = nextNode.nextNode;
                node = node.nextNode;
            }
        }

        return false;
    }

    /**
     * given an object obj, it will check if the obj exists.
     * 
     * @param obj type Object
     * @return Boolean, false if the obj does not exist in the table and true if and
     *         only if the element exists in the table
     */
    @Override
    public boolean contains(Object obj) {

        // checking if input obj is null
        if (obj == null)
            return false;

        int index = obj.hashCode() % this.table.length;
        Node<E> node = table[index];

        // checking if the location based on the given object is null, if so return
        // false as the element at that index does not exist
        if (node == null || node.element == null)
            return false;

        // comparing the two object values
        if (node.element.equals(obj)) {
            return true;
        } else if (node.hasNext) { // executes if the node has links to the next nodes
            // iterates over the linked nodes and compares those values
            while (node != null) {
                if (node.element.equals(obj)) {
                    return true;
                }
                node = node.nextNode;
            }
        }

        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    /**
     * converts the table array to an array of type object.
     * 
     * @return objArr, the new array of type Object is returned.
     */

    @Override
    public Object[] toArray() {
        Object[] objArr = new Object[table.length];

        for (int i = 0; i < this.numElements; i++) {
            objArr[i] = table[i];
        }

        return objArr;
    }

    /**
     * @param a type T[], given an array of type T, it will take that array set the
     *          values of the given array to the values of the table.
     * 
     * @return boolean, false if the element is not added and true if and only if
     *         the element has been added.
     */
    @Override
    public <T> T[] toArray(T[] a) throws IllegalArgumentException {

        if (a == null)
            throw new IllegalArgumentException();

        a = (T[]) (new Object[table.length]);

        for (int i = 0; i < this.numElements; i++) {
            a[i] = (T) table[i];
        }

        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c == null)
            return false;

        int counter = 0;
        for (Object val : c) {
            if (contains(val))
                counter++;
        }
        return counter == c.size();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null)
            return false;

        int counter = 0;
        for (E val : c) {
            if (add(val))
                counter++;
        }
        return counter == c.size();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int expectedSize = table.length - c.size();

        if (c == null)
            return false;
        for (Object val : c) {
            remove(val);
        }

        return true;
    }

    /**
     * clears all the elements in the table and sets their values to null
     */
    @Override
    public void clear() {
        table = new Node[defaultInitialCapacity];
        this.numElements = 0;
    }

    /**
     * increases the size of the table
     */
    private void expandTable() {
        /* testing reasons */
        // System.out.println("[expanding array by " + (table.length * 2 + 1) + "]");

        Node[] expandedArray = new Node[table.length * 2 + 1];

        for (int i = 0; i < numElements; i++) {
            expandedArray[i] = table[i];
        }

        table = expandedArray;
    }

    /* Node Inner Class */
    private class Node<E> {
        private E element;
        private boolean hasNext;
        private Node<E> nextNode;

        public Node(E element) {
            this.element = element;
            this.nextNode = null;
            this.hasNext = false;
        }
    }

    @Override
    public String toString() {
        String str = "";
        int rowCounter = 0;

        for (int i = 0; i < table.length; i++) {
            str += "row " + rowCounter++ + ": ";
            if (table[i] != null) {
                if (table[i].hasNext) {
                    Node<E> currentNode = table[i];
                    while (currentNode.nextNode != null) {
                        str += currentNode.nextNode.element + " --> ";
                        currentNode = currentNode.nextNode;
                    }
                }
                str += table[i].element + "";
            }
            str += "\n";
        }
        return str;
    }

    public static void main(String[] args) {
        HashSetWithChaining<Driver> table = new HashSetWithChaining<Driver>();
        // HashSetWithChaining<Driver> table = new HashSetWithChaining<Driver>(5, 0.5);

        // test data
        Driver d1 = new Driver("Jill");
        Driver d2 = new Driver("Jeff");
        Driver d3 = new Driver("Tim");
        Driver d4 = new Driver("Jim");
        Driver d5 = new Driver("Josh");
        Driver d6 = new Driver("Bob");
        Driver d7 = new Driver("Simon");
        Driver d8 = new Driver("Harry");

        /* not added for the testing purpose of contains() method */
        Driver d9 = new Driver("Leo");
        Driver d10 = new Driver("John");

        /******* add() method test *******/
        table.add(d1);
        table.add(d2);
        table.add(d3);
        table.add(d4);
        table.add(d5);
        table.add(d6);
        table.add(d7);
        table.add(d8);
        table.add(null);

        // System.out.println(table);

        /******* contains() method test *******/
        System.out.println("contains Josh: " + table.contains(d5));
        System.out.println("contains Jim: " + table.contains(d4));
        System.out.println("contains Simon: " + table.contains(d7));
        System.out.println("contains Jeff: " + table.contains(d2));
        System.out.println("contains Harry: " + table.contains(d8));
        System.out.println("contains Leo: " + table.contains(d9));
        System.out.println("contains John: " + table.contains(d10));
        System.out.println("contains null: " + table.contains(null));

        System.out.println("size: " + table.size());

        /******* clear() method test *******/
        // table.clear();
        // System.out.println(table);
        /******************************/

        /******* remove() method test *******/

        // System.out.println("is Josh removed: " + table.remove(d5));
        // System.out.println("contains Josh: " + table.contains(d5));
        // System.out.println("size: " + table.size());

        // System.out.println("is Jeff removed: " + table.remove(d2));
        // System.out.println("contains Jeff: " + table.contains(d2));
        // System.out.println("size: " + table.size());

        // System.out.println("is Simon removed: " + table.remove(d7));
        // System.out.println("contains Simon: " + table.contains(d7));
        // System.out.println("size: " + table.size());

        // System.out.println(table.size());
        // table.clear();
        // table.add(d6);
        // System.out.println("size: " + table.size());
        System.out.println(table);

    }

}