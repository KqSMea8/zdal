/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.expr;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLExprImpl;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author 伯牙
 * @version $Id: MySqlIntervalExpr.java, v 0.1 2012-11-17 下午3:30:37 Exp $
 */
public class MySqlIntervalExpr extends SQLExprImpl implements MySqlExpr {

    private static final long serialVersionUID = 1L;

    private SQLExpr           value;
    private MySqlIntervalUnit unit;

    public MySqlIntervalExpr() {
    }

    public SQLExpr getValue() {
        return value;
    }

    public void setValue(SQLExpr value) {
        this.value = value;
    }

    public MySqlIntervalUnit getUnit() {
        return unit;
    }

    public void setUnit(MySqlIntervalUnit unit) {
        this.unit = unit;
    }

    @Override
    public void output(StringBuffer buf) {
        value.output(buf);
        buf.append(' ');
        buf.append(unit.name());
    }

    protected void accept0(SQLASTVisitor visitor) {
        MySqlASTVisitor mysqlVisitor = (MySqlASTVisitor) visitor;
        if (mysqlVisitor.visit(this)) {
            acceptChild(visitor, this.value);
        }
        mysqlVisitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MySqlIntervalExpr other = (MySqlIntervalExpr) obj;
        if (unit != other.unit) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

}
