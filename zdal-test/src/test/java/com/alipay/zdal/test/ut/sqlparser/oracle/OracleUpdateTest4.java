package com.alipay.zdal.test.ut.sqlparser.oracle;

import java.util.List;

import org.junit.Test;

import junit.framework.Assert;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alipay.zdal.parser.sql.stat.TableStat;


public class OracleUpdateTest4   {
@Test
    public void test_0() throws Exception {
        String sql = "update tabpart$ set "
                     + //
                     "dataobj# = :1, part# = :2, hiboundlen = :3, hiboundval = :4, ts# = :5, file# = :6, block# = :7, pctfree$ = :8, pctused$ = :9, initrans = :10, maxtrans = :11, flags = :12, analyzetime = :13, samplesize = :14, rowcnt = :15, blkcnt = :16, empcnt = :17, avgspc = :18, chncnt = :19, avgrln = :20, bhiboundval = EMPTY_BLOB() "
                     + //
                     "where obj# = :21 " + //
                     "returning bhiboundval into :22"; //

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

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("tabpart$")));

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(22, visitor.getColumns().size());

        Assert.assertTrue(visitor.getColumns().contains(
            new TableStat.Column("tabpart$", "dataobj#")));
        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("sys.col_usage$", "intcol#")));
    }

}
