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
 * @version $Id: OracleUpdateTest2.java, v 0.1 2012-5-17 ����10:22:40 xiaoqing.zhouxq Exp $
 */
public class OracleUpdateTest2 extends OracleTest {

    public void test_0() throws Exception {
        String sql = "UPDATE wrh$_tempfile tfh " //
                     + "    SET (snap_id, filename, tsname) =" //
                     + "    (SELECT :lah_snap_id, tf.name name, ts.name tsname"
                     + //
                     "      FROM v$tempfile tf, ts$ ts"
                     + //
                     "      WHERE tf.ts# = ts.ts# AND tfh.file# = tf.file# AND tfh.creation_change# = tf.creation_change#"
                     + //
                     "  )"
                     + //
                     "WHERE (file#, creation_change#) IN        ("
                     + //
                     "          SELECT tf.tfnum, to_number(tf.tfcrc_scn) creation_change#           "
                     + //
                     "          FROM x$kcctf tf           " + //
                     "          WHERE tf.tfdup != 0)    AND dbid    = :dbid    AND snap_id < :snap_id"; //

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
        System.out.println("--------------------------------");

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("wrh$_tempfile")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("x$kcctf")));

        Assert.assertEquals(4, visitor.getTables().size());
        Assert.assertEquals(18, visitor.getColumns().size());

        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("table1", "column")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("table2", "expr")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("table2", "column")));
    }

}
