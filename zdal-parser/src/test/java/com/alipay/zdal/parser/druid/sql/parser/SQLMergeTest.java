package com.alipay.zdal.parser.druid.sql.parser;

import java.util.List;

import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.ast.expr.SQLInListExpr;
import com.alipay.zdal.parser.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleOutputVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: SQLMergeTest.java, v 0.1 2012-5-17 ����10:25:35 xiaoqing.zhouxq Exp $
 */
public class SQLMergeTest extends TestCase {

    protected String sql;

    protected void setUp() throws Exception {
        sql = "SELECT /*mark for picman*/ * FROM WP_ALBUM WHERE MEMBER_ID = ? AND ID IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        sql = "SELECT * FROM my_table WHERE TRUNC(SYSDATE) = DATE '2002-10-03';";
    }

    public void test_merge_1() throws Exception {
        String result = merge();
        System.out.println(result);

    }

    public void test_merge_2() throws Exception {
        String result = merge2();
        System.out.println(result);
    }

    public void test_perf() {
        for (int i = 0; i < 10; ++i) {
            perf();
        }
    }

    void perf() {
        long startMillis = System.currentTimeMillis();
        for (int i = 0; i < 1000 * 1000; ++i) {
            merge();
        }
        long millis = System.currentTimeMillis() - startMillis;
        System.out.println(millis);
    }

    private String merge() {
        StringBuilder out = new StringBuilder();
        OracleOutputVisitor visitor = new OracleOutputVisitor(out) {

            public boolean visit(SQLInListExpr x) {
                x.getExpr().accept(this);

                if (x.isNot()) {
                    print(" NOT IN (##)");
                } else {
                    print(" IN (##)");
                }
                return false;
            }
        };

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        for (SQLStatement statement : statementList) {
            statement.accept(visitor);
            visitor.println();
        }

        return out.toString();
    }

    private String merge2() {
        String sql = "INSERT INTO T (F1, F2, F3, F4, F5) VALUES (?, ?, ?, ?, ?), (?, ?, ?, ?, ?), (?, ?, ?, ?, ?)";

        StringBuilder out = new StringBuilder();
        MySqlOutputVisitor visitor = new MySqlOutputVisitor(out) {

            public boolean visit(SQLInListExpr x) {
                x.getExpr().accept(this);

                if (x.isNot()) {
                    print(" NOT IN (##)");
                } else {
                    print(" IN (##)");
                }
                return false;
            }

            @Override
            public boolean visit(MySqlInsertStatement x) {
                print("INSERT ");

                if (x.isLowPriority()) {
                    print("LOW_PRIORITY ");
                }

                if (x.isDelayed()) {
                    print("DELAYED ");
                }

                if (x.isHighPriority()) {
                    print("HIGH_PRIORITY ");
                }

                if (x.isIgnore()) {
                    print("IGNORE ");
                }

                print("INTO ");

                x.getTableName().accept(this);

                if (x.getColumns().size() > 0) {
                    print(" (");
                    for (int i = 0, size = x.getColumns().size(); i < size; ++i) {
                        if (i != 0) {
                            print(", ");
                        }
                        x.getColumns().get(i).accept(this);
                    }
                    print(")");
                }

                if (x.getValuesList().size() != 0) {
                    print(" VALUES ");
                    int size = x.getValuesList().size();
                    if (size == 0) {
                        print("()");
                    } else {
                        for (int i = 0; i < 1; ++i) {
                            if (i != 0) {
                                print(", ");
                            }
                            x.getValuesList().get(i).accept(this);
                        }
                    }
                }
                if (x.getQuery() != null) {
                    print(" ");
                    x.getQuery().accept(this);
                }

                if (x.getDuplicateKeyUpdate().size() != 0) {
                    print(" ON DUPLICATE KEY UPDATE ");
                    printAndAccept(x.getDuplicateKeyUpdate(), ", ");
                }

                return false;
            }
        };

        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        for (SQLStatement statement : statementList) {
            statement.accept(visitor);
            visitor.println();
        }

        return out.toString();
    }
}
