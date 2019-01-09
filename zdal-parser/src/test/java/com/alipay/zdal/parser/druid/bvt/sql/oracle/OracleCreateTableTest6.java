package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.OracleTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleCreateTableTest6.java, v 0.1 2012-5-17 ����10:16:17 xiaoqing.zhouxq Exp $
 */
public class OracleCreateTableTest6 extends OracleTest {

    public void test_0() throws Exception {
        String sql = //
        "CREATE GLOBAL TEMPORARY TABLE \"SYS\".\"SYS_TEMP_0FD9D66FD_93E068F3\" (\"C0\" NUMBER,\"C1\" NUMBER ) IN_MEMORY_METADATA CURSOR_SPECIFIC_SEGMENT STORAGE (OBJNO 4254951165 ) NOPARALLEL";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(1, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(
            new TableStat.Name("SYS.SYS_TEMP_0FD9D66FD_93E068F3")));

        Assert.assertEquals(2, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("SYS.SYS_TEMP_0FD9D66FD_93E068F3", "C0")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("SYS.SYS_TEMP_0FD9D66FD_93E068F3", "C1")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
