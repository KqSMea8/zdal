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
 * @version $Id: OracleAlterTableTest3.java, v 0.1 2012-5-17 ����10:13:37 xiaoqing.zhouxq Exp $
 */
public class OracleAlterTableTest3 extends OracleTest {

    public void test_0() throws Exception {
        String sql = //
        "alter table WRH$_ROWCACHE_SUMMARY split partition WRH$_ROWCAC_1870432296_13862 " //
                + "   at (1870432296,13910) " //
                + "   into (partition WRH$_ROWCAC_1870432296_13862 tablespace SYSAUX, partition WRH$_ROWCAC_1870432296_13910 tablespace SYSAUX" //
                + ") " + //
                "   update indexes";

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
            new TableStat.Name("WRH$_ROWCACHE_SUMMARY")));

        Assert.assertEquals(0, visitor.getColumns().size());

        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "*")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "YEAR")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
