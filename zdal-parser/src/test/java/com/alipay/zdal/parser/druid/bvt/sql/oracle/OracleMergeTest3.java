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
 * @version $Id: OracleMergeTest3.java, v 0.1 2012-5-17 ����10:19:01 xiaoqing.zhouxq Exp $
 */
public class OracleMergeTest3 extends OracleTest {

    public void test_0() throws Exception {
        String sql = "merge /*+ dynamic_sampling(mm 4) dynamic_sampling_est_cdn(mm) dynamic_sampling(m 4) dynamic_sampling_est_cdn(m) */ into sys.mon_mods_all$ mm using (select m.obj# obj#, m.inserts inserts, m.updates updates, m.deletes deletes, m.flags flags, m.timestamp timestamp, m.drop_segments drop_segments from sys.mon_mods$ m, tab$ t where m.obj# = t.obj# ) v on (mm.obj# = v.obj#) when matched then update set mm.inserts = mm.inserts + v.inserts, mm.updates = mm.updates + v.updates, mm.deletes = mm.deletes + v.deletes, mm.flags = mm.flags + v.flags - bitand(mm.flags,v.flags) /* bitor(mm.flags,v.flags) */, mm.timestamp = v.timestamp, mm.drop_segments = mm.drop_segments + v.drop_segments when NOT matched then insert (obj#, inserts, updates, deletes, timestamp, flags, drop_segments) values (v.obj#, v.inserts, v.updates, v.deletes, sysdate, v.flags, v.drop_segments);";

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

        Assert.assertEquals(3, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("sys.mon_mods$")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("tab$")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("sys.mon_mods_all$")));

        Assert.assertEquals(15, visitor.getColumns().size());

        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "employee_id")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "salary")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "department_id")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("bonuses", "employee_id")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("bonuses", "bonus")));
    }

}
