// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class JdbcUtils {

	public static void rollback(Connection conn) throws SQLException {
		if (conn != null) {
			conn.rollback();
		}
	}

	public static void setAutoCommit(Connection conn) throws SQLException {
		if (conn != null) {
			conn.setAutoCommit(true);
		}
	}

	public static void closeQuietly(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ignore) {
			}
		}
	}

	public static void closeQuietly(Statement stat) {
		if (stat != null) {
			try {
				stat.close();
			} catch (SQLException ignore) {
			}
		}
	}

	public static void closeQuietly(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ignore) {
			}
		}
	}

	public static String[] getColumnLabels(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		List<String> labels = new ArrayList<>(md.getColumnCount());

		for (int idx = 1; idx <= md.getColumnCount(); idx++) {
			labels.add(md.getColumnLabel(idx));
		}
		return labels.toArray(new String[labels.size()]);
	}
}
