package basic;

import java.util.Scanner;

public class Mission2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] numbers = new int[5];

        System.out.println("5개의 정수를 입력하세요:");
        for (int i = 0; i < 5; i++) {
            numbers[i] = scanner.nextInt();
        }

        System.out.print("입력 값 [");
        for (int i = 0; i < 5; i++) {
            System.out.print(numbers[i]);
            if (i < 4) {
                System.out.print(",");
            }
        }System.out.print("] -> ");

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4 - i; j++) {
                if (numbers[j] > numbers[j + 1]) {
                    int temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                }
            }
        }
        System.out.print("정렬 후 [");
        for (int i = 0; i < 5; i++) {
            System.out.print(numbers[i]);
            if (i < 4) {
                System.out.print(",");
            }
        }System.out.print("]");
    }
}
