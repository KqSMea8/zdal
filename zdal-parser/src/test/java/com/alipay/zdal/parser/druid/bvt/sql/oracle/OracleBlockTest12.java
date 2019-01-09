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
 * @version $Id: OracleBlockTest12.java, v 0.1 2012-5-17 ����10:14:41 xiaoqing.zhouxq Exp $
 */
public class OracleBlockTest12 extends OracleTest {

    public void test_0() throws Exception {
        String sql = "DECLARE" + "  emp_id        NUMBER(6);" + "  emp_lastname  VARCHAR2(25);"
                     + "  emp_salary    NUMBER(8,2);" + "  emp_jobid     VARCHAR2(10);" + "BEGIN"
                     + "  SELECT employee_id, last_name, salary, job_id"
                     + "  INTO emp_id, emp_lastname, emp_salary, emp_jobid" + "  FROM employees"
                     + "  WHERE employee_id = 120;" + ""
                     + "  INSERT INTO emp_name (employee_id, last_name)"
                     + "  VALUES (emp_id, emp_lastname); "
                     + "  INSERT INTO emp_sal (employee_id, salary) "
                     + "  VALUES (emp_id, emp_salary);" + ""
                     + "  INSERT INTO emp_job (employee_id, job_id)"
                     + "  VALUES (emp_id, emp_jobid);" + " " + "EXCEPTION"
                     + "  WHEN DUP_VAL_ON_INDEX THEN" + "    ROLLBACK;"
                     + "    DBMS_OUTPUT.PUT_LINE('Inserts were rolled back');" + "END;"; //

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        for (SQLStatement statement : statementList) {
            statement.accept(visitor);
        }

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(4, visitor.getTables().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("employees")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("emp_name")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("emp_sal")));
        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("emp_job")));

        Assert.assertEquals(11, visitor.getColumns().size());
        Assert.assertEquals(1, visitor.getConditions().size());

        //        Assert.assertTrue(visitor.getColumns().contains(new TableStat.Column("employees", "salary")));
    }
}
