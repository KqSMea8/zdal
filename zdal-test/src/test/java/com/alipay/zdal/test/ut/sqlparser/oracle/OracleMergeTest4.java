package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleMergeTest4  {
@Test
    public void test_0() throws Exception {
        String sql = "MERGE INTO \"ESCROW \". \"HT_TASK_TRADE_HISTORY_NEW \" SNA$ USING (SELECT \"CURRENT$ \". \"ID \", \"OUTERTAB$ \". \"GMT_MODIFIED \" \"GMT_MODIFIED \", \"OUTERTAB$ \". \"GMT_CREATE \" \"GMT_CREATE \", \"OUTERTAB$ \". \"TRADE_ID \" \"TRADE_ID \", \"OUTERTAB$ \". \"STATUS \" \"STATUS \", \"OUTERTAB$ \". \"OWNER \" \"OWNER \", \"OUTERTAB$ \". \"GMT_FETCH_TASK \" \"GMT_FETCH_TASK \", \"OUTERTAB$ \". \"GMT_FINISH_TASK \" \"GMT_FINISH_TASK \", \"OUTERTAB$ \". \"VERSION \" \"VERSION \", \"OUTERTAB$ \". \"RECORD_TYPE \" \"RECORD_TYPE \", \"OUTERTAB$ \". \"TASK_FLOW_LEVEL \" \"TASK_FLOW_LEVEL \", \"OUTERTAB$ \". \"DEAL_TYPE \" \"DEAL_TYPE \", \"OUTERTAB$ \". \"END_REASON \" \"END_REASON \", \"OUTERTAB$ \". \"TRANSIT_TIME \" \"TRANSIT_TIME \" FROM (SELECT /*+ NO_MERGE NO_MERGE(LL$) ROWID(MAS$) ORDERED USE_NL(MAS$) NO_INDEX(MAS$) PQ_DISTRIBUTE(MAS$,RANDOM,NONE) */ \"MAS$ \". \"ID \" \"ID \" FROM \"ALL_SUMDELTA \" \"LL$ \", \"ESCROW \". \"HT_TASK_TRADE_HISTORY \" \"MAS$ \" WHERE ((LL$.TABLEOBJ# = :1 AND LL$.TIMESTAMP > :2 AND \"MAS$ \".ROWID BETWEEN LL$.LOWROWID AND LL$.HIGHROWID))) CURRENT$, \"ESCROW \". \"HT_TASK_TRADE_HISTORY \" OUTERTAB$ WHERE CURRENT$. \"ID \" = OUTERTAB$. \"ID \") AS OF SNAPSHOT(:SCN) MAS$ ON (SNA$. \"ID \" = MAS$. \"ID \") WHEN MATCHED THEN UPDATE SET SNA$. \"ID \" = MAS$. \"ID \", SNA$. \"GMT_MODIFIED \" = MAS$. \"GMT_MODIFIED \", SNA$. \"GMT_CREATE \" = MAS$. \"GMT_CREATE \", SNA$. \"TRADE_ID \" = MAS$. \"TRADE_ID \", SNA$. \"STATUS \" = MAS$. \"STATUS \", SNA$. \"OWNER \" = MAS$. \"OWNER \", SNA$. \"GMT_FETCH_TASK \" = MAS$. \"GMT_FETCH_TASK \", SNA$. \"GMT_FINISH_TASK \" = MAS$. \"GMT_FINISH_TASK \", SNA$. \"VERSION \" = MAS$. \"VERSION \", SNA$. \"RECORD_TYPE \" = MAS$. \"RECORD_TYPE \", SNA$. \"TASK_FLOW_LEVEL \" = MAS$. \"TASK_FLOW_LEVEL \", SNA$. \"DEAL_TYPE \" = MAS$. \"DEAL_TYPE \", SNA$. \"END_REASON \" = MAS$. \"END_REASON \", SNA$. \"TRANSIT_TIME \" = MAS$. \"TRANSIT_TIME \" WHEN NOT MATCHED THEN INSERT ( \"ID \", \"GMT_MODIFIED \", \"GMT_CREATE \", \"TRADE_ID \", \"STATUS \", \"OWNER \", \"GMT_FETCH_TASK \", \"GMT_FINISH_TASK \", \"VERSION \", \"RECORD_TYPE \", \"TASK_FLOW_LEVEL \", \"DEAL_TYPE \", \"END_REASON \", \"TRANSIT_TIME \") VALUES (MAS$. \"ID \",MAS$. \"GMT_MODIFIED \",MAS$. \"GMT_CREATE \",MAS$. \"TRADE_ID \",MAS$. \"STATUS \",MAS$. \"OWNER \",MAS$. \"GMT_FETCH_TASK \",MAS$. \"GMT_FINISH_TASK \",MAS$. \"VERSION \",MAS$. \"RECORD_TYPE \",MAS$. \"TASK_FLOW_LEVEL \",MAS$. \"DEAL_TYPE \",MAS$. \"END_REASON \",MAS$. \"TRANSIT_TIME \")";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());

        Assert.assertEquals(3, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("ALL_SUMDELTA")));
        Assert.assertTrue(visitor.getTables().containsKey(
            new TableStat.Name("ESCROW.HT_TASK_TRADE_HISTORY")));
        Assert.assertTrue(visitor.getTables().containsKey(
            new TableStat.Name("ESCROW.HT_TASK_TRADE_HISTORY_NEW")));

        Assert.assertEquals(34, visitor.getColumns().size());

        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "employee_id")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "salary")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "department_id")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("bonuses", "employee_id")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("bonuses", "bonus")));
    }

}
