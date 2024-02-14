package sorting;

import java.util.Collections;
import java.util.Random;

public class Bogosort {

    public static <T extends Comparable<T>> void sort(T[] arr){
        while(!isSorted(arr)){
            shuffle(arr);
        }
    }

    private static <T extends Comparable<T>> boolean isSorted(T[] arr){
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].compareTo(arr[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    //Fisher–Yates shuffle
    private static <T extends Comparable<T>> void shuffle(T[] arr) {
        Random random = new Random();
        for(int i = arr.length-1; i >= 0;i--){
            int j = random.nextInt(i+1);
            T temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public static void main(String[] args) {

        Integer[] arr = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        shuffle(arr);

        print(arr);

        sort(arr);

        print(arr);
    }

    private static void print(Object[] arr){
        for(Object obj : arr){
            System.out.print(obj + " ");
        }

        System.out.println();
    }
}
