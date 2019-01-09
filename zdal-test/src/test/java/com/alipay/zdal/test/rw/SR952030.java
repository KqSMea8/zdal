package com.alipay.zdal.test.rw;

import java.sql.SQLException;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.util.HashMap;
import java.util.List;
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
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("��д��������ȼ�,�����ȼ�Ϊp,д���ȼ�Ϊq�����Զ�")
public class SR952030 {
	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	private int countA = 0;
	private int countB = 0;

	@Before
	public void beforeTestCase() {
		// ����׼��
		ZdalTestCommon.dataPrepareForZds();
	}

	@After
	public void afterTestCase() {
		ZdalTestCommon.dataDeleteForZds();
	}

	
	@Subject("��:ds0:r1w1p0,ds1:r1w0p1����ds0����������������䵽�����ȼ��Ŀ�")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952031() {
        Step("��:ds0:r1w1p0,ds1:r1w0p1����ds0����������������䵽�����ȼ��Ŀ�");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority1");
		testReadDb(sqlMap);
		Assert.areEqual(true, countA == 10 && countB == 0, "countAֵ");
	}

	@Subject("��:ds0:r1w1p1,ds1:r1w0p1�����ȼ���ȣ��򰴱�������")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952032() {
		Step("��:ds0:r1w1p1,ds1:r1w0p1�����ȼ���ȣ��򰴱�������");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority2");
		testReadDb(sqlMap);
		Assert.areEqual(true, countA < 10 && countB < 10, "countAֵ");
	}

	@Subject("��:ds0:r1w1p0,ds1:r1w0p0�����ȼ���ȣ��򰴱�������")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952033() {
		Step("��:ds0:r1w1p0,ds1:r1w0p0�����ȼ���ȣ��򰴱�������");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority3");
		testReadDb(sqlMap);
		Assert.areEqual(true, countA < 10 && countB < 10, "countAֵ");
	}

	@Subject("��:ds0:r0w1p0,ds1:r1w0p1��ds0�����ã�ֱ�Ӵ�ds1�ж�ȡ")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952034() {
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority4");
		testReadDb(sqlMap);

		Assert.areEqual(true, countA == 0 && countB == 10, "countAֵ");

	}
	
	
	@Subject("��:ds0:r2w4p0q1,ds1:r1w1p1q0��ȫ����ds0�ж�ȡ")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952035() {
		Step("��:ds0:r2w4p0q1,ds1:r1w1p1q0��ȫ����ds0�ж�ȡ");
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalRwPriority8");
		testReadDb(sqlMap);

		Assert.areEqual(true, countA == 10 && countB == 0, "countAֵ");

	}
	
	

	

	/**
	 * ������ȡdb
	 * 
	 * @param sqlMa
	 */
	@SuppressWarnings("unchecked")
	public void testReadDb(SqlMapClient sqlMa) {
		countA = 0;
		countB = 0;
		// ������10��
		for (int i = 0; i < 10; i++) {
			try {
				List<Object> a = (List<Object>) sqlMa
						.queryForList("queryRwSql");
				for (int j = 0; j < a.size(); j++) {
					HashMap<String, String> hs = (HashMap<String, String>) a
							.get(j);
					if ("DB_A".equalsIgnoreCase((String) hs.get("colu2"))) {
						countA++;
					} else if ("DB_B"
							.equalsIgnoreCase((String) hs.get("colu2"))) {
						countB++;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
