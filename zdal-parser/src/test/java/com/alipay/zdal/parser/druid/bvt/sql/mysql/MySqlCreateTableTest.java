package com.alipay.zdal.parser.druid.bvt.sql.mysql;

import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.MysqlTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: MySqlCreateTableTest.java, v 0.1 2012-5-17 ����10:05:52 xiaoqing.zhouxq Exp $
 */
public class MySqlCreateTableTest extends MysqlTest {

    public void test_0() throws Exception {
        String sql = "CREATE TABLE t (c CHAR(20) CHARACTER SET utf8 COLLATE utf8_bin);";

        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(1, visitor.getColumns().size());
        Assert.assertEquals(0, visitor.getConditions().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("t")));

        //        Assert.assertTrue(visitor.getColumns().contains(new Column("mytable", "last_name")));
    }
}
