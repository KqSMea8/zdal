package com.alipay.zdal.test.rw;

import static com.alipay.ats.internal.domain.ATS.Step;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.alipay.zdal.client.ThreadLocalString;
import com.alipay.zdal.client.util.ThreadLocalMap;
import com.alipay.zdal.test.common.ZdalTestBase;

@RunWith(ATSJUnitRunner.class)
@Feature("zdal���oracle����Դ��preparedStatement��executeQuery()��executeUpdate()")
public class SR952070 extends ZdalTestBase {
    private Connection   connection = null;
    public TestAssertion Assert     = new TestAssertion();

    @Before
    public void beforeTestCase() {
        localFile = "./config/rw";
        zdalDataSource.setAppName("zdalPreparedStatement");
        zdalDataSource.setAppDsName("preparedStatementDsOracle");
        zdalDataSource.setConfigPath(localFile);
        zdalDataSource.init();
    }

    @After
    public void afterTestCase() {
        ThreadLocalMap.reset();
    }

    @Subject("preparedStatement��ִ��insert��select")
    @Priority(PriorityLevel.HIGHEST)
    @Test
    public void TC952071() {

        PreparedStatement preparedStatement_1 = null;
        ResultSet rs = null;
        int res_1 = 0;
        int res_2 = 0;
        int res_3 = 0;

        Step("1�� ��֤executeUpdate��insert���");
        String sql = "insert into ACM_TARGET_RECORD (id,test_varchar,test_date,int_field_1,int_field_2,var_field_1,var_field_2) values ("
                     + "99,'DB_G',to_date('2012-06-15 20:46:34','YYYY-MM-DD-HH24:MI:SS'),1,1,'a','b')";
        res_1 = testExecuteUpdate(sql);
        Assert.areEqual(1, res_1, "oracle ����ԴprepareStatement.executeUpdate");

        Step("2�� ��֤executeQuery()��select���");
        String sql1 = "select * from  ACM_TARGET_RECORD where test_varchar='DB_G' ";
        try {
            // ָ��������
            ThreadLocalMap.put(ThreadLocalString.DATABASE_INDEX, 1);
            connection = zdalDataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement_1 = connection.prepareStatement(sql1);
            rs = preparedStatement_1.executeQuery();
            connection.commit();
            connection.setAutoCommit(true);
            Assert.isTrue(rs.next(), "oracle ����ԴprepareStatement.executeQuery");
            preparedStatement_1.close();
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Step("3�� ��֤executeQuery��update���");
        String sql2 = "update ACM_TARGET_RECORD set test_varchar='DB_GG' where id=99";
        res_2 = testExecuteUpdate(sql2);
        Assert.areEqual(1, res_2, "oracle ����ԴprepareStatement.executeUpdate");

        Step("4�� ��֤executeQuery��delete���");
        String sql3 = "delete from ACM_TARGET_RECORD where id =99";
        res_3 = testExecuteUpdate(sql3);

        Assert.areEqual(1, res_3, "oracle ����ԴprepareStatement.executeUpdate");

    }

    /**
     * ִ��executeUpdate
     */
    private int testExecuteUpdate(String sqlStr) {
        int re = 0;
        try {
            PreparedStatement preparedStatement = null;
            connection = zdalDataSource.getConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sqlStr);
            re = preparedStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re;
    }

}
