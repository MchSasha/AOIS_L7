public class Main {

    public static void main(String[] args) {
        AssociativeMemory memory = new AssociativeMemory();
        System.out.println("Associative memory\n" + memory);
        System.out.println("Associative memory with diagonal addressing \n" + memory.toDiagonal());
        System.out.println("Write word in Associative memory with diagonal addressing");
        memory.writeWordInMemory("1010101101010100", 5);
        System.out.println(memory.toDiagonal());
        System.out.println("Write column in Associative memory with diagonal addressing");
        memory.writeColumnInMemory("0000000000000000", 0);
        System.out.println(memory.toDiagonal() + "\nAssociative memory after column writing\n" + memory);
        System.out.printf("Read column from Associative memory with diagonal addressing\n%s\n%n", memory.readColumnFromMemory(0));
        System.out.printf("Read word from Associative memory with diagonal addressing\n%s\n%n", memory.readWordFromMemory(0));
        System.out.printf("f1 function in Associative memory with diagonal addressing \n%s\n%n", memory.f1(0, 1));
        System.out.printf("f14 function in Associative memory with diagonal addressing \n%s\n%n", memory.f14(0, 1));
        System.out.printf("f3 function in Associative memory with diagonal addressing \n%s\n%n", memory.f3(0));
        System.out.printf("f12 function in Associative memory with diagonal addressing \n%s\n%n", memory.f12(0));
        System.out.printf("The closest words in Associative memory with diagonal addressing \n%s\n%n", memory.findTheClosest("0010101101010100"));
        System.out.println("Mask search and addition in Associative memory");
        memory.makeFieldAddition("000");
        System.out.println(memory);
    }
}