/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock.Limit;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author 伯牙
 * @version $Id: MySqlShowErrorsStatement.java, v 0.1 2012-11-17 下午3:36:22 Exp $
 */
public class MySqlShowErrorsStatement extends MySqlStatementImpl {

    private static final long serialVersionUID = 1L;

    private boolean           count            = false;
    private Limit             limit;

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, limit);
        }
        visitor.endVisit(this);
    }
}
