package com.alipay.zdal.test.rw;

import java.sql.ResultSet;
import static com.alipay.ats.internal.domain.ATS.Step;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;

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
@Feature("����insert into   select  from ���")
public class SR952160 {
	public TestAssertion Assert = new TestAssertion();
	private SqlMapClient sqlMap;

	@Subject("����insert into select from")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952161() {

		Step("����׼��");
		String url = ConstantsTest.mysql12UrlZds1;
		String psd = ConstantsTest.mysq112Psd;
		String user = ConstantsTest.mysq112User;
		String sqlStr = "insert into test2(clum,colu2)values(99,'DB_Z')";
		ZdalTestCommon.dataUpdateJDBC(sqlStr, url, psd, user);
		sqlMap = (SqlMapClient) ZdalRwSuite.context.getBean("zdalRwMysql4");
         Step("��insert into select from ������");
		try {
			sqlMap.insert("rw-Insert-Select");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Step("��֤");
		int rownum=0;
		String sqlStr2="select count(*) from test1 where clum=99";
		ResultSet rs=ZdalTestCommon.dataCheckFromJDBC(sqlStr2, url, psd, user);
		try {
			rs.next();
			rownum=rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.areEqual(1, rownum, "��֤test1");
		
		Step("�������");
		String sqlStr3="delete from test1";
		String sqlStr4="delete from test2";
		ZdalTestCommon.dataUpdateJDBC(sqlStr3, url, psd, user);
		ZdalTestCommon.dataUpdateJDBC(sqlStr4, url, psd, user);
		

	}

}
