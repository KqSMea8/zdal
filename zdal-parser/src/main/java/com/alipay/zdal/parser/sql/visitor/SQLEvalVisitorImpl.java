/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.visitor;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLCharExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLVariantRefExpr;

/**
 * 
 * @author 伯牙
 * @version $Id: SQLEvalVisitorImpl.java, v 0.1 2012-11-17 下午3:56:46 Exp $
 */
public class SQLEvalVisitorImpl extends SQLASTVisitorAdapter implements SQLEvalVisitor {

    private List<Object> parameters       = new ArrayList<Object>();

    private int          variantIndex     = -1;

    private boolean      markVariantIndex = true;

    public SQLEvalVisitorImpl() {
        this(new ArrayList<Object>(1));
    }

    public SQLEvalVisitorImpl(List<Object> parameters) {
        this.parameters = parameters;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public boolean visit(SQLCharExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public int incrementAndGetVariantIndex() {
        return ++variantIndex;
    }

    public int getVariantIndex() {
        return variantIndex;
    }

    public boolean visit(SQLVariantRefExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean visit(SQLBinaryOpExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }

    public boolean isMarkVariantIndex() {
        return markVariantIndex;
    }

    public void setMarkVariantIndex(boolean markVariantIndex) {
        this.markVariantIndex = markVariantIndex;
    }

}
