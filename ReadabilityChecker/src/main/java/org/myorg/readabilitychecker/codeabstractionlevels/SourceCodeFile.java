package org.myorg.readabilitychecker.codeabstractionlevels;

import org.myorg.readabilitychecker.formulas.objects.SRES;
import org.myorg.readabilitychecker.formulas.objects.CommentsRatio;
import java.io.File;
import java.util.ArrayList;
import org.myorg.readabilitychecker.formulas.objects.BW;

/**
 * A source code file (.java file).
 *
 * <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class SourceCodeFile {

    private File file;
    private CommentsRatio commentsRatio;
    private SRES sres;
    private BW bw;
    private ArrayList<Method> methods;

    public SourceCodeFile() {
        this(null, null, null, null, null);
    }

    public SourceCodeFile(File file) {
        this(file, new CommentsRatio(), new SRES(), new BW(), new ArrayList<Method>());
    }

    public SourceCodeFile(File file, CommentsRatio commentsRatio, SRES sres, BW bw, ArrayList<Method> methods) {
        setFile(file);
        setCommentsRatio(commentsRatio);
        setSres(sres);
        setBw(bw);
        setMethods(methods);
    }

    public File getFile() {
        return file;
    }

    public CommentsRatio getCommentsRatio() {
        return commentsRatio;
    }

    public SRES getSres() {
        return sres;
    }

    public BW getBw() {
        return bw;
    }

    public ArrayList<Method> getMethods() {
        return methods;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setCommentsRatio(CommentsRatio commentsRatio) {
        this.commentsRatio = commentsRatio;
    }

    public void setSres(SRES sres) {
        this.sres = sres;
    }

    public void setBw(BW bw) {
        this.bw = bw;
    }

    public void setMethods(ArrayList<Method> methods) {
        this.methods = methods;
    }

}
