package readabilitychecker.formulas.logic;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.io.File;
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
import org.myorg.readabilitychecker.formulas.logic.CommentsRatioLogic;
import org.myorg.readabilitychecker.formulas.objects.CommentsRatio;
import org.myorg.readabilitychecker.logic.SourceCodeFileLogic;

/**
 * Unit tests for CommentsRatioLogic.java.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class CommentsRatioLogicTest {

    SourceCodeFileLogic scfl;
    String code;
    CompilationUnit cu, cuNull;
    List<SourceCodeFile> javaFilesEmpty, javaFilesNull, javaFiles, javaFilesWithEntriesToBeIgnored;
    SourceCodeFile scf1, scf2, scf3, scf4;
    File f;
    ArrayList<Method> methods, methodsEmpty, methodsNull;
    Method m;
    MethodDeclaration md;
    CommentsRatio cr1, cr2, cr3, cr4;
    Exception ex;

    public CommentsRatioLogicTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        scfl = new SourceCodeFileLogic();

        code = "public class Test {\n"
                + "\n"
                + "	/**\n"
                + "	 *	Javadoc example\n"
                + "	 */\n"
                + "	public static void test() {\n"
                + "		// comment\n"
                + "		System.out.println(\"example\");\n"
                + "	}\n"
                + "\n"
                + "}";

        cu = JavaParser.parse(code);
        cuNull = null;

        f = new File("");

        javaFilesEmpty = new ArrayList<>();
        javaFilesNull = null;
        javaFiles = new ArrayList<>();
        javaFilesWithEntriesToBeIgnored = new ArrayList<>();

        methods = new ArrayList<>();
        methodsEmpty = new ArrayList<>();
        methodsNull = null;

        cr1 = new CommentsRatio(10, 3, 3.33);
        cr2 = new CommentsRatio(100, 16, 6.25);
        cr3 = new CommentsRatio(0, 0, 0);
        cr4 = null;

//        scf1 = new SourceCodeFile(f, cr1, null, null);
//        scf2 = new SourceCodeFile(f, cr2, null, null);
//        scf3 = new SourceCodeFile(f, cr3, null, null);
//        scf4 = new SourceCodeFile(null, cr4, null, null);

        javaFiles.add(scf1);
        javaFiles.add(scf2);

        javaFilesWithEntriesToBeIgnored.add(scf1);
        javaFilesWithEntriesToBeIgnored.add(scf2);
        javaFilesWithEntriesToBeIgnored.add(scf3);
        javaFilesWithEntriesToBeIgnored.add(scf4);

        md = scfl.getMethodsFromFile(cu).get(0).getMethodDeclaration();
        m = new Method(md, null, null);

        methods.add(m);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getLinesOfCodeFromFile method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnCorrectResult__When__CheckingNumberOfLinesInFile() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        assertEquals(11, crl.getLinesOfCodeFromFile(cu));
    }

    /**
     * Test of getLinesOfCodeFromMethod method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnCorrectResult__When__CheckingNumberOfLinesInMethod() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        assertEquals(7, crl.getLinesOfCodeFromMethod(m));
    }

    /**
     * Test of getLinesWithCommentsFromFile method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnCorrectResult__When__CheckingNumberOfLinesWithCommentsInFile() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        assertEquals(4, crl.getLinesWithCommentsFromFile(cu));
    }

    /**
     * Test of getLinesWithCommentsFromMethod method, of class
     * CommentsRatioLogic.
     */
    @Test
    public void Should__ReturnCorrectResult__When__CheckingNumberOfLinesWithCommentsInMethod() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        assertEquals(4, crl.getLinesWithCommentsFromMethod(m));
    }

    /**
     * Test of analyzeFile method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnEmptyObject__When__CompilationUnitIsNull() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        // the returned object should have every argument initialized to 0
        assertEquals(0, crl.analyzeFile(cuNull).getLinesOfCode());
        assertEquals(0, crl.analyzeFile(cuNull).getLinesWithComments());
        assertEquals(0, crl.analyzeFile(cuNull).getValue(), 0.009f);
    }

    /**
     * Test of analyzeFile method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnObjectWithCorrectArgumentsValues__When__CompilationUnitIsValid() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        assertEquals(11, crl.analyzeFile(cu).getLinesOfCode());
        assertEquals(4, crl.analyzeFile(cu).getLinesWithComments());
        assertEquals(2.75, crl.analyzeFile(cu).getValue(), 0.009f);
    }

    /**
     * Test of analyzeMethods method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_NotBreak__When__MethodsListIsEmpty() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        try {
            crl.analyzeMethods(methodsEmpty);
        } catch (Exception e) {
            ex = e;
        }
        assertNull(ex);
    }

    /**
     * Test of analyzeMethods method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_NotBreak__When__MethodsListIsNull() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        try {
            crl.analyzeMethods(methodsNull);
        } catch (Exception e) {
            ex = e;
        }
        assertNull(ex);
    }

    /**
     * Test of analyzeMethods method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_UpdateCommentsRatioObjects__When__MethodsListHasElements() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        // when the list has elements, after it is ran by analyzeMethods() method, all CommentsRatio entries should be updated from null to an object
        crl.analyzeMethods(methods);
        for (Method m : methods) {
            assertNotNull(m.getCommentsRatio());
        }
    }

    /**
     * Test of calculateCommentsRatio method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnZero_When_LocEqualsZero() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        assertEquals(0, crl.calculateCommentsRatio(0, 1), 0.009f);
    }

    /**
     * Test of calculateCommentsRatio method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnZero_When_LomEqualsZero() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        assertEquals(0, crl.calculateCommentsRatio(1, 0), 0.009f);
    }

    /**
     * Test of calculateCommentsRatio method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnZero_When_LomBiggerThanLoc() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        assertEquals(0, crl.calculateCommentsRatio(1, 2), 0.009f);
    }

    /**
     * Test of calculateCommentsRatio method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnCommentsRatio_When_LocAndLomMeetParameters() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        assertEquals(2, crl.calculateCommentsRatio(2, 1), 0.009f);
        assertEquals(1, crl.calculateCommentsRatio(2, 2), 0.009f);
    }

    /**
     * Test of getReadabilityOfProject method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnZero_When_SourceCodeFilesListIsEmpty() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        assertEquals(0, crl.getReadabilityOfProject(javaFilesEmpty), 0.009f);
    }

    /**
     * Test of getReadabilityOfProject method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnZero_When_SourceCodeFilesListIsNull() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        assertEquals(0, crl.getReadabilityOfProject(javaFilesNull), 0.009f);
    }

    /**
     * Test of getReadabilityOfProject method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnProjectReadability_When_SourceCodeFilesHasAtLeastOneEntry() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        assertEquals(4.79, crl.getReadabilityOfProject(javaFiles), 0.009f);
    }

    /**
     * Test of getReadabilityOfProject method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_IgnoreFile_When_OneFileHasNoCommentsRatioValue() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        // if the project has a file without comments, this file should be discarded from the calculations
        assertEquals(4.79, crl.getReadabilityOfProject(javaFilesWithEntriesToBeIgnored), 0.009f);
    }

    /**
     * Test of getReadabilityOfProject method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_IgnoreFile_When_OneEntryHasNoCommentsRatioObjectOrObjectHasNoReadabilityValue() {
        CommentsRatioLogic crl = new CommentsRatioLogic();

        // if the project has a file without comments, it should be discarded from the calculations
        assertEquals(4.79, crl.getReadabilityOfProject(javaFilesWithEntriesToBeIgnored), 0.009f);
    }

    /**
     * Test of getDetailedResults method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnDetailedEmptyString_When_SourceCodeFilesListIsEmpty() {
        assertEquals("", CommentsRatioLogic.getDetailedResults(javaFilesEmpty));
    }

    /**
     * Test of getDetailedResults method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnDetailedEmptyString_When_SourceCodeFilesListIsNull() {
        assertEquals("", CommentsRatioLogic.getDetailedResults(javaFilesNull));
    }

    /**
     * Test of getDetailedResults method, of class CommentsRatioLogic.
     */
    @Test
    public void Should_ReturnSomething_When_SourceCodeFilesListMeetsParameters() {
        String returnedString = CommentsRatioLogic.getDetailedResults(javaFiles);
        assertFalse(returnedString.isEmpty());
    }

}
