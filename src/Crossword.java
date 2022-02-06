import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Crossword {
    private final String[] words;
    private final String[] hints;
    private final ArrayList<Word> objects = new ArrayList<>(5);
    private final ArrayList<String> remainingWords;
    private final char[][] board;
    private final int rows;
    private final int columns;
    private final int midRow;

    public Crossword(String[] words, String[] hints) {
        this.words = words;
        this.hints = hints;
        sort();
        rows = words[0].length() * 3;
        columns = words[0].length() * 3;
        int midColumn = columns / 2;
        midRow = rows / 2;
        board = new char[rows][columns];
        objects.add(new Word(words[0], midRow, midColumn - words[0].length() / 2, false));
        remainingWords = new ArrayList<>(Arrays.asList(words));
    }

    public void createCrossword() {
        System.out.println();
        generateGrid();
        remainingWords.remove(0);
        int tracker = 0;
        try {
            while (objects.size() < 5) {
                for (int i = 0; i < objects.size(); i++) {
                    for (int j = 0; j < remainingWords.size(); j++) {
                        placeWord(objects.get(i), Arrays.asList(words).indexOf(remainingWords.get(j)));
                        tracker++;
                        if (tracker > 100) throw new Exception("runtime error");
                    }
                }
            }
        } catch (Exception e) {
            for (int i = 0; i < objects.size(); i++) {
                for (int j = 0; j < remainingWords.size(); j++) {
                    placeWord(objects.get(i), Arrays.asList(words).indexOf(remainingWords.get(j)));
                }
            }
            for (int i = 0; i < remainingWords.size(); i++) {
                placeRemaining(remainingWords.get(i), i);
            }
        }
        printGrid();
        printHints();
    }

    public void placeRemaining(String word, int column) {
        int row = 0;
        for (int i = 0; i < word.length(); i++) {
            board[row][column] = word.charAt(i);
            row++;
        }
    }

    public void printHints() {
        System.out.println("HORIZONTALS:");
        for (int i = 0; i < objects.size(); i++) {
            if (!objects.get(i).isVertical()) {
                System.out.println(hints[Arrays.asList(words).indexOf(objects.get(i).getWord())]);
            }
        }
        System.out.println("VERTICALS:");
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).isVertical()) {
                System.out.println(hints[Arrays.asList(words).indexOf(objects.get(i).getWord())]);
            }
        }
    }


    public void sort() {
        boolean sorted = true;
        while (sorted) {
            sorted = false;
            for (int i = 0; i < words.length - 1; i++) {
                if (words[i].length() < words[i + 1].length()) {
                    String temp = words[i];
                    words[i] = words[i + 1];
                    words[i + 1] = temp;
                    String tempHints = hints[i];
                    hints[i] = hints[i + 1];
                    hints[i + 1] = tempHints;
                    sorted = true;
                }
            }
        }
    }


    public void generateGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = '_';
            }
        }
        int startColumn = objects.get(0).getColumn();
        // center longest word
        for (int j = 0; j < words[0].length(); j++) {
            board[midRow][startColumn] = words[0].charAt(j);
            startColumn++;
        }
    }

    public void printGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void placeWord(Word word1, int word2) {
        HashMap<Integer, Integer> sameLetterIndices = searchSameLetter(word1, word2);
        String stringWord2 = words[word2];
        for (Map.Entry<Integer, Integer> i : sameLetterIndices.entrySet()) {
            int cutIndex = i.getValue();
            String firstHalf = stringWord2.substring(0, cutIndex);
            String lastHalf = stringWord2.substring(cutIndex + 1);
            if (checkLegalPlacement(word1, stringWord2, i)) {
                if (!word1.isVertical()) {
                    int finalColumn = word1.getColumn() + i.getKey();
                    int startingRowCopy1 = word1.getRow();
                    int startingRowCopy2 = word1.getRow();
                    for (int j = firstHalf.length() - 1; j >= 0; j--) {
                        board[startingRowCopy1 - 1][finalColumn] = firstHalf.charAt(j);
                        startingRowCopy1--;
                    }
                    for (int j = 0; j < lastHalf.length(); j++) {
                        board[startingRowCopy2 + 1][finalColumn] = lastHalf.charAt(j);
                        startingRowCopy2++;
                    }
                    objects.add(new Word(words[word2], startingRowCopy1, finalColumn, true));
                    remainingWords.remove(word1.getWord());
                    remainingWords.remove(stringWord2);
                    break;
                }
                if (word1.isVertical()) {
                    int finalRow = word1.getRow() + (int) i.getKey();
                    int startingColumnCopy1 = word1.getColumn();
                    int startingColumnCopy2 = word1.getColumn();
                    for (int j = firstHalf.length() - 1; j >= 0; j--) {
                        board[finalRow][startingColumnCopy1 - 1] = firstHalf.charAt(j);
                        startingColumnCopy1--;
                    }
                    for (int j = 0; j < lastHalf.length(); j++) {
                        board[finalRow][startingColumnCopy2 + 1] = lastHalf.charAt(j);
                        startingColumnCopy2++;
                    }
                    objects.add(new Word(words[word2], finalRow, startingColumnCopy1, false));
                    remainingWords.remove(word1.getWord());
                    remainingWords.remove(stringWord2);
                    break;
                }
            }
        }

    }

    public boolean checkLegalPlacement(Word word1, String word2, Map.Entry<Integer, Integer> pair) {
        int cutIndex = pair.getValue();
        String firstHalf = word2.substring(0, cutIndex);
        String lastHalf = word2.substring(cutIndex+1);
        if (!word1.isVertical()) {
            int finalColumn = word1.getColumn() + pair.getKey();
            int startingRowCopy1 = word1.getRow();
            int startingRowCopy2 = word1.getRow();
            for (int j = firstHalf.length()-1; j >= 0; j--) {
                if (board[startingRowCopy1-1][finalColumn] != '_') {
                    return false;
                }
                startingRowCopy1--;
            }
            for (int j = 0; j < lastHalf.length(); j++) {
                if (board[startingRowCopy2+1][finalColumn] != '_') {
                    return false;
                }
                startingRowCopy2++;
            }
        }
        if (word1.isVertical()){
            int finalRow = word1.getRow() + pair.getKey();
            int startingColumnCopy1 = word1.getColumn();
            int startingColumnCopy2 = word1.getColumn();
            for (int j = firstHalf.length()-1; j >= 0; j--) {
                if (board[finalRow][startingColumnCopy1-1] != '_') {
                    return false;
                }
                startingColumnCopy1--;
            }
            for (int j = 0; j < lastHalf.length(); j++) {
                if (board[finalRow][startingColumnCopy2+1] != '_') {
                    return false;
                }
                startingColumnCopy2++;
            }
        }
        return true;
    }

    public HashMap<Integer, Integer> searchSameLetter(Word word1, int word2) {
        HashMap<Integer, Integer> sameLetterIndices = new HashMap<>();

        String stringWord1 = words[Arrays.asList(words).indexOf(word1.getWord())];
        String stringWord2 = words[word2];
        for (int i = 0; i < stringWord1.length(); i++) {
            for (int j = 0; j < stringWord2.length(); j++) {
                if (stringWord1.charAt(i) == stringWord2.charAt(j)
                        && !sameLetterIndices.containsKey(i) && !sameLetterIndices.containsValue(j)) {
                    sameLetterIndices.put(i, j);
                }
            }
        }
        return sameLetterIndices;
    }

}
