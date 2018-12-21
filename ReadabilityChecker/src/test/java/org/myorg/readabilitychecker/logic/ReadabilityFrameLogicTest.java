/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.readabilitychecker.logic;

import com.github.javaparser.ast.body.MethodDeclaration;
import java.awt.Container;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.myorg.readabilitychecker.codeabstractionlevels.Method;
import org.myorg.readabilitychecker.codeabstractionlevels.SourceCodeFile;
import org.myorg.readabilitychecker.formulas.logic.PhdLogic;
import org.myorg.readabilitychecker.formulas.objects.CommentsRatio;
import org.myorg.readabilitychecker.formulas.objects.PHD;
import org.myorg.readabilitychecker.formulas.objects.SRES;

/**
 *
 * @author Claudio
 */
public class ReadabilityFrameLogicTest {

    List<SourceCodeFile> javaFilesEmpty, javaFilesNull, javaFiles/*, javaFilesWithEntriesToBeIgnored*/;
    SourceCodeFile scf1, scf2, scf3, scf4;
    CommentsRatio cr1, cr2, cr3;
    SRES sres1, sres2, sres3;
    //PHD phd1, phd2, phd3, phd4;
    File f;
    Method m;
    ArrayList<Method> methods;

    public ReadabilityFrameLogicTest() {
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
        //javaFilesWithEntriesToBeIgnored = new ArrayList<>();

        f = new File("");

        m = new Method(new MethodDeclaration());

        methods = new ArrayList<>();
        methods.add(m);

        cr1 = new CommentsRatio(10, 3, 3.33);
        cr2 = new CommentsRatio(100, 16, 6.25);
        cr3 = new CommentsRatio(0, 0, 0);

        sres1 = new SRES(5, 4, 4.6);
        sres2 = new SRES(7.3, 3.49, 6.95);
        sres3 = new SRES(0, 0, 0);

        //phd1 = new PHD(30.2, 6, 3.6, 0.99);
        //phd2 = new PHD(97.12, 10, 6, 0.66);
        //phd3 = new PHD(0, 0, 0, 0);
//        scf1 = new SourceCodeFile(f, cr1, sres1, null);
//        scf2 = new SourceCodeFile(f, cr2, sres2, methods);
//        scf3 = new SourceCodeFile(f, cr3, sres3, null);
//        scf4 = new SourceCodeFile(null, null, null, null);

        javaFiles.add(scf1);
        javaFiles.add(scf2);
        //javaFiles.add(scf3);
        //javaFiles.add(scf4);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getResultsFileName method, of class ReadabilityFrameLogic.
     */
    @Test
    public void Should_ReturnCorrectFileName_When_GetFileNameMethodIsCalled() {
        //String openedProjectName = "project_name";
        //String currentTime = "2018-08-22-19-09-33";
        //assertEquals("rc_results_project_name_2018-08-22-19-09-33.txt", ReadabilityFrameLogic.getResultsFileName());

        // FAZER ESTE TESTE DEPOIS DE FAZER O TESTE AO METODO getOpenedProjectName DA CLASSE SOURCECODEFILELOGIC
    }

}
