package linearDatastructures;

import java.util.Iterator;


public class MyALDAList<E> implements ALDAList<E>{

    static class Node<E>{
        public Node(E data){
            this.data = data;
            this.next = null;
        }

        public Node(){
            this.data = null;
            this.next = null;
        }
        private E data;
        private Node<E> next;
    }

    private Node<E> head;

    private int size = 0;

    @Override
    public void add(E element) {
        Node<E> temp = head;
        while(temp != null){
            temp = temp.next;
        }
        temp = new Node<>(element); //TODO: Fix, Sätter pekaren/referesen till ny nod och inte next
    }

    @Override
    public void add(int index, E element) {
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }


    }

    @Override
    public E remove(int index) {
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }

        Node<E> node = head;
        for(int i = 0; i < index; i++){
            node = head.next;
        }



        return null;
    }

    @Override
    public boolean remove(E element) {
        return false;
    }

    @Override
    public E get(int index) {
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }

        Node<E> node = head;
        for(int i = 0; i < index; i++){
            node = head.next;
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
        }
        return -1;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
