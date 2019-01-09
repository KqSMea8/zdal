/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.test.ut.sqlparser.mysql;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.parser.DefaultSQLParser;
import com.alipay.zdal.parser.GroupFunctionType;
import com.alipay.zdal.parser.SQLParser;
import com.alipay.zdal.parser.result.DefaultSqlParserResult;
import com.alipay.zdal.parser.result.SqlParserResult;

public class SQLParserOfMysqlWithDeleteTest {
    //    private static final String   MYSQL_DELETE        = "delete from  users  where c3 = ? and  c4='xiaoqing.zhouxq'";
    private static final String   MYSQL_DELETE        = "DELETE FROM users where SGW_TRANSACTION_UNIQUE.OUT_ORDER_NO='COMMYZT0000000001' and id=10 and SGW_TRANSACTION_UNIQUE.user = ?";

    private static final Object[] MYSQL_DELETE_ARGS   = new Object[] { 100 };

    private static final String   MYSQL_DELETE_NOBIND = "delete from  users  where c3 = 300 and  c4='xiaoqing.zhouxq'";

    private static final String   PATITION_NAME       = "c3";

    /**
     * ���԰󶨲���ʱ���жϲ�ֹ����Ƿ���ȷ.
     */
    @Test
    public void testParseWithPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(MYSQL_DELETE, DBType.MYSQL);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(Arrays
            .asList(MYSQL_DELETE_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(MYSQL_DELETE_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME);
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(MYSQL_DELETE_ARGS), partinationSet);
        Assert.assertEquals(0, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
            Assert.assertEquals(100, entry.getValue().getValue());
        }
    }

    /**
     * ���԰󶨲����ǣ��������ֶβ���sql����У���ֹ���ᱨ��.
     */
    @Test
    public void testParserWithoutPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(MYSQL_DELETE, DBType.MYSQL);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(Arrays
            .asList(MYSQL_DELETE_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(MYSQL_DELETE_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME + 1);
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(MYSQL_DELETE_ARGS), partinationSet);
        Assert.assertEquals(0, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
            Assert.assertEquals(100, entry.getValue().getValue());
        }
    }

    /**
     * ���԰󶨲���ʱ���ж϶���ֶεĲ�ֹ����Ƿ���ȷ.
     */
    @Test
    public void testParserWithMultiPartinations() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(MYSQL_DELETE, DBType.MYSQL);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(Arrays
            .asList(MYSQL_DELETE_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(MYSQL_DELETE_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("c3");
        partinationSet.add("c4");
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(MYSQL_DELETE_ARGS), partinationSet);

        Assert.assertEquals(0, patitions.size());

        Comparative idCompa = patitions.get("c3");
        //  Assert.assertEquals(Comparative.Equivalent, idCompa.getComparison());
        // Assert.assertEquals(100, idCompa.getValue());

        Comparative nameCompa = patitions.get("c4");
        // Assert.assertEquals(Comparative.Equivalent, nameCompa.getComparison());
        // Assert.assertEquals("xiaoqing.zhouxq", nameCompa.getValue());
    }

    /**
     * ���Էǰ󶨲���ʱ���жϵ����ֶεĲ�ֹ����Ƿ���ȷ.
     */
    @Test
    public void testParserWithNoBindPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(MYSQL_DELETE_NOBIND, DBType.MYSQL);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(null));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(null));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME);
        Map<String, Comparative> partitions = parserResult.getComparativeMapChoicer()
            .getColumnsMap(null, partinationSet);
        Assert.assertEquals(1, partitions.size());
        for (Entry<String, Comparative> entry : partitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals(300, entry.getValue().getValue());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
        }
    }
}
