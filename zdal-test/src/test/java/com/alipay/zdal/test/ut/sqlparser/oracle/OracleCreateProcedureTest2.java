package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleCreateProcedureTest2   {
@Test
    public void test_0() throws Exception {
        String sql = "CREATE OR REPLACE PROCEDURE transfer (" + //
                     "  from_acct  NUMBER," + //
                     "  to_acct    NUMBER," + //
                     "  amount     NUMBER" + //
                     ") AS " + //
                     "BEGIN" + //
                     "  UPDATE accounts" + //
                     "  SET balance = balance - amount" + //
                     "  WHERE account_id = from_acct;" + //
                     " " + //
                     "  UPDATE accounts" + //
                     "  SET balance = balance + amount" + //
                     "  WHERE account_id = to_acct;" + //
                     " " + //
                     "  COMMIT WRITE IMMEDIATE NOWAIT;" + //
                     "END;"; //

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
       

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

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("accounts")));

        Assert.assertEquals(5, visitor.getColumns().size());
        Assert.assertEquals(3, visitor.getConditions().size());
        Assert.assertEquals(2, visitor.getRelationships().size());

        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "salary")));
    }
}
