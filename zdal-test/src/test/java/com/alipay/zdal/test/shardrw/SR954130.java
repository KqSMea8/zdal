package com.alipay.zdal.test.shardrw;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.alipay.ats.internal.domain.ATS.Step;
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
import com.ibatis.common.jdbc.exception.NestedSQLException;
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestCommon;
import com.ibatis.sqlmap.client.SqlMapClient;

@RunWith(ATSJUnitRunner.class)
@Feature("shard+rw����ķ���")
public class SR954130 {
	public TestAssertion Assert = new TestAssertion();
	private TransactionTemplate tt;
	private SqlMapClient sqlMap;
	private String dburl ;
	private String dburl2;
	private String dbpsd ;
	private String dbuser ;

	@Before
	public void beforeTestCase() {
		 dburl = ConstantsTest.mysql12UrlTranation0;
		 dburl2=ConstantsTest.mysql12UrlTranation1;
		 dbpsd = ConstantsTest.mysq112Psd;
		 dbuser = ConstantsTest.mysq112User;
		 
		sqlMap = (SqlMapClient) ZdalShardrwSuite.context
				.getBean("zdalShardrwShardDbShardTable");
		tt = (TransactionTemplate) ZdalShardrwSuite.context
				.getBean("shardrwtransactionTemplate1");
	}

	@Subject("shard+rw:zdal����������룬Ԥ��д��group_0��ds0��0��Ĳ��ԣ�ʵ���ڶ�ȡʱ��ds0��0��")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954131() {

		testTransactionInsertSelect();

		Step("������֤���������");
		testCheckData();
	}

	@Subject("shard+rw:zdal����������룬Ԥ��д��group_0��ds0��0��Ĳ��ԣ�ʵ���ڶ�ȡʱ��ds0��1��")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954132() {

		testTransactionInsertSelectOther();

		Step("������֤���������");
		testCheckData();
	}

	@Subject("shard+rw:zdal����������룬Ԥ��д��group_0��ds0��0��Ĳ��ԣ�����ʱ������group_1�����ʱ����group_1��д��")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954133() {

		testTransactionInsertSelectOther2();

		Step("������֤���������");
		testCheckData();
	}

	@Subject("shard+rw:zdal����������룬����д���group��ͬ")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954134() {

		Step("shard+rw:zdal����������룬����д���group��ͬ");
		testTransactionInsertInsert();
		
		String delSql="delete from user_0";
		ZdalTestCommon.dataUpdateJDBC(delSql, dburl, dbpsd, dbuser);
		ZdalTestCommon.dataUpdateJDBC(delSql, dburl2, dbpsd, dbuser);
	}

	/**
	 * ��insert��select�������������еĶ������������ֵ�ͬһ��group��ʱ�������ǵ�ǰд��
	 */
	private void testTransactionInsertSelect() {
		try {
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("user_id", 10);
					params.put("age", 10);
					params.put("name", "test");
					String insertSql = "insertShardrwMysql";
					String selectSql = "selectShardrwMysql";
					try {
						sqlMap.insert(insertSql, params);
						List<Object> res_1 = sqlMap.queryForList(selectSql);
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
	 * д�⵽group��user_0����ȡͬһ��group��user_1��
	 */
	private void testTransactionInsertSelectOther() {
		try {
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("user_id", 10);
					params.put("age", 10);
					params.put("name", "test");
					String insertSql = "insertShardrwMysql";
					String selectSql = "selectShardrwMysqlOther";
					try {
						sqlMap.insert(insertSql, params);
						List<Object> res_1 = sqlMap.queryForList(selectSql);
						Step("��ʱ��ȡ����ds0��user_1��");
						Assert.areEqual(0, res_1.size(), "��֤select����ǿ�");

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
	 * д�⵽group��user_0����ȡ��ͬ��group��user_0��
	 */
	private void testTransactionInsertSelectOther2() {
		try {
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("user_id", 10);
					params.put("age", 10);
					params.put("name", "test");
					String insertSql = "insertShardrwMysql";
					String selectSql = "selectShardrwMysqlOther2";
					try {
						sqlMap.insert(insertSql, params);
						List<Object> res_1 = sqlMap.queryForList(selectSql);

						Assert.areEqual(0, res_1.size(), "��֤select����ǿ�,"+res_1.size());

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
	 * ǰ�����β����sql�����ֿ���򣬹����ڲ�ͬ��group��
	 */
	private void testTransactionInsertInsert() {
		try {
			tt.execute(new TransactionCallback() {
				@SuppressWarnings("unchecked")
				public Object doInTransaction(TransactionStatus status) {
					Map<String, Object> params = new HashMap<String, Object>();
					Map<String, Object> params2 = new HashMap<String, Object>();
					params.put("user_id", 10);
					params.put("age", 10);
					params.put("name", "testA");
					params2.put("user_id", 11);
					params2.put("age", 10);
					params.put("name", "testB");
					String insertSql = "insertShardrwMysql";
					try {
						sqlMap.insert(insertSql, params);
						sqlMap.insert(insertSql, params2);

					} catch (Exception e) {
						status.setRollbackOnly();
						e.printStackTrace();
						Assert.areEqual(NestedSQLException.class, e.getClass(),
								"�쳣");
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
	 * @param dburl
	 */
	private void testCheckData() {
		
		String sql = "select count(*) from user_0";
		ResultSet rs = ZdalTestCommon.dataCheckFromJDBC(sql, dburl, dbpsd,
				dbuser);
		try {
			rs.next();
			Assert.areEqual(1, rs.getInt(1), "���ݼ��");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String delStr = "delete from user_0";
		ZdalTestCommon.dataUpdateJDBC(delStr, dburl, dbpsd, dbuser);
	}

}
