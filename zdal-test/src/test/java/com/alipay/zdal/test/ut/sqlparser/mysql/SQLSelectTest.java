package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.List;

import org.junit.Test;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

public class SQLSelectTest {
	@Test
	 public void test_select() throws Exception {
	        String sql = "SELECT ALL FID FROM T1;SELECT DISTINCT FID FROM T1;SELECT DISTINCTROW FID FROM T1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        output(stmtList);
	    }
	@Test
	    public void test_select_1() throws Exception {
	        String sql = "SELECT HIGH_PRIORITY STRAIGHT_JOIN SQL_SMALL_RESULT SQL_BIG_RESULT SQL_BUFFER_RESULT FID FROM T1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        output(stmtList);
	    }
	@Test
	    public void test_select_2() throws Exception {
	        String sql = "SELECT SQL_CACHE FID FROM T1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        output(stmtList);
	    }
	@Test
	    public void test_select_3() throws Exception {
	        String sql = "SELECT SQL_NO_CACHE FID FROM T1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        output(stmtList);
	    }
	@Test
	    public void test_select_4() throws Exception {
	        String sql = "SELECT SQL_CALC_FOUND_ROWS FID FROM T1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        output(stmtList);
	    }
	@Test
	    public void test_select_5() throws Exception {
	        String sql = "SELECT 1 + 1;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        output(stmtList);
	    }
	@Test
	    public void test_select_6() throws Exception {
	        String sql = "SELECT CONCAT(last_name,', ',first_name) AS full_name FROM mytable ORDER BY full_name;";

	        SQLStatementParser parser = new MySqlStatementParser(sql);
	        List<SQLStatement> stmtList = parser.parseStatementList();

	        output(stmtList);
	    }

	    private void output(List<SQLStatement> stmtList) {
	        for (SQLStatement stmt : stmtList) {
	            stmt.accept(new MySqlOutputVisitor(System.out));
	            System.out.println(";");
	            System.out.println();
	        }
	    }

}
