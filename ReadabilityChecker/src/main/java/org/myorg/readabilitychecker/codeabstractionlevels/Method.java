package org.myorg.readabilitychecker.codeabstractionlevels;

import org.myorg.readabilitychecker.formulas.objects.PHD;
import org.myorg.readabilitychecker.formulas.objects.CommentsRatio;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * A Java method.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class Method {

    private MethodDeclaration methodDeclaration;
    private CommentsRatio commentsRatio;
    private PHD phd;                                  // PHD will only evaluate method level readability

    public Method() {
        this(null, null, null);
    }

    public Method(MethodDeclaration methodDeclaration) {
        this(methodDeclaration, null, null);
    }

    public Method(MethodDeclaration methodDeclaration, CommentsRatio commentsRatio, PHD phd) {
        setMethodDeclaration(methodDeclaration);
        setCommentsRatio(commentsRatio);
        setPhd(phd);
    }

    public MethodDeclaration getMethodDeclaration() {
        return methodDeclaration;
    }

    public CommentsRatio getCommentsRatio() {
        return commentsRatio;
    }

    public PHD getPhd() {
        return phd;
    }

    public void setMethodDeclaration(MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
    }

    public void setCommentsRatio(CommentsRatio commentsRatio) {
        this.commentsRatio = commentsRatio;
    }

    public void setPhd(PHD phd) {
        this.phd = phd;
    }

}
