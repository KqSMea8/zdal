package com.alipay.zdal.test.rw;

import static com.alipay.ats.internal.domain.ATS.Step;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
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
import com.alipay.zdal.test.common.AllTestSuit;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapExecutor;

@RunWith(ATSJUnitRunner.class)
@Feature("��������")
public class SR952140 {
	public TestAssertion Assert = new TestAssertion();
	private  SqlMapClient sqlMap;
	private  SqlMapClientTemplate sqlMapClientTemplate;
	private TransactionTemplate tt;
	String url;
	String user;
	String psd;
	String url2;
	
	
	@Before
	public void beforeTestCase(){
		String delStr="delete from master_0";
		ZdalTestCommon.dataUpdateJDBC(delStr, url, psd, user);
		ZdalTestCommon.dataUpdateJDBC(delStr, url2, psd, user);
		
		url = ConstantsTest.mysq112UrlFail0;
		url2=ConstantsTest.mysq112UrlFail1;
		user = ConstantsTest.mysq112User;
		psd = ConstantsTest.mysq112Psd;
		
		sqlMap = (SqlMapClient) ZdalRwSuite.context
		.getBean("zdalTransactionManagerTwo");
		tt = (TransactionTemplate) ZdalRwSuite.context
		.getBean("transactionTwoTemplate1");
		sqlMapClientTemplate=new SqlMapClientTemplate(sqlMap);
	}
	
	@After
	public void afterTestCase(){
		String delStr="delete from master_0";
		ZdalTestCommon.dataUpdateJDBC(delStr, url, psd, user);
		ZdalTestCommon.dataUpdateJDBC(delStr, url2, psd, user);
	}

	
	@Subject("������������,��ͨ������ģ��")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952141(){
		Step("������������");
		try{
		inserts(getTestData(3));
		}catch(Exception ex){
			ex.printStackTrace();
		}	
		Step("��֤����");
		assertLineCount();	
	}
	
	
	
	@Subject("����ģ��,��������ѯ�� Ȼ����ɾ��")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952142(){
		Step("����׼��");
		ZdalTestCommon.dataPrepareForFail0();
		try {
			tt.execute(new TransactionCallback() {
				public Object doInTransaction(TransactionStatus status) {
					Step("������ѯ");
					List<Map<String,Object>> paramList = getTestData(1);
					selects(paramList);
					Step("ɾ������");
					String deleteSql = "rw-Delete";
					try {
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
	}
	
	
	
	@Subject("����ģ��,���������룬 Ȼ��������ѯ")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952143(){
		try {
			tt.execute(new TransactionCallback() {
				public Object doInTransaction(TransactionStatus status) {
					Step("��������");
					List<Map<String,Object>> ls=getTestData(3);
					inserts(ls);
					Step("������ѯ");
					List<Map<String,Object>> paramList = getTestData(1);
					selects(paramList);

					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		//��֤����
		assertLineCount();
	}
	
	/**��������Ч
	@Subject("����ģ��,��delete ����������")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC951232(){
		// ����׼��
		ZdalTestCommon.dataPrepareForFail0();
		try {
			tt.execute(new TransactionCallback() {
				public Object doInTransaction(TransactionStatus status) {
					String deleteSql = "rw-Delete";
					try {
						Step("������ɾ������");
						int res_2 = sqlMap.delete(deleteSql);
						Assert.areEqual(1, res_2, "��֤delete�ɹ�");						
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
					//��������
					List<Map<String,Object>> ls=getTestData(3);
					inserts(ls);

					return null;
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		//��֤����
		//assertLineCount();
	}
	*/
	
	
	
	/**
	 * ��֯����map��LIST
	 * @param num
	 * @return
	 */
	 private List<Map<String,Object>> getTestData(int num){
	    	List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
	    	for(int i=0;i<num;i++){
	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("user_id", i);
	    		params.put("age", Integer.valueOf(10));
	    		params.put("name", "a");
	    		params.put("content", "s");
	    		
	    		dataList.add(params);
	    	}
	    	
	    	return dataList;
	    }
	 /**
	  * ��������
	  * @param paramList
	  */
	  private void inserts( final List<Map<String,Object>> paramList){
			sqlMapClientTemplate.execute(new SqlMapClientCallback(){						
				@Override
				public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
					executor.startBatch();
					for(Map<String,Object> param:paramList){
						executor.insert("rw-insert-withParam",param);
					}	
					return executor.executeBatch();
					
				}
			});
	    }
	  
	  /**
	   * ������ѯ
	   * @param paramList
	   */
	  public void selects(final List<Map<String,Object>> paramList){
			sqlMapClientTemplate.execute(new SqlMapClientCallback(){						
				@Override
				public Object doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
					executor.startBatch();
					for(Map<String,Object> param:paramList){
						executor.queryForList("rw-Select-withParam",param);
					}	
					return executor.executeBatch();
				}
			});
	    }
	  
	  /**
	   * ��֤����
	   */
	  private void  assertLineCount(){
		Step("��֤����");
			String sqlStr1="select count(*) from master_0 where age=10";
			ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(sqlStr1, url, psd, user);
			try {
				Assert.areEqual(true, rs.next(), "���ݳɹ�");
				Assert.areEqual(3, rs.getInt(1), "��֤������:"+rs.getInt(1));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }

}
