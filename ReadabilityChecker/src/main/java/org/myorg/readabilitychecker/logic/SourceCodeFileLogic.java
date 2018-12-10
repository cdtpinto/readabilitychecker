package org.myorg.readabilitychecker.logic;

import org.myorg.readabilitychecker.codeabstractionlevels.SourceCodeFile;
import org.myorg.readabilitychecker.codeabstractionlevels.Method;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.net.URI;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Contains the logical methods for the handling of Java files.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class SourceCodeFileLogic {

    private static final String FILE_EXTENSION = "java";

    // Suppresses default constructor, ensuring non-instantiability.
    public SourceCodeFileLogic() {
    }

    /**
     * Gets all the Java files in <code>directory</code>.
     *
     * @param directory full path of the directory.
     * @return a list in which each entry represents a Java file.
     * @throws IOException in case "file" doesn't have a canonical path.
     */
    public static ArrayList<SourceCodeFile> getJavaFilesInDirectory(URI directory) throws IOException {
        ArrayList<SourceCodeFile> filesInDirectory = new ArrayList<>();

        if (directory != null) {
            File dir = Utilities.toFile(directory);

            List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            for (File file : files) {
                if (FilenameUtils.getExtension(file.getCanonicalPath()).equalsIgnoreCase(FILE_EXTENSION)) {
                    filesInDirectory.add(new SourceCodeFile(file)); // creates a SourceCodeFile object from File
                }
            }
        }

        return filesInDirectory;
    }

    /**
     * Gets the NetBeans API's <code>Project</code> object that represents the
     * currently opened project in the IDE.
     *
     * @return a <code>Project</code> object that represents the currently
     * opened project in the IDE.
     */
    public static Project getCurrentlyOpenedProjectObject() {
        return new MainProjectManager().getMainProject();
    }

    /**
     * Gets the name of the currently opened project in the IDE.
     *
     * @return a String with the opened project name (not the path).
     */
    public static String getOpenedProjectName() {
        Project openedProject = getCurrentlyOpenedProjectObject();

        return openedProject.getProjectDirectory().getName();
    }

    /**
     * Gets all Java files from the currently opened project in the IDE.
     *
     * @return a list in which each entry represents a Java file.
     * @throws IOException in case <code>getFilesInDirectory</code> method
     * throws this exception.
     */
    public ArrayList<SourceCodeFile> getJavaFilesFromProject() throws IOException {
        ArrayList<SourceCodeFile> javaFiles = new ArrayList<>();

        if (getCurrentlyOpenedFile() != null) {   // if there is no opened file, there is no need to load the opened project
            Project openedProject = getCurrentlyOpenedProjectObject();

            if (openedProject != null) {
                javaFiles = getJavaFilesInDirectory(openedProject.getProjectDirectory().toURI());
            }
        }

        return javaFiles;
    }

    /**
     * Gets the currently opened Java file in the IDE.
     *
     * @return a new SourceCodeFile from the currently opened Java file or null,
     * if there is no opened file, or the opened file is not a Java file.
     */
    public static SourceCodeFile getCurrentlyOpenedFile() {
        Project openedProject = getCurrentlyOpenedProjectObject();

        // Get Java file currently displaying in the IDE if there is an opened project
        if (openedProject != null) {
            TopComponent activeTC = TopComponent.getRegistry().getActivated();
            DataObject dataLookup = activeTC.getLookup().lookup(DataObject.class);
            File file;

            try {
                file = FileUtil.toFile(dataLookup.getPrimaryFile());                   // currently opened file
            } catch (NullPointerException ex) {
                System.out.println(ex);
                return null;
            }

            if (FilenameUtils.getExtension(file.getAbsoluteFile().getAbsolutePath()).equalsIgnoreCase(FILE_EXTENSION)) {
                return new SourceCodeFile(file);
            }

            return null;
        }

        return null;
    }

    /**
     * Gets all the methods in a Java file.
     *
     * @param cu represents a Java file parsed by JavaParser.
     * @return an ArrayList of type Method with all the methods in
     * <code>cu</code>.
     */
    public ArrayList<Method> getMethodsFromFile(CompilationUnit cu) {
        ArrayList<Method> methods = new ArrayList<>();

        if (cu != null) {
            cu.walk(node -> {
                if (node instanceof MethodDeclaration) {
                    MethodDeclaration m = (MethodDeclaration) node;
                    methods.add(new Method(m));
                }
            });
        }

        return methods;
    }

}
