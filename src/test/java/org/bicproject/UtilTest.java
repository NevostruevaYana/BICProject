package org.bicproject;

import org.junit.jupiter.api.Test;

import static org.bicproject.Util.checkWrongElements;
import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void checkCorrectLineTest() {
        assertFalse(checkWrongElements(";;".split(";")));
        assertFalse(checkWrongElements("111;123;222".split(";")));
        assertFalse(checkWrongElements("\"111\";\"123\";\"222\"".split(";")));
        assertFalse(checkWrongElements("300;;100".split(";")));
        assertFalse(checkWrongElements("\"\";\"8\";".split(";")));

        assertTrue(checkWrongElements("\"8383\"200000741652251\"".split(";")));
        assertTrue(checkWrongElements("\"79855053897\"83100000580443402\";\"200000133000191\"".split(";")));
    }
}