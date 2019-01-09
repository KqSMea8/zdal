/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author 伯牙
 * @version $Id: OracleIfStatement.java, v 0.1 2012-11-17 下午3:48:18 Exp $
 */
public class OracleIfStatement extends OracleStatementImpl {

    private static final long  serialVersionUID = 1L;

    private SQLExpr            condition;
    private List<SQLStatement> statements       = new ArrayList<SQLStatement>();
    private List<ElseIf>       elseIfList       = new ArrayList<ElseIf>();
    private Else               elseItem;

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, condition);
            acceptChild(visitor, statements);
            acceptChild(visitor, elseIfList);
            acceptChild(visitor, elseItem);
        }
        visitor.endVisit(this);
    }

    public SQLExpr getCondition() {
        return condition;
    }

    public void setCondition(SQLExpr condition) {
        this.condition = condition;
    }

    public List<SQLStatement> getStatements() {
        return statements;
    }

    public void setStatements(List<SQLStatement> statements) {
        this.statements = statements;
    }

    public List<ElseIf> getElseIfList() {
        return elseIfList;
    }

    public void setElseIfList(List<ElseIf> elseIfList) {
        this.elseIfList = elseIfList;
    }

    public Else getElseItem() {
        return elseItem;
    }

    public void setElseItem(Else elseItem) {
        this.elseItem = elseItem;
    }

    public static class ElseIf extends OracleSQLObjectImpl {

        private static final long  serialVersionUID = 1L;

        private SQLExpr            condition;
        private List<SQLStatement> statements       = new ArrayList<SQLStatement>();

        @Override
        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, condition);
                acceptChild(visitor, statements);
            }
            visitor.endVisit(this);
        }

        public SQLExpr getCondition() {
            return condition;
        }

        public void setCondition(SQLExpr condition) {
            this.condition = condition;
        }

        public List<SQLStatement> getStatements() {
            return statements;
        }

        public void setStatements(List<SQLStatement> statements) {
            this.statements = statements;
        }

    }

    public static class Else extends OracleSQLObjectImpl {

        private static final long  serialVersionUID = 1L;

        private List<SQLStatement> statements       = new ArrayList<SQLStatement>();

        @Override
        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, statements);
            }
            visitor.endVisit(this);
        }

        public List<SQLStatement> getStatements() {
            return statements;
        }

        public void setStatements(List<SQLStatement> statements) {
            this.statements = statements;
        }

    }
}
