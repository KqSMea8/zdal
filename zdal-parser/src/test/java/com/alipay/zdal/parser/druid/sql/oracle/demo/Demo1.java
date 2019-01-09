package com.alipay.zdal.parser.druid.sql.oracle.demo;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.SQLUtils;
import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLIdentifierExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLPropertyExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLVariantRefExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLExprTableSource;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: Demo1.java, v 0.1 2012-5-17 ����10:24:26 xiaoqing.zhouxq Exp $
 */
public class Demo1 extends TestCase {

    public void test_0() throws Exception {
        String sql = "select * from user where uid = ? and uname = ?";
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(1);
        parameters.add("wenshao");

        SQLStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList(); //

        SQLStatement first = (SQLStatement) stmtList.get(0);

        GetVariantVisitor variantVisitor = new GetVariantVisitor();
        first.accept(variantVisitor);

        SQLVariantRefExpr firstVar = variantVisitor.getVariantList().get(0);

        int varIndex = (Integer) firstVar.getAttribute("varIndex");
        Integer param = (Integer) parameters.get(varIndex);

        String tableName;
        if (param.intValue() == 1) {
            tableName = "user_1";
        } else {
            tableName = "user_x";
        }

        MyOracleVisitor visitor = new MyOracleVisitor(tableName);
        first.accept(visitor);

        String realSql = SQLUtils.toOracleString(first);
        System.out.println(realSql);
    }

    private static class GetVariantVisitor extends OracleASTVisitorAdapter {

        private int                     varIndex    = 0;
        private List<SQLVariantRefExpr> variantList = new ArrayList<SQLVariantRefExpr>();

        public boolean visit(SQLVariantRefExpr x) {
            x.getAttributes().put("varIndex", varIndex++);
            return true;
        }

        public boolean visit(SQLBinaryOpExpr x) {
            if (x.getLeft() instanceof SQLIdentifierExpr
                && x.getRight() instanceof SQLVariantRefExpr) {
                SQLIdentifierExpr identExpr = (SQLIdentifierExpr) x.getLeft();
                String ident = identExpr.getName();
                if (ident.equals("uid")) {
                    variantList.add((SQLVariantRefExpr) x.getRight());
                }
            }

            return true;
        }

        //        public int getVarIndex() {
        //            return varIndex;
        //        }
        //
        //        public void setVarIndex(int varIndex) {
        //            this.varIndex = varIndex;
        //        }

        public List<SQLVariantRefExpr> getVariantList() {
            return variantList;
        }

        //        public void setVariantList(List<SQLVariantRefExpr> variantList) {
        //            this.variantList = variantList;
        //        }

    }

    private static class MyOracleVisitor extends OracleASTVisitorAdapter {

        private String tableName;

        public MyOracleVisitor(String tableName) {
            this.tableName = tableName;
        }

        public boolean visit(OracleSelectTableReference x) {
            SQLExpr expr = x.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr identExpr = (SQLIdentifierExpr) expr;
                String tableName = identExpr.getName();

                if (tableName.equals("user")) {
                    identExpr.setName(this.tableName);
                }
            } else if (expr instanceof SQLPropertyExpr) {
                SQLPropertyExpr proExpr = (SQLPropertyExpr) expr;
                String tableName = proExpr.getName();

                if (tableName.equals("user")) {
                    proExpr.setName(this.tableName);
                }
            }

            return true;
        }

        public boolean visit(SQLExprTableSource x) {
            SQLExpr expr = x.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr identExpr = (SQLIdentifierExpr) expr;
                String tableName = identExpr.getName();

                if (tableName.equals("user")) {
                    identExpr.setName(this.tableName);
                }
            } else if (expr instanceof SQLPropertyExpr) {
                SQLPropertyExpr proExpr = (SQLPropertyExpr) expr;
                String tableName = proExpr.getName();

                if (tableName.equals("user")) {
                    proExpr.setName(this.tableName);
                }
            }

            return true;
        }
    }

}
