/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import com.alipay.zdal.common.DBType;
import com.alipay.zdal.common.sqljep.function.Comparative;
import com.alipay.zdal.parser.exceptions.SqlParserException;
import com.alipay.zdal.parser.result.DefaultSqlParserResult;
import com.alipay.zdal.parser.result.SqlParserResult;

/**
 * 测试mysql的insert的sql语句.
 * @author xiaoqing.zhouxq
 * @version $Id: SQLParserOfMysqlWithInsertTest.java, v 0.1 2012-5-29 上午10:42:53 xiaoqing.zhouxq Exp $
 */
public class SQLParserOfMysqlWithInsertTest {
    private static final String   MYSQL_INSERT        = "insert into users (id, gmt_create,name) values(?,now(), ?)";

    private static final Object[] MYSQL_INSERT_ARGS   = new Object[] { 100, "zhouxiaoqing" };

    private static final String   MYSQL_INSERT_NOBIND = "insert into users (id, gmt_create,name) values(22,now(), 'xiaoqing.zhouxq')";

    private static final String   PATITION_NAME       = "name";

    /**
     * 测试绑定参数时，判断拆分规则是否正确.
     */
    @Test
    public void testParseWithPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(MYSQL_INSERT, DBType.MYSQL);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(Arrays
            .asList(MYSQL_INSERT_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(MYSQL_INSERT_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME);
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(MYSQL_INSERT_ARGS), partinationSet);
        Assert.assertEquals(1, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
            Assert.assertEquals("zhouxiaoqing", entry.getValue().getValue());
        }
    }

    /**
     * 测试绑定参数是，如果拆分字段不在sql语句中，拆分规则会报错.
     */
    @Test(expected = SqlParserException.class)
    public void testParserWithoutPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(MYSQL_INSERT, DBType.MYSQL);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(Arrays
            .asList(MYSQL_INSERT_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(MYSQL_INSERT_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add(PATITION_NAME + 1);
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(MYSQL_INSERT_ARGS), partinationSet);
        Assert.assertEquals(1, patitions.size());
        for (Entry<String, Comparative> entry : patitions.entrySet()) {
            Assert.assertEquals(PATITION_NAME, entry.getKey());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
            Assert.assertEquals("zhouxiaoqing", entry.getValue().getValue());
        }
    }

    /**
     * 测试绑定参数时，判断多个字段的拆分规则是否正确.
     */
    @Test
    public void testParserWithMultiPartinations() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(MYSQL_INSERT, DBType.MYSQL);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getMax(Arrays
            .asList(MYSQL_INSERT_ARGS)));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(Arrays
            .asList(MYSQL_INSERT_ARGS)));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("id");
        partinationSet.add(PATITION_NAME);
        Map<String, Comparative> patitions = parserResult.getComparativeMapChoicer().getColumnsMap(
            Arrays.asList(MYSQL_INSERT_ARGS), partinationSet);

        Assert.assertEquals(2, patitions.size());

        Comparative idCompa = patitions.get("id");
        Assert.assertEquals(Comparative.Equivalent, idCompa.getComparison());
        Assert.assertEquals(100, idCompa.getValue());

        Comparative nameCompa = patitions.get(PATITION_NAME);
        Assert.assertEquals(Comparative.Equivalent, nameCompa.getComparison());
        Assert.assertEquals("zhouxiaoqing", nameCompa.getValue());
    }

    /**
     * 测试非绑定参数时，判断单个字段的拆分规则是否正确.
     */
    @Test
    public void testParserWithNoBindPartination() {
        SQLParser sqlParser = new DefaultSQLParser();
        SqlParserResult parserResult = sqlParser.parse(MYSQL_INSERT_NOBIND, DBType.MYSQL);
        Assert.assertEquals("users", parserResult.getTableName());
        Assert.assertEquals(true, parserResult.getGroupByEles().isEmpty());
        Assert.assertEquals(GroupFunctionType.NORMAL, parserResult.getGroupFuncType());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult
            .getMax(new ArrayList<Object>()));
        Assert.assertEquals(true, parserResult.getOrderByEles().isEmpty());
        Assert.assertEquals(DefaultSqlParserResult.DEFAULT_SKIP_MAX, parserResult.getSkip(null));

        Set<String> partinationSet = new HashSet<String>();
        partinationSet.add("id");
        Map<String, Comparative> partitions = parserResult.getComparativeMapChoicer()
            .getColumnsMap(null, partinationSet);
        Assert.assertEquals(1, partitions.size());
        for (Entry<String, Comparative> entry : partitions.entrySet()) {
            Assert.assertEquals("id", entry.getKey());
            Assert.assertEquals(22, entry.getValue().getValue());
            Assert.assertEquals(Comparative.Equivalent, entry.getValue().getComparison());
        }
    }
}
