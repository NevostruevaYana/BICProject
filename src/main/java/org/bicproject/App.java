package org.bicproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.bicproject.Util.*;

/**
 * Hello world!
 */
public class App {
    private static List<Line> linesList = new ArrayList<>();
    private static Map<Integer, Integer> groupMerging = new HashMap<>();
    private static List<List<String>> groups = new ArrayList<>();

    public static void main(String[] args) {
        //long start = System.currentTimeMillis();
        findGroups(args[0]);
        showCountOfGroups();
        putGroups();
        showGroups();
        //long end = System.currentTimeMillis();
        //System.out.println(end - start);
    }

    public static void showCountOfGroups() {
        System.out.println("Count of group: " + Set.copyOf(groupMerging.values()).size());
    }

    public static void showGroups() {
        groups.sort((list1, list2) -> list2.size() - list1.size());
        int i = 0;
        while (i < groups.size()) {
            System.out.println("Group " + ++i);
            groups.get(i - 1).forEach(System.out::println);
        }
    }

    public static void putGroups() {
        groupMerging = Util.sortByValue(groupMerging);
        int strNumber = -1;
        int strNumberTmp;
        List<String> group = new ArrayList<>();
        for (Map.Entry<Integer, Integer> m: groupMerging.entrySet()) {
            strNumberTmp = strNumber;
            strNumber = m.getValue();
            String str1 = linesList.get(m.getValue()).getLine();
            String str2 = linesList.get(m.getKey()).getLine();
            if (strNumber != strNumberTmp) {
                if (!group.isEmpty()) {
                    groups.add(group);
                }
                group = new ArrayList<>();
                group.add(str1);
                group.add(str2);
            } else {
                group.add(str2);
            }
        }
        groups.add(group);
    }

    public static void findGroups(String file_name) {
        List<Map<String, Integer>> elemsToGroupNumbers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String line;
            int linesCounter = 0;

            while ((line = br.readLine()) != null) {

                if (checkCorrectLine(line)) {
                    continue;
                }

                String[] multiElements = line.replace("\"", "").split(";", -1);
                linesList.add(new Line(line));

                for (int index = 0; index < multiElements.length; index++) {
                    String elem = multiElements[index];

                    if (elemsToGroupNumbers.size() == index) {
                        elemsToGroupNumbers.add(new HashMap<>());
                    }

                    if (elem.equals("")) {
                        continue;
                    }

                    Map<String, Integer> elemInGroup = elemsToGroupNumbers.get(index);
                    Integer group = elemInGroup.get(elem);

                    if (group == null) {
                        elemInGroup.put(elem, linesCounter);
                    } else {
                        Line currentGroup = linesList.get(linesCounter);
                        Line groupLine = linesList.get(group);
                        if (currentGroup.getLine().equals(groupLine.getLine())) {
                            break;
                        }
                        if (groupLine.isActive()) {
                            groupMerging.put(linesCounter, group);
                            currentGroup.setInactive();
                        } else {
                            groupMerging.put(linesCounter, groupMerging.get(group));
                            currentGroup.setInactive();
                        }
                    }

                }
                linesCounter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}