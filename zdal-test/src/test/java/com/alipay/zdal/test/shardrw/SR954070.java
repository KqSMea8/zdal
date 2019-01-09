package com.alipay.zdal.test.shardrw;

import java.sql.ResultSet;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("shard+rw ��ָ̬����")
public class SR954070 {

	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	
	@Subject("��ָ̬���⣬rw д�� ��������group:group_0,group_1.����group_0Ϊds0:r0w10,ds2:r10w0,group_1Ϊds1:r0w10,ds3:r10w0")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954071(){
		sqlMap = (SqlMapClient) ZdalShardrwSuite.context.getBean("zdalShardrwMysql");		
		Map<String, Object> params=new HashMap<String, Object>();

		ThreadLocalMap.put(ThreadLocalString.DATABASE_INDEX, 1);
		
		params.put("user_id", 10);
		params.put("age", 10);
		params.put("name", "testOnly");
		try {
			Step("������Ӧ��д��group_0���ds0������ָ����1�ˡ�����Ҫд��group_0���ds2��");
			sqlMap.insert("insertShardrwMysql", params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Step("������֤");
		 String  dburl1=ConstantsTest.mysql12UrlTranation0_bac ;
		 String   dbuser=ConstantsTest.mysq112User;
		 String   dbpsd=ConstantsTest.mysq112Psd;
		String sqlStr="select count(*) from user_0 where user_id=10";
		ResultSet se=ZdalTestCommon.dataCheckFromJDBC(sqlStr, dburl1, dbpsd,dbuser);
		try {
			se.next();
			Assert.areEqual(1, se.getInt(1), "������֤");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Step("ɾ������");
		String delStr="delete from user_0";
		ZdalTestCommon.dataUpdateJDBC(delStr, dburl1, dbpsd, dbuser);
		
	}
}
