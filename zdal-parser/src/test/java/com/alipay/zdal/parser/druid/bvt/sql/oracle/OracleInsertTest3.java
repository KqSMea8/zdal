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
 * @version $Id: OracleInsertTest3.java, v 0.1 2012-5-17 ����10:17:33 xiaoqing.zhouxq Exp $
 */
public class OracleInsertTest3 extends OracleTest {

    public void test_0() throws Exception {
        String sql = "INSERT INTO raises" + //
                     "   SELECT employee_id, salary*1.1 FROM employees" + //
                     "   WHERE commission_pct > .2" + //
                     "   LOG ERRORS INTO errlog ('my_bad') REJECT LIMIT 10;";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("--------------------------------");

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("raises")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));

        Assert.assertEquals(2, visitor.getTables().size());
        Assert.assertEquals(3, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("employees", "employee_id")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("employees", "salary")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("employees", "commission_pct")));
    }

}
