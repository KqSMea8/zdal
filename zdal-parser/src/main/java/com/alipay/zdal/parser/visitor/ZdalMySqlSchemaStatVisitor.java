/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.visitor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOperator;
import com.alipay.zdal.parser.sql.ast.expr.SQLIdentifierExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLIntegerExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLVariantRefExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLCreateTableStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLDropTableStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLExprTableSource;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQueryBlock;
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement;
import com.alipay.zdal.parser.sql.ast.statement.SQLUnionQuery;
import com.alipay.zdal.parser.sql.ast.statement.SQLUpdateStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlForceIndexHint;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlIgnoreIndexHint;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlKey;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.MySqlUseIndexHint;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlBinaryExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlBooleanExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlCharExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlExtractExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlIntervalExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlMatchAgainstExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.expr.MySqlUserName;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.CobarShowStatus;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableAddColumn;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableAddIndex;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableAddUnique;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableChangeColumn;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableCharacter;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableOption;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlAlterTableStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlBinlogStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlCommitStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlCreateIndexStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlDescribeStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlDropTableStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlDropUser;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlDropViewStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlExecuteStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlHelpStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlKillStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlLoadDataInFileStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlLoadXmlStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlLockTableStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlPartitionByKey;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlPrepareStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlReplicateStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlResetStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlRollbackStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectGroupBy;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSetCharSetStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSetNamesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSetTransactionIsolationLevelStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowAuthorsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowBinLogEventsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowBinaryLogsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCharacterSetStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCollationStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowColumnsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowContributorsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateDatabaseStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateEventStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateFunctionStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateProcedureStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateTableStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateTriggerStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowCreateViewStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowDatabasesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowEngineStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowEnginesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowErrorsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowEventsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowFunctionCodeStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowFunctionStatusStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowGrantsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowIndexesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowKeysStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowMasterLogsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowMasterStatusStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowOpenTablesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowPluginsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowPrivilegesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowProcedureCodeStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowProcedureStatusStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowProcessListStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowProfileStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowProfilesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowRelayLogEventsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowSlaveHostsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowSlaveStatusStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowStatusStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowTableStatusStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowTablesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowTriggersStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowVariantsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlShowWarningsStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlStartTransactionStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlTableIndex;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlUnionQuery;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlUnlockTablesStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement.UserSpecification;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock.Limit;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;
import com.alipay.zdal.parser.sql.stat.TableStat.Mode;
import com.alipay.zdal.parser.sql.util.JdbcUtils;

/**
 * 
 * @author 伯牙
 * @version $Id: ZdalMySqlSchemaStatVisitor.java, v 0.1 2012-11-17 下午3:57:17 Exp $
 */
public class ZdalMySqlSchemaStatVisitor extends ZdalSchemaStatVisitor implements MySqlASTVisitor {
    private Set<BindVarCondition> limits = new HashSet<BindVarCondition>();

    /** 
     * @see com.alipay.zdal.parser.sql.visitor.SchemaStatVisitor#visit(com.alipay.zdal.parser.sql.ast.statement.SQLSelectStatement)
     */
    public boolean visit(SQLSelectStatement x) {
        setAliasMap();
        getAliasMap().put("DUAL", null);

        return true;
    }

    /** 
     * @see com.alipay.zdal.parser.sql.visitor.SchemaStatVisitor#getDbType()
     */
    public String getDbType() {
        return JdbcUtils.MYSQL;
    }

    public boolean visit(MySqlSelectQueryBlock x) {
        boolean result = this.visit((SQLSelectQueryBlock) x);
        if (x.getLimit() != null) {
            if (x.getLimit().getOffset() instanceof SQLVariantRefExpr) {
                SQLVariantRefExpr expr = (SQLVariantRefExpr) x.getLimit().getOffset();
                BindVarCondition bindVarCondition = new BindVarCondition();
                bindVarCondition.setTableName(getCurrentTable());
                bindVarCondition.setColumnName(OFFSET);
                bindVarCondition.setOperator(SQLBinaryOperator.Equality.name);
                bindVarCondition.setIndex(expr.getIndex());

                super.getBindVarConditions().add(bindVarCondition);
                this.limits.add(bindVarCondition);
            } else if (x.getLimit().getOffset() instanceof SQLIntegerExpr) {
                SQLIntegerExpr expr = (SQLIntegerExpr) x.getLimit().getOffset();
                BindVarCondition bindVarCondition = new BindVarCondition();
                bindVarCondition.setTableName(getCurrentTable());
                bindVarCondition.setColumnName(OFFSET);
                bindVarCondition.setOperator(SQLBinaryOperator.Equality.name);
                bindVarCondition.setValue((Comparable<?>) expr.getNumber());
                this.limits.add(bindVarCondition);
            }
            if (x.getLimit().getRowCount() instanceof SQLVariantRefExpr) {
                SQLVariantRefExpr expr = (SQLVariantRefExpr) x.getLimit().getRowCount();
                BindVarCondition bindVarCondition = new BindVarCondition();
                bindVarCondition.setTableName(getCurrentTable());
                bindVarCondition.setColumnName(ROWCOUNT);
                bindVarCondition.setOperator(SQLBinaryOperator.Equality.name);
                bindVarCondition.setIndex(expr.getIndex());

                super.getBindVarConditions().add(bindVarCondition);
                this.limits.add(bindVarCondition);
            } else if (x.getLimit().getRowCount() instanceof SQLIntegerExpr) {
                SQLIntegerExpr expr = (SQLIntegerExpr) x.getLimit().getRowCount();
                BindVarCondition bindVarCondition = new BindVarCondition();
                bindVarCondition.setTableName(getCurrentTable());
                bindVarCondition.setColumnName(ROWCOUNT);
                bindVarCondition.setOperator(SQLBinaryOperator.Equality.name);
                bindVarCondition.setValue((Comparable<?>) expr.getNumber());
                this.limits.add(bindVarCondition);
            }
        }
        return result;

    }

    public boolean visit(MySqlUpdateStatement x) {
        setAliasMap();

        setMode(x, Mode.Update);

        if ((x.getLimit() != null) && (x.getLimit().getOffset() instanceof SQLVariantRefExpr)) {
            SQLVariantRefExpr expr = (SQLVariantRefExpr) x.getLimit().getOffset();
            BindVarCondition bindVarCondition = new BindVarCondition();
            bindVarCondition.setTableName(getCurrentTable());
            bindVarCondition.setColumnName(OFFSET);
            bindVarCondition.setOperator(SQLBinaryOperator.Equality.name);
            bindVarCondition.setIndex(expr.getIndex());

            super.getBindVarConditions().add(bindVarCondition);
            this.limits.add(bindVarCondition);
        }
        if ((x.getLimit() != null) && (x.getLimit().getOffset() instanceof SQLIntegerExpr)) {
            SQLIntegerExpr expr = (SQLIntegerExpr) x.getLimit().getOffset();
            BindVarCondition bindVarCondition = new BindVarCondition();
            bindVarCondition.setTableName(getCurrentTable());
            bindVarCondition.setColumnName(OFFSET);
            bindVarCondition.setOperator(SQLBinaryOperator.Equality.name);
            bindVarCondition.setValue((Comparable<?>) expr.getNumber());
            this.limits.add(bindVarCondition);
        }
        if ((x.getLimit() != null) && (x.getLimit().getRowCount() instanceof SQLVariantRefExpr)) {
            SQLVariantRefExpr expr = (SQLVariantRefExpr) x.getLimit().getRowCount();
            BindVarCondition bindVarCondition = new BindVarCondition();
            bindVarCondition.setTableName(getCurrentTable());
            bindVarCondition.setColumnName(ROWCOUNT);
            bindVarCondition.setOperator(SQLBinaryOperator.Equality.name);
            bindVarCondition.setIndex(expr.getIndex());

            super.getBindVarConditions().add(bindVarCondition);
            this.limits.add(bindVarCondition);
        }
        if ((x.getLimit() != null) && (x.getLimit().getRowCount() instanceof SQLIntegerExpr)) {
            SQLIntegerExpr expr = (SQLIntegerExpr) x.getLimit().getRowCount();
            BindVarCondition bindVarCondition = new BindVarCondition();
            bindVarCondition.setTableName(getCurrentTable());
            bindVarCondition.setColumnName(ROWCOUNT);
            bindVarCondition.setOperator(SQLBinaryOperator.Equality.name);
            bindVarCondition.setValue((Comparable<?>) expr.getNumber());
            this.limits.add(bindVarCondition);
        }
        return visit((SQLUpdateStatement) x);
    }

    // DUAL
    public boolean visit(MySqlDeleteStatement x) {
        setAliasMap();

        setMode(x, Mode.Delete);

        accept(x.getFrom());
        accept(x.getUsing());
        x.getTableSource().accept(this);

        if (x.getTableSource() instanceof SQLExprTableSource) {
            SQLName tableName = (SQLName) ((SQLExprTableSource) x.getTableSource()).getExpr();
            String ident = tableName.toString();
            setCurrentTable(x, ident);

            TableStat stat = this.getTableStat(ident);
            stat.incrementDeleteCount();
        }

        accept(x.getWhere());

        accept(x.getOrderBy());
        accept(x.getLimit());

        return false;
    }

    public void endVisit(MySqlDeleteStatement x) {
        setAliasMap(null);
    }

    public void endVisit(MySqlInsertStatement x) {
        setModeOrigin(x);
    }

    /** 
     * @see com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor#visit(com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlInsertStatement)
     */
    public boolean visit(MySqlInsertStatement x) {
        setMode(x, Mode.Insert);

        setAliasMap();

        if (x.getTableName() instanceof SQLIdentifierExpr) {
            String ident = ((SQLIdentifierExpr) x.getTableName()).getName();
            setCurrentTable(x, ident);

            TableStat stat = getTableStat(ident);
            stat.incrementInsertCount();

            Map<String, String> aliasMap = getAliasMap();
            if (aliasMap != null) {
                if (x.getAlias() != null) {
                    aliasMap.put(x.getAlias(), ident);
                }
                aliasMap.put(ident, ident);
            }
        }

        accept(x.getColumns());
        accept(x.getValuesList());
        //��insert�л�ȡ�󶨲���.
        acceptInsertValueClauses(x.getColumns(), x.getValuesList());
        accept(x.getQuery());
        accept(x.getDuplicateKeyUpdate());

        return false;
    }

    public boolean visit(MySqlBooleanExpr x) {

        return true;
    }

    public void endVisit(MySqlBooleanExpr x) {

    }

    public boolean visit(Limit x) {

        return true;
    }

    public void endVisit(Limit x) {

    }

    public boolean visit(MySqlTableIndex x) {

        return true;
    }

    public void endVisit(MySqlTableIndex x) {

    }

    public boolean visit(MySqlKey x) {

        return true;
    }

    public void endVisit(MySqlKey x) {

    }

    public boolean visit(MySqlPrimaryKey x) {

        return true;
    }

    public void endVisit(MySqlPrimaryKey x) {

    }

    public void endVisit(MySqlIntervalExpr x) {

    }

    public boolean visit(MySqlIntervalExpr x) {

        return true;
    }

    public void endVisit(MySqlExtractExpr x) {

    }

    public boolean visit(MySqlExtractExpr x) {

        return true;
    }

    public void endVisit(MySqlMatchAgainstExpr x) {

    }

    public boolean visit(MySqlMatchAgainstExpr x) {

        return true;
    }

    public void endVisit(MySqlBinaryExpr x) {

    }

    public boolean visit(MySqlBinaryExpr x) {

        return true;
    }

    public void endVisit(MySqlPrepareStatement x) {

    }

    public boolean visit(MySqlPrepareStatement x) {

        return true;
    }

    public void endVisit(MySqlExecuteStatement x) {

    }

    public boolean visit(MySqlExecuteStatement x) {

        return true;
    }

    public void endVisit(MySqlLoadDataInFileStatement x) {

    }

    public boolean visit(MySqlLoadDataInFileStatement x) {

        return true;
    }

    public void endVisit(MySqlLoadXmlStatement x) {

    }

    public boolean visit(MySqlLoadXmlStatement x) {

        return true;
    }

    public void endVisit(MySqlReplicateStatement x) {

    }

    public boolean visit(MySqlReplicateStatement x) {

        return true;
    }

    public void endVisit(MySqlSelectGroupBy x) {

    }

    public boolean visit(MySqlSelectGroupBy x) {

        return true;
    }

    public void endVisit(MySqlStartTransactionStatement x) {

    }

    public boolean visit(MySqlStartTransactionStatement x) {

        return true;
    }

    public void endVisit(MySqlCommitStatement x) {

    }

    public boolean visit(MySqlCommitStatement x) {

        return true;
    }

    public void endVisit(MySqlRollbackStatement x) {

    }

    public boolean visit(MySqlRollbackStatement x) {

        return true;
    }

    public void endVisit(MySqlShowColumnsStatement x) {

    }

    public boolean visit(MySqlShowColumnsStatement x) {
        return true;
    }

    public void endVisit(MySqlShowTablesStatement x) {

    }

    public boolean visit(MySqlShowTablesStatement x) {
        return true;
    }

    public void endVisit(MySqlShowDatabasesStatement x) {

    }

    public boolean visit(MySqlShowDatabasesStatement x) {
        return true;
    }

    public void endVisit(MySqlShowWarningsStatement x) {

    }

    public boolean visit(MySqlShowWarningsStatement x) {
        return true;
    }

    public void endVisit(MySqlShowStatusStatement x) {

    }

    public boolean visit(MySqlShowStatusStatement x) {
        return true;
    }

    public void endVisit(CobarShowStatus x) {

    }

    public boolean visit(CobarShowStatus x) {
        return true;
    }

    public void endVisit(MySqlKillStatement x) {

    }

    public boolean visit(MySqlKillStatement x) {
        return true;
    }

    public void endVisit(MySqlBinlogStatement x) {

    }

    public boolean visit(MySqlBinlogStatement x) {
        return true;
    }

    public void endVisit(MySqlResetStatement x) {

    }

    public boolean visit(MySqlResetStatement x) {
        return true;
    }

    public void endVisit(MySqlCreateUserStatement x) {

    }

    public boolean visit(MySqlCreateUserStatement x) {
        return true;
    }

    public void endVisit(UserSpecification x) {

    }

    public boolean visit(UserSpecification x) {
        return true;
    }

    public void endVisit(MySqlDropUser x) {

    }

    public boolean visit(MySqlDropUser x) {
        return true;
    }

    public void endVisit(MySqlDropTableStatement x) {

    }

    public boolean visit(MySqlDropTableStatement x) {
        return visit((SQLDropTableStatement) x);
    }

    public void endVisit(MySqlPartitionByKey x) {

    }

    public boolean visit(MySqlPartitionByKey x) {
        accept(x.getColumns());
        return false;
    }

    public void endVisit(MySqlSelectQueryBlock x) {

    }

    public boolean visit(MySqlOutFileExpr x) {
        return false;
    }

    public void endVisit(MySqlOutFileExpr x) {

    }

    public boolean visit(MySqlDescribeStatement x) {
        getTableStat(x.getObject().toString());
        return false;
    }

    public void endVisit(MySqlDescribeStatement x) {

    }

    public void endVisit(MySqlUpdateStatement x) {

    }

    public boolean visit(MySqlSetTransactionIsolationLevelStatement x) {
        return false;
    }

    public void endVisit(MySqlSetTransactionIsolationLevelStatement x) {

    }

    public boolean visit(MySqlSetNamesStatement x) {
        return false;
    }

    public void endVisit(MySqlSetNamesStatement x) {

    }

    public boolean visit(MySqlSetCharSetStatement x) {
        return false;
    }

    public void endVisit(MySqlSetCharSetStatement x) {

    }

    public boolean visit(MySqlShowAuthorsStatement x) {
        return false;
    }

    public void endVisit(MySqlShowAuthorsStatement x) {

    }

    public boolean visit(MySqlShowBinaryLogsStatement x) {
        return false;
    }

    public void endVisit(MySqlShowBinaryLogsStatement x) {

    }

    public boolean visit(MySqlShowMasterLogsStatement x) {
        return false;
    }

    public void endVisit(MySqlShowMasterLogsStatement x) {

    }

    public boolean visit(MySqlShowCollationStatement x) {
        return false;
    }

    public void endVisit(MySqlShowCollationStatement x) {

    }

    public boolean visit(MySqlShowBinLogEventsStatement x) {
        return false;
    }

    public void endVisit(MySqlShowBinLogEventsStatement x) {

    }

    public boolean visit(MySqlShowCharacterSetStatement x) {
        return false;
    }

    public void endVisit(MySqlShowCharacterSetStatement x) {

    }

    public boolean visit(MySqlShowContributorsStatement x) {
        return false;
    }

    public void endVisit(MySqlShowContributorsStatement x) {

    }

    public boolean visit(MySqlShowCreateDatabaseStatement x) {
        return false;
    }

    public void endVisit(MySqlShowCreateDatabaseStatement x) {

    }

    public boolean visit(MySqlShowCreateEventStatement x) {
        return false;
    }

    public void endVisit(MySqlShowCreateEventStatement x) {

    }

    public boolean visit(MySqlShowCreateFunctionStatement x) {
        return false;
    }

    public void endVisit(MySqlShowCreateFunctionStatement x) {

    }

    public boolean visit(MySqlShowCreateProcedureStatement x) {
        return false;
    }

    public void endVisit(MySqlShowCreateProcedureStatement x) {

    }

    public boolean visit(MySqlShowCreateTableStatement x) {
        return false;
    }

    public void endVisit(MySqlShowCreateTableStatement x) {

    }

    public boolean visit(MySqlShowCreateTriggerStatement x) {
        return false;
    }

    public void endVisit(MySqlShowCreateTriggerStatement x) {

    }

    public boolean visit(MySqlShowCreateViewStatement x) {
        return false;
    }

    public void endVisit(MySqlShowCreateViewStatement x) {

    }

    public boolean visit(MySqlShowEngineStatement x) {
        return false;
    }

    public void endVisit(MySqlShowEngineStatement x) {

    }

    public boolean visit(MySqlShowEnginesStatement x) {
        return false;
    }

    public void endVisit(MySqlShowEnginesStatement x) {

    }

    public boolean visit(MySqlShowErrorsStatement x) {
        return false;
    }

    public void endVisit(MySqlShowErrorsStatement x) {

    }

    public boolean visit(MySqlShowEventsStatement x) {
        return false;
    }

    public void endVisit(MySqlShowEventsStatement x) {

    }

    public boolean visit(MySqlShowFunctionCodeStatement x) {
        return false;
    }

    public void endVisit(MySqlShowFunctionCodeStatement x) {

    }

    public boolean visit(MySqlShowFunctionStatusStatement x) {
        return false;
    }

    public void endVisit(MySqlShowFunctionStatusStatement x) {

    }

    public boolean visit(MySqlShowGrantsStatement x) {
        return false;
    }

    public void endVisit(MySqlShowGrantsStatement x) {

    }

    public boolean visit(MySqlUserName x) {
        return false;
    }

    public void endVisit(MySqlUserName x) {

    }

    public boolean visit(MySqlShowIndexesStatement x) {
        return false;
    }

    public void endVisit(MySqlShowIndexesStatement x) {

    }

    public boolean visit(MySqlShowKeysStatement x) {
        return false;
    }

    public void endVisit(MySqlShowKeysStatement x) {

    }

    public boolean visit(MySqlShowMasterStatusStatement x) {
        return false;
    }

    public void endVisit(MySqlShowMasterStatusStatement x) {

    }

    public boolean visit(MySqlShowOpenTablesStatement x) {
        return false;
    }

    public void endVisit(MySqlShowOpenTablesStatement x) {

    }

    public boolean visit(MySqlShowPluginsStatement x) {
        return false;
    }

    public void endVisit(MySqlShowPluginsStatement x) {

    }

    public boolean visit(MySqlShowPrivilegesStatement x) {
        return false;
    }

    public void endVisit(MySqlShowPrivilegesStatement x) {

    }

    public boolean visit(MySqlShowProcedureCodeStatement x) {
        return false;
    }

    public void endVisit(MySqlShowProcedureCodeStatement x) {

    }

    public boolean visit(MySqlShowProcedureStatusStatement x) {
        return false;
    }

    public void endVisit(MySqlShowProcedureStatusStatement x) {

    }

    public boolean visit(MySqlShowProcessListStatement x) {
        return false;
    }

    public void endVisit(MySqlShowProcessListStatement x) {

    }

    public boolean visit(MySqlShowProfileStatement x) {
        return false;
    }

    public void endVisit(MySqlShowProfileStatement x) {

    }

    public boolean visit(MySqlShowProfilesStatement x) {
        return false;
    }

    public void endVisit(MySqlShowProfilesStatement x) {

    }

    public boolean visit(MySqlShowRelayLogEventsStatement x) {
        return false;
    }

    public void endVisit(MySqlShowRelayLogEventsStatement x) {

    }

    public boolean visit(MySqlShowSlaveHostsStatement x) {
        return false;
    }

    public void endVisit(MySqlShowSlaveHostsStatement x) {

    }

    public boolean visit(MySqlShowSlaveStatusStatement x) {
        return false;
    }

    public void endVisit(MySqlShowSlaveStatusStatement x) {

    }

    public boolean visit(MySqlShowTableStatusStatement x) {
        return false;
    }

    public void endVisit(MySqlShowTableStatusStatement x) {

    }

    public boolean visit(MySqlShowTriggersStatement x) {
        return false;
    }

    public void endVisit(MySqlShowTriggersStatement x) {

    }

    public boolean visit(MySqlShowVariantsStatement x) {
        return false;
    }

    public void endVisit(MySqlShowVariantsStatement x) {

    }

    public boolean visit(MySqlAlterTableStatement x) {
        return false;
    }

    public void endVisit(MySqlAlterTableStatement x) {

    }

    public boolean visit(MySqlAlterTableAddColumn x) {
        return false;
    }

    public void endVisit(MySqlAlterTableAddColumn x) {

    }

    public boolean visit(MySqlCreateIndexStatement x) {
        return false;
    }

    public void endVisit(MySqlCreateIndexStatement x) {

    }

    public boolean visit(MySqlRenameTableStatement.Item x) {
        return false;
    }

    public void endVisit(MySqlRenameTableStatement.Item x) {

    }

    public boolean visit(MySqlRenameTableStatement x) {
        return false;
    }

    public void endVisit(MySqlRenameTableStatement x) {

    }

    public boolean visit(MySqlDropViewStatement x) {
        return true;
    }

    public void endVisit(MySqlDropViewStatement x) {

    }

    public boolean visit(MySqlUnionQuery x) {
        return visit((SQLUnionQuery) x);
    }

    public void endVisit(MySqlUnionQuery x) {

    }

    public boolean visit(MySqlUseIndexHint x) {
        return false;
    }

    public void endVisit(MySqlUseIndexHint x) {

    }

    public boolean visit(MySqlIgnoreIndexHint x) {
        return false;
    }

    public void endVisit(MySqlIgnoreIndexHint x) {

    }

    public boolean visit(MySqlLockTableStatement x) {
        return false;
    }

    public void endVisit(MySqlLockTableStatement x) {

    }

    public boolean visit(MySqlUnlockTablesStatement x) {
        return false;
    }

    public void endVisit(MySqlUnlockTablesStatement x) {

    }

    public boolean visit(MySqlForceIndexHint x) {
        return false;
    }

    public void endVisit(MySqlForceIndexHint x) {

    }

    public boolean visit(MySqlAlterTableChangeColumn x) {
        return false;
    }

    public void endVisit(MySqlAlterTableChangeColumn x) {

    }

    public boolean visit(MySqlAlterTableCharacter x) {
        return false;
    }

    public void endVisit(MySqlAlterTableCharacter x) {

    }

    public boolean visit(MySqlAlterTableAddIndex x) {
        return false;
    }

    public void endVisit(MySqlAlterTableAddIndex x) {

    }

    public boolean visit(MySqlAlterTableOption x) {
        return false;
    }

    public void endVisit(MySqlAlterTableOption x) {

    }

    public boolean visit(MySqlCreateTableStatement x) {
        return super.visit((SQLCreateTableStatement) x);
    }

    public void endVisit(MySqlCreateTableStatement x) {

    }

    public boolean visit(MySqlHelpStatement x) {
        return false;
    }

    public void endVisit(MySqlHelpStatement x) {

    }

    public boolean visit(MySqlCharExpr x) {
        return false;
    }

    public void endVisit(MySqlCharExpr x) {

    }

    public boolean visit(MySqlAlterTableAddUnique x) {
        return false;
    }

    public void endVisit(MySqlAlterTableAddUnique x) {

    }

    public Set<BindVarCondition> getLimits() {
        return limits;
    }
}
