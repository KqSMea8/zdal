/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLHint;
import com.alipay.zdal.parser.sql.ast.statement.SQLInsertInto;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelect;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObject;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.OracleErrorLoggingClause;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.clause.OracleReturningClause;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author 伯牙
 * @version $Id: OracleMultiInsertStatement.java, v 0.1 2012-11-17 下午3:48:51 Exp $
 */
public class OracleMultiInsertStatement extends OracleStatementImpl {

    public static enum Option {
        ALL, FIRST
    }

    private static final long serialVersionUID = 1L;

    private SQLSelect         subQuery;
    private Option            option;
    private List<Entry>       entries          = new ArrayList<Entry>();
    private List<SQLHint>     hints            = new ArrayList<SQLHint>(1);

    public List<SQLHint> getHints() {
        return hints;
    }

    public void setHints(List<SQLHint> hints) {
        this.hints = hints;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public SQLSelect getSubQuery() {
        return subQuery;
    }

    public void setSubQuery(SQLSelect subQuery) {
        this.subQuery = subQuery;
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.entries);
            acceptChild(visitor, this.subQuery);
        }
        visitor.endVisit(this);
    }

    public static interface Entry extends OracleSQLObject {

    }

    public static class ConditionalInsertClause extends OracleSQLObjectImpl implements Entry {

        private static final long                 serialVersionUID = 1L;
        private List<ConditionalInsertClauseItem> items            = new ArrayList<ConditionalInsertClauseItem>();
        private InsertIntoClause                  elseItem;

        public InsertIntoClause getElseItem() {
            return elseItem;
        }

        public void setElseItem(InsertIntoClause elseItem) {
            this.elseItem = elseItem;
        }

        public List<ConditionalInsertClauseItem> getItems() {
            return items;
        }

        public void setItems(List<ConditionalInsertClauseItem> items) {
            this.items = items;
        }

        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, items);
                acceptChild(visitor, elseItem);
            }
            visitor.endVisit(this);
        }

    }

    public static class ConditionalInsertClauseItem extends OracleSQLObjectImpl {

        private static final long serialVersionUID = 1L;
        private SQLExpr           when;
        private InsertIntoClause  then;

        public SQLExpr getWhen() {
            return when;
        }

        public void setWhen(SQLExpr when) {
            this.when = when;
        }

        public InsertIntoClause getThen() {
            return then;
        }

        public void setThen(InsertIntoClause then) {
            this.then = then;
        }

        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, when);
                acceptChild(visitor, then);
            }
            visitor.endVisit(this);
        }

    }

    public static class InsertIntoClause extends SQLInsertInto implements OracleSQLObject, Entry {

        private static final long        serialVersionUID = 1L;

        private OracleReturningClause    returning;
        private OracleErrorLoggingClause errorLogging;

        public InsertIntoClause() {

        }

        public OracleReturningClause getReturning() {
            return returning;
        }

        public void setReturning(OracleReturningClause returning) {
            this.returning = returning;
        }

        public OracleErrorLoggingClause getErrorLogging() {
            return errorLogging;
        }

        public void setErrorLogging(OracleErrorLoggingClause errorLogging) {
            this.errorLogging = errorLogging;
        }

        protected void accept0(SQLASTVisitor visitor) {
            this.accept0((OracleASTVisitor) visitor);
        }

        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, tableSource);
                this.acceptChild(visitor, columns);
                this.acceptChild(visitor, values);
                this.acceptChild(visitor, query);
                this.acceptChild(visitor, returning);
                this.acceptChild(visitor, errorLogging);
            }

            visitor.endVisit(this);
        }
    }
}
