import processor.AssociativeProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AssociativeMemory {
    private final List<String> associativeMemory = new ArrayList<>();
    private List<String> diagonalAssociativeMemory;

    public static final int BITS = 16;

    public AssociativeMemory() {
        fillAssociativeMemory();
        diagonalAssociativeMemory = toDiagonalAddressing();
    }



    private void fillAssociativeMemory() {
        for (int wordsCounter = 0; wordsCounter < AssociativeMemory.BITS; wordsCounter++) {
            String newWord = IntStream.range(0, AssociativeMemory.BITS)
                    .mapToObj(i -> new Random().nextInt(2))
                    .map(String::valueOf)
                    .collect(Collectors.joining());

            associativeMemory.add(newWord);
        }
    }

    private List<String> toDiagonalAddressing() {
        List<String> diagonalAddressing = new ArrayList<>();

        int wordsCounter = 0;

        for (String word : associativeMemory) {

            StringBuilder wordAsColumn = new StringBuilder(word.substring(0, BITS - wordsCounter));
            for (int newIter = 0, prevPositionIter = word.length() - wordsCounter; newIter < wordsCounter && prevPositionIter < word.length(); newIter++, prevPositionIter++) {
                wordAsColumn.insert(newIter, word.charAt(prevPositionIter));
            }

            diagonalAddressing.add(String.valueOf(wordAsColumn));
            wordsCounter++;
        }
        diagonalAddressing = reverseMemoryRepresentation(diagonalAddressing);
        return diagonalAddressing;
    }

    private void toStandardAddressing(String word, int bit) {
        int iter = 0;

        while (iter < BITS) {
            StringBuilder oldValue = new StringBuilder(associativeMemory.get(iter));
            oldValue.setCharAt(bit, word.charAt(iter));
            associativeMemory.set(iter, oldValue.toString());
            iter++;
        }
    }

    private List<String> reverseMemoryRepresentation(List<String> memory) {
        List<String> reversedDiagonalAddressing = new ArrayList<>();
        for (int iter = 0; iter < BITS; iter++) {
            reversedDiagonalAddressing.add("");
        }

        for (int wordIter = 0; wordIter < BITS; wordIter++) {
            String word = memory.get(wordIter);

            int bitsIter = 0;
            while (bitsIter < BITS) {
                reversedDiagonalAddressing.set(bitsIter, reversedDiagonalAddressing.get(bitsIter) + word.charAt(bitsIter));
                bitsIter++;
            }
        }

        return reversedDiagonalAddressing;
    }



    public void writeWordInMemory(String word, int address) {
        associativeMemory.set(address, word);
        diagonalAssociativeMemory = toDiagonalAddressing();
    }

    public String readWordFromMemory(int address) {
        return associativeMemory.get(address);
    }


    public void writeColumnInMemory(String column, int bit) {
        int columnIter = 0, memoryColumn = bit;
        for (int memoryRow = bit; columnIter < BITS && memoryRow < BITS; columnIter++, memoryColumn++, memoryRow++) {
            StringBuilder newColumn = new StringBuilder(diagonalAssociativeMemory.get(memoryRow));
            newColumn.setCharAt(memoryColumn, column.charAt(columnIter));

            diagonalAssociativeMemory.set(memoryRow, newColumn.toString());
        }
        for (int memoryRow = 0; columnIter < BITS && memoryRow < bit; columnIter++, memoryColumn++, memoryRow++) {
            StringBuilder newColumn = new StringBuilder(diagonalAssociativeMemory.get(memoryRow));
            newColumn.setCharAt(memoryColumn, column.charAt(columnIter));

            diagonalAssociativeMemory.set(memoryRow, newColumn.toString());
        }

        toStandardAddressing(column, bit);
    }

    public String readColumnFromMemory(int bit) {
        StringBuilder result = new StringBuilder();

        for (String word : associativeMemory) {
            result.append(word.charAt(bit));
        }

        return result.toString();
    }



    public String f1(int bit1, int bit2) {
        StringBuilder result = new StringBuilder();

        String word1 = readColumnFromMemory(bit1);
        String word2 = readColumnFromMemory(bit2);

        for (int iter = 0; iter < BITS; iter++) {
            result.append(conjunction(word1.charAt(iter), word2.charAt(iter)));
        }

        return String.valueOf(result);
    }

    public String f14(int bit1, int bit2) {
        StringBuilder result = new StringBuilder(f1(bit1, bit2));

        for (int iter = 0; iter < BITS; iter++) {
            result.setCharAt(iter, negotiation(result.charAt(iter)));
        }

        return String.valueOf(result);
    }

    public String f3(int bit) {
        return readColumnFromMemory(bit);
    }

    public String f12(int bit) {
        String word = readColumnFromMemory(bit);
        StringBuilder result = new StringBuilder(word);

        for (int iter = 0; iter < BITS; iter++) {
            result.setCharAt(iter, negotiation(result.charAt(iter)));
        }

        return String.valueOf(result);
    }



    public List<String> findTheClosest(String word) {
        AssociativeProcessor processor = new AssociativeProcessor(associativeMemory, BITS);
        List<String> sortedMemory = processor.sortMinToMax();

        for (int iter = 0; iter < BITS; iter++) {
            if (processor.compare(sortedMemory.get(iter), word) == 0) {

                if (iter != 0 && iter != BITS - 1) {
                    return List.of(sortedMemory.get(iter - 1), sortedMemory.get(iter + 1));
                }
                if (iter == 0) {
                    return List.of(sortedMemory.get(iter + 1));
                }
                if (iter == BITS - 1) {
                    return List.of(sortedMemory.get(iter-1));
                }
            }
        }
        return new ArrayList<>();
    }

    public void makeFieldAddition(String mask) {
        int iter = 0;
        while (iter < BITS) {
            String word = associativeMemory.get(iter);

            if (Objects.equals(mask, word.substring(0, 3))) {
                String result = addition(word.substring(3, 7), word.substring(7, 11));
                StringBuilder res = new StringBuilder(word);
                res.replace(11, 16, result);

                associativeMemory.set(iter, String.valueOf(res));
            }
            iter++;
        }

        diagonalAssociativeMemory = toDiagonalAddressing();
    }



    public static String addition(String valueA, String valueB) {
        StringBuilder result = new StringBuilder();
        int shift = 0;
        int aIter = valueA.length() - 1;
        int bIter = valueB.length() - 1;

        while (aIter >= 0 || bIter >= 0 || shift > 0) {
            int digit1 = aIter >= 0 ? valueA.charAt(aIter) - '0' : 0;
            int digit2 = bIter >= 0 ? valueB.charAt(bIter) - '0' : 0;

            int digitSum = digit1 + digit2 + shift;
            result.insert(0, digitSum % 2);
            shift = digitSum / 2;

            aIter--;
            bIter--;
        }
        while (result.length() != 5) {
            result.insert(0, '0');
        }

        return result.toString();
    }

    private char conjunction(char conjunct1, char conjunct2) {
        if (conjunct1 == conjunct2 && conjunct1 == '1') {
            return '1';
        }
        return '0';
    }

    private char negotiation(char value) {
        return (value == '1') ? '0' : '1';
    }



    public String toDiagonal() {
        StringBuilder memory = new StringBuilder();
        for (String row : diagonalAssociativeMemory) {
            memory.append(row).append('\n');
        }

        return memory.toString();
    }

    @Override
    public String toString() {
        StringBuilder memory = new StringBuilder();
        for (String row : associativeMemory) {
            memory.append(row).append('\n');
        }

        return memory.toString();
    }

}