/*
 * Copyright of Koninklijke Philips N.V. 2020
 */
package com.philips.swcoe.cerberus.hounds;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.philips.swcoe.cerberus.unit.test.utils.CerberusBaseTest;

import picocli.CommandLine;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class SuppressedWarningsTest extends CerberusBaseTest {

    private String pathToTestSource = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE;

    @BeforeEach
    public void beforeEach() {
        super.setUpStreams();
    }

    @AfterEach
    public void afterEach() {
        super.restoreStreams();
    }

    @Test
    public void testSuppressedWarningsWithParams() throws Exception {
        SuppressedWarnings suppressedWarnings = new SuppressedWarnings();
        int exitCode = new CommandLine(suppressedWarnings).execute(new String[] { "--files", pathToTestSource, "--language", "JAVA" });
        assertEquals(0, exitCode);
    }

    @Test
    public void testSuppressedWarningsWithOutParams() throws Exception {
        SuppressedWarnings suppressedWarnings = new SuppressedWarnings();
        int exitCode = new CommandLine(suppressedWarnings).execute();
        assertTrue(getModifiedErrorStream().toString().contains("Specify language of the source code"));
        assertTrue(getModifiedErrorStream().toString().contains("Specify the absolute path to source code"));
        assertNotEquals(0, exitCode);
    }

    @Test
    public void testLanguageError() throws Exception {
        SuppressedWarnings suppressedWarnings = new SuppressedWarnings();
        int exitCode = new CommandLine(suppressedWarnings).execute(new String[] { "--files", pathToTestSource });
        assertTrue(getModifiedErrorStream().toString().contains("Specify language of the source code"));
        assertNotEquals(0, exitCode);
    }

    @Test
    public void testFilePathError() throws Exception {
        SuppressedWarnings suppressedWarnings = new SuppressedWarnings();
        int exitCode = new CommandLine(suppressedWarnings).execute(new String[] { "--language", "JAVA" });
        assertTrue(getModifiedErrorStream().toString().contains("Specify the absolute path to source code"));
        assertNotEquals(0, exitCode);
    }

    @Test
    public void testSuppressedWarningsReportWithParams() throws Exception {
        SuppressedWarnings suppressedWarnings = new SuppressedWarnings();
        int exitCode = new CommandLine(suppressedWarnings).execute(new String[] { "--files", pathToTestSource , "--language", "JAVA" });
        assertEquals(0, exitCode);
        assertTrue(getModifiedOutputStream().toString().contains("GenericSuppressedWarnings.java"));
        assertTrue(getModifiedOutputStream().toString().contains("SonarQubeSuppressedWarnings.java"));
        assertTrue(getModifiedOutputStream().toString().contains("SuppressedWarningWithFullPackageName.java"));

        assertTrue(getModifiedOutputStream().toString().contains("Line No 12 = @SuppressWarnings(\"JmsConsumer Warning Disabled\")"));
        assertTrue(getModifiedOutputStream().toString().contains("Line No 11 = @SuppressWarnings(\"squid:S00112\")"));
        assertTrue(getModifiedOutputStream().toString().contains("Line No 1 = @java.lang.SuppressWarnings(\"Some warning suppressed With Full package name\")"));

    }

}