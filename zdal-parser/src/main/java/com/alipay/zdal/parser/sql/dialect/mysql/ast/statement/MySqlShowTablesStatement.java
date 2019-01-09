/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author 伯牙
 * @version $Id: MySqlShowTablesStatement.java, v 0.1 2012-11-17 下午3:38:23 Exp $
 */
public class MySqlShowTablesStatement extends MySqlStatementImpl {

    private static final long serialVersionUID = 1L;

    private boolean           full;

    private SQLName           database;
    private SQLExpr           like;
    private SQLExpr           where;

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public SQLName getDatabase() {
        return database;
    }

    public void setDatabase(SQLName database) {
        this.database = database;
    }

    public SQLExpr getLike() {
        return like;
    }

    public void setLike(SQLExpr like) {
        this.like = like;
    }

    public SQLExpr getWhere() {
        return where;
    }

    public void setWhere(SQLExpr where) {
        this.where = where;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, database);
            acceptChild(visitor, like);
            acceptChild(visitor, where);
        }
        visitor.endVisit(this);
    }
}
