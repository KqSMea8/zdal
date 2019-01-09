package com.alipay.zdal.test.rw;

import java.sql.ResultSet;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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

@RunWith(ATSJUnitRunner.class)
@Feature("rw��д��������ȼ�,�����ȼ�Ϊp,д���ȼ�Ϊq������д")
public class SR952040 {

	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	private int countA = 0;
	private int countB = 0;

	private String url1;
	private String url2;
	private String user;
	private String psd;

	@Before
	public void beforeTestCase() {
		url1 = ConstantsTest.mysql12UrlZds1;
		url2 = ConstantsTest.mysql12UrlZds2;
		user = ConstantsTest.mysq112User;
		psd = ConstantsTest.mysq112Psd;
	}

	@After
	public void afterTestCase() {
		ZdalTestCommon.dataDeleteForZds();
	}

	@Subject("д:ds0:r1w1q0,ds1:r1w1q1��ȫ��д��ds0��")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952041() {
		Step("д:ds0:r1w1q0,ds1:r1w1q1��ȫ��д��ds0��");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority5");
		testWriteDb(sqlMap);
		Assert.areEqual(true, countA==10&&countB==0, "��֤���ȼ���д");
	}
	
	
	@Subject("д:ds0:r1w1q1,ds1:r1w1q1�����ȼ�һ��������������")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952042() {
		Step("д:ds0:r1w1q1,ds1:r1w1q1�����ȼ�һ��������������");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority6");
		testWriteDb(sqlMap);
		Assert.areEqual(true, countA<10&&countB<10, "��֤���ȼ���д,countA:"+countA+",countB:"+countB);
	}
	
	
	@Subject("д:ds0:r1w0q0,ds1:r1w1q1��ds0�����á�ȫ��д��ds1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952043() {
		Step("д:ds0:r1w0q0,ds1:r1w1q1��ds0�����á�ȫ��д��ds1");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority7");
		testWriteDb(sqlMap);
		Assert.areEqual(true, countA==0&&countB ==10, "��֤���ȼ���д");
	}
	
	
	@Subject("д:ds0:r2w4p0q1,ds1:r1w1p1q0��ȫ��д��ds1")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952044() {
		Step("д:ds0:r2w4p0q1,ds1:r1w1p1q0��ȫ��д��ds1");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority8");
		testWriteDb(sqlMap);
		Assert.areEqual(true, countA==0&&countB ==10, "��֤���ȼ���д");
	}
	
	/**
	 * ����д��
	 * @param sqlMa
	 */
	private void testWriteDb(SqlMapClient sqlMa){
		//countA=0;
		//countB=0;
		// ����д10��
		for (int i = 1; i <= 10; i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("num", i);
			try {
				sqlMa.insert("insertRwSql", params);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		String querySql = "select count(*) from test1 where colu2 = 'DB_G'";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(querySql, url1, psd,
				user);
		ResultSet rs2 = ZdalTestCommon.dataCheckFromJDBC(querySql, url2, psd,
				user);
		try {
			Assert.areEqual(true, rs.next() && rs2.next(), "the value");
			countA = rs.getInt(1);
			countB = rs2.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
