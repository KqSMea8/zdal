package com.alipay.zdal.parser.druid.sql.oracle.demo;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.SQLUtils;
import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.ast.expr.SQLBinaryOpExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLIdentifierExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLNumericLiteralExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLPropertyExpr;
import com.alipay.zdal.parser.sql.ast.expr.SQLVariantRefExpr;
import com.alipay.zdal.parser.sql.ast.statement.SQLExprTableSource;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: Demo2.java, v 0.1 2012-5-17 ����10:25:09 xiaoqing.zhouxq Exp $
 */
public class Demo2 extends TestCase {

    public void test_0() throws Exception {
        String sql = "select * from user where uid = 2 and uname = ?";
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(1);
        parameters.add("wenshao");

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList(); //

        SQLStatement first = (SQLStatement) stmtList.get(0);

        MyVisitor visitor = new MyVisitor();
        first.accept(visitor);

        SQLExpr firstVar = visitor.getVariantList().get(0);

        int userId;
        if (firstVar instanceof SQLVariantRefExpr) {
            int varIndex = (Integer) firstVar.getAttribute("varIndex");
            userId = (Integer) parameters.get(varIndex);
        } else {
            userId = ((SQLNumericLiteralExpr) firstVar).getNumber().intValue();
        }

        String tableName;
        if (userId == 1) {
            tableName = "user_1";
        } else {
            tableName = "user_x";
        }

        for (SQLExprTableSource tableSource : visitor.getTableSourceList()) {
            SQLExpr expr = tableSource.getExpr();
            if (expr instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr identExpr = (SQLIdentifierExpr) expr;
                String ident = identExpr.getName();

                if (ident.equals("user")) {
                    identExpr.setName(tableName);
                }
            } else if (expr instanceof SQLPropertyExpr) {
                SQLPropertyExpr proExpr = (SQLPropertyExpr) expr;
                String ident = proExpr.getName();

                if (ident.equals("user")) {
                    proExpr.setName(tableName);
                }
            }
        }

        String realSql = SQLUtils.toOracleString(first);
        System.out.println(realSql);
    }

    private static class MyVisitor extends MySqlASTVisitorAdapter {

        private int                      varIndex        = 0;
        private List<SQLExpr>            variantList     = new ArrayList<SQLExpr>();
        private List<SQLExprTableSource> tableSourceList = new ArrayList<SQLExprTableSource>();

        public boolean visit(SQLVariantRefExpr x) {
            x.getAttributes().put("varIndex", varIndex++);
            return true;
        }

        public boolean visit(SQLBinaryOpExpr x) {
            if (x.getLeft() instanceof SQLIdentifierExpr) {
                if (x.getRight() instanceof SQLVariantRefExpr) {
                    SQLIdentifierExpr identExpr = (SQLIdentifierExpr) x.getLeft();
                    String ident = identExpr.getName();
                    if (ident.equals("uid")) {
                        variantList.add(x.getRight());
                    }
                } else if (x.getRight() instanceof SQLNumericLiteralExpr) {
                    variantList.add(x.getRight());
                }
            }

            return true;
        }

        public boolean visit(SQLExprTableSource x) {
            tableSourceList.add(x);
            return true;
        }

        //        public int getVarIndex() {
        //            return varIndex;
        //        }
        //
        //        public void setVarIndex(int varIndex) {
        //            this.varIndex = varIndex;
        //        }

        public List<SQLExpr> getVariantList() {
            return variantList;
        }

        public List<SQLExprTableSource> getTableSourceList() {
            return tableSourceList;
        }

    }

}
