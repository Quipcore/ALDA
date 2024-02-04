package tree;

/**
 *
 * Detta är den enda av de tre klasserna ni ska göra några ändringar i. (Om ni
 * inte vill lägga till fler testfall.) Det är också den enda av klasserna ni
 * ska lämna in. Glöm inte att namn och användarnamn ska stå i en kommentar
 * högst upp, och att en eventuell paketdeklarationen måste plockas bort vid inlämningen för
 * att koden ska gå igenom de automatiska testerna.
 *
 * De ändringar som är tillåtna är begränsade av följande:
 * <ul>
 * <li>Ni får INTE byta namn på klassen.
 * <li>Ni får INTE lägga till några fler instansvariabler.
 * <li>Ni får INTE lägga till några statiska variabler.
 * <li>Ni får INTE använda några loopar någonstans. Detta gäller också alterntiv
 * till loopar, så som strömmar.
 * <li>Ni FÅR lägga till fler metoder, dessa ska då vara privata.
 * <li>Ni får INTE låta NÅGON metod ta en parameter av typen
 * BinarySearchTreeNode. Enbart den generiska typen (T eller vad ni väljer att
 * kalla den), String, StringBuilder, StringBuffer, samt primitiva typer är
 * tillåtna.
 * </ul>
 *
 * @author henrikbe
 *
 * @param <T>
 */
@SuppressWarnings("unused") // Denna rad ska plockas bort. Den finns här
// tillfälligt för att vi inte ska tro att det är
// fel i koden. Varningar ska normalt inte döljas på
// detta sätt, de är (oftast) fel som ska fixas.
public class BinarySearchTreeNode<T extends Comparable<T>> {

    private T data;
    private BinarySearchTreeNode<T> left;
    private BinarySearchTreeNode<T> right;

    public BinarySearchTreeNode(T data) {
        this.data = data;
    }

    public boolean add(T data) {

        int compareResult = data.compareTo(this.data);
        if(compareResult == 0){
            return false;
        }

        if(compareResult < 0) {
            if (left == null) {
                left = new BinarySearchTreeNode<>(data);
                return true;
            }
            return left.add(data);
        }

        if (right == null) {
            right = new BinarySearchTreeNode<>(data);
            return true;
        }
        return right.add(data);
    }

    private T findMin() {
        if(left == null){
            return data;
        }

        return left.findMin();
    }

    public BinarySearchTreeNode<T> remove(T data) {

        //No need to remove an element that doesn't exist
        if(!contains(data)){
            return this;
        }

        //Root
        if(data.equals(this.data)){
            if(right != null){
                T min = right.findMin();
                right = right.remove(min);
                this.data = min;
                return this;
            } else if (left != null) {
                return left;
            }else {
                return null;
            }
        }

        //Remove from left
        if(left != null && left.contains(data)){
            if(left.isLeaf()){
                left = null;
                return this;
            }
            left = left.remove(data);
            return this;
        }
        //Remove from right
        else if (right != null && right.contains(data)) {
            if(right.isLeaf()){
                right = null;
                return this;
            }
            right = right.remove(data);
            return this;
        }

        return null; //Never hit by test
    }



    private boolean isLeaf(){
        return left == null && right == null;
    }

    public boolean contains(T data) {

        if(this.data == null){return false;}
        if(this.data.equals(data)){return true;}

        if(data.compareTo(this.data) < 0){
            if(left == null){
                return false;
            }
            return left.contains(data);
        }

        if(right == null){
            return false;
        }
        return right.contains(data);
    }

    public int size() {

        int sizeOfLeft = left != null ? left.size() : 0;
        int sizeOfRight = right != null ? right.size() : 0;
        int sizeOfCurrent = data != null ? 1 : 0;

        return sizeOfCurrent + sizeOfLeft + sizeOfRight;
    }

    public int depth() {
        if(isLeaf()){
            return 0;
        }

        int depthOfLeft = left == null ? 0 : left.depth();
        int depthOfRight = right == null ? 0 : right.depth();
        return Math.max(depthOfLeft, depthOfRight) + 1;
    }

    public String toString() {
        String node = "";
        if(left != null){
            node += left.toString();
        }

        if(data != null){
            if(!node.isEmpty()){
                node += ", ";
            }
            node += data.toString();
        }

        if(right != null){
            if(!node.isEmpty()){
                node += ", ";
            }
            node += right.toString();
        }

        return node;
    }
}