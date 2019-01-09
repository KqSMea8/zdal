package com.alipay.zdal.test.shardfailover;

import java.sql.SQLException;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

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
@Feature("shard+failover����Դ��ȡdbid������������������group,������ִ��sql����")
public class SR953010 {
	public TestAssertion Assert = new TestAssertion();;
	private SqlMapClient sqlMap;

	
	@SuppressWarnings("unchecked")
	@Subject("��ȡdbId: zoneDs=master_0,zoneError=Exception.д�� master_0���users_0��")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953011() {
		Step("��ȡdbId: zoneDs=master_0,zoneError=Exception.д�� master_0���users_0��");
		sqlMap = (SqlMapClient) ZdalShardfailoverSuite.context
				.getBean("zdalZoneDsZoneErrorRight");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("10"));
		params.put("name", "test_users");
		params.put("address", "test_address");

		try {
			sqlMap.insert("insertZoneDsZoneError", params);
			Map<String, DataSource> map = (Map<String, DataSource>) ThreadLocalMap
					.get(ThreadLocalString.GET_ID_AND_DATABASE);
			for (Map.Entry<String, DataSource> entry : map.entrySet()) {

				String dbId = entry.getKey();
				Assert.areEqual("zdalZoneDsZoneErrorRight.master_0", dbId, "��֤dbid");

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.areEqual(1, 2, "insertZoneDsZoneError Exception");
		}
		Step("ɾ������");
		String delsqlStr = "delete from users_0";
		String dburl = ConstantsTest.mysq112UrlTddl0;
		String dbpsd = ConstantsTest.mysq112Psd;
		String dbuser = ConstantsTest.mysq112User;
		ZdalTestCommon.dataUpdateJDBC(delsqlStr, dburl, dbpsd, dbuser);
	}

	
	@SuppressWarnings("unchecked")
	@Subject("��ȡdbId: zoneDs=master_0,zoneError=Exception.���� master_0���users_0��")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953012() {
		Step("��ȡdbId: zoneDs=master_0,zoneError=Exception.���� master_0���users_0��");
		sqlMap = (SqlMapClient) ZdalShardfailoverSuite.context
				.getBean("zdalZoneDsZoneErrorRight");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", Integer.valueOf("10"));
		try {
			sqlMap.queryForList("selectZoneDsZoneError", params);
			Map<String, DataSource> map = (Map<String, DataSource>) ThreadLocalMap
					.get(ThreadLocalString.GET_ID_AND_DATABASE);
			for (Map.Entry<String, DataSource> entry : map.entrySet()) {

				String dbId = entry.getKey();
				Assert.areEqual("zdalZoneDsZoneErrorRight.master_0", dbId, "��֤dbid");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
