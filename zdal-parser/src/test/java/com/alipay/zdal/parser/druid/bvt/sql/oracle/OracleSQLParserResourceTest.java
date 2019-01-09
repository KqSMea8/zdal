package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.druid.sql.test.TestUtils;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.util.IOUtils;
import com.alipay.zdal.parser.sql.util.JdbcUtils;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleSQLParserResourceTest.java, v 0.1 2012-5-17 ����10:22:10 xiaoqing.zhouxq Exp $
 */
public class OracleSQLParserResourceTest extends TestCase {

    public void test_0() throws Exception {
        // for (int i = 0; i <= 53; ++i) {
        // String resource = "bvt/parser/oracle-" + i + ".txt";
        // exec_test(resource);
        // }
        exec_test("oracle-55.txt");
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

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();

        Assert.assertEquals(1, statementList.size());

        String text = TestUtils.outputOracle(statementList);
        expect = expect.replaceAll("\\r\\n", "\n");
        System.out.println(text);
        Assert.assertEquals(expect, text.trim());

    }
}
