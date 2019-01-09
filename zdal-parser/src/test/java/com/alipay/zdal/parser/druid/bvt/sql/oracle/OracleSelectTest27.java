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
 * @version $Id: OracleSelectTest27.java, v 0.1 2012-5-17 ����10:20:56 xiaoqing.zhouxq Exp $
 */
public class OracleSelectTest27 extends OracleTest {

    public void test_0() throws Exception {
        String sql = //
        "select object_id tab_id_noprint , -9999999 col_id_noprint "
                + //
                ", 'table '||lower(owner)||' '||object_id||' '||lower(object_name)||' '||y.tid from dba_objects x"
                + //
                ", search.retl_table_config_search y where object_type='TABLE' and x.owner=upper(case when instr(y.TSCHEMA,'|',1,1)>0 then substr(y.TSCHEMA,1,instr(y.TSCHEMA,'|',1,1)-1) else y.TSCHEMA end) and x.object_name=upper(y.TNAME) and ( owner not in ('SYS','SYSTEM','OUTLN','PUBLIC','WMSYS') or ( owner in ('SYS') and object_name in ('TAB$','OBJ$','COL$','CCOL$','CDEF$') ) ) union all select g.object_id as tab_id_noprint, g.column_id as tab_id_noprint , 'column '||g.column_id||' '||g.data_type||' '||g.data_length ||' '||decode(h.column_name,null ,0 ,1)||' '||lower(g.column_name) from ( select a.object_id ,a.owner, a.object_name as table_name, b.name as column_name ,b.segcol# as column_id, b.type# as data_type, b.segcollength as data_length from dba_objects a ,sys.col$ b ,search.retl_table_config_search g where a.object_id=b.obj# and a.object_type='TABLE' and a.owner=upper(case when instr(g.TSCHEMA,'|',1,1)>0 then substr(g.TSCHEMA,1,instr(g.TSCHEMA,'|',1,1)-1) else g.TSCHEMA end) and a.object_name=upper(g.TNAME) and b.segcol#!=0 and ( owner not in ('SYS','SYSTEM','OUTLN','PUBLIC','WMSYS') or ( owner in ('SYS') and a.object_name in ('TAB$','OBJ$','COL$','CCOL$','CDEF$') ) ) ) g, ( select upper(case when instr(TSCHEMA,'|',1,1)>0 then substr(TSCHEMA,1,instr(TSCHEMA,'|',1,1)-1) else TSCHEMA end) as owner, upper(a.tname) as table_name, upper(b.COLUMN_VALUE) as column_name from search.retl_table_config_search a, table(cast(erosazm.str2varlist(a.pks) as erosazm.vartabletype)) b ) h where g.owner=h.owner(+) and g.table_name=h.table_name(+) and g.column_name=h.column_name(+) order by tab_id_noprint,col_id_noprint";

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

        Assert.assertEquals(3, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("dba_objects")));
        Assert.assertTrue(visitor.getTables().containsKey(
            new TableStat.Name("search.retl_table_config_search")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("sys.col$")));

        Assert.assertEquals(20, visitor.getColumns().size());

        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "*")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "YEAR")));
        // Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("pivot_table", "order_mode")));
    }
}
