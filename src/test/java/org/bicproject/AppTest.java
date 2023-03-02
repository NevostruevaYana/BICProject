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
        assertEquals(findGroups("src/test/resources/empty"), List.of(List.of()));
        assertEquals(findGroups("src/test/resources/example"),
                List.of(List.of("\"111\";\"123\";\"222\"",
                                "\"200\";\"123\";\"100\"",
                                "\"300\";\"\";\"100\"")));
        assertEquals(findGroups("src/test/resources/lineWithError"), List.of(List.of()));
        assertEquals(findGroups("src/test/resources/lineWithErrorAndGroup"),
                List.of(List.of("\"111\";\"123\";\"222\"",
                                    "\"111\";\"\";\"100\"")));
        assertEquals(findGroups("src/test/resources/theSameLines"),
                List.of(List.of("\"111\";\"123\";\"222\"",
                        "\"200\";\"123\";\"100\"",
                        "\"300\";\"\";\"100\"")));
    }

    @Test
    void mainTest() throws IOException {
        main(new String[]{"src/test/resources/empty", "empty"});
        File expected = new File("src/test/resources/expectedEmpty");
        File output = new File(OUT_DIR + SEPARATOR + "empty");
        assertTrue(FileUtils.contentEquals(expected, output));

        main(new String[]{"src/test/resources/example", "example"});
        expected = new File("src/test/resources/expectedExample");
        output = new File(OUT_DIR + SEPARATOR + "example");
        System.out.println(expected.getAbsolutePath());
        System.out.println(output.getAbsolutePath());
        assertTrue(FileUtils.contentEquals(expected, output));
    }
}