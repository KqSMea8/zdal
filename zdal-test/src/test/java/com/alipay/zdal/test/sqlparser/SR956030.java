package com.alipay.zdal.test.sqlparser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;
import static com.alipay.ats.internal.domain.ATS.Step;
@RunWith(ATSJUnitRunner.class)
@Feature("rw����Դ:insert,update,delete")
public class SR956030 {
	String url1 = ConstantsTest.mysql12UrlZds1;
	String psd = ConstantsTest.mysq112Psd;
	String user = ConstantsTest.mysq112User;

	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	
	@Before
	public void beforeTestcase() {
		// ����׼��
		prepareData();

		sqlMap = (SqlMapClient) ZdalSqlParserSuite.context
				.getBean("zdalsqlParserMysql01");
	}
	
	@After
	public void afterTestcase(){
		deleteData();
	}
	
	
	@Subject("ִ��:insert into")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC956031(){
		Step("ִ��:insert into");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("clum", 300);
		params.put("colu2", "DB_C");
		try {
			 sqlMap.insert("zdalsqlParserMysqlInsertSql",
					params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String selectSql="select count(*) from test1 where colu2='DB_C'";
		ResultSet rs=ZdalTestCommon.dataCheckFromJDBCOracle(selectSql,url1,psd,user);
		try {
			Assert.isTrue(rs.next(), "��֤��ѯ���");
			Assert.areEqual(1, rs.getInt(1), "��֤insert into���");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Subject("ִ��:update set")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC956032(){
		Step("ִ��:update set");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("clum", 100);
		params.put("colu2", "DB_G");
		try {
			 sqlMap.insert("zdalsqlParserMysqlUpdateSql",
					params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String selectSql="select count(*) from test1 where colu2='DB_G'";
		ResultSet rs=ZdalTestCommon.dataCheckFromJDBCOracle(selectSql,url1,psd,user);
		try {
			Assert.isTrue(rs.next(), "��֤��ѯ���");
			Assert.areEqual(1, rs.getInt(1), "��֤updates set ���");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Subject("ִ��:delete")
	@Priority(PriorityLevel.NORMAL)
	@Test
	public void TC956033(){
		Step("ִ��:delete");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("clum", 100);
		try {
			 sqlMap.insert("zdalsqlParserMysqlDeleteSql",
					params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String selectSql="select count(*) from test1";
		ResultSet rs=ZdalTestCommon.dataCheckFromJDBCOracle(selectSql,url1,psd,user);
		try {
			Assert.isTrue(rs.next(), "��֤��ѯ���");
			Assert.areEqual(1, rs.getInt(1), "��֤delete���");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * ����׼��
	 */
	private void prepareData() {
		String insertSql1 = "insert into test1(clum,colu2) values (100,'DB_A')";
		String insertSql2 = "insert into test1(clum,colu2) values (200,'DB_B')";
		ZdalTestCommon.dataUpdateJDBC(insertSql1, url1, psd, user);
		ZdalTestCommon.dataUpdateJDBC(insertSql2, url1, psd, user);
	}

	/**
	 * ɾ������
	 */
	private void deleteData() {
		String deleteSql = "delete from test1";
		ZdalTestCommon.dataUpdateJDBC(deleteSql, url1, psd, user);
	}

}
