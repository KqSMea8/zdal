package com.alipay.zdal.test.rw;

import static com.alipay.ats.internal.domain.ATS.Step;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("rw����Դds0:r10w10��ʹ������ģ�壺������Դ����")
public class SR952110 {
	public TestAssertion Assert = new TestAssertion();
	private TransactionTemplate tt;
	private SqlMapClient sqlMap;
	String url;
	String user;
	String psd;

	@Before
	public void beforeTestCase() {
		url = ConstantsTest.mysq112UrlFail0;
		user = ConstantsTest.mysq112User;
		psd = ConstantsTest.mysq112Psd;
		sqlMap = (SqlMapClient) ZdalRwSuite.context
				.getBean("zdalTransactionManager");
		tt = (TransactionTemplate) ZdalRwSuite.context
				.getBean("transactionTemplate1");
	}

	@Subject("��������:��select��delete")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952111() {
		Step(" ����׼��");
		ZdalTestCommon.dataPrepareForFail0();
		try {
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					String querySql = "rw-Select";
					String deleteSql = "rw-Delete";
					try {
						List<Object> res_1 = sqlMap.queryForList(querySql);
						Assert.areEqual(1, res_1.size(), "��֤select����ǿ�");

						Step("������ɾ������");
						int res_2 = sqlMap.delete(deleteSql);
						Assert.areEqual(1, res_2, "��֤delete�ɹ�");
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	      Step("��֤Ŀǰ������");
		String sqlStr = "select count(*) from master_0 where user_id =20";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(sqlStr, url, psd, user);
		try {
			Assert.areEqual(true, rs.next(), "���ݳɹ�");
			Assert.areEqual(0, rs.getInt(1), "��֤������");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Subject("��������:��insert��select")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952112() {
		try {
			Step("��������:��insert��select");
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
						throw new RuntimeException(e);
					}
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
		String delStr="delete from master_0 where user_id=20";
		ZdalTestCommon.dataUpdateJDBC(delStr, url, psd, user);
		
	}


	@Subject("�쳣����;��������������ͻ��sql���ع�")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952113() {
	Step("�쳣����;��������������ͻ��sql���ع�");
		try {
			tt.execute(new TransactionCallback() {
				public Object doInTransaction(TransactionStatus status) {
					String insertSql = "rw-insert";					
					try {						
						sqlMap.insert(insertSql);
						sqlMap.insert(insertSql);						
										        
					} catch (SQLException e) {
						Step("��Ҫ�ع���ȥ������һ�䣬���ύһ�����ݵ�db");
						status.setRollbackOnly();
						e.printStackTrace();
					}					
					return 0;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Step("��֤Ŀǰ������");
		String sqlStr = "select count(*) from master_0 where user_id =20";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(sqlStr, url, psd, user);
		try {
			Assert.areEqual(true, rs.next(), "���ݳɹ�");
			Assert.areEqual(0, rs.getInt(1), "��֤������");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
	
	
	@Subject("��������:��insert��delete")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952114() {
		Step("��������:��insert��delete");
		try {
			tt.execute(new TransactionCallback() {
				public Object doInTransaction(TransactionStatus status) {
					String insertSql = "rw-insert";
					String deleteSql = "rw-Delete";
					try {
						 sqlMap.insert(insertSql);
						 sqlMap.delete(deleteSql);					
					} catch (SQLException e) {
						status.setRollbackOnly();
						throw new RuntimeException(e);
					}
					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	
	
}
