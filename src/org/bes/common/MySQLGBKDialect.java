package org.bes.common;

import org.hibernate.NullPrecedence;
import org.hibernate.dialect.MySQL5Dialect;

public class MySQLGBKDialect extends MySQL5Dialect {

	public MySQLGBKDialect() {
	}

	public String renderOrderByElement(String expression, String collation, String order, NullPrecedence nulls) {
		expression = (new StringBuilder("convert(")).append(expression).append(" using 'gbk')").toString();
		return super.renderOrderByElement(expression, collation, order, nulls);
	}
}