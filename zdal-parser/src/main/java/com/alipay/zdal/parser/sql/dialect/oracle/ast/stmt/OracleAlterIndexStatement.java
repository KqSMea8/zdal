/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLObject;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author 伯牙
 * @version $Id: OracleAlterIndexStatement.java, v 0.1 2012-11-17 下午3:44:49 Exp $
 */
public class OracleAlterIndexStatement extends OracleStatementImpl {

    private static final long serialVersionUID = 1L;

    private SQLName           name;

    private SQLName           renameTo;

    private boolean           compile;

    private Boolean           enable;

    private Boolean           monitoringUsage;

    private Rebuild           rebuild;

    private SQLExpr           parallel;

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
            acceptChild(visitor, renameTo);
            acceptChild(visitor, rebuild);
            acceptChild(visitor, parallel);
        }
        visitor.endVisit(this);
    }

    public SQLName getRenameTo() {
        return renameTo;
    }

    public void setRenameTo(SQLName renameTo) {
        this.renameTo = renameTo;
    }

    public SQLExpr getParallel() {
        return parallel;
    }

    public void setParallel(SQLExpr parallel) {
        this.parallel = parallel;
    }

    public Boolean getMonitoringUsage() {
        return monitoringUsage;
    }

    public void setMonitoringUsage(Boolean monitoringUsage) {
        this.monitoringUsage = monitoringUsage;
    }

    public Rebuild getRebuild() {
        return rebuild;
    }

    public void setRebuild(Rebuild rebuild) {
        this.rebuild = rebuild;
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

    public boolean isCompile() {
        return compile;
    }

    public void setCompile(boolean compile) {
        this.compile = compile;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public static class Rebuild extends OracleSQLObjectImpl {

        private static final long serialVersionUID = 1L;
        private SQLObject         option;

        public SQLObject getOption() {
            return option;
        }

        public void setOption(SQLObject option) {
            this.option = option;
        }

        @Override
        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, option);
            }
            visitor.endVisit(this);
        }

    }
}
