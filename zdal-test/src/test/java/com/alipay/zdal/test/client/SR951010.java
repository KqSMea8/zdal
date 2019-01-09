package com.alipay.zdal.test.client;

import static com.alipay.ats.internal.domain.ATS.Step;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alipay.ats.annotation.Feature;
import com.alipay.ats.annotation.Priority;
import com.alipay.ats.annotation.Subject;
import com.alipay.ats.enums.PriorityLevel;
import com.alipay.ats.junit.ATSJUnitRunner;
import com.alipay.zdal.client.exceptions.ZdalClientException;
import com.alipay.zdal.test.common.ZdalTestBase;

@RunWith(ATSJUnitRunner.class)
@Feature("zdal ��ʼ��")
public class SR951010 extends ZdalTestBase {

    @Before
    public void beginTestCase() {
        appName = "zdalClientInitApp";
        localFile = "./config/client";
        zdalDataSource.setAppName(appName);
        zdalDataSource.setConfigPath(localFile);
    }

    @Subject("appdsName Ϊnull�����г�ʼ��")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951011() throws Throwable {
        try {
            Step("appDsName is null ,���г�ʼ��");
            zdalDataSource.init();
        } catch (IllegalArgumentException e) {
            Assert.areEqual(IllegalArgumentException.class, e.getClass(), "appDSName �� null�����г�ʼ��");

        }
    }

    @Subject("appdsName Ϊ��ֵ�����г�ʼ��")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951012() throws Throwable {
        appDsName = "";
        zdalDataSource.setAppDsName(appDsName);
        Step("appName Ϊ '',���г�ʼ��");
        try {
            Step("appDsName Ϊ '',���г�ʼ��");
            zdalDataSource.init();
        } catch (IllegalArgumentException e) {
            Assert.areEqual(IllegalArgumentException.class, e.getClass(), "appDSName Ϊ ''�����г�ʼ��");

        }
    }

    @Subject(" ZdataconsoleUrl Ϊ��ֵ,zdalconfigLocal Ϊ false")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951013() throws Throwable {
        appDsName = "zdataconsoleUrlIsNull";
        zdalDataSource.setAppDsName(appDsName);
        Step("ZdataconsoleUrl Ϊ��ֵ,zdalconfigLocal Ϊ false�����г�ʼ��");
        try {
            zdalDataSource.init();
        } catch (IllegalArgumentException e) {
            Assert.areEqual(IllegalArgumentException.class, e.getClass(),
                "zdataconsoleUrl Ϊ ''��zdalconfigLocal Ϊ false,���г�ʼ��");
        }
    }

    @Subject("the ZdataconsoleUrl Ϊ '',�� zdalconfigLocal Ϊ true")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951014() throws Throwable {
        int notException = 0;
        appDsName = "zdataconsoleUrlIsNull";
        zdalDataSource.setAppDsName(appDsName);
        Step("the ZdataconsoleUrl Ϊ '',�� zdalconfigLocal Ϊ true,���г�ʼ��");
        try {
            zdalDataSource.init();
        } catch (IllegalArgumentException e) {
            notException = 1;
        }
        Assert.areEqual(0, notException, "not Exception");

    }

    @Subject(" ZdataconsoleUrl Ϊ null, zdalconfigLocal Ϊ false")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951015() throws Throwable {
        appDsName = "zdataconsoleUrlIsNull";
        zdalDataSource.setAppDsName(appDsName);
        Step("ZdataconsoleUrl Ϊ null, zdalconfigLocal Ϊ false,���г�ʼ��");
        try {
            zdalDataSource.init();
        } catch (IllegalArgumentException e) {
            Assert.areEqual(IllegalArgumentException.class, e.getClass(),
                "zdataconsoleUrl Ϊ null��zdalconfigLocal Ϊ false,���г�ʼ��");
        }
    }

    @Subject(" configPath Ϊ ''")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951016() throws Throwable {
        appDsName = "configPathisnull";
        zdalDataSource.setAppDsName(appDsName);
        zdalDataSource.setConfigPath("");
        Step("configPath Ϊ ��ֵ,���г�ʼ��");
        try {
            zdalDataSource.init();
        } catch (IllegalArgumentException e) {
            Assert.areEqual(IllegalArgumentException.class, e.getClass(), "configPath Ϊ '',���г�ʼ��");
        }
    }

    @Subject(" dbmode Ϊ nullֵ")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951017() throws Throwable {
        appDsName = "configPathisnull";
        zdalDataSource.setAppDsName(appDsName);
        //zdalDataSource.setDbmode(null);
        Step("dbmode Ϊ nullֵ,���г�ʼ��");
        try {
            zdalDataSource.init();
        } catch (ZdalClientException e) {
            Assert.areEqual(ZdalClientException.class, e.getClass(), "dbmode Ϊ nullֵ,���г�ʼ��");
        }
    }

    @Subject("ZdataconsoleUrl Ϊ OK,�� localFile�ļ�������")
    @Priority(PriorityLevel.NORMAL)
    @Test
    public void TC951018() throws Throwable {
        appDsName = "testDsNotExist";
        zdalDataSource.setAppDsName(appDsName);
        Step("ZdataconsoleUrl Ϊ OK,�� localFile�ļ������ڣ����г�ʼ��");
        try {
            zdalDataSource.init();
        } catch (ZdalClientException e) {
            Assert.areEqual(ZdalClientException.class, e.getClass(),
                "ZdataconsoleUrl Ϊ OK,�� localFile�ļ������ڣ����г�ʼ��");
        }
    }

}
