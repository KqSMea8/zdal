package com.alipay.zdal.test.ut.datasource;

import static com.alipay.ats.internal.domain.ATS.Step;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.annotation.Tester;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.datasource.LocalTxDataSourceDO;
import com.alipay.zdal.datasource.ZDataSource;
/**
 * ����zdatasourceͬʱ����valve����
 * @author yin.meng
 *
 */
@RunWith(ATSJUnitRunner.class)
@Feature("����zdatasourceͬʱ����valve����")
public class ZDS951050 extends ZDSTest{
	ZDataSource zDataSource = null;
	LocalTxDataSourceDO localTxDSDo = new LocalTxDataSourceDO();
	
	@Before
	public void zdsSetUp() throws Exception{
		localTxDSDo.setConnectionURL("jdbc:oracle:thin:@10.253.94.6:1521:perfdb6");
		localTxDSDo.setDriverClass("oracle.jdbc.OracleDriver");
		try {
			localTxDSDo.setEncPassword("-7cda29b2eef25d0e");
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
		localTxDSDo.setExceptionSorterClassName("com.alipay.zdatasource.resource.adapter.jdbc.vendor.OracleExceptionSorter");
		localTxDSDo.setMaxPoolSize(12);
		localTxDSDo.setMinPoolSize(6);
		localTxDSDo.setPreparedStatementCacheSize(100);
		localTxDSDo.setUserName("ACM");
		localTxDSDo.setDsName("ds");
	}
	
	@After
	public void zdsTearDown() {		
	    try {
			zDataSource.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /*@Subject("������valve����")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951051() {		
    	Step("��������Դ");
    	try {
			zDataSource = new ZDataSource(localTxDSDo);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			fail();
		}
		
		Step("��ȡvalve����");
		Valve valve = zDataSource.getValve();
		ThresholdAndPeriod sqlLimit=valve.getSqlValve();
		ThresholdAndPeriod txLimit=valve.getTXValve();
		Map<String, ThresholdAndPeriod> tableLimit=valve.getTableValve();

	    ThresholdAndPeriod   noLimit    = new ThresholdAndPeriod(-1, -1);
	    HashMap<String, ThresholdAndPeriod> emptyTable = new HashMap<String, ThresholdAndPeriod>();
	    
	    Step("��֤valve����");
	    Assert.isTrue(sqlLimit.equals(noLimit),"У��sql����");
	    Assert.isTrue(txLimit.equals(txLimit),"У��tx����");
	    Assert.isTrue(tableLimit.equals(emptyTable),"У��table����");	    
	}*/

    @Subject("���úϷ�valve����")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951052() {
		Step("׼��valve����");
		
		Step("��������Դ");
		try {
			zDataSource = new ZDataSource(localTxDSDo);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			fail();
		}
		
		Step("��ȡvalve����");
		/*Valve valve = zDataSource.getValve();
		ThresholdAndPeriod sqlLimit=valve.getSqlValve();
		ThresholdAndPeriod txLimit=valve.getTXValve();
		Map<String, ThresholdAndPeriod> tableLimit=valve.getTableValve();

	    ThresholdAndPeriod   Limit1    = new ThresholdAndPeriod(1, 1);
	    ThresholdAndPeriod   Limit2    = new ThresholdAndPeriod(1, 60);
	    HashMap<String, ThresholdAndPeriod> Limit3 = new HashMap<String, ThresholdAndPeriod>();
	    Limit3.put("t1",Limit1);
	    Limit3.put("t2", Limit2);
	    
		Step("��֤valve����");
        Assert.isTrue(sqlLimit.equals(Limit1),"У��sql����");
	    Assert.isTrue(txLimit.equals(Limit2),"У��tx����");
	    Assert.isTrue(tableLimit.equals(Limit3),"У��table����");*/
	}
	
    @Subject("���ò��Ϸ�valve����")
    @Priority(PriorityLevel.NORMAL)
    @Tester("riqiu")
    @Test
	public void testTC951053() {		
		Step("׼��valve����");
		
		Step("��������Դ");
		try {
			zDataSource = new ZDataSource(localTxDSDo);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			fail();
		}
		
		Step("��ȡvalve����");
		/*Valve valve = zDataSource.getValve();
		ThresholdAndPeriod sqlLimit=valve.getSqlValve();
		ThresholdAndPeriod txLimit=valve.getTXValve();
		Map<String, ThresholdAndPeriod> tableLimit=valve.getTableValve();

	    ThresholdAndPeriod   noLimit    = new ThresholdAndPeriod(-1, -1);
	    HashMap<String, ThresholdAndPeriod> emptyTable = new HashMap<String, ThresholdAndPeriod>();
	    
		Step("��֤valve����");
	    Assert.isTrue(sqlLimit.equals(noLimit),"У��sql����");
	    Assert.isTrue(txLimit.equals(txLimit),"У��tx����");
	    Assert.isTrue(tableLimit.equals(emptyTable),"У��table����");	    */
	}
}
