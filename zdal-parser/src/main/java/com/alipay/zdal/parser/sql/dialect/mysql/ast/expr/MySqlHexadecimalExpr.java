/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.expr;

import com.alipay.zdal.parser.sql.ast.expr.SQLLiteralExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author 伯牙
 * @version $Id: MySqlHexadecimalExpr.java, v 0.1 2012-11-17 下午3:30:31 Exp $
 */
public class MySqlHexadecimalExpr extends MySqlExprImpl implements SQLLiteralExpr {

    private static final long serialVersionUID = 1L;

    @Override
    public void accept0(MySqlASTVisitor visitor) {

    }

}
