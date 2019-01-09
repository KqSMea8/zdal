package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.OracleTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleMergeTest5.java, v 0.1 2012-5-17 ����10:19:10 xiaoqing.zhouxq Exp $
 */
public class OracleMergeTest5 extends OracleTest {

    public void test_0() throws Exception {
        String sql = "MERGE "
                     + //
                     "INTO MEMBER_LAST_LOGIN M2 "
                     + //
                     "USING MEMBER_LAST_LOGIN_HZ M1 ON (M1.ID = M2.ID) "
                     + //
                     "  WHEN MATCHED THEN "
                     + //
                     "      UPDATE SET M2.LAST_LOGIN_TIME = M1.LAST_LOGIN_TIME, M2.GMT_MODIFIED = M1.GMT_MODIFIED"
                     + //
                     "        , M2.OWNER_SEQ = M1.OWNER_SEQ, M2.OWNER_MEMBER_ID = M1.OWNER_MEMBER_ID, M2.IP = M1.IP "
                     + //
                     "  WHEN NOT MATCHED THEN " + //
                     "      INSERT VALUES (M1.ID, M1.GMT_CREATE, M1.GMT_MODIFIED, M1.OWNER_SEQ" + //
                     "        , M1.LAST_LOGIN_TIME, M1.OWNER_MEMBER_ID, M1.IP)";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());

        Assert.assertEquals(2, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(
            new TableStat.Name("MEMBER_LAST_LOGIN_HZ")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("MEMBER_LAST_LOGIN")));

        Assert.assertEquals(12, visitor.getColumns().size());

        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "employee_id")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "salary")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "department_id")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("bonuses", "employee_id")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("bonuses", "bonus")));
    }

}
