package com.alipay.zdal.test.client;

import static com.alipay.ats.internal.domain.ATS.Step;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.jdbc.ZdalDataSource;
import com.alipay.zdal.datasource.ZDataSource;
import com.alipay.zdal.test.common.ConstantsTest;
import com.alipay.zdal.test.common.ZdalTestBase;

@RunWith(ATSJUnitRunner.class)
@Feature("��ȡzdal������Ϣ��zdal getRwDataSourcePoolConfig,getdbmode,getzone.zdal getDataSourcesMap")
public class SR951100 extends ZdalTestBase {

    @Before
    public void beginTestCase() {
        appName = "zdalClientInitApp";
        localFile = "./config/client";
        zdalDataSource.setAppName(appName);
        zdalDataSource.setConfigPath(localFile);
        appDsName = "zdataconsoleUrlIsNull";
        zdalDataSource.setAppDsName(appDsName);
    }

    @Subject("��ʼ��zdalDatasource,��ȡ getRwDataSourcePoolConfig")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951101() {

        Step("��ʼ��zdalDatasource,��ȡ getRwDataSourcePoolConfig");
        try {
            zdalDataSource.init();
            Map<String, ?> hs = zdalDataSource.getRwDataSourcePoolConfig();
            Assert.areEqual("ds0:rw1,ds1:r0w2", hs.get("group_0"),
                "init zdalDataSource,then getRwDataSourcePoolConfig");
            Step("�������ֵ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Subject("��ʼ��zdalDatasource,,Ȼ������  getRwDataSourcePoolConfig")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951102() {
        try {
            Step("��ʼ��zdalDatasource,,Ȼ������  getRwDataSourcePoolConfig");
            Map<String, String> hm = new HashMap<String, String>();
            hm.put("group_0", "ds0:r10w0,ds1:r0w10");
            zdalDataSource.init();
            zdalDataSource.resetWeight(hm);
            Map<String, ?> hs = zdalDataSource.getRwDataSourcePoolConfig();
            Assert.areEqual("ds0:r10w0,ds1:r0w10", hs.get("group_0"),
                "init zdalDataSource,then getRwDataSourcePoolConfig");
            Step("��������ֵ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Subject("��ʼ��zdalDatasource, ��ȡ getd bmode,zone")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951103() {
        Step("��ʼ��zdalDatasource, ��ȡ getd bmode,zone");
        zdalDataSource.init();
        Assert.areEqual(ConstantsTest.dbmode, zdalDataSource.getDbmode(),
            "init zdalDataSource,then getdbmode");

    }

    @Subject("��ʼ��zdalDatasource,zone Ϊ null,�����ֵ Ϊ gz00a")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951104() {
        Step("��ʼ��zdalDatasource,zone Ϊ null,�����ֵ Ϊ gz00a");
        ZdalDataSource zd = new ZdalDataSource();
        zd.setDbmode(ConstantsTest.dbmode);
        zd.setAppName(appName + "gz00a");
        zd.setConfigPath(localFile);
        zd.setAppDsName(appDsName + "gz00a");
        zd.init();
        Step("��������ֵ");

    }

    @Subject("��ʼ��zdalDatasource,getDataSourcesMap")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951105() {
        Step("��ʼ��zdalDatasource,getDataSourcesMap");
        int min = 1;
        int max = 20;
        int pscachesize = 100;
        int querytimeout = 180;
        int blockingtime = 180;
        int idetimeout = 180;

        zdalDataSource.init();

        Map<String, ZDataSource> dsMap = zdalDataSource.getDataSourcesMap();
        Set<String> set = dsMap.keySet();
        Iterator<String> it = set.iterator();
        ZDataSource zDataSource = null;
        while (it.hasNext()) {
            String s = (String) it.next();
            zDataSource = dsMap.get(s);

            int min_ds = zDataSource.getLocalTxDataSource().getMinSize();
            int max_ds = zDataSource.getLocalTxDataSource().getMaxSize();
            int ps_ds = zDataSource.getLocalTxDataSource().getPreparedStatementCacheSize();
            int query_ds = zDataSource.getLocalTxDataSource().getQueryTimeout();
            int block_ds = zDataSource.getLocalTxDataSource().getBlockingTimeoutMillis();
            long ide_ds = zDataSource.getLocalTxDataSource().getIdleTimeoutMinutes();

            Step("��������ֵ");

            Assert.areEqual(min, min_ds, "min");
            Assert.areEqual(max, max_ds, "max");
            Assert.areEqual(pscachesize, ps_ds, "pscachesize");
            Assert.areEqual(querytimeout, query_ds, "querytimeout");
            Assert.areEqual(blockingtime, block_ds, "blockingtime");
            Assert.areEqual((long) idetimeout, ide_ds, "idetimeout");

        }
    }

}
