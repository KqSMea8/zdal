package com.alipay.zdal.parser.druid.sql.oracle.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.alipay.zdal.parser.sql.ast.statement.SQLSelectQueryBlock;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: Demo3.java, v 0.1 2012-5-17 ����10:25:13 xiaoqing.zhouxq Exp $
 */
public class Demo3 extends TestCase {

    public void test_0() throws Exception {
        String sql = "select * from user u where u.uid = 2 and uname = ?";
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(1);
        parameters.add("wenshao");

        String realSql = convert(sql, parameters);
        System.out.println(realSql);
    }

    public void test_1() throws Exception {
        String sql = "select * from user where uid = ? and uname = ?";
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(1);
        parameters.add("wenshao");

        String realSql = convert(sql, parameters);
        System.out.println(realSql);
    }

    public void test_2() throws Exception {
        String sql = "select * from (select * from user where uid = ? and uname = ?) t";
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(1);
        parameters.add("wenshao");

        String realSql = convert(sql, parameters);
        System.out.println(realSql);
    }

    public void test_3() throws Exception {
        String sql = "select * from groups where uid = ? and uname = ? or t=?";
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(1);
        parameters.add("wenshao");

        String realSql = convert(sql, parameters);
        System.out.println(realSql);
    }

    private String convert(String sql, List<Object> parameters) {
        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList(); //

        SQLStatement first = (SQLStatement) stmtList.get(0);

        MyVisitor visitor = new MyVisitor();
        first.accept(visitor);

        if (visitor.getVariantList().size() > 0) {
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
        }

        String realSql = SQLUtils.toOracleString(first);
        return realSql;
    }

    private static class MyVisitor extends MySqlASTVisitorAdapter {

        private int                      varIndex        = 0;
        private List<SQLExpr>            variantList     = new ArrayList<SQLExpr>();
        private List<SQLExprTableSource> tableSourceList = new ArrayList<SQLExprTableSource>();

        private Map<String, String>      tableAlias      = new HashMap<String, String>();
        private String                   defaultTableName;

        public boolean visit(SQLVariantRefExpr x) {
            x.getAttributes().put("varIndex", varIndex++);
            return true;
        }

        public boolean visit(SQLBinaryOpExpr x) {
            if (isUserId(x.getLeft())) {
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

        private boolean isUserId(SQLExpr x) {
            if (x instanceof SQLIdentifierExpr) {
                if ("user".equals(defaultTableName)
                    && "uid".equals(((SQLIdentifierExpr) x).getName())) {
                    return true;
                }

                return false;
            }

            if (x instanceof SQLPropertyExpr) {
                SQLPropertyExpr propExpr = (SQLPropertyExpr) x;

                String columnName = propExpr.getName();

                if (!"uid".equals(columnName)) {
                    return false;
                }

                if (propExpr.getOwner() instanceof SQLIdentifierExpr) {
                    String ownerName = ((SQLIdentifierExpr) propExpr.getOwner()).getName();
                    if ("user".equals(ownerName) || "user".equals(tableAlias.get(ownerName))) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean visit(SQLExprTableSource x) {
            recordTableSource(x);

            return true;
        }

        private String recordTableSource(SQLExprTableSource x) {
            if (x.getExpr() instanceof SQLIdentifierExpr) {
                String tableName = ((SQLIdentifierExpr) x.getExpr()).getName();

                if (x.getAlias() != null) {
                    tableAlias.put(x.getAlias(), tableName);
                }

                if ("user".equals(tableName)) {
                    if (!tableSourceList.contains(x)) {
                        tableSourceList.add(x);
                    }
                }

                return tableName;
            }

            return null;
        }

        public boolean visit(SQLSelectQueryBlock queryBlock) {
            if (queryBlock.getFrom() instanceof SQLExprTableSource) {
                defaultTableName = recordTableSource((SQLExprTableSource) queryBlock.getFrom());
            }
            return true;
        }

        public boolean visit(MySqlSelectQueryBlock queryBlock) {
            if (queryBlock.getFrom() instanceof SQLExprTableSource) {
                defaultTableName = recordTableSource((SQLExprTableSource) queryBlock.getFrom());
            }
            return true;
        }

        public List<SQLExpr> getVariantList() {
            return variantList;
        }

        public List<SQLExprTableSource> getTableSourceList() {
            return tableSourceList;
        }

    }

}
