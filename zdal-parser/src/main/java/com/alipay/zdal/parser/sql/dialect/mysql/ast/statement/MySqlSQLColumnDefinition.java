/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.ast.statement.SQLColumnDefinition;

/**
 * 
 * @author 伯牙
 * @version $Id: MySqlSQLColumnDefinition.java, v 0.1 2012-11-17 下午3:38:54 Exp $
 */
public class MySqlSQLColumnDefinition extends SQLColumnDefinition {

    private static final long serialVersionUID = 1L;

    private boolean           autoIncrement    = false;

    public MySqlSQLColumnDefinition() {

    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

}
