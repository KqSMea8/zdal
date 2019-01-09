package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class StringComparisonFunctionsTest {
	@Test
	 public void test_0() throws Exception {
	        String sql = "SELECT '?' LIKE 'ae' COLLATE latin1_german2_ci;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT '?' LIKE 'ae' COLLATE latin1_german2_ci;", text);
	    }
	@Test
	    public void test_1() throws Exception {
	        String sql = "SELECT '?' = 'ae' COLLATE latin1_german2_ci;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT '?' = 'ae' COLLATE latin1_german2_ci;", text);
	    }
	@Test
	    public void test_2() throws Exception {
	        String sql = "SELECT 'a' = 'a ', 'a' LIKE 'a ';";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 'a' = 'a ', 'a' LIKE 'a ';", text);
	    }
	@Test
	    public void test_3() throws Exception {
	        String sql = "SELECT 'David!' LIKE 'David_';";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 'David!' LIKE 'David_';", text);
	    }
	@Test
	    public void test_4() throws Exception {
	        String sql = "SELECT 'David!' LIKE '%D%v%';";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 'David!' LIKE '%D%v%';", text);
	    }
	@Test
	    public void test_5() throws Exception {
	        String sql = "SELECT 'David!' LIKE 'David\\_';";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 'David!' LIKE 'David_';", text);
	    }
	@Test
	    public void test_6() throws Exception {
	        String sql = "SELECT 'David_' LIKE 'David|_' ESCAPE '|'";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 'David_' LIKE 'David|_' ESCAPE '|';", text);
	    }
	@Test
	    public void test_7() throws Exception {
	        String sql = "SELECT 'abc' LIKE BINARY 'ABC'";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 'abc' LIKE BINARY 'ABC';", text);
	    }
	@Test
	    public void test_8() throws Exception {
	        String sql = "SELECT 10 LIKE '1%'";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 10 LIKE '1%';", text);
	    }
	@Test
	    public void test_9() throws Exception {
	        String sql = "SELECT filename, filename LIKE '%\\\\' FROM t1";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT filename, filename LIKE '%\\'\nFROM t1;", text);
	    }
	@Test
	    public void test_10() throws Exception {
	        String sql = "SELECT STRCMP('text', 'text2')";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT STRCMP('text', 'text2');", text);
	    }
	@Test
	    public void test_11() throws Exception {
	        String sql = "SET @s1 = _latin1 'x' COLLATE latin1_general_ci;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SET @s1 = _latin1 'x' COLLATE latin1_general_ci;", text);
	    }
	@Test
	    public void test_12() throws Exception {
	        String sql = "SET @s2 = _latin1 'X' COLLATE latin1_general_ci;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SET @s2 = _latin1 'X' COLLATE latin1_general_ci;", text);
	    }
	@Test
	    public void test_13() throws Exception {
	        String sql = "SELECT STRCMP(@s1, @s2), STRCMP(@s3, @s4);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT STRCMP(@s1, @s2), STRCMP(@s3, @s4);", text);
	    }
	@Test
	    public void test_14() throws Exception {
	        String sql = "SELECT STRCMP(@s1, @s3 COLLATE latin1_general_ci);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT STRCMP(@s1, @s3 COLLATE latin1_general_ci);", text);
	    }

	    private String output(List<SQLStatement> stmtList) {
	        StringBuilder out = new StringBuilder();

	        for (SQLStatement stmt : stmtList) {
	            stmt.accept(new MySqlOutputVisitor(out));
	            out.append(";");
	        }

	        return out.toString();
	    }

}
