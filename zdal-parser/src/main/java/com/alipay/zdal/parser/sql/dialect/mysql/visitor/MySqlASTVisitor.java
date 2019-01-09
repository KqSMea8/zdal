/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.visitor;

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
import com.alipay.zdal.parser.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author 伯牙
 * @version $Id: MySqlASTVisitor.java, v 0.1 2012-11-17 下午3:40:38 Exp $
 */
public interface MySqlASTVisitor extends SQLASTVisitor {

    boolean visit(MySqlBooleanExpr x);

    void endVisit(MySqlBooleanExpr x);

    boolean visit(MySqlSelectQueryBlock.Limit x);

    void endVisit(MySqlSelectQueryBlock.Limit x);

    boolean visit(MySqlTableIndex x);

    void endVisit(MySqlTableIndex x);

    boolean visit(MySqlKey x);

    void endVisit(MySqlKey x);

    boolean visit(MySqlPrimaryKey x);

    void endVisit(MySqlPrimaryKey x);

    void endVisit(MySqlIntervalExpr x);

    boolean visit(MySqlIntervalExpr x);

    void endVisit(MySqlExtractExpr x);

    boolean visit(MySqlExtractExpr x);

    void endVisit(MySqlMatchAgainstExpr x);

    boolean visit(MySqlMatchAgainstExpr x);

    void endVisit(MySqlBinaryExpr x);

    boolean visit(MySqlBinaryExpr x);

    void endVisit(MySqlPrepareStatement x);

    boolean visit(MySqlPrepareStatement x);

    void endVisit(MySqlExecuteStatement x);

    boolean visit(MySqlExecuteStatement x);

    void endVisit(MySqlDeleteStatement x);

    boolean visit(MySqlDeleteStatement x);

    void endVisit(MySqlInsertStatement x);

    boolean visit(MySqlInsertStatement x);

    void endVisit(MySqlLoadDataInFileStatement x);

    boolean visit(MySqlLoadDataInFileStatement x);

    void endVisit(MySqlLoadXmlStatement x);

    boolean visit(MySqlLoadXmlStatement x);

    void endVisit(MySqlReplicateStatement x);

    boolean visit(MySqlReplicateStatement x);

    void endVisit(MySqlSelectGroupBy x);

    boolean visit(MySqlSelectGroupBy x);

    void endVisit(MySqlStartTransactionStatement x);

    boolean visit(MySqlStartTransactionStatement x);

    void endVisit(MySqlCommitStatement x);

    boolean visit(MySqlCommitStatement x);

    void endVisit(MySqlRollbackStatement x);

    boolean visit(MySqlRollbackStatement x);

    void endVisit(MySqlShowColumnsStatement x);

    boolean visit(MySqlShowColumnsStatement x);

    void endVisit(MySqlShowTablesStatement x);

    boolean visit(MySqlShowTablesStatement x);

    void endVisit(MySqlShowDatabasesStatement x);

    boolean visit(MySqlShowDatabasesStatement x);

    void endVisit(MySqlShowWarningsStatement x);

    boolean visit(MySqlShowWarningsStatement x);

    void endVisit(MySqlShowStatusStatement x);

    boolean visit(MySqlShowStatusStatement x);

    void endVisit(MySqlShowAuthorsStatement x);

    boolean visit(MySqlShowAuthorsStatement x);

    void endVisit(CobarShowStatus x);

    boolean visit(CobarShowStatus x);

    void endVisit(MySqlKillStatement x);

    boolean visit(MySqlKillStatement x);

    void endVisit(MySqlBinlogStatement x);

    boolean visit(MySqlBinlogStatement x);

    void endVisit(MySqlResetStatement x);

    boolean visit(MySqlResetStatement x);

    void endVisit(MySqlDropUser x);

    boolean visit(MySqlDropUser x);

    void endVisit(MySqlCreateUserStatement x);

    boolean visit(MySqlCreateUserStatement x);

    void endVisit(MySqlCreateUserStatement.UserSpecification x);

    boolean visit(MySqlCreateUserStatement.UserSpecification x);

    void endVisit(MySqlDropTableStatement x);

    boolean visit(MySqlDropTableStatement x);

    void endVisit(MySqlPartitionByKey x);

    boolean visit(MySqlPartitionByKey x);

    boolean visit(MySqlSelectQueryBlock x);

    void endVisit(MySqlSelectQueryBlock x);

    boolean visit(MySqlOutFileExpr x);

    void endVisit(MySqlOutFileExpr x);

    boolean visit(MySqlDescribeStatement x);

    void endVisit(MySqlDescribeStatement x);

    boolean visit(MySqlUpdateStatement x);

    void endVisit(MySqlUpdateStatement x);

    boolean visit(MySqlSetTransactionIsolationLevelStatement x);

    void endVisit(MySqlSetTransactionIsolationLevelStatement x);

    boolean visit(MySqlSetNamesStatement x);

    void endVisit(MySqlSetNamesStatement x);

    boolean visit(MySqlSetCharSetStatement x);

    void endVisit(MySqlSetCharSetStatement x);

    boolean visit(MySqlShowBinaryLogsStatement x);

    void endVisit(MySqlShowBinaryLogsStatement x);

    boolean visit(MySqlShowMasterLogsStatement x);

    void endVisit(MySqlShowMasterLogsStatement x);

    boolean visit(MySqlShowCharacterSetStatement x);

    void endVisit(MySqlShowCharacterSetStatement x);

    boolean visit(MySqlShowCollationStatement x);

    void endVisit(MySqlShowCollationStatement x);

    boolean visit(MySqlShowBinLogEventsStatement x);

    void endVisit(MySqlShowBinLogEventsStatement x);

    boolean visit(MySqlShowContributorsStatement x);

    void endVisit(MySqlShowContributorsStatement x);

    boolean visit(MySqlShowCreateDatabaseStatement x);

    void endVisit(MySqlShowCreateDatabaseStatement x);

    boolean visit(MySqlShowCreateEventStatement x);

    void endVisit(MySqlShowCreateEventStatement x);

    boolean visit(MySqlShowCreateFunctionStatement x);

    void endVisit(MySqlShowCreateFunctionStatement x);

    boolean visit(MySqlShowCreateProcedureStatement x);

    void endVisit(MySqlShowCreateProcedureStatement x);

    boolean visit(MySqlShowCreateTableStatement x);

    void endVisit(MySqlShowCreateTableStatement x);

    boolean visit(MySqlShowCreateTriggerStatement x);

    void endVisit(MySqlShowCreateTriggerStatement x);

    boolean visit(MySqlShowCreateViewStatement x);

    void endVisit(MySqlShowCreateViewStatement x);

    boolean visit(MySqlShowEngineStatement x);

    void endVisit(MySqlShowEngineStatement x);

    boolean visit(MySqlShowEnginesStatement x);

    void endVisit(MySqlShowEnginesStatement x);

    boolean visit(MySqlShowErrorsStatement x);

    void endVisit(MySqlShowErrorsStatement x);

    boolean visit(MySqlShowEventsStatement x);

    void endVisit(MySqlShowEventsStatement x);

    boolean visit(MySqlShowFunctionCodeStatement x);

    void endVisit(MySqlShowFunctionCodeStatement x);

    boolean visit(MySqlShowFunctionStatusStatement x);

    void endVisit(MySqlShowFunctionStatusStatement x);

    boolean visit(MySqlShowGrantsStatement x);

    void endVisit(MySqlShowGrantsStatement x);

    boolean visit(MySqlUserName x);

    void endVisit(MySqlUserName x);

    boolean visit(MySqlShowIndexesStatement x);

    void endVisit(MySqlShowIndexesStatement x);

    boolean visit(MySqlShowKeysStatement x);

    void endVisit(MySqlShowKeysStatement x);

    boolean visit(MySqlShowMasterStatusStatement x);

    void endVisit(MySqlShowMasterStatusStatement x);

    boolean visit(MySqlShowOpenTablesStatement x);

    void endVisit(MySqlShowOpenTablesStatement x);

    boolean visit(MySqlShowPluginsStatement x);

    void endVisit(MySqlShowPluginsStatement x);

    boolean visit(MySqlShowPrivilegesStatement x);

    void endVisit(MySqlShowPrivilegesStatement x);

    boolean visit(MySqlShowProcedureCodeStatement x);

    void endVisit(MySqlShowProcedureCodeStatement x);

    boolean visit(MySqlShowProcedureStatusStatement x);

    void endVisit(MySqlShowProcedureStatusStatement x);

    boolean visit(MySqlShowProcessListStatement x);

    void endVisit(MySqlShowProcessListStatement x);

    boolean visit(MySqlShowProfileStatement x);

    void endVisit(MySqlShowProfileStatement x);

    boolean visit(MySqlShowProfilesStatement x);

    void endVisit(MySqlShowProfilesStatement x);

    boolean visit(MySqlShowRelayLogEventsStatement x);

    void endVisit(MySqlShowRelayLogEventsStatement x);

    boolean visit(MySqlShowSlaveHostsStatement x);

    void endVisit(MySqlShowSlaveHostsStatement x);

    boolean visit(MySqlShowSlaveStatusStatement x);

    void endVisit(MySqlShowSlaveStatusStatement x);

    boolean visit(MySqlShowTableStatusStatement x);

    void endVisit(MySqlShowTableStatusStatement x);

    boolean visit(MySqlShowTriggersStatement x);

    void endVisit(MySqlShowTriggersStatement x);

    boolean visit(MySqlShowVariantsStatement x);

    void endVisit(MySqlShowVariantsStatement x);

    boolean visit(MySqlAlterTableStatement x);

    void endVisit(MySqlAlterTableStatement x);

    boolean visit(MySqlAlterTableAddColumn x);

    void endVisit(MySqlAlterTableAddColumn x);

    boolean visit(MySqlCreateIndexStatement x);

    void endVisit(MySqlCreateIndexStatement x);

    boolean visit(MySqlRenameTableStatement.Item x);

    void endVisit(MySqlRenameTableStatement.Item x);

    boolean visit(MySqlRenameTableStatement x);

    void endVisit(MySqlRenameTableStatement x);

    boolean visit(MySqlDropViewStatement x);

    void endVisit(MySqlDropViewStatement x);

    boolean visit(MySqlUnionQuery x);

    void endVisit(MySqlUnionQuery x);

    boolean visit(MySqlUseIndexHint x);

    void endVisit(MySqlUseIndexHint x);

    boolean visit(MySqlIgnoreIndexHint x);

    void endVisit(MySqlIgnoreIndexHint x);

    boolean visit(MySqlLockTableStatement x);

    void endVisit(MySqlLockTableStatement x);

    boolean visit(MySqlUnlockTablesStatement x);

    void endVisit(MySqlUnlockTablesStatement x);

    boolean visit(MySqlForceIndexHint x);

    void endVisit(MySqlForceIndexHint x);

    boolean visit(MySqlAlterTableChangeColumn x);

    void endVisit(MySqlAlterTableChangeColumn x);

    boolean visit(MySqlAlterTableCharacter x);

    void endVisit(MySqlAlterTableCharacter x);

    boolean visit(MySqlAlterTableAddIndex x);

    void endVisit(MySqlAlterTableAddIndex x);

    boolean visit(MySqlAlterTableOption x);

    void endVisit(MySqlAlterTableOption x);

    boolean visit(MySqlCreateTableStatement x);

    void endVisit(MySqlCreateTableStatement x);

    boolean visit(MySqlHelpStatement x);

    void endVisit(MySqlHelpStatement x);

    boolean visit(MySqlCharExpr x);

    void endVisit(MySqlCharExpr x);

    boolean visit(MySqlAlterTableAddUnique x);

    void endVisit(MySqlAlterTableAddUnique x);
}
