package com.alipay.zdal.test.rw;

import java.sql.ResultSet;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
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
import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("rw����Դds0:r0w10,ds1:r10w0��ʹ������ģ�壺������Դ�����д��������Դָ����")
public class SR952120 {
	public TestAssertion Assert = new TestAssertion();
	private TransactionTemplate tt;
	private SqlMapClient sqlMap;
	String url;
	String user;
	String psd;
	String url2;
	
	@Before
	public void beforeTestCase() {
		url = ConstantsTest.mysq112UrlFail0;
		url2=ConstantsTest.mysq112UrlFail1;
		user = ConstantsTest.mysq112User;
		psd = ConstantsTest.mysq112Psd;
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalTransactionManagerTwo");
		tt = (TransactionTemplate) ZdalRwSuite.context
				.getBean("transactionTwoTemplate1");
	}
	
	@After
	public void afterTestCase(){
		ThreadLocalMap.reset();
		String delStr="delete from master_0 where user_id=20";
		ZdalTestCommon.dataUpdateJDBC(delStr, url2, psd, user);
		ZdalTestCommon.dataUpdateJDBC(delStr, url, psd, user);
	}
	
	@Subject("��������:��insert��select�������У��Ǵ�д��������")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952121(){
		
		testTransactionInsertSelect();
		
		Step("��֤Ŀǰ������");
		String sqlStr = "select count(*) from master_0 where user_id =20";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(sqlStr, url, psd, user);
		try {
			Assert.areEqual(true, rs.next(), "���ݳɹ�");
			Assert.areEqual(1, rs.getInt(1), "��֤������");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	@Subject("��������:��insert��select��ָд��Ͷ��������дr10w0��")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952122(){
		Step("ָ���⣬�Զ������д");
		ThreadLocalMap.put(ThreadLocalString.DATABASE_INDEX, 1);
		testTransactionInsertSelect();
		
		Step("��֤Ŀǰ������");
		String sqlStr = "select count(*) from master_0 where user_id =20";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(sqlStr, url2, psd, user);
		try {
			Assert.areEqual(true, rs.next(), "���ݳɹ�");
			Assert.areEqual(1, rs.getInt(1), "��֤������");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	/**
	 * ��insert��select������
	 */
	private void testTransactionInsertSelect(){
		try {
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					String insertSql = "rw-insert";
					String selectSql = "rw-Select";
					try {
						 sqlMap.insert(insertSql);
						 List<Object> res_1 =sqlMap.queryForList(selectSql);
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

}
