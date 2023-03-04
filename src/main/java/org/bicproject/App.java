package org.bicproject;

import java.io.*;
import java.util.*;

import static org.bicproject.Util.*;

public class App {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        int argsCount = args.length;
        if (argsCount == 0) {
            throw new IllegalArgumentException("Enter file path");
        }
        if (argsCount > 2) {
            throw new IllegalArgumentException("Too much arguments");
        }

        List<Set<String>> groups = null;
        try {
            groups = findGroups(args[0]);
        } catch (IOException e) {
            return;
        }

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

        printAndWriteResult(groups, outFileName);

        long end = System.currentTimeMillis();
        System.out.println();
        System.out.println(end - start);
    }

    public static List<Set<String>> findGroups(String file_name) throws IOException {
        List<Line> linesList = new ArrayList<>();
        List<Map<String, Integer>> elemsToGroupNumbers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file_name))) {
            String line;
            int linesCounter = 0;

            while ((line = br.readLine()) != null) {

                String[] multiElements = line.split(";", -1);

                if (checkWrongElements(multiElements)) {
                    linesCounter++;
                    linesList.add(new Line("", false));
                    continue;
                }

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
                        Line currentGroupLine = linesList.get(linesCounter);
                        Line mainGroupLine = linesList.get(group);
                        if (line.equals(mainGroupLine.getLine())) {
                            currentGroupLine.setInactive();
                            break;
                        }
                        if (mainGroupLine.isActive()) {
                            if (currentGroupLine.isActive()) {
                                mainGroupLine.setMain();
                                mainGroupLine.setMember(linesCounter);
                                currentGroupLine.setInactive();
                                currentGroupLine.setBelongGroup(group);
                            } else {
                                int currentGroupLineBelong = currentGroupLine.getBelongGroup();
                                Line currentMainGroupLine = linesList.get(currentGroupLineBelong);
                                if (mainGroupLine.isMain() && !(currentGroupLineBelong == group)) {
                                    currentMainGroupLine.setMainFalse();
                                    currentMainGroupLine.setInactive();
                                    currentGroupLine.setBelongGroup(group);
                                    currentMainGroupLine.setBelongGroup(group);
                                    mainGroupLine.setMembers(currentMainGroupLine.getGroupMembers());
                                    mainGroupLine.setMember(currentGroupLineBelong);
                                    currentMainGroupLine.deleteMembers();
                                } else {
                                    if (!mainGroupLine.isMain()) {
                                        mainGroupLine.setInactive();
                                        mainGroupLine.setBelongGroup(currentGroupLineBelong);
                                        currentMainGroupLine.setMember(group);
                                    }
                                }
                            }
                        } else {
                            if (currentGroupLine.isActive()) {
                                currentGroupLine.setInactive();
                                int newMain = mainGroupLine.getBelongGroup();
                                currentGroupLine.setBelongGroup(newMain);
                                linesList.get(newMain).setMember(linesCounter);
                            } else {
                                int currentGroupLineBelong = currentGroupLine.getBelongGroup();
                                int mainGroupLineBelong = mainGroupLine.getBelongGroup();
                                Line currentMainGroupLine = linesList.get(currentGroupLineBelong);
                                if (!(currentGroupLineBelong == mainGroupLineBelong)) {
                                    currentMainGroupLine.setMainFalse();
                                    currentMainGroupLine.setInactive();
                                    currentMainGroupLine.setBelongGroup(mainGroupLineBelong);
                                    currentGroupLine.setBelongGroup(mainGroupLineBelong);
                                    linesList.get(mainGroupLineBelong).setMembers(currentMainGroupLine.getGroupMembers());
                                    linesList.get(mainGroupLineBelong).setMember(currentGroupLineBelong);
                                    currentMainGroupLine.deleteMembers();
                                }
                            }
                        }
                    }
                }
                linesCounter++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IOException();
        }
        return getGroupsWithOrder(linesList);
    }

    private static List<Set<String>> getGroupsWithOrder(List<Line> linesList) {

        if (linesList.isEmpty())
            return new ArrayList<>();

        List<Set<String>> groups = new ArrayList<>();

        for (Line l : linesList) {
            Set<String> set = new HashSet<>();
            if (l.isActive() && l.getGroupMembers().size() > 0) {
                set.add(l.getLine());
                for (int s : l.getGroupMembers()) {
                    set.add(linesList.get(s).getLine());
                }
            }
            if (!set.isEmpty())
                groups.add(set);
        }

        groups.sort((o1, o2) -> o2.size() - o1.size());

        return groups;
    }

    public static void printAndWriteResult(List<Set<String>> groups, String outFileName) {
        int size = groups.size();

        int i = 0;
        try (FileWriter writer = new FileWriter(outFileName, false)) {
            if (size == 0) {
                System.out.println("Count of groups: 0");
                writer.write("Count of groups: 0");
                writer.flush();
                return;
            }

            System.out.println("Count of groups: " + size + "\n");
            writer.write("Count of groups: " + size + "\n");
            for (Set<String> s : groups) {
                System.out.println("Group " + ++i);
                writer.write("\nGroup " + i + "\n\n");
                for (String str : s) {
                    System.out.println(str);
                    writer.write(str + "\n");
                }
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Something went wrong when writing the result to the file " + outFileName);
        }
    }
}