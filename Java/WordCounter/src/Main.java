package WordCounter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        bw.write("줄 개수 : ");
        bw.flush();
        int lineCount = Integer.parseInt(br.readLine());

        Map<String, Integer> wordCounts = new HashMap<>();

        for (int i = 0; i < lineCount; i++) {
            bw.write((i + 1) + "번째 문장 : ");
            bw.flush();
            String line = br.readLine();

            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }
        }

        StringBuilder result = new StringBuilder();

        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(wordCounts.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if (o2.getValue().equals(o1.getValue())) {
                    return o1.getKey().compareTo(o2.getKey());
                }
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        result.append("\n(Iterator 순회)\n");
        Iterator<Map.Entry<String, Integer>> iterator = sortedList.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            result.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }

        result.append("\n(Stream 정렬)\n");
        wordCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER))
                .forEach(entry -> result.append(entry.getKey()).append("=").append(entry.getValue()).append("\n"));

        bw.write(result.toString());

        bw.write("\n<선택 심화>\n");

        bw.write("\n가장 많이 나온 단어: ");
        wordCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry -> {
                    try {
                        bw.write(entry.getKey());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        bw.newLine();

        bw.write("단어 길이 평균: ");
        double averageLength = wordCounts.keySet().stream()
                .mapToInt(String::length)
                .average()
                .orElse(0.0);
        bw.write(String.format("%.2f", averageLength));
        bw.newLine();

        bw.flush();
        br.close();
        bw.close();
    }
}