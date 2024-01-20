package linearDatastructures;

import java.util.*;


public class MyALDAList<E> implements ALDAList<E>{
    private Node<E> head;
    private int size;


    public MyALDAList(){
        this.head = new Node<>();
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

        Node<E> node = head;
        size++;

        if(index == 0){
            Node<E> newHead = new Node<>(element);
            newHead.next = head;
            head.prev = newHead;
            head = newHead;
            return;
        }

        for(int i = 0; i < index; i++){
            node = node.next;
        }

        Node<E> nextNode = node.next;
        node.next = new Node<>(element,node);
        node.next.next = nextNode;
    }


    @Override
    public E remove(int index) {
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }

        LinkedList<E> list = new LinkedList<>();

        Node<E> node = head;
        for(int i = 0; i < index; i++){
            node = node.next;
        }

        E data = node.data;
        node.prev = node.next;
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
        head = new Node<>();
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
        for(int i = 0; i < size-1; i++){
            stringBuilder.append(node.data);
            stringBuilder.append(", ");
            node = node.next;
        }
        if(node.data != null){
            stringBuilder.append(node.data);
        }


        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private static class Node<E>{
        public Node(E data){
            this.data = data;
            this.next = null;
            this.prev = null;
        }

        public Node(E data, Node<E> prev){
            this.data = data;
            this.next = null;
            this.prev = prev;
        }

        public Node(){
            this.data = null;
            this.next = null;
            this.prev = null;
        }
        private E data;
        private Node<E> next;
        private Node<E> prev;
    }

    private class ALDAListIterator implements Iterator<E>{
        private int nextIndex;

        ALDAListIterator(){
            nextIndex = 0;
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
            E data = get(nextIndex);
            nextIndex++;
            return data;
        }

        @Override
        public void remove() {
            MyALDAList.this.remove(nextIndex);
        }
    }
}
