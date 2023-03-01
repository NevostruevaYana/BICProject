package org.bicproject;

import java.io.*;
import java.nio.BufferOverflowException;
import java.util.*;

import static org.bicproject.Util.*;

public class App {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        int argsCount = args.length;
        if (argsCount != 1 && argsCount != 2) {
            throw new IllegalArgumentException("Enter file path");
        }

        List<List<String>> groups = findGroups(args[0]);

        if (groups.isEmpty())
            return;

        String outFileName;

        File dir = new File(OUT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (argsCount == 2) {
            outFileName = OUT_DIR + SEPARATOR + args[1];
        } else {
            outFileName = OUT_DIR + OUT_FILE_NAME;
        }

        try (FileWriter writer = new FileWriter(outFileName, false)) {
            writeAndShowCountOfGroups(writer, groups);
            writer.flush();
        } catch (IOException e) {
            System.out.println("Something went wrong when writing the result to the file " + outFileName);
            return;
        }

        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(end - start);
    }

    public static List<List<String>> findGroups(String file_name) {
        List<Line> linesList = new ArrayList<>();
        Map<Integer, Integer> groupMerging = new HashMap<>();
        List<Map<String, Integer>> elemsToGroupNumbers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String line;
            int linesCounter = 0;

            while ((line = br.readLine()) != null) {

                if (!checkCorrectLine(line)) {
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
            System.out.println("Something went wrong when reading lines in the input file " + file_name);
            return List.of();
        }
        return putGroups(linesList, groupMerging);
    }

    public static List<List<String>> putGroups(List<Line> linesList, Map<Integer, Integer> groupMerging) {
        List<List<String>> groups = new ArrayList<>();
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
                if (!group.contains(str2)) {
                    group.add(str2);
                }
            }
        }
        groups.add(group);
        return groups;
    }

    public static void writeAndShowCountOfGroups(FileWriter writer, List<List<String>> groups) {
        int countOfGroup = groups.size();
        try {
            if (countOfGroup == 1 && groups.get(0).isEmpty()) {
                writer.write("Count of group: 0");
                System.out.println("Count of group: 0");
                return;
            }
            writer.write("Count of group: " + countOfGroup);
            writer.append('\n');
        } catch (IOException e) {
            System.out.println("Something went wrong when writing the result to the output file");
        }
        System.out.println("Count of group: " + countOfGroup);
        writeAndShowGroups(writer, groups);
    }

    public static void writeAndShowGroups(FileWriter writer, List<List<String>> groups) {
        groups.sort((list1, list2) -> list2.size() - list1.size());
        int i = 0;
        while (i < groups.size()) {
            try {
                writer.write("Group " + ++i);
                System.out.println("Group " + i);
                writer.append('\n');
                groups.get(i - 1).forEach(it -> {
                    try {
                        writer.write(it);
                        System.out.println(it);
                        writer.append('\n');
                    } catch (IOException e) {
                        System.out.println("Something went wrong when writing the result to the output file");
                    }
                });
            } catch (IOException e) {
                System.out.println("Something went wrong when writing the result to the output file");
            }
        }
    }
}