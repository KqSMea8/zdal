package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class CastFunctionsAndOperatorsTest {
	@Test
	 public void test_0() throws Exception {
	        String sql = "SELECT 'a' = 'A'";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 'a' = 'A';", text);
	    }
	@Test
	    public void test_1() throws Exception {
	        String sql = "SELECT BINARY 'a' = 'A'";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT BINARY 'a' = 'A';", text);
	    }
	    @Test
	    public void test_2() throws Exception {
	        String sql = "SELECT CONVERT('abc' USING utf8)";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT CONVERT('abc' USING utf8);", text);
	    }
	    @Test
	    public void test_3() throws Exception {
	        String sql = "SELECT 'A' LIKE CONVERT(blob_col USING latin1) FROM tbl_name;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT 'A' LIKE CONVERT(blob_col USING latin1)\nFROM tbl_name;", text);
	    }
	    @Test
	    public void test_4() throws Exception {
	        String sql = "SELECT 'A' LIKE CONVERT(blob_col USING latin1) COLLATE latin1_german1_ci FROM tbl_name;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert
	            .assertEquals(
	                "SELECT 'A' LIKE CONVERT(blob_col USING latin1) COLLATE latin1_german1_ci\nFROM tbl_name;",
	                text);
	    }
	    @Test
	    public void test_5() throws Exception {
	        String sql = "SET @str = BINARY 'New York';";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SET @str = BINARY 'New York';", text);
	    }
	    @Test
	    public void test_6() throws Exception {
	        String sql = "SELECT LOWER(@str), LOWER(CONVERT(@str USING latin1))";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT LOWER(@str), LOWER(CONVERT(@str USING latin1));", text);
	    }
	    @Test
	    public void test_7() throws Exception {
	        String sql = "SELECT enum_col FROM tbl_name ORDER BY CAST(enum_col AS CHAR);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT enum_col\nFROM tbl_name\nORDER BY CAST(enum_col AS CHAR);",
	            text);
	    }
	    @Test
	    public void test_8() throws Exception {
	        String sql = "SELECT CONCAT('hello you ',2);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT CONCAT('hello you ', 2);", text);
	    }
	    @Test
	    public void test_9() throws Exception {
	        String sql = "SELECT CAST(1-2 AS UNSIGNED);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT CAST(1 - 2 AS UNSIGNED);", text);
	    }
	    @Test
	    public void test_10() throws Exception {
	        String sql = "SELECT CAST(CAST(1-2 AS UNSIGNED) AS SIGNED);";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT CAST(CAST(1 - 2 AS UNSIGNED) AS SIGNED);", text);
	    }
	    @Test
	    public void test_11() throws Exception {
	        String sql = "SELECT CAST(1 AS UNSIGNED) - 2.0;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        String text = output(stmtList);

	        Assert.assertEquals("SELECT CAST(1 AS UNSIGNED) - 2.0;", text);
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
