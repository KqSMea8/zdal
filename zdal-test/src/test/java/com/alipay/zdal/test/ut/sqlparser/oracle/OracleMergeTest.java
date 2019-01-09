package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleMergeTest   {
@Test
    public void test_0() throws Exception {
        String sql = "MERGE INTO bonuses D" + //
                     "   USING (SELECT employee_id, salary, department_id FROM employees" + //
                     "   WHERE department_id = 80) S" + //
                     "   ON (D.employee_id = S.employee_id)" + //
                     "   WHEN MATCHED THEN UPDATE SET D.bonus = D.bonus + S.salary*0.01" + //
                     "     DELETE WHERE (S.salary > 8000)" + //
                     "   WHEN NOT MATCHED THEN INSERT (D.employee_id, D.bonus)" + //
                     "     VALUES (S.employee_id, S.salary*0.01)" + //
                     "     WHERE (S.salary <= 8000);";

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

        Assert.assertEquals(2, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("bonuses")));

        Assert.assertEquals(5, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("employees", "employee_id")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("employees", "salary")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("employees", "department_id")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("bonuses", "employee_id")));
        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("bonuses", "bonus")));
    }

}
