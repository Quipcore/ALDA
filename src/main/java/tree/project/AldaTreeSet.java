package tree.project;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/*
 * Write an implementation of the TreeSet class, with associated iterators, using a
 * binary search tree. Add to each node a link to the next smallest and next largest
 * node. To make your code simpler, add a header and tail node which are not part of
 * the binary search tree, but help make the linked list part of the code simpler
 */

/* Java requires that TreeSet and TreeMap support the basic add, remove, and contains oper-
 * ations in logarithmic worst-case time. Consequently, the underlying implementation is a
 * balanced binary search tree. Typically, an AVL tree is not used; instead, top-down red-black
 * trees, which are discussed in Section 12.2, are often used
 */



public class AldaTreeSet<E> implements Set<E> {

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }
}
