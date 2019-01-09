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
 * @version $Id: OracleSelectTest24.java, v 0.1 2012-5-17 ����10:20:45 xiaoqing.zhouxq Exp $
 */
public class OracleSelectTest24 extends OracleTest {

    public void test_0() throws Exception {
        String sql = //
        "select /*+ no_parallel_index(t, \"HT_TASK_TRADE_HIS_GOR_IND \") dbms_stats cursor_sharing_exact use_weak_name_resl dynamic_sampling(0) no_monitoring no_expand index_ffs(t, \"HT_TASK_TRADE_HIS_GOR_IND \") */ count(*) as nrw,count(distinct sys_op_lbid(196675,'L',t.rowid)) as nlb,count(distinct hextoraw(sys_op_descend( \"GMT_MODIFIED \")||sys_op_descend( \"OWNER \")||sys_op_descend( \"RECORD_TYPE \"))) as ndk,sys_op_countchg(substrb(t.rowid,1,15),1) as clf from  \"ESCROW \". \"HT_TASK_TRADE_HISTORY \" sample block ( 34.6426417370,1)  t where  \"GMT_MODIFIED \" is not null or  \"OWNER \" is not null or  \"RECORD_TYPE \" is not null"; //

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());

        Assert.assertEquals(1, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(
            new TableStat.Name("ESCROW.HT_TASK_TRADE_HISTORY")));

        Assert.assertEquals(5, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("ESCROW.HT_TASK_TRADE_HISTORY", "*")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "YEAR")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
