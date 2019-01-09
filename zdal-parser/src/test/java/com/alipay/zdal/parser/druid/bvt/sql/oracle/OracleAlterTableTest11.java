package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import java.util.List;

import junit.framework.Assert;

import com.alipay.zdal.parser.druid.sql.OracleTest;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.stat.TableStat;
import com.alipay.zdal.parser.visitor.ZdalOracleSchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleAlterTableTest11.java, v 0.1 2012-5-17 ����10:13:24 xiaoqing.zhouxq Exp $
 */
public class OracleAlterTableTest11 extends OracleTest {

    public void test_0() throws Exception {
        String sql = //
        "alter table PRODUCT_IDS_ZZJ_TBD0209 move tablespace MCSHADOWTS";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(1, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(
            new TableStat.Name("PRODUCT_IDS_ZZJ_TBD0209")));

        Assert.assertEquals(0, visitor.getColumns().size());

        //         Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("PRODUCT_IDS_ZZJ_TBD0209", "discount_amount")));
        //         Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("ws_affiliate_tran_product", "commission_amount")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
