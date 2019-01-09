package com.alipay.zdal.parser.druid.bvt.sql.oracle.visitor;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.OracleTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlParameterizedOutputVisitor;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.util.IOUtils;
import com.alipay.zdal.parser.sql.util.JdbcUtils;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleResourceTest.java, v 0.1 2012-5-17 ����10:23:13 xiaoqing.zhouxq Exp $
 */
public class OracleResourceTest extends OracleTest {

    public void test_0() throws Exception {
        // 13
        //        exec_test("bvt/parser/oracle-56.txt");
        for (int i = 0; i <= 56; ++i) {
            exec_test("oracle-" + i + ".txt");
        }
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
        //        String expect = items[1].trim();

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();

        // Assert.assertEquals(1, statementList.size());

        System.out.println(sql);

        print(statementList);

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();

        for (int i = 0, size = statementList.size(); i < size; ++i) {
            SQLStatement statement = statementList.get(i);
            statement.accept(visitor);
        }

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());

        System.out.println();
        System.out.println();
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

        Assert.assertEquals(expect, out.toString());
    }

}
