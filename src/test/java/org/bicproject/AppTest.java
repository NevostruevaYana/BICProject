package org.bicproject;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import static org.bicproject.App.main;
import static org.bicproject.Util.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.bicproject.App.findGroups;

public class AppTest {

    @Nested
    class InnerAppTest {
        private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        private final PrintStream originalOut = System.out;

        @BeforeEach
        public void setUpStreams() {
            System.setOut(new PrintStream(outContent));
        }

        @AfterEach
        public void restoreStreams() {
            System.setOut(originalOut);
        }

        @Test
        void findEmptyGroupsTest() throws IOException {
            assertEquals(findGroups("src/test/resources/empty"), List.of());
            assertEquals(findGroups("src/test/resources/example"),
                    List.of(Set.of("\"300\";\"\";\"100\"",
                            "\"111\";\"123\";\"222\"",
                            "\"200\";\"123\";\"100\"")));
            assertEquals(findGroups("src/test/resources/lineWithError"), List.of());
            assertEquals(findGroups("src/test/resources/lineWithErrorAndGroup"),
                    List.of(Set.of("\"111\";\"\";\"100\"",
                            "\"111\";\"123\";\"222\"")));
            assertEquals(findGroups("src/test/resources/theSameLines"),
                    List.of(Set.of("\"300\";\"\";\"100\"",
                            "\"111\";\"123\";\"222\"",
                            "\"200\";\"123\";\"100\"")));
            assertEquals(findGroups("src/test/resources/test1"),
                    List.of(Set.of(";\"1\";\"1\"",
                            "\"1\";\"1\";\"1\"",
                            "\"1\";;",
                            "\"1\";;\"1\"",
                            ";;\"1\"",
                            ";\"1\";",
                            "\"1\";\"1\";")));
            assertEquals(findGroups("src/test/resources/test2"),
                    List.of(Set.of("\"2\";;",
                            "\"2\";\"2\";\"2\"",
                            ";;\"2\"",
                            ";\"2\";\"2\"",
                            "\"2\";;\"2\"")));
            assertEquals(findGroups("src/test/resources/test3"),
                    List.of(Set.of(";\"3\";\"3\"",
                            "\"3\";;",
                            ";;\"3\"",
                            "\"3\";\"3\";")));
            assertEquals(findGroups("src/test/resources/test4"),
                    List.of(Set.of(";\"102941.1\";",
                            ";;\"102941.1\"",
                            ";\"102941.1\";\"102941.2\"",
                            "\"102941.2\";\"102941.2\";\"102941.1\"",
                            "\"102941.1\";\"102941.2\";\"102941.2\"",
                            "\"102941.1\";\"102941.1\";")));
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

        @Test
        void testException() {
            Exception exception = assertThrows(IllegalArgumentException.class, () ->
                    main(new String[]{})
            );

            String expectedMessage = "Enter file path";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage));

            Exception exception2 = assertThrows(IllegalArgumentException.class, () ->
                    main(new String[]{"", "", ""})
            );

            String expectedMessage2 = "Too much arguments";
            String actualMessage2 = exception2.getMessage();

            assertTrue(actualMessage2.contains(expectedMessage2));
        }

        @Test
        public void out() {
            main(new String[]{FILE_NAME + "error"});
            assertEquals(FILE_NAME + "error" + " (Не удается найти указанный файл)\r\n",
                    outContent.toString());
        }
    }

    @Nested
    class InnerAppTest2 {

        @Test
        public void startBigFile() {
            main(new String[]{BIG_FILE_NAME});
        }
    }
}