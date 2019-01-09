/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.alipay.zdal.parser.sql.ast.SQLExpr;
import com.alipay.zdal.parser.sql.ast.SQLName;
import com.alipay.zdal.parser.sql.ast.SQLObject;
import com.alipay.zdal.parser.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * 
 * @author 伯牙
 * @version $Id: OracleAlterTableSplitPartition.java, v 0.1 2012-11-17 下午3:45:51 Exp $
 */
public class OracleAlterTableSplitPartition extends OracleAlterTableItem {

    private static final long              serialVersionUID = 1L;

    private SQLName                        name;
    private List<SQLExpr>                  at               = new ArrayList<SQLExpr>();
    private List<SQLExpr>                  values           = new ArrayList<SQLExpr>();
    private List<NestedTablePartitionSpec> into             = new ArrayList<NestedTablePartitionSpec>();

    private UpdateIndexesClause            updateIndexes    = null;

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
            acceptChild(visitor, at);
            acceptChild(visitor, values);
            acceptChild(visitor, updateIndexes);
        }
        visitor.endVisit(this);
    }

    public UpdateIndexesClause getUpdateIndexes() {
        return updateIndexes;
    }

    public void setUpdateIndexes(UpdateIndexesClause updateIndexes) {
        this.updateIndexes = updateIndexes;
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

    public List<SQLExpr> getAt() {
        return at;
    }

    public void setAt(List<SQLExpr> at) {
        this.at = at;
    }

    public List<NestedTablePartitionSpec> getInto() {
        return into;
    }

    public void setInto(List<NestedTablePartitionSpec> into) {
        this.into = into;
    }

    public List<SQLExpr> getValues() {
        return values;
    }

    public void setValues(List<SQLExpr> values) {
        this.values = values;
    }

    public static class NestedTablePartitionSpec extends OracleSQLObjectImpl {

        private static final long serialVersionUID      = 1L;

        private SQLName           partition;

        private List<SQLObject>   segmentAttributeItems = new ArrayList<SQLObject>();

        @Override
        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, partition);
                acceptChild(visitor, segmentAttributeItems);
            }
            visitor.endVisit(this);
        }

        public SQLName getPartition() {
            return partition;
        }

        public void setPartition(SQLName partition) {
            this.partition = partition;
        }

        public List<SQLObject> getSegmentAttributeItems() {
            return segmentAttributeItems;
        }

        public void setSegmentAttributeItems(List<SQLObject> segmentAttributeItems) {
            this.segmentAttributeItems = segmentAttributeItems;
        }

    }

    public static class TableSpaceItem extends OracleSQLObjectImpl {

        private static final long serialVersionUID = 1L;
        private SQLName           tablespace;

        public TableSpaceItem() {

        }

        public TableSpaceItem(SQLName tablespace) {
            this.tablespace = tablespace;
        }

        @Override
        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, tablespace);
            }
            visitor.endVisit(this);
        }

        public SQLName getTablespace() {
            return tablespace;
        }

        public void setTablespace(SQLName tablespace) {
            this.tablespace = tablespace;
        }
    }

    public static class UpdateIndexesClause extends OracleSQLObjectImpl {

        private static final long serialVersionUID = 1L;
        private List<SQLObject>   items            = new ArrayList<SQLObject>();

        @Override
        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, items);
            }
            visitor.endVisit(this);
        }

        public List<SQLObject> getItems() {
            return items;
        }

        public void setItems(List<SQLObject> items) {
            this.items = items;
        }

    }
}
