package org.myorg.readabilitychecker.formulas.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.myorg.readabilitychecker.codeabstractionlevels.SourceCodeFile;
import org.myorg.readabilitychecker.formulas.logic.SresLogic;
import org.myorg.readabilitychecker.formulas.objects.SRES;

/**
 * Unit tests for SresLogic.java.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class SresLogicTest {

    List<SourceCodeFile> javaFilesEmpty, javaFilesNull, javaFiles, javaFilesWithEntriesToBeIgnored;
    SourceCodeFile scf1, scf2, scf3, scf4;
    SRES sres1, sres2, sres3, sres4;
    File f;

    public SresLogicTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        javaFilesEmpty = new ArrayList<>();
        javaFilesNull = null;
        javaFiles = new ArrayList<>();
        javaFilesWithEntriesToBeIgnored = new ArrayList<>();

        f = new File("");

        sres1 = new SRES(5, 4, 4.6);
        sres2 = new SRES(7.3, 3.49, 6.95);
        sres3 = new SRES(0, 0, 0);
        sres4 = null;

//        scf1 = new SourceCodeFile(f, null, sres1, null);
//        scf2 = new SourceCodeFile(f, null, sres2, null);
//        scf3 = new SourceCodeFile(f, null, sres3, null);
//        scf4 = new SourceCodeFile(null, null, sres4, null);

        javaFiles.add(scf1);
        javaFiles.add(scf2);

        javaFilesWithEntriesToBeIgnored.add(scf1);
        javaFilesWithEntriesToBeIgnored.add(scf2);
        javaFilesWithEntriesToBeIgnored.add(scf3);
        javaFilesWithEntriesToBeIgnored.add(scf4);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of resetSresVars method, of class SresLogic.
     */
    @Test
    public void Should_ResetPogjeSresVariables_When_Called() {
        edu.cs.umu.sres.SRES.setWordsCount(1);
        edu.cs.umu.sres.SRES.setWordsLength(1);
        edu.cs.umu.sres.SRES.setSentencesCount(1);

        assertTrue(edu.cs.umu.sres.SRES.getWordsCount() != 0);
        assertTrue(edu.cs.umu.sres.SRES.getWordsLength() != 0);
        assertTrue(edu.cs.umu.sres.SRES.getSentencesCount() != 0);

        SresLogic.resetSresVars();

        assertEquals(0, edu.cs.umu.sres.SRES.getWordsCount());
        assertEquals(0, edu.cs.umu.sres.SRES.getWordsLength());
        assertEquals(0, edu.cs.umu.sres.SRES.getSentencesCount());
    }

    /**
     * Test of createSresObject method, of class SresLogic.
     */
    @Test
    public void Should_ReturnObjectInitializedToZero_When_AnyParameterIsZero() {
        // only testing getValue() not to have many unnecessary asserts
        assertEquals(0.0, SresLogic.createSresObject(0, 0, 0).getValue(), 0.009f);
        assertEquals(0.0, SresLogic.createSresObject(1, 0, 0).getValue(), 0.009f);
        assertEquals(0.0, SresLogic.createSresObject(0, 1, 0).getValue(), 0.009f);
        assertEquals(0.0, SresLogic.createSresObject(0, 0, 1).getValue(), 0.009f);
        assertEquals(0.0, SresLogic.createSresObject(1, 1, 0).getValue(), 0.009f);
        assertEquals(0.0, SresLogic.createSresObject(1, 0, 1).getValue(), 0.009f);
        assertEquals(0.0, SresLogic.createSresObject(0, 1, 1).getValue(), 0.009f);
    }

    /**
     * Test of createSresObject method, of class SresLogic.
     */
    @Test
    public void Should_ReturnCorrectlyInitializedObject_When_ParameterAreOk() {
        assertTrue(SresLogic.createSresObject(1, 1, 1).getAsl() != 0);
        assertTrue(SresLogic.createSresObject(1, 1, 1).getAwl() != 0);
        assertTrue(SresLogic.createSresObject(1, 1, 1).getValue() != 0);
    }

    /**
     * Test of calculateSres method, of class SresLogic.
     */
    @Test
    public void Should_ReturnZero_When_AslEqualsZero() {
        SresLogic sl = new SresLogic();

        assertEquals(0, sl.calculateSres(0, 1), 0.009f);
    }

    /**
     * Test of calculateSres method, of class SresLogic.
     */
    @Test
    public void Should_ReturnZero_When_AwlEqualsZero() {
        SresLogic sl = new SresLogic();

        assertEquals(0, sl.calculateSres(1, 0), 0.009f);
    }

    /**
     * Test of calculateSres method, of class SresLogic.
     */
    @Test
    public void Should_ReturnCorrectValue_When_AslAndAwlMeetParameters() {
        SresLogic sl = new SresLogic();

        assertEquals(4.6, sl.calculateSres(5, 4), 0.009f);
    }

    /**
     * Test of getReadabilityOfProject method, of class SresLogic.
     */
    @Test
    public void Should_ReturnZero_When_SourceCodeFilesListIsEmpty() {
        SresLogic sl = new SresLogic();

        assertEquals(0, sl.getReadabilityOfProject(javaFilesEmpty), 0.009f);
    }

    /**
     * Test of getReadabilityOfProject method, of class SresLogic.
     */
    @Test
    public void Should_ReturnZero_When_SourceCodeFilesListIsNull() {
        SresLogic sl = new SresLogic();

        assertEquals(0, sl.getReadabilityOfProject(javaFilesNull), 0.009f);
    }

    /**
     * Test of getReadabilityOfProject method, of class SresLogic.
     */
    @Test
    public void Should_ReturnProjectReadability_When_SourceCodeFilesHasAtLeastOneEntry() {
        SresLogic sl = new SresLogic();

        assertEquals(5.78, sl.getReadabilityOfProject(javaFiles), 0.09f);
    }

    /**
     * Test of getReadabilityOfProject method, of class SresLogic.
     */
    @Test
    public void Should_IgnoreFile_When_OneEntryHasNoSresObjectOrObjectHasNoReadabilityValue() {
        SresLogic sl = new SresLogic();

        assertEquals(5.78, sl.getReadabilityOfProject(javaFilesWithEntriesToBeIgnored), 0.09f);
    }

    /**
     * Test of getDetailedResults method, of class SresLogic.
     */
    @Test
    public void Should_ReturnSresDetailedEmptyString_When_SourceCodeFilesListIsEmpty() {
        assertEquals("", SresLogic.getDetailedResults(javaFilesEmpty));
    }

    /**
     * Test of getDetailedResults method, of class SresLogic.
     */
    @Test
    public void Should_ReturnSresDetailedEmptyString_When_SourceCodeFilesListIsNull() {
        assertEquals("", SresLogic.getDetailedResults(javaFilesNull));
    }

    /**
     * Test of getDetailedResults method, of class SresLogic.
     */
    @Test
    public void Should_ReturnSresDetailedSomething_When_SourceCodeFilesListMeetsParameters() {
        String returnedString = SresLogic.getDetailedResults(javaFiles);
        assertFalse(returnedString.isEmpty());
    }

}
