package com.alipay.zdal.test.client;

import static com.alipay.ats.internal.domain.ATS.Step;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.jdbc.ZdalConnection;
import com.alipay.zdal.test.common.ZdalTestBase;

@RunWith(ATSJUnitRunner.class)
@Feature("zdal getConnection")
public class SR951120 extends ZdalTestBase {

    @Before
    public void beginTestCase() {
        appName = "zdalClientInitApp";
        localFile = "./config/client";
        zdalDataSource.setAppName(appName);
        zdalDataSource.setConfigPath(localFile);
        appDsName = "zdataconsoleUrlIsNull";
        zdalDataSource.setAppDsName(appDsName);
    }

    @Subject("��ʼ��zdalDatasource,��ȡ getConnection")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951121() {
        Step("��ʼ��zdalDatasource,��ȡ getConnection");
        int noException = 0;
        try {
            zdalDataSource.init();
            zdalDataSource.getConnection();
        } catch (SQLException ex) {
            noException = 1;
        }
        Step("����connection");
        Assert.areEqual(0, noException, "zdalDataSource getConnection");

    }

    @Subject("��ʼ��zdalDatasource,��ȡ getConnection2")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951122() {

        try {
            Step("��ʼ��");
            zdalDataSource.init();
            ZdalConnection zcn = (ZdalConnection) zdalDataSource.getConnection();
            Step("����connection");
            Assert.areEqual(appName, zcn.getAppDsName(), "��֤connection");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
