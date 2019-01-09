package com.alipay.zdal.test.rw;

import static com.alipay.ats.internal.domain.ATS.Step;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.test.common.ZdalTestCommon;

@RunWith(ATSJUnitRunner.class)
@Feature("�����ڵ���������")
public class SR952090 {
	public TestAssertion Assert = new TestAssertion();
	private Connection cn = null;
	private Statement st = null;
	private ZdalDataSource zdalDataSource = null;

	@Before
	public void beforeTestCase() {
		zdalDataSource = (ZdalDataSource) ZdalRwSuite.context
				.getBean("zdalRwDSMysql2");
		
		try {
			cn = zdalDataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void afterTestCase() {
		try {
			ZdalTestCommon.dataDeleteForZds();

			st.close();
			cn.close();
			//zdalDataSource.close();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			st = null;
			cn = null;
			zdalDataSource = null;
		}
	}

	
	@Subject("��������д���,��д�������[Ŀǰֻ֧��һ��д��]����Ϊ��������")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952091() {
		try {
			Step("��������д���,��д�������[Ŀǰֻ֧��һ��д��]����Ϊ��������");
			cn.setAutoCommit(false);
			st = cn.createStatement();
			st.execute("insert into test1 value(111,'DB_G')");
			ResultSet rs = st
					.executeQuery("select count(*) from test1 where clum=111");
			rs.next();
			int count = rs.getInt(1);
			Step("��д���ж�����");
			Assert.areEqual(1, count, "��д���ж�����");

			cn.commit();
			cn.setAutoCommit(true);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.isFalse(true, "��Ԥ���쳣" + e);
		}
	}
	

	@Subject("�������ȶ���д,��д�������[Ŀǰֻ֧��һ��д��]����Ϊ��������")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952092() {
		Step("����׼��");
		ZdalTestCommon.dataPrepareForZds();
		Step("��������");
		try {
		
			cn.setAutoCommit(false);
			st = cn.createStatement();
			st.execute("select count(*) from test1 where clum=100");
			st.execute("insert into test1 value(112,'DB_G')");
			cn.commit();
			cn.setAutoCommit(true);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.isFalse(true, "��Ԥ���쳣" + e);
		}
	}
	
	
	@Subject("������������,��д�������[Ŀǰֻ֧��һ��д��]����Ϊ��������")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952093(){
		Step("����׼��");
		ZdalTestCommon.dataPrepareForZds();
		Step("��������");
		try {
		
			cn.setAutoCommit(false);
			st = cn.createStatement();
			st.execute("select count(*) from test1 where clum=100");
			st.execute("select count(*) from test1 where clum=112");
			st.execute("select count(*) from test1 where clum=113");
			cn.commit();
			cn.setAutoCommit(true);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.isFalse(true, "��Ԥ���쳣" + e);
		}
	}

	@Subject("����������д")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952094(){
		Step(" ��������");
		try {
			
			cn.setAutoCommit(false);
			st = cn.createStatement();
			st.execute("insert into test1 value(112,'DB_G')");
			st.execute("insert into test1 value(113,'DB_G')");
			st.execute("insert into test1 value(114,'DB_G')");
			cn.commit();
			cn.setAutoCommit(true);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.isFalse(true, "��Ԥ���쳣" + e);
		}
	}
	
	@Subject("����������ͬʱ�ر�")
	@Priority(PriorityLevel.HIGHEST)
	@Test
	public void TC952095(){
		long cCount=1;
		try {
			cn.setAutoCommit(false);
			st =cn.createStatement();
			Step("����1���������ύ");
			st.execute("select * from test1 where clum = 12");
			st.execute("delete from test1 where clum = 12");
			cn.commit();
			cn.setAutoCommit(true);
			
			Step("������һ������");
			Step("����2���������ύ");
			cn.setAutoCommit(false);
			java.sql.Statement st1 = cn.createStatement();
			st1.execute("select * from test1 where clum = 12");
			st1.execute("delete from test1 where clum = 12");
			cn.commit();
			cn.setAutoCommit(true);
			
			cn.close();
			cCount=zdalDataSource.getDataSourcesMap().get("ds1").getLocalTxDataSource().getPoolCondition().getInUseConnectionCount();		    
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.areEqual(Long.parseLong("0"), cCount, "���������ѹر�");
	}
	
	
	

}
