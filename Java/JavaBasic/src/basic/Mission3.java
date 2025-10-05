package basic;

public class Mission3 {

    public static void main(String[] args) {
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.println("합계=" + sum);

        System.out.println("\n[구구단]");
        for (int j = 1; j <= 9; j++) {
            for (int i = 2; i <= 9; i++) {
                System.out.print(i + " x " + j + " = " + (i * j) + "   \t");
            }
            System.out.println();
        }
    }
}
