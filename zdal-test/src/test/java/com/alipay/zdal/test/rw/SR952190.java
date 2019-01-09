package com.alipay.zdal.test.rw;

import java.sql.SQLException;
import java.util.HashMap;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.ibatis.sqlmap.client.SqlMapClient;
import static com.alipay.ats.internal.domain.ATS.Step;


@RunWith(ATSJUnitRunner.class)
@Feature("rw ,��zone����")
public class SR952190 {
	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	
	@After
	public void afterTestCase(){
		ZdalTestCommon.dataDeleteForZds();
	}
	
	
	@Subject("��zone���ʣ� zoneDs=ds0,zoneError=Exception.д��ds1�ı����������")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952191(){
		Step("��zone���ʣ� zoneDs=ds0,zoneError=Exception.д��ds1�ı����������");
		sqlMap = (SqlMapClient) ZdalRwSuite.context.getBean("zdalRwVisitOtherZone");
		HashMap<String ,Object> params = new HashMap<String, Object>();	
		Step("��������");
		try {
			params.put("num", 123);
			sqlMap.insert("insertRwSql", params);
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.areEqual(NestedSQLException.class, e.getClass(), "the visit other zone");
	
		}	
	}
	
	
	@Subject("��zone���ʣ� zoneDs=ds0,zoneError=Exception.����ds1�ı����������")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952192(){
	    Step("��zone���ʣ� zoneDs=ds0,zoneError=Exception.����ds1�ı����������");
		sqlMap = (SqlMapClient) ZdalRwSuite.context.getBean("zdalRwVisitOtherZone");
	
		try {
			sqlMap.queryForList("queryRwSql");
		} catch (SQLException e) {
			e.printStackTrace();
			Assert.areEqual(NestedSQLException.class, e.getClass(), "the visit other zone");
	
		}	
		
	}
	
}
