package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;


public class OracleInsertTest7   {
@Test
    public void test_0() throws Exception {
        String sql = "insert into \"OPS$ADMIN\".\"ORASTAT\" select /*+ rule */ :5 statid, 'C' type, :6 version, bitand(h.spare2,7) flags,       ot.name c1, null c2, null c3, c.name c4, u.name c5,       h.distcnt n1, h.density n2, h.spare1 n3, h.sample_size n4, h.null_cnt n5,       h.minimum n6, h.maximum n7, h.avgcln n8,       decode(h.cache_cnt,0,null,1) n9, hg.bucket n10, hg.endpoint n11,       null n12,       h.timestamp# d1,       h.lowval r1, h.hival r2, hg.epvalue ch1     from sys.user$ u,  sys.obj$ ot, sys.col$ c,   sys.hist_head$ h, histgrm$ hg     where       :3 is null and       u.name = :1 and ot.owner# = u.user# and       ot.name = :2 and ot.type# = 2 and        c.obj# = ot.obj# and       (:4 is null or c.name = :4) and       h.obj# = ot.obj# and h.intcol# = c.intcol# and       hg.obj#(+) = h.obj# and hg.intcol#(+) = h.intcol#     union all     select        :5 statid, 'C' type, :6 version, bitand(h.spare2,7) flags,       ot.name c1, op.subname c2, null c3, c.name c4, u.name c5,       h.distcnt n1, h.density n2, h.spare1 n3, h.sample_size n4, h.null_cnt n5,       h.minimum n6, h.maximum n7, h.avgcln n8,       decode(h.cache_cnt,0,null,1) n9, hg.bucket n10, hg.endpoint n11,       null n12,       h.timestamp# d1,       h.lowval r1, h.hival r2, hg.epvalue ch1     from sys.user$ u,  sys.obj$ ot, sys.col$ c,   sys.tabpart$ tp, sys.obj$ op,   sys.hist_head$ h, histgrm$ hg     where       u.name = :1 and ot.owner# = u.user# and       ot.name = :2 and ot.type# = 2 and        c.obj# = ot.obj# and       (:4 is null or c.name = :4) and       tp.bo# = ot.obj# and tp.obj# = op.obj# and       ((:3 is null and :vc_cascade_parts is not null)         or op.subname = :3) and       h.obj# = op.obj# and h.intcol# = c.intcol# and       hg.obj#(+) = h.obj# and hg.intcol#(+) = h.intcol#     union all     select        :5 statid, 'C' type, :6 version, bitand(h.spare2,7) flags,       op.name c1, op.subname c2, null c3, c.name c4, u.name c5,       h.distcnt n1, h.density n2, h.spare1 n3, h.sample_size n4, h.null_cnt n5,       h.minimum n6, h.maximum n7, h.avgcln n8,       decode(h.cache_cnt,0,null,1) n9, hg.bucket n10, hg.endpoint n11,       null n12,       h.timestamp# d1,       h.lowval r1, h.hival r2, hg.epvalue ch1     from sys.user$ u, sys.col$ c,   sys.tabcompart$ tp, sys.obj$ op,   sys.hist_head$ h, histgrm$ hg     where       u.name = :1 and op.owner# = u.user# and       op.name = :2 and op.type# = 19 and        ((:3 is null and :vc_cascade_parts is not null)         or op.subname = :3) and       tp.obj# = op.obj# and c.obj# = tp.bo# and       (:4 is null or c.name = :4) and       h.obj# = op.obj# and h.intcol# = c.intcol# and       hg.obj#(+) = h.obj# and hg.intcol#(+) = h.intcol#     union all     select        :5 statid, 'C' type, :6 version, bitand(h.spare2,7) flags,       op.name c1, op.subname c2, os.subname c3, c.name c4, u.name c5,       h.distcnt n1, h.density n2, h.spare1 n3, h.sample_size n4, h.null_cnt n5,       h.minimum n6, h.maximum n7, h.avgcln n8,       decode(h.cache_cnt,0,null,1) n9, hg.bucket n10, hg.endpoint n11,       null n12,       h.timestamp# d1,       h.lowval r1, h.hival r2, hg.epvalue ch1     from sys.user$ u, sys.col$ c,   sys.tabcompart$ tp, sys.obj$ op,   sys.tabsubpart$ ts, sys.obj$ os,   sys.hist_head$ h, histgrm$ hg     where       u.name = :1 and op.owner# = u.user# and       op.name = :2 and op.type# = 19 and        tp.obj# = op.obj# and c.obj# = tp.bo# and       (:4 is null or c.name = :4) and       ts.pobj# = tp.obj# and ts.obj# = os.obj# and       ((:3 is null and :vc_cascade_parts is not null)         or (op.subname = :3             and (:vc_cascade_parts is not null                    or os.subname is null))              or os.subname = :3) and                 h.obj# = os.obj# and h.intcol# = c.intcol# and       hg.obj#(+) = h.obj# and hg.intcol#(+) = h.intcol#     order by c5,c1,c2,c3,c4,n10";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);
        

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
        System.out.println("--------------------------------");

        Assert.assertEquals(9, visitor.getTables().size());
        Assert.assertEquals(42, visitor.getColumns().size());

        //        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("raises")));
        //        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));
        //
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "employee_id")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "salary")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "commission_pct")));
    }

}
