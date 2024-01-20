package linearDatastructures;

//Felix Lidö feli8145

import java.util.*;


public class MyALDAList<E> implements ALDAList<E>{
    private Node<E> head;
    private int size;


    public MyALDAList(){
        this.head = new Node<>(null);
        this.size = 0;
    }


    @Override
    public void add(E element) {
        size++;
        if(head.data == null){
            head.data = element;
            return;
        }

        Node<E> temp = head;
        while(temp.next != null){
            temp = temp.next;
        }
        temp.next = new Node<>(element,temp);
    }

    @Override
    public void add(int index, E element) {
        if(index < 0 || index > size){
            throw new IndexOutOfBoundsException();
        }

        if(index == size){
            add(element);
            return;
        }

        size++;

        if(index == 0){
            Node<E> newHead = new Node<>(element);
            newHead.next = head;
            head.prev = newHead;
            head = newHead;
            return;
        }

        Node<E> node = head;
        for(int i = 0; i < index-1; i++){
            node = node.next;
        }

        Node<E> nextNode = node.next;
        node.next = new Node<>(element,node);
        node.next.next = nextNode;
        nextNode.prev = node.next;
    }


    @Override
    public E remove(int index) {
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }

        size--;

        //Set to head to next
        if(index == 0) {
            E data = head.data;
            if (size == 0) { //Compare to zero because of the size--
                clear();
            }
            else{
                head.next.prev = null;
                head = head.next;
            }
            return data;
        }

        Node<E> nodeToRemove = head;
        for(int i = 0; i < index; i++){
            nodeToRemove = nodeToRemove.next;
        }

        E data = nodeToRemove.data;

        //Node to remove is tail, so just move the tail
        if(nodeToRemove.next == null){
            nodeToRemove.prev.next = null;
            return data;
        }

        Node<E> prev = nodeToRemove.prev;
        Node<E> next = nodeToRemove.next;

        prev.next = next;
        next.prev = prev;

        return data;
    }
    @Override
    public boolean remove(E element) {
        if(head.data == null){
            return false;
        }

        for(E elem : this){
            if(elem.equals(element)){
                remove(indexOf(element));
                return true;
            }
        }

        return false;
    }

    @Override
    public E get(int index) {
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }

        Node<E> node = head;
        for(int i = 0; i < index; i++){
            node = node.next;
        }

        return node.data;
    }

    @Override
    public boolean contains(E element) {
        for(E e : this){
            if(e.equals(element)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int indexOf(E element) {
        Node<E> node = head;
        int i = 0;
        while(node != null){
            if(node.data.equals(element)){
                return i;
            }
            i++;
            node = node.next;
        }
        return -1;
    }

    @Override
    public void clear() {
        head = new Node<>(null);
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new ALDAListIterator();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");

        Node<E> node = head;
        if(node == null){
            return "[]";
        }

        for(int i = 0; i < size-1; i++){
            stringBuilder.append(node.data);
            stringBuilder.append(", ");
            node = node.next;
        }

        if(node != null && node.data != null){
            stringBuilder.append(node.data);
        }


        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private static class Node<E>{

        private E data;
        private Node<E> next;
        private Node<E> prev;

        Node(E data){
            this.data = data;
            this.next = null;
            this.prev = null;
        }

        Node(E data, Node<E> prev){
            this.data = data;
            this.next = null;
            this.prev = prev;
        }


        @Override
        public String toString() {
            return data.toString();
        }
    }

    private class ALDAListIterator implements Iterator<E>{
        private int nextIndex;
        private boolean nextHasBeenCalled;

        ALDAListIterator(){
            nextIndex = 0;
            nextHasBeenCalled = false;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public E next() {
            if(nextIndex >= size){
                throw new NoSuchElementException();
            }
            E data = MyALDAList.this.get(nextIndex);
            nextIndex++;
            nextHasBeenCalled = true;
            return data;
        }

        @Override
        public void remove() {
            if(!nextHasBeenCalled){
                throw new IllegalStateException();
            }
            nextIndex--;
            nextHasBeenCalled = false;
            MyALDAList.this.remove(nextIndex);
        }
    }
}
