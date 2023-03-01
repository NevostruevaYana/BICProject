package org.bicproject;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.bicproject.App.main;
import static org.bicproject.Util.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.bicproject.App.findGroups;

public class AppTest {

    @Test
    void findEmptyGroupsTest() {
        assertEquals(findGroups("src/theSameLines/resources/empty"), List.of(List.of()));
        assertEquals(findGroups("src/theSameLines/resources/example"),
                List.of(List.of("\"111\";\"123\";\"222\"",
                                "\"200\";\"123\";\"100\"",
                                "\"300\";\"\";\"100\"")));
        assertEquals(findGroups("src/theSameLines/resources/lineWithError"), List.of(List.of()));
        assertEquals(findGroups("src/theSameLines/resources/lineWithErrorAndGroup"),
                List.of(List.of("\"111\";\"123\";\"222\"",
                                    "\"111\";\"\";\"100\"")));
        assertEquals(findGroups("src/theSameLines/resources/theSameLines"),
                List.of(List.of("\"111\";\"123\";\"222\"",
                        "\"200\";\"123\";\"100\"",
                        "\"300\";\"\";\"100\"")));
    }

    @Test
    void mainTest() throws IOException {
        main(new String[]{"src/theSameLines/resources/empty", "empty"});
        File expected = new File("src/theSameLines/resources/expectedEmpty");
        File output = new File(OUT_DIR + SEPARATOR + "empty");
        assertTrue(FileUtils.contentEquals(expected, output));

        main(new String[]{"src/theSameLines/resources/example", "example"});
        expected = new File("src/theSameLines/resources/expectedExample");
        output = new File(OUT_DIR + SEPARATOR + "example");
        System.out.println(expected.getAbsolutePath());
        System.out.println(output.getAbsolutePath());
        assertTrue(FileUtils.contentEquals(expected, output));
    }

    @Test
    void test() {
        main(new String[]{BIG_FILE_NAME, "big.txt"});
    }
}