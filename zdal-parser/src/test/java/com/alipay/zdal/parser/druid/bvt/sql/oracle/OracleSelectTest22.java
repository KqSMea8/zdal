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
 * @version $Id: OracleSelectTest22.java, v 0.1 2012-5-17 ����10:20:38 xiaoqing.zhouxq Exp $
 */
public class OracleSelectTest22 extends OracleTest {

    public void test_0() throws Exception {
        String sql = //
        "select /*+ no_parallel(t) no_parallel_index(t) dbms_stats cursor_sharing_exact use_weak_name_resl dynamic_sampling(0) no_monitoring */ count(*),sum(sys_op_opnsize(\"ID\")),substrb(dump(min(\"ID\"),16,0,32),1,120),substrb(dump(max(\"ID\"),16,0,32),1,120),count(distinct \"GMT_MODIFIED\"),substrb(dump(min(\"GMT_MODIFIED\"),16,0,32),1,120),substrb(dump(max(\"GMT_MODIFIED\"),16,0,32),1,120),count(distinct \"GMT_CREATE\"),substrb(dump(min(\"GMT_CREATE\"),16,0,32),1,120),substrb(dump(max(\"GMT_CREATE\"),16,0,32),1,120),count(\"TRADE_ID\"),count(distinct \"TRADE_ID\"),sum(sys_op_opnsize(\"TRADE_ID\")),substrb(dump(min(\"TRADE_ID\"),16,0,32),1,120),substrb(dump(max(\"TRADE_ID\"),16,0,32),1,120),count(\"STATUS\"),count(distinct \"STATUS\"),sum(sys_op_opnsize(\"STATUS\")),substrb(dump(min(substrb(\"STATUS\",1,32)),16,0,32),1,120),substrb(dump(max(substrb(\"STATUS\",1,32)),16,0,32),1,120),count(\"OWNER\"),count(distinct \"OWNER\"),sum(sys_op_opnsize(\"OWNER\")),substrb(dump(min(substrb(\"OWNER\",1,32)),16,0,32),1,120),substrb(dump(max(substrb(\"OWNER\",1,32)),16,0,32),1,120),count(\"GMT_FETCH_TASK\"),count(distinct \"GMT_FETCH_TASK\"),substrb(dump(min(\"GMT_FETCH_TASK\"),16,0,32),1,120),substrb(dump(max(\"GMT_FETCH_TASK\"),16,0,32),1,120),count(\"GMT_FINISH_TASK\"),count(distinct \"GMT_FINISH_TASK\"),substrb(dump(min(\"GMT_FINISH_TASK\"),16,0,32),1,120),substrb(dump(max(\"GMT_FINISH_TASK\"),16,0,32),1,120),count(\"VERSION\"),count(distinct \"VERSION\"),sum(sys_op_opnsize(\"VERSION\")),substrb(dump(min(\"VERSION\"),16,0,32),1,120),substrb(dump(max(\"VERSION\"),16,0,32),1,120),count(\"RECORD_TYPE\"),count(distinct \"RECORD_TYPE\"),sum(sys_op_opnsize(\"RECORD_TYPE\")),substrb(dump(min(substrb(\"RECORD_TYPE\",1,32)),16,0,32),1,120),substrb(dump(max(substrb(\"RECORD_TYPE\",1,32)),16,0,32),1,120),count(\"TASK_FLOW_LEVEL\"),count(distinct \"TASK_FLOW_LEVEL\"),sum(sys_op_opnsize(\"TASK_FLOW_LEVEL\")),substrb(dump(min(\"TASK_FLOW_LEVEL\"),16,0,32),1,120),substrb(dump(max(\"TASK_FLOW_LEVEL\"),16,0,32),1,120),count(\"DEAL_TYPE\"),count(distinct \"DEAL_TYPE\"),sum(sys_op_opnsize(\"DEAL_TYPE\")),substrb(dump(min(\"DEAL_TYPE\"),16,0,32),1,120),substrb(dump(max(\"DEAL_TYPE\"),16,0,32),1,120),count(\"END_REASON\"),count(distinct \"END_REASON\"),sum(sys_op_opnsize(\"END_REASON\")),substrb(dump(min(\"END_REASON\"),16,0,32),1,120),substrb(dump(max(\"END_REASON\"),16,0,32),1,120),count(\"TRANSIT_TIME\"),count(distinct \"TRANSIT_TIME\"),sum(sys_op_opnsize(\"TRANSIT_TIME\")),substrb(dump(min(\"TRANSIT_TIME\"),16,0,32),1,120),substrb(dump(max(\"TRANSIT_TIME\"),16,0,32),1,120) from \"ESCROW\".\"HT_TASK_TRADE_HISTORY\" sample (   .5000000000) t "; //

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

        Assert.assertEquals(15, visitor.getColumns().size());

        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "*")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "YEAR")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
