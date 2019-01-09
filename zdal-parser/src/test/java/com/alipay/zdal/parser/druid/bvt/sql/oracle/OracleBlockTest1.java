package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.OracleTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleBlockTest1.java, v 0.1 2012-5-17 ����10:14:29 xiaoqing.zhouxq Exp $
 */
public class OracleBlockTest1 extends OracleTest {

    public void test_0() throws Exception {
        String sql = "DECLARE   n        NUMBER;   str_stmt VARCHAR2(4000);   sql_text ora_name_list_t;   l_trace  NUMBER;   l_alert  NUMBER; BEGIN   n := ora_sql_txt(sql_text);   FOR i IN 1 .. n LOOP     str_stmt := SUBSTR(str_stmt || sql_text(i), 1, 300);   END LOOP;    SELECT COUNT(*)     INTO l_trace     FROM DUAL    WHERE (sys_context('userenv', 'ip_address') IS NOT NULL)      and lower(str_stmt) like 'alter% compile%';    IF l_trace > 0 THEN     RAISE_APPLICATION_ERROR(-20001,'Please try later,DBA is publishing DDL for project');   END IF; END;";

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
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(0, visitor.getTables().size());

        //        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("departments")));
        //        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));

        //        Assert.assertEquals(0, visitor.getColumns().size());

        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("UNKNOWN", "location_id")));
    }
}
