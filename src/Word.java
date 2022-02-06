public class Word {
    private String word;
    private boolean isVertical;
    private int row;
    private int column;
    public Word(String word, int row, int column, boolean isVertical) {
        this.word = word;
        this.row = row;
        this.column = column;
        this.isVertical = isVertical;
    }

    public String getWord() {
        return word;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public String toString() {
        if (isVertical) {
            return "The word, " + word + ", starts at row " + row
                    + " and is in column " + column + ". It is vertical";

        }
        return "The word, " + word + ", is in row " + row
                + " and starts in column " + column + ". It is horizontal";
    }
}
