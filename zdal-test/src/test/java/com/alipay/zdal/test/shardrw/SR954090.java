package com.alipay.zdal.test.shardrw;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static com.alipay.ats.internal.domain.ATS.Step;
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
@Feature("shard+rw����Դ����д��������ȼ�,�����ȼ�Ϊp,д���ȼ�Ϊq������д")
public class SR954090 {

	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;
	private String user ;
	private String psd ;
	
	@Before
	public void beforeTestCase(){
		user = ConstantsTest.mysq112User;
		psd = ConstantsTest.mysq112Psd;
	}

	@Subject("����Դ���ȼ�,д�⡣������group:group_0,group_1.����group_0Ϊds0:r1w2q1,ds2:r2w1q2,group_1Ϊds1:r5w10p2,ds3:r10w3p3")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954091() {
		sqlMap = (SqlMapClient) ZdalShardrwSuite.context
				.getBean("zdalShardrwPriority");
		Map<String, Object> params = new HashMap<String, Object>();	
		try {
			Step("���ֿ��Ӧ����0�⣬Ҳ����group_0��ds0�����ȼ�����ds2������Ӧ����дds0��user_0��");
			for (int i = 0; i < 10; i++) {
				params.put("user_id", 2*i);
				params.put("age", 10);
				params.put("name", "testOnly" );
				sqlMap.insert("insertShardrwMysql", params);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Step("�����ж�");
		String sqlStr="select count(*) from user_0 where age=10";
		String url=ConstantsTest.mysql12UrlTranation0;

		ResultSet rs0 = ZdalTestCommon.dataCheckFromJDBC(sqlStr, url, psd,
				user);		
		try {
			rs0.next();
			Assert.areEqual(10, rs0.getInt(1), "date count:"+rs0.getInt(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Step("�������");
		String delSqlStr="delete from user_0";
		ZdalTestCommon.dataUpdateJDBC(delSqlStr, url, psd, user);
	}
	
	@SuppressWarnings("unchecked")
	@Subject("����Դ���ȼ�,���⡣������group:group_0,group_1.����group_0Ϊds0:r1w2q1,ds2:r2w1q2,group_1Ϊds1:r5w10p2,ds3:r10w3p3")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC954092(){
		int countA=0;
		int countB=0;
		testPrepareData();
		Step("zdal��ѯ����");
		sqlMap = (SqlMapClient) ZdalShardrwSuite.context
		.getBean("zdalShardrwPriority");	
		try {
			for(int j=0;j<10;j++){						
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("user_id", 11);
				params.put("age", 10);
			List<Object> res=(List<Object>)sqlMap.queryForList("selectShardrwMysqlPriority",params);			
			
			for (int i = 0; i < res.size(); i++) {
				HashMap<String, String> hs = (HashMap<String, String>) res
						.get(i);
				if ("DB_A".equalsIgnoreCase((String) hs.get("name"))) {
					countA++;
				} else if ("DB_B"
						.equalsIgnoreCase((String) hs.get("name"))) {
					countB++;
				}
			  }
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.areEqual(true, countA == 10&&countB==0, "shard+rw�����ȼ��ж�,countA="+countA+",countB="+countB);
		
		deleteData();
	}
	
	
	/**
	 * ��������
	 */
	private void testPrepareData(){
		Step("����׼��");
		String sqlStr1="insert into user_0 (user_id,age,name,gmt_created,gmt_modified) values(11,10,'DB_A',now(),now()) ";
		String sqlStr2="insert into user_0 (user_id,age,name,gmt_created,gmt_modified) values(11,10,'DB_B',now(),now()) ";
		String url1=ConstantsTest.mysql12UrlTranation1;
		String url2=ConstantsTest.mysql12UrlTranation1_bac;
		
		ZdalTestCommon.dataUpdateJDBC(sqlStr1, url1, psd, user);
		ZdalTestCommon.dataUpdateJDBC(sqlStr2, url2, psd, user);
	}
	
	/**
	 *  ɾ��׼��������
	 */
	private void deleteData(){
		String url1=ConstantsTest.mysql12UrlTranation1;
		String url2=ConstantsTest.mysql12UrlTranation1_bac;
		String delStr="delete from user_0";
		ZdalTestCommon.dataUpdateJDBC(delStr, url1, psd, user);
		ZdalTestCommon.dataUpdateJDBC(delStr, url2, psd, user);
	}
	
	


}
