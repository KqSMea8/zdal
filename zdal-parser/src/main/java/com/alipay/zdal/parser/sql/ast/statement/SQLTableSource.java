/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.ast.statement;

import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLHint;
import com.alipay.zdal.parser.sql.ast.SQLObject;

/**
 * 
 * @author 伯牙
 * @version $Id: SQLTableSource.java, v 0.1 2012-11-17 下午3:27:06 Exp $
 */
public interface SQLTableSource extends SQLObject {

    String getAlias();

    void setAlias(String alias);

    List<SQLHint> getHints();
}
