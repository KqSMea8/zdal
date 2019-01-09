package com.alipay.zdal.parser.druid.bvt.sql.mysql;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlParameterizedOutputVisitor;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleParameterizedOutputVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: MySqlParameterizedOutputVisitorTest2.java, v 0.1 2012-5-17 ����10:06:50 xiaoqing.zhouxq Exp $
 */
public class MySqlParameterizedOutputVisitorTest2 extends TestCase {

    public void test_0() throws Exception {
        String sql = "SELECT * FROM T WHERE ID = ?";
        for (int i = 0; i < 10000; ++i) {
            sql += " OR ID = ?";
        }

        validate(sql, "SELECT *\nFROM T\nWHERE ID = ?");
        validateOracle(sql, "SELECT *\nFROM T\nWHERE ID = ?");
    }

    void validate(String sql, String expect) {

        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        StringBuilder out = new StringBuilder();
        MySqlParameterizedOutputVisitor visitor = new MySqlParameterizedOutputVisitor(out);
        statemen.accept(visitor);

        Assert.assertEquals(expect, out.toString());
    }

    void validateOracle(String sql, String expect) {

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        StringBuilder out = new StringBuilder();
        OracleParameterizedOutputVisitor visitor = new OracleParameterizedOutputVisitor(out, false);
        statemen.accept(visitor);

        Assert.assertEquals(expect, out.toString());
    }
}
