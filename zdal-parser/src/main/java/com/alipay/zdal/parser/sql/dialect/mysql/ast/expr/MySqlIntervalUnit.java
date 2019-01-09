/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.expr;

/**
 * 
 * @author 伯牙
 * @version $Id: MySqlIntervalUnit.java, v 0.1 2012-11-17 下午3:30:43 Exp $
 */
public enum MySqlIntervalUnit {
    YEAR, YEAR_MONTH,

    QUARTER,

    MONTH, WEEK, DAY, DAY_HOUR, DAY_MINUTE, DAY_SECOND, DAY_MICROSECOND,

    HOUR, HOUR_MINUTE, HOUR_SECOND, HOUR_MICROSECOND,

    MINUTE, MINUTE_SECOND, MINUTE_MICROSECOND,

    SECOND, SECOND_MICROSECOND,

    MICROSECOND
}
