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
package com.alipay.zdal.parser.druid.bvt.sql.mysql;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alipay.zdal.parser.sql.parser.SQLStatementParser;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: REPLACE_Syntax_Test.java, v 0.1 2012-5-17 ����10:07:37 xiaoqing.zhouxq Exp $
 */
public class REPLACE_Syntax_Test extends TestCase {

    public void test_0() throws Exception {
        String sql = "REPLACE INTO T SELECT * FROM T;";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);

        Assert.assertEquals("REPLACE INTO T\n\tSELECT *\n\tFROM T;", text);
    }

    public void test_1() throws Exception {
        String sql = "REPLACE DELAYED INTO `online_users` SET `session_id`='3580cc4e61117c0785372c426eddd11c', `user_id` = 'XXX', `page` = '/', `lastview` = NOW();";

        SQLStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> stmtList = parser.parseStatementList();

        String text = output(stmtList);
        System.out.println(text);
        Assert
            .assertEquals(
                "REPLACE DELAYED INTO `online_users` (`session_id`, `user_id`, `page`, `lastview`)\nVALUES ('3580cc4e61117c0785372c426eddd11c', 'XXX', '/', NOW());",
                text);
    }

    private String output(List<SQLStatement> stmtList) {
        StringBuilder out = new StringBuilder();

        for (SQLStatement stmt : stmtList) {
            stmt.accept(new MySqlOutputVisitor(out));
            out.append(";");
        }

        return out.toString();
    }
}
