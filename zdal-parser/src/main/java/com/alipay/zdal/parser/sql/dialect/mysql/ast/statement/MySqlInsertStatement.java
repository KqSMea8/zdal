/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLInsertStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author 伯牙
 * @version $Id: MySqlInsertStatement.java, v 0.1 2012-11-17 下午3:33:19 Exp $
 */
public class MySqlInsertStatement extends SQLInsertStatement {

    private static final long   serialVersionUID   = 1L;

    private boolean             lowPriority        = false;
    private boolean             delayed            = false;
    private boolean             highPriority       = false;
    private boolean             ignore             = false;

    private List<ValuesClause>  valuesList         = new ArrayList<ValuesClause>();

    private final List<SQLExpr> duplicateKeyUpdate = new ArrayList<SQLExpr>();

    public List<SQLExpr> getDuplicateKeyUpdate() {
        return duplicateKeyUpdate;
    }

    public ValuesClause getValues() {
        if (valuesList.size() == 0) {
            return null;
        }
        return valuesList.get(0);
    }

    public void setValues(ValuesClause values) {
        if (valuesList.size() == 0) {
            valuesList.add(values);
        } else {
            valuesList.set(0, values);
        }
    }

    public List<ValuesClause> getValuesList() {
        return valuesList;
    }

    public boolean isLowPriority() {
        return lowPriority;
    }

    public void setLowPriority(boolean lowPriority) {
        this.lowPriority = lowPriority;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public void setDelayed(boolean delayed) {
        this.delayed = delayed;
    }

    public boolean isHighPriority() {
        return highPriority;
    }

    public void setHighPriority(boolean highPriority) {
        this.highPriority = highPriority;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        } else {
            throw new IllegalArgumentException("not support visitor type : "
                                               + visitor.getClass().getName());
        }
    }

    public void output(StringBuffer buf) {
        new MySqlOutputVisitor(buf).visit(this);
    }

    protected void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, getTableSource());
            this.acceptChild(visitor, getColumns());
            this.acceptChild(visitor, getValuesList());
            this.acceptChild(visitor, getQuery());
            this.acceptChild(visitor, getDuplicateKeyUpdate());
        }

        visitor.endVisit(this);
    }
}
