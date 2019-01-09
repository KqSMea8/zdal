/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;

/**
 * 
 * @author 伯牙
 * @version $Id: SQLUniqueConstraint.java, v 0.1 2012-11-17 下午3:28:30 Exp $
 */
public interface SQLUniqueConstraint extends SQLConstaint {

    List<SQLExpr> getColumns();

}
