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
 * @version $Id: OracleCreateIndexTest3.java, v 0.1 2012-5-17 ����10:15:33 xiaoqing.zhouxq Exp $
 */
public class OracleCreateIndexTest3 extends OracleTest {

    public void test_0() throws Exception {
        String sql = //
        "create index PRODUCT_ADDITION_NCID_IND on PRODUCT_ADDITION(NEW_CLUSTER_ID,GMT_LAST_FEEDBACK) tablespace APPINDX4M online parallel 4";

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

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("PRODUCT_ADDITION")));

        Assert.assertEquals(2, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("PRODUCT_ADDITION", "NEW_CLUSTER_ID")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("PRODUCT_ADDITION", "GMT_LAST_FEEDBACK")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "YEAR")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
