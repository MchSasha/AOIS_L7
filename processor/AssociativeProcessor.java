package processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class AssociativeProcessor {
    private final List<String> associativeMemory;
    private final int bitsCount;

    public AssociativeProcessor(List<String> memory, int bitsCount) {
        this.bitsCount = bitsCount;

        associativeMemory = memory;
    }

    private int getGValue(int prevGValue, int aValue, int sValue, int prevLValue) {
        boolean gValue = toBoolean(prevGValue) || ((!toBoolean(aValue) && toBoolean(sValue)) && !toBoolean(prevLValue));

        return gValue ? 1 : 0;
    }

    private int getLValue(int prevLValue, int aValue, int sValue, int prevGValue) {
        boolean lValue = toBoolean(prevLValue) || ((toBoolean(aValue) && !toBoolean(sValue)) && !toBoolean(prevGValue));

        return lValue ? 1 : 0;
    }

    public int compare(String word, String argument) {
        if (word.length() != argument.length()) return -2;

        int prevLValue = 0;
        int prevGValue = 0;

        for (int bit = 0; bit < bitsCount; bit++) {
            int gValue = getGValue(prevGValue, toInt(argument.charAt(bit)), toInt(word.charAt(bit)), prevLValue);
            int lValue = getLValue(prevLValue, toInt(argument.charAt(bit)), toInt(word.charAt(bit)), prevGValue);

            prevLValue = lValue;
            prevGValue = gValue;
        }

        return Integer.compare(prevGValue, prevLValue);
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