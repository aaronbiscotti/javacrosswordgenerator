import java.util.Scanner;
class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] words = new String[5];
        String[] hints = new String[5];
        for (int i = 0; i < 5; i++) {
            System.out.println("Word " + (i+1) + ": ");
            words[i] = sc.next();
        }
        sc.nextLine();
        for (int i = 0; i < 5; i++) {
            System.out.println("Hint " + (i+1) + ": ");
            hints[i] = sc.nextLine();
        }
        System.out.println();
        System.out.println("Enter a title:");
        String title = sc.nextLine();
        System.out.println(title.toUpperCase());
        Crossword crossword = new Crossword(words, hints);
        crossword.createCrossword();
//        System.out.println((int)(Math.random() * 2));

    }
}