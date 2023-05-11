import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AssociativeProcessor {
    List<String> associativeMemory = new ArrayList<>();
    int wordsCount;
    int bitsCount;

    public AssociativeProcessor(int wordsCount, int bitsCount) {
        this.wordsCount = wordsCount;
        this.bitsCount = bitsCount;

        fillAssociativeMemory(wordsCount, bitsCount);
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

    public int compare(String word, String argument) {
        List<Integer> qValues = new ArrayList<>();
        List<Integer> lValues = new ArrayList<>();

        int prevLValue = 0;
        int prevQValue = 0;

        for (int bit = 0; bit < bitsCount; bit++) {
            int qValue = getQValue(prevQValue, toInt(argument.charAt(bit)), toInt(word.charAt(bit)), prevLValue);
            int lValue = getLValue(prevLValue, toInt(argument.charAt(bit)), toInt(word.charAt(bit)), prevQValue);

            qValues.add(qValue);
            lValues.add(lValue);

            prevLValue = lValue;
            prevQValue = qValue;
        }

        System.out.println("word is " + word + " argument is " + argument);
        System.out.println("q_i_j: " + qValues);
        System.out.println("l_i_j: " + lValues);

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
                minWords.add(minWord);
                break;
            }

            IntStream.range(0, Collections.frequency(memoryContent, minWord)).forEach(i -> minWords.add(minWord));
            memoryContent.remove(minWord);
            IntStream.range(0, Collections.frequency(memoryContent, maxWord)).forEach(i -> maxWords.add(0, maxWord));
            memoryContent.remove(maxWord);
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