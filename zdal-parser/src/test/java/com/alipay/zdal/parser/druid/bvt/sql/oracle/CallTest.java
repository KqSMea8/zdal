/*
 * Copyright 1999-2011 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.zdal.parser.druid.bvt.sql.oracle;

import java.util.List;

import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleOutputVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: CallTest.java, v 0.1 2012-5-17 ����10:12:31 xiaoqing.zhouxq Exp $
 */
public class CallTest extends TestCase {

    public void test_select() throws Exception {
        String sql = "CALL test(1)";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();

        output(statementList);
    }

    private void output(List<SQLStatement> stmtList) {
        StringBuilder out = new StringBuilder();
        OracleOutputVisitor visitor = new OracleOutputVisitor(out);

        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
            visitor.println();
        }

        System.out.println(out.toString());
    }
}
