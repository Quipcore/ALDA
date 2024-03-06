package linearDatastructures.project;

import java.util.*;

public class SubList {


    public static void main(String[] args) {
        List<Integer> indices = getIndices();
        List<Integer> values = getValues();
        List<Integer> subList = getSubList(values,indices);

        System.out.println(subList);
        System.out.println(indices);
    }

    private static List<Integer> getValues() {
        return List.of(1,2,3);
    }

    private static List<Integer> getIndices() {
        List<Integer> values = new LinkedList<>(List.of(0,2,10));
        values.sort(Comparator.comparingInt(Integer::intValue));
        return values;
    }

    public static <T> List<T> getSubList(List<T> values, List<Integer> indices){
        if(!isSorted(indices)) {
            throw new IllegalArgumentException("Indices is not sorted!");
        }

        List<T> subList = new LinkedList<>();

        ListIterator<Integer> listIterator = indices.listIterator();
        Integer node = listIterator.next();

        int index = 0;
        for (T value : values){
            if(node.equals(index)){
                subList.add(value);
                if(!listIterator.hasNext()) {
                    return subList;
                }

                node = listIterator.next();
            }
            index++;
        }
        return subList;
    }

    private static boolean isSorted(List<Integer> indices) {
        return true;
    }
}
