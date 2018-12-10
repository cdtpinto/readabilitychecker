package org.myorg.readabilitychecker.formulas.logic;

import com.github.javaparser.JavaParser;
import com.github.javaparser.JavaToken;
import com.github.javaparser.Token;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.myorg.readabilitychecker.codeabstractionlevels.Method;
import org.myorg.readabilitychecker.formulas.logic.PhdLogic;
import org.myorg.readabilitychecker.logic.SourceCodeFileLogic;

/**
 * Unit tests for PhdLogic.java.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class PhdLogicTest {

    //PhdLogic pl;
    SourceCodeFileLogic scfl;
    CompilationUnit cu, cuTmp;
    MethodDeclaration md;
    Method methodObj, methodObjWithoutMethodDeclaration, methodObjNull;
    String code, codeTmp;
    ArrayList<Method> methods, methodsEmpty, methodsNull;

    public PhdLogicTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        //pl = new PhdLogic();
        scfl = new SourceCodeFileLogic();

        code = "public class Test {\n"
                + "\n"
                + "	public static void test() {\n"
                + "		int a = 1;\n"
                + "		int b = 2;\n"
                + "		int sum = a + b;\n"
                + "		System.out.pritnln(sum);\n"
                + "	}\n"
                + "\n"
                + "}";

        codeTmp = "package bla;\n"
                + "\n"
                + "import java.awt.event.ActionEvent;\n"
                + "\n"
                + "public class Test {\n"
                + "\n"
                + "	interface NumericTest {\n"
                + "		boolean computeTest(int n); \n"
                + "	}\n"
                + "\n"
                + "	/**\n"
                + "	 *	Javadoc example\n"
                + "	 */\n"
                + "	public static void test() {\n"
                + "		// comment\n"
                + "		System.out.println(\"example\");\n"
                + "		ArrayList<String> a = new ArrayList<String>();\n"
                + "	}\n"
                + "	\n"
                + "}";

        cu = JavaParser.parse(code);
        cuTmp = JavaParser.parse(code);

        md = scfl.getMethodsFromFile(cu).get(0).getMethodDeclaration();
        methodObj = new Method(md, null, null);
        methodObjWithoutMethodDeclaration = new Method(null);
        methodObjNull = null;

        methodsEmpty = new ArrayList<>();
        methodsNull = null;
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getLinesFromMethod method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnSix_When_CheckingNumberOfLinesInMethod() {
        PhdLogic plTst = new PhdLogic();

        assertEquals(6, plTst.getLinesFromMethod(methodObj));
    }

    /**
     * Test of getVolumeFromMethod method, of class PhdLogic.
     */
    @Test
    public void testGetVolumeFromMethod() throws Exception {
        PhdLogic plTst = new PhdLogic();
        SourceCodeFileLogic scflTst = new SourceCodeFileLogic();

        plTst.getVolumeFromMethod(scflTst.getMethodsFromFile(cuTmp).get(0));
    }

    /**
     * Test of removeOrDecrementFooAsOperand method, of class PhdLogic.
     */
    @Test
    public void testRemoveOrDecrementFooAsOperand() {
    }

    /**
     * Test of getVolumeFromMethodWithAametwallysLibrary method, of class
     * PhdLogic.
     */
    @Test
    public void testGetVolumeFromMethodWithAametwallysLibrary() {
    }

    /**
     * Test of getTokensFromMethod method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnEmptyTokensList_When_MethodObjHasNoMethodDeclaration() {
        PhdLogic plTst = new PhdLogic();

        ArrayList<JavaToken> tokens = plTst.getTokensFromMethod(methodObjWithoutMethodDeclaration);
        assertTrue(tokens.isEmpty());
    }

    /**
     * Test of getTokensFromMethod method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnEmptyTokensList_When_MethodObjIsNull() {
        PhdLogic plTst = new PhdLogic();

        ArrayList<JavaToken> tokens = plTst.getTokensFromMethod(methodObjNull);
        assertTrue(tokens.isEmpty());
    }

    /**
     * Test of getTokensFromMethod method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnTokensList_When_MethodObjMeetsParameters() {
        PhdLogic plTst = new PhdLogic();

        ArrayList<JavaToken> tokens = plTst.getTokensFromMethod(methodObj);
        assertFalse(tokens.isEmpty());
    }

    /**
     * Test of getBytesFromMethod method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnEmptyBytesList_When_MethodObjHasNoMethodDeclaration() {
        PhdLogic plTst = new PhdLogic();

        ArrayList<Character> bytes = plTst.getBytesFromMethod(methodObjWithoutMethodDeclaration);
        assertTrue(bytes.isEmpty());
    }

    /**
     * Test of getBytesFromMethod method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnEmptyBytesList_When_MethodObjIsNull() {
        PhdLogic plTst = new PhdLogic();

        ArrayList<Character> bytes = plTst.getBytesFromMethod(methodObjNull);
        assertTrue(bytes.isEmpty());
    }

    /**
     * Test of getBytesFromMethod method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnBytesList_When_MethodObjMeetsParameters() {
        PhdLogic plTst = new PhdLogic();

        ArrayList<Character> bytes = plTst.getBytesFromMethod(methodObj);
        assertFalse(bytes.isEmpty());
    }

    /**
     * Test of getEntropyFromMethod method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnZero_When_MethodObjHasNoMethodDeclaration() {
        PhdLogic plTst = new PhdLogic();

        assertEquals(0, plTst.getEntropyFromMethod(methodObjWithoutMethodDeclaration), 0.009f);
    }

    /**
     * Test of getEntropyFromMethod method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnZero_When_MethodObjIsNull() {
        PhdLogic plTst = new PhdLogic();

        assertEquals(0, plTst.getEntropyFromMethod(methodObjNull), 0.009f);
    }

    /**
     * Test of getEntropyFromMethod method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnEntropyValue_When_MethodObjMeetsParameters() {
        PhdLogic plTst = new PhdLogic();

        Double entropy = plTst.getEntropyFromMethod(methodObj);
        assertTrue(entropy > 0);
    }

    /**
     * Test of analyzeMethods method, of class PhdLogic.
     */
    @Test
    public void Should_NotUpdateNumberOfMethodsAnalysed_When_MethodsListIsEmptyOrNull() throws Exception {
        PhdLogic plTst = new PhdLogic();

        assertEquals(2, plTst.analyzeMethods(methodsEmpty, 11, 2));
        assertEquals(2, plTst.analyzeMethods(methodsNull, 11, 2));
    }

    /**
     * Test of calculateRegressionVariable method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnCorrectValue_When_CalculatingRegressionVariable() {
        PhdLogic plTst = new PhdLogic();

        assertEquals(4.87, plTst.calculateRegressionVariable(30.2, 6, 3.6), 0.009f);
    }

    /**
     * Test of applyLogisticFunction method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnCorrectValue_When_CalculatingLogisticVariable() {
        PhdLogic plTst = new PhdLogic();

        assertEquals(0.99, plTst.applyLogisticFunction(4.87), 0.009f);
    }

    /**
     * Test of calculatePhd method, of class PhdLogic.
     */
    @Test
    public void Should_ReturnPhdValueZero_When_AnyParameterIsZero() {
        PhdLogic plTst = new PhdLogic();

        assertEquals(0, plTst.calculatePhd(0, 1, 1), 0.009f);
        assertEquals(0, plTst.calculatePhd(1, 0, 1), 0.009f);
        assertEquals(0, plTst.calculatePhd(1, 1, 0), 0.009f);
        assertEquals(0, plTst.calculatePhd(0, 0, 1), 0.009f);
        assertEquals(0, plTst.calculatePhd(0, 1, 0), 0.009f);
        assertEquals(0, plTst.calculatePhd(1, 0, 0), 0.009f);
        assertEquals(0, plTst.calculatePhd(0, 0, 0), 0.009f);
    }

    /**
     * Test of calculatePhd method, of class PhdLogic.
     */
    //@Test
    public void Should_ReturnPhdValue_When_AllParametersAreBiggerThanZero() {
        PhdLogic plTst = new PhdLogic();

        assertEquals(0.99, plTst.calculatePhd(30.2, 6, 3.6), 0.009f);
    }

    /**
     * Test of getDetailedResults method, of class PhdLogic.
     */
    @Test
    public void testGetDetailedResults() {
    }
}
