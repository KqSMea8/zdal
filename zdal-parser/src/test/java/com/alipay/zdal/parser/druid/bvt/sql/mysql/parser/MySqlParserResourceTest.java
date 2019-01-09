package com.alipay.zdal.parser.druid.bvt.sql.mysql.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlParameterizedOutputVisitor;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alipay.zdal.parser.sql.util.IOUtils;
import com.alipay.zdal.parser.sql.util.JdbcUtils;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: MySqlParserResourceTest.java, v 0.1 2012-5-17 ����10:11:46 xiaoqing.zhouxq Exp $
 */
public class MySqlParserResourceTest extends TestCase {

    public void test_0() throws Exception {
        exec_test("mysql-0.txt");
        exec_test("mysql-1.txt");
        exec_test("mysql-2.txt");
        exec_test("mysql-3.txt");
        exec_test("mysql-4.txt");
        exec_test("mysql-5.txt");
        exec_test("mysql-6.txt");
        exec_test("mysql-7.txt");
        exec_test("mysql-8.txt");
        exec_test("mysql-9.txt");
        exec_test("mysql-10.txt");
        exec_test("mysql-11.txt");
        exec_test("mysql-12.txt");
        exec_test("mysql-13.txt");
    }

    public void exec_test(String resource) throws Exception {
        System.out.println(resource);
        InputStream is = null;

        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        Reader reader = new InputStreamReader(is, "UTF-8");
        String input = IOUtils.read(reader);
        JdbcUtils.close(reader);
        String[] items = input.split("---------------------------");
        String sql = items[0].trim();
        String expect = items[1].trim();

        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();

        Assert.assertEquals(1, statementList.size());

        String text = output(statementList);
        System.out.println(text);
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statementList.get(0).accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("relationShips : " + visitor.getRelationships());
        System.out.println();

        expect = expect.replaceAll("\\r\\n", "\n");

        String mergeExpect = null;
        if (items.length == 3) {
            mergeExpect = items[2].trim();
        }
        if (mergeExpect != null) {
            mergValidate(sql, mergeExpect);
        }
    }

    void mergValidate(String sql, String expect) {

        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        StringBuilder out = new StringBuilder();
        MySqlParameterizedOutputVisitor visitor = new MySqlParameterizedOutputVisitor(out);
        statemen.accept(visitor);

        System.out.println(out.toString());

        expect = expect.replaceAll("\\r\\n", "\n");
        Assert.assertEquals(expect, out.toString());
    }

    private String output(List<SQLStatement> stmtList) {
        StringBuilder out = new StringBuilder();
        MySqlOutputVisitor visitor = new MySqlOutputVisitor(out);

        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }

        return out.toString();
    }
}
