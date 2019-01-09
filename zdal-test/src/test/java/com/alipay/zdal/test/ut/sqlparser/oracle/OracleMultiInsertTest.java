package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleMultiInsertTest   {
@Test
    public void test_0() throws Exception {
        String sql = "INSERT ALL" + //
                     "      INTO sales (prod_id, cust_id, time_id, amount)" + //
                     "      VALUES (product_id, customer_id, weekly_start_date, sales_sun)" + //
                     "      INTO sales (prod_id, cust_id, time_id, amount)" + //
                     "      VALUES (product_id, customer_id, weekly_start_date+1, sales_mon)" + //
                     "      INTO sales (prod_id, cust_id, time_id, amount)" + //
                     "      VALUES (product_id, customer_id, weekly_start_date+2, sales_tue)" + //
                     "      INTO sales (prod_id, cust_id, time_id, amount)" + //
                     "      VALUES (product_id, customer_id, weekly_start_date+3, sales_wed)" + //
                     "      INTO sales (prod_id, cust_id, time_id, amount)" + //
                     "      VALUES (product_id, customer_id, weekly_start_date+4, sales_thu)" + //
                     "      INTO sales (prod_id, cust_id, time_id, amount)" + //
                     "      VALUES (product_id, customer_id, weekly_start_date+5, sales_fri)" + //
                     "      INTO sales (prod_id, cust_id, time_id, amount)" + //
                     "      VALUES (product_id, customer_id, weekly_start_date+6, sales_sat)" + //
                     "   SELECT product_id, customer_id, weekly_start_date, sales_sun," + //
                     "      sales_mon, sales_tue, sales_wed, sales_thu, sales_fri, sales_sat" + //
                     "      FROM sales_input_table;"; //

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
       

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("sales")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("sales_input_table")));

        Assert.assertEquals(2, visitor.getTables().size());
        Assert.assertEquals(14, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales", "prod_id")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales", "cust_id")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales", "time_id")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales", "amount")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales_input_table", "product_id")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales_input_table", "customer_id")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales_input_table", "weekly_start_date")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales_input_table", "sales_sun")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales_input_table", "sales_mon")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales_input_table", "sales_tue")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales_input_table", "sales_wed")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales_input_table", "sales_thu")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales_input_table", "sales_fri")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("sales_input_table", "sales_sat")));

    }

}
