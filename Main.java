public class Main {

    public static void main(String[] args) {
        AssociativeProcessor a = new AssociativeProcessor(5, 8);

        System.out.println("Memory                    " + a.getAssociativeMemory());

        System.out.println("Min to max                " + a.sortMinToMax());

        System.out.println("Max to min                " + a.sortMaxToMin());

        String function = "(x * y) + !x + (y * !z)";
        System.out.println("Boolean function search   " + a.booleanFunctionSearch(function));
    }
}