/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.exception.sqlexceptionwrapper;

import java.sql.SQLException;

/**
 * 
 * @author 伯牙
 * @version $Id: ZdalSQLExceptionWrapper.java, v 0.1 2014-1-6 下午05:19:12 Exp $
 */
public class ZdalSQLExceptionWrapper extends SQLException {

    public ZdalSQLExceptionWrapper(String message, SQLException targetSQLESqlException) {
        if (targetSQLESqlException == null) {
            throw new IllegalArgumentException("必须填入SQLException");
        }
        this.targetSQLException = targetSQLESqlException;
        this.message = message;
    }

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4558269080286141706L;
    final SQLException        targetSQLException;
    final String              message;

    public String getSQLState() {
        return targetSQLException.getSQLState();
    }

    public int getErrorCode() {
        return targetSQLException.getErrorCode();
    }

    public SQLException getNextException() {
        return targetSQLException.getNextException();
    }

    public void setNextException(SQLException ex) {
        targetSQLException.setNextException(ex);
    }

    public Throwable getCause() {
        return targetSQLException;
    }

    public String getMessage() {
        return message;
    }
}
