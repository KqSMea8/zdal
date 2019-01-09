/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast;

import com.alipay.zdal.parser.sql.ast.SQLObject;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author 伯牙
 * @version $Id: MySqlObject.java, v 0.1 2012-11-17 下午3:29:33 Exp $
 */
public interface MySqlObject extends SQLObject {
    void accept0(MySqlASTVisitor visitor);
}
