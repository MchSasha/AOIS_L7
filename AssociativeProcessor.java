import truthtable.TruthTable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AssociativeProcessor {
    private final List<String> associativeMemory = new ArrayList<>();
    private final int bitsCount;

    public AssociativeProcessor(int wordsCount, int bitsCount) {
        this.bitsCount = bitsCount;

        fillAssociativeMemory(wordsCount, bitsCount);
    }

    public List<String> getAssociativeMemory() {
        return associativeMemory;
    }

    private void fillAssociativeMemory(int wordsCount, int bitsCount) {
        for (int wordsCounter = 0; wordsCounter < wordsCount; wordsCounter++) {
            String newWord = IntStream.range(0, bitsCount)
                    .mapToObj(i -> new Random().nextInt(2))
                    .map(String::valueOf)
                    .collect(Collectors.joining());

            associativeMemory.add(newWord);
        }
    }

    private int getQValue(int prevQValue, int aValue, int sValue, int prevLValue) {
        boolean qValue = toBoolean(prevQValue) || ((!toBoolean(aValue) && toBoolean(sValue)) && !toBoolean(prevLValue));

        return qValue ? 1 : 0;
    }

    private int getLValue(int prevLValue, int aValue, int sValue, int prevQValue) {
        boolean lValue = toBoolean(prevLValue) || ((toBoolean(aValue) && !toBoolean(sValue)) && !toBoolean(prevQValue));

        return lValue ? 1 : 0;
    }

    private int compare(String word, String argument) {
        if (word.length() != argument.length()) return -2;

        int prevLValue = 0;
        int prevQValue = 0;

        for (int bit = 0; bit < bitsCount; bit++) {
            int qValue = getQValue(prevQValue, toInt(argument.charAt(bit)), toInt(word.charAt(bit)), prevLValue);
            int lValue = getLValue(prevLValue, toInt(argument.charAt(bit)), toInt(word.charAt(bit)), prevQValue);

            prevLValue = lValue;
            prevQValue = qValue;
        }

        return Integer.compare(prevQValue, prevLValue);
    }

    public List<String> sortMinToMax() {
        List<String> memoryContent = new ArrayList<>(associativeMemory);
        List<String> minWords = new ArrayList<>();
        List<String> maxWords = new ArrayList<>();

        while (memoryContent.size() > 0) {
            String minWord = findMinWord(memoryContent);
            String maxWord = findMaxWord(memoryContent);

            if (Objects.equals(minWord, maxWord)) {
                IntStream.range(0, Collections.frequency(memoryContent, minWord)).forEach(i -> minWords.add(minWord));
                break;
            }

            IntStream.range(0, Collections.frequency(memoryContent, minWord)).forEach(i -> minWords.add(minWord));
            memoryContent.removeAll(minWords);
            IntStream.range(0, Collections.frequency(memoryContent, maxWord)).forEach(i -> maxWords.add(0, maxWord));
            memoryContent.removeAll(maxWords);
        }
        memoryContent.clear();
        memoryContent.addAll(minWords);
        memoryContent.addAll(maxWords);

        return memoryContent;
    }

    public String booleanFunctionSearch(String function) {
        TruthTable table = new TruthTable(function);
        String tableValue = table.getTableValue(table.getOperandsNumber());

        for (String word : associativeMemory) {
            int comparingResult = compare(word, tableValue);

            if (comparingResult != 0) {
                continue;
            }

            System.out.println("Search according to the boolean function finished successfully, the appropriate word " + word + " was found");
            return word;
        }
        System.out.println("Search according to the boolean function finished unsuccessfully, the appropriate word " + tableValue + " was not found");
        return "";
    }

    public List<String> sortMaxToMin() {
        List<String> memoryContent = new ArrayList<>(sortMinToMax());
        Collections.reverse(memoryContent);

        return memoryContent;
    }



    private String findMinWord(List<String> range) {
        int iter = range.size()-1;
        String minWord = range.get(iter);

        while (--iter >= 0) {
            int comparisonResult = compare(range.get(iter), minWord);

            if(comparisonResult >= 0) continue;

            minWord = range.get(iter);
        }
        return minWord;
    }

    private String findMaxWord(List<String> range) {
        int iter = range.size()-1;
        String maxWord = range.get(iter);

        while (--iter >= 0) {
            int comparisonResult = compare(range.get(iter), maxWord);

            if(comparisonResult <= 0) continue;

            maxWord = range.get(iter);
        }
        return maxWord;
    }

    private boolean toBoolean(int number) {
        return number != 0;
    }

    private int toInt(char character) {
        return character - '0';
    }
}


/*




* */