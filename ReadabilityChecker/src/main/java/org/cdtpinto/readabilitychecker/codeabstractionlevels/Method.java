package org.cdtpinto.readabilitychecker.codeabstractionlevels;

import org.cdtpinto.readabilitychecker.formulas.objects.CommentsRatio;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * A Java method.
 *
 * @author <a href="mailto:1120301@isep.ipp.pt">Cláudio Pinto</a>
 */
public class Method {

    private MethodDeclaration methodDeclaration;
    private CommentsRatio commentsRatio;

    public Method() {
        this(null, null);
    }

    public Method(MethodDeclaration methodDeclaration) {
        this(methodDeclaration, null);
    }

    public Method(MethodDeclaration methodDeclaration, CommentsRatio commentsRatio) {
        setMethodDeclaration(methodDeclaration);
        setCommentsRatio(commentsRatio);
    }

    public MethodDeclaration getMethodDeclaration() {
        return methodDeclaration;
    }

    public CommentsRatio getCommentsRatio() {
        return commentsRatio;
    }

    public void setMethodDeclaration(MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
    }

    public void setCommentsRatio(CommentsRatio commentsRatio) {
        this.commentsRatio = commentsRatio;
    }

}
