package org.bicproject;

import org.junit.jupiter.api.Test;

import static org.bicproject.Util.checkCorrectLine;
import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void checkCorrectLineTest() {
        assertTrue(checkCorrectLine("\"\""));
        assertTrue(checkCorrectLine("\"\";\"\";\"\""));
        assertTrue(checkCorrectLine("\"345\""));
        assertTrue(checkCorrectLine("\"355\";\"75857\""));

        assertFalse(checkCorrectLine("345"));
        assertFalse(checkCorrectLine("\"35\"5\";\"75857\""));
        assertFalse(checkCorrectLine("\"8383\"200000741652251\""));
        assertFalse(checkCorrectLine("\"79855053897\"83100000580443402\";\"200000133000191\""));
    }
}