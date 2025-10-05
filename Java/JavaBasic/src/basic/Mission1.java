package basic;

import java.util.Scanner;

public class Mission1 {

    public static void calculateOperations(int num1, int num2) {
        int sum = num1 + num2;
        int difference;
        if (num1 >= num2) {
            difference = num1 - num2;
        } else {
            difference = num2 - num1;
        }
        System.out.println("합 = " + sum);
        System.out.println("차 = " + difference);
    }

    public static double calculateAverage(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0.0;
        }
        int totalSum = 0;
        for (int number : arr) {
            totalSum += number;
        }
        return (double) totalSum / arr.length;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("두 정수 입력: ");
        int[] numbers = new int[2];
        for (int i = 0; i < 2; i++) {
            numbers[i] = scanner.nextInt();
        }
        calculateOperations(numbers[0], numbers[1]);
        double average = calculateAverage(numbers);

        System.out.println("평균 = " + average);
    }
}