package algorithmAnalisys;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Experiment {
    public static void main(String[] args) {
        List<Integer> stk = new Stack<>();
        stk.add(1);
        stk.add(2);
        stk.add(3);
        stk.add(4);
        stk.add(1,124);

        System.out.println(stk);
        System.out.println(stk.get(3));

        int i = ((Stack<Integer>)stk).pop();

        System.out.println(i);

        List<Integer> linked = new LinkedList<>(stk);
        System.out.println(linked.get(3));
    }
}
