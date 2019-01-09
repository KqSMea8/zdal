package com.alipay.zdal.test.rw;

import static com.alipay.ats.internal.domain.ATS.Step;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.assertion.TestAssertion;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.exceptions.ZdalClientException;
import com.alipay.zdal.test.common.ZdalTestBase;

@RunWith(ATSJUnitRunner.class)
@Feature("rw ReadWriteRule�Ƿ�")
public class SR952130 extends ZdalTestBase {
    public TestAssertion Assert = new TestAssertion();

    @Subject("readWriteRuleΪ''")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952131() {
        Step("readWriteRuleΪ''");
        zdalDataSource.setAppName("zdalReadWriteRule");
        zdalDataSource.setAppDsName("readWriteRuleDs1");
        localFile = "./config/rw";
        zdalDataSource.setConfigPath(localFile);
        Step("��ʼ������");
        try {
            zdalDataSource.init();
        } catch (Exception e) {
            Assert.areEqual(ZdalClientException.class, e.getClass(), "��֤�쳣��");
        }
    }

    @Subject("readWriteRuleΪ�Ƿ��ַ�")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952132() {
        Step("readWriteRuleΪ�Ƿ��ַ�");
        zdalDataSource.setAppName("zdalReadWriteRule");
        zdalDataSource.setAppDsName("readWriteRuleDs2");
        localFile = "./config/rw";
        zdalDataSource.setConfigPath(localFile);
        try {
            Step("��ʼ���쳣");
            zdalDataSource.init();
        } catch (Exception e) {
            Assert.areEqual(ZdalClientException.class, e.getClass(), "��֤�쳣��");
        }
    }

    @Subject("readWriteRuleָ����ds������")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952133() {
        Step("readWriteRuleָ����ds������");
        zdalDataSource.setAppName("zdalReadWriteRule");
        zdalDataSource.setAppDsName("readWriteRuleDs3");
        localFile = "./config/rw";
        zdalDataSource.setConfigPath(localFile);
        Step("��ʼ���쳣");
        try {
            zdalDataSource.init();
        } catch (Exception e) {
            Assert.areEqual(ZdalClientException.class, e.getClass(), "��֤�쳣��");
        }
    }

}
