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
 * @version $Id: OracleBlockTest8.java, v 0.1 2012-5-17 ����10:15:14 xiaoqing.zhouxq Exp $
 */
public class OracleBlockTest8 extends OracleTest {

    public void test_0() throws Exception {
        String sql = "DECLARE" + //
                     "  daily_order_total    NUMBER(12,2);" + //
                     "  weekly_order_total   NUMBER(12,2); " + //
                     "  monthly_order_total  NUMBER(12,2);" + //
                     "BEGIN" + //
                     "   COMMIT; -- end previous transaction\n" + //
                     "   SET TRANSACTION READ ONLY NAME 'Calculate Order Totals';" + //
                     "" + //
                     "   SELECT SUM (order_total)" + //
                     "   INTO daily_order_total" + //
                     "   FROM orders" + //
                     "   WHERE order_date = SYSDATE;" + //
                     "" + //
                     "   SELECT SUM (order_total)" + //
                     "   INTO weekly_order_total" + //
                     "   FROM orders" + //
                     "   WHERE order_date = SYSDATE - 7;" + //
                     "" + //
                     "   SELECT SUM (order_total)" + //
                     "   INTO monthly_order_total" + //
                     "   FROM orders" + //
                     "   WHERE order_date = SYSDATE - 30;" + //
                     "" + //
                     "   COMMIT; -- ends read-only transaction\n" + //
                     "END;"; //

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        for (SQLStatement statement : statementList) {
            statement.accept(visitor);
        }

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(1, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("orders")));

        Assert.assertEquals(2, visitor.getColumns().size());
        Assert.assertEquals(1, visitor.getConditions().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("orders", "order_total")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("orders", "order_date")));
    }
}
