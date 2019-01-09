package com.alipay.zdal.test.shardfailover;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.alipay.ats.internal.domain.ATS.Step;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

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
@Feature("shard+failover����ķ���")
public class SR953050 {
	public TestAssertion Assert = new TestAssertion();
	private TransactionTemplate tt;
	private SqlMapClient sqlMap;

	@Subject("shard+rw:zdal����������룬Ԥ��д��master_1��1��Ĳ���")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC953051() {
		Step("shard+rw:zdal����������룬Ԥ��д��master_1��1��Ĳ���");
		sqlMap = (SqlMapClient) ZdalShardfailoverSuite.context
				.getBean("zdalShardfailoverShardDbShardTable");
		tt = (TransactionTemplate) ZdalShardfailoverSuite.context
				.getBean("shardfailovertransactionTemplate1");
        
		testTransactionInsertSelect();
		
		Step("��֤���ݣ��������");
		testCheckData();
	}
	
	/**
	 * ��insert��select������
	 */
	private void testTransactionInsertSelect(){
		try {
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					Map<String, Object> params = new HashMap<String, Object>();
					Map<String, Object> params2 = new HashMap<String, Object>();
					params.put("user_id", 1);
					params.put("name", "testshardfailover");
					params.put("address", "test");				
					String insertSql = "insertShardfailoverMysql";
					params2.put("user_id", 1);
					String selectSql = "selectShardfailoverMysql";
					try {
						 sqlMap.insert(insertSql,params);
						 List<Object> res_1 =sqlMap.queryForList(selectSql,params2);
						 Assert.areEqual(1, res_1.size(), "��֤select����ǿ�");						
					} catch (SQLException e) {
						status.setRollbackOnly();
						e.printStackTrace();
					}
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * �����������,֮��ɾ������
	 * 
	 * 
	 */
	private void testCheckData() {
		String dburl=ConstantsTest.mysq112UrlTddl1;
		String dbpsd=ConstantsTest.mysq112Psd;
		String dbuser=ConstantsTest.mysq112User;
		String sql = "select count(*) from users_1";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(sql, dburl, dbpsd,
				dbuser);
		try {
			rs.next();
			Assert.areEqual(1, rs.getInt(1), "���ݼ��");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String delStr="delete from users_1";
		ZdalTestCommon.dataUpdateJDBC(delStr,dburl,dbpsd,dbuser);
	}
	

}
