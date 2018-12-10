/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.readabilitychecker.logic;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.openide.filesystems.FileObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.myorg.readabilitychecker.codeabstractionlevels.SourceCodeFile;
import org.myorg.readabilitychecker.logic.SourceCodeFileLogic;
import static org.myorg.readabilitychecker.logic.SourceCodeFileLogic.getCurrentlyOpenedFile;
import static org.myorg.readabilitychecker.logic.SourceCodeFileLogic.getCurrentlyOpenedProjectObject;
import org.netbeans.api.project.Project;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.openide.util.Utilities;
import org.openide.util.BaseUtilities;

/**
 *
 * @author Claudio
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SourceCodeFileLogic.class})
public class SourceCodeFileLogicTest {

    String code;
    CompilationUnit cu, cuNull;
    Project projectMocked, projectNull;
    SourceCodeFile scf;
    List<File> javaFilesInDirectory;
    ArrayList<SourceCodeFile> sourceCodeFilesInDirectory;
    File f1, f2;
    FileObject foMocked;
    URI mockedUri, uriNull;

    public SourceCodeFileLogicTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {
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

        uriNull = null;

        f1 = new File("Foo.java");
        f2 = new File("Bar.java");

        projectMocked = Mockito.mock(Project.class);
        foMocked = Mockito.mock(FileObject.class);
        scf = Mockito.mock(SourceCodeFile.class);
        mockedUri = Mockito.mock(URI.class);

        javaFilesInDirectory = new ArrayList<>();
        javaFilesInDirectory.add(f1);
        javaFilesInDirectory.add(f2);

        sourceCodeFilesInDirectory = new ArrayList<>();
        sourceCodeFilesInDirectory.add(scf);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getJavaFilesInDirectory method, of class SourceCodeFileLogic.
     */
    @Test
    public void Should_ReturnEmptyList_When_UriIsNull() throws Exception {
        assertTrue(SourceCodeFileLogic.getJavaFilesInDirectory(uriNull).isEmpty());
    }

    /**
     * Test of getJavaFilesInDirectory method, of class SourceCodeFileLogic.
     */
    @Test
    public void Should_ReturnNonEmptyList_When_UriPointsToDirectoryWithJavaFiles() throws Exception {
        PowerMockito.stub(PowerMockito.method(FileUtils.class, "listFiles", File.class, IOFileFilter.class, IOFileFilter.class)).toReturn(javaFilesInDirectory);
        File fm = Mockito.mock(File.class);
        PowerMockito.stub(PowerMockito.method(org.openide.util.Utilities.class, "toFile", URI.class)).toReturn(fm);

        //assertFalse(SourceCodeFileLogic.getJavaFilesInDirectory(mockedUri).isEmpty());
    }

    /**
     * Test of getOpenedProjectName method, of class SourceCodeFileLogic.
     */
    @Test
    public void testGetOpenedProjectName() {
    }

    // ARRANJAR MAIS CASOS DE TESTE PARA ESTE MÉTODO?
    /**
     * Test of getJavaFilesFromProject method, of class SourceCodeFileLogic.
     */
    @Test
    public void Should_ReturnEmptyList_When_ThereIsNoOpenedProject() throws Exception {
        SourceCodeFileLogic scfl = new SourceCodeFileLogic();

        PowerMockito.stub(PowerMockito.method(SourceCodeFileLogic.class, "getCurrentlyOpenedFile")).toReturn(null); // if there is no opened file, then, there is no opened project

        assertTrue(scfl.getJavaFilesFromProject().isEmpty());
    }

    /**
     * Test of getJavaFilesFromProject method, of class SourceCodeFileLogic.
     */
    @Test
    public void Should_ReturnNonEmptyList_When_ThereIsAnOpenedProject() throws Exception {
        SourceCodeFileLogic scfl = new SourceCodeFileLogic();
        FileObject mocked = Mockito.mock(FileObject.class);

        PowerMockito.stub(PowerMockito.method(SourceCodeFileLogic.class, "getCurrentlyOpenedFile")).toReturn(new SourceCodeFile(f1));
        PowerMockito.stub(PowerMockito.method(SourceCodeFileLogic.class, "getCurrentlyOpenedProjectObject")).toReturn(projectMocked);
        Mockito.when(projectMocked.getProjectDirectory()).thenReturn(foMocked);
        Mockito.when(mocked.toURI()).thenReturn(new URI("path/to/dir"));
        PowerMockito.stub(PowerMockito.method(SourceCodeFileLogic.class, "getJavaFilesInDirectory", URI.class)).toReturn(sourceCodeFilesInDirectory);

        assertFalse(scfl.getJavaFilesFromProject().isEmpty());
    }

    /**
     * Test of getCurrentlyOpenedFile method, of class SourceCodeFileLogic.
     */
    @Test
    public void Should_ReturnNull_When_ThereIsNoOpenedProject() {
        assertEquals(null, SourceCodeFileLogic.getCurrentlyOpenedFile());
    }

    /**
     * Test of getCurrentlyOpenedFile method, of class SourceCodeFileLogic.
     */
    @Test
    public void Should_ReturnSourceCodeFile_When_ThereIsNoOpenedProject() {
        //PowerMockito.stub(PowerMockito.method(SourceCodeFileLogic.class, "getCurrentlyOpenedProjectObject")).toReturn(projectMocked);
    }

    // ARRANJAR MAIS CASOS DE TESTE PARA ESTE MÉTODO?
    /**
     * Test of getMethodsFromFile method, of class SourceCodeFileLogic.
     */
    @Test
    public void Should_ReturnEmptyMethodsList_When_CuIsNull() {
        SourceCodeFileLogic scfl = new SourceCodeFileLogic();

        assertTrue(scfl.getMethodsFromFile(cuNull).isEmpty());
    }

    /**
     * Test of getMethodsFromFile method, of class SourceCodeFileLogic.
     */
    @Test
    public void Should_ReturnMethodsList_When_CuMeetsParameters() {
        SourceCodeFileLogic scfl = new SourceCodeFileLogic();

        assertFalse(scfl.getMethodsFromFile(cu).isEmpty());
    }
}
