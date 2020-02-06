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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.appslandia.common.utils.AssertUtils;
import com.appslandia.common.utils.IOUtils;
import com.appslandia.common.utils.ObjectUtils;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class StatementImpl implements PreparedStatement {

	protected final Sql sql;
	protected final PreparedStatement stat;

	public StatementImpl(PreparedStatement stat) {
		this.stat = stat;
		this.sql = null;
	}

	public StatementImpl(Connection conn, String sql) throws java.sql.SQLException {
		this.stat = conn.prepareStatement(sql);
		this.sql = null;
	}

	public StatementImpl(Connection conn, String sql, int resultSetType, int resultSetConcurrency) throws java.sql.SQLException {
		this.stat = conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
		this.sql = null;
	}

	public StatementImpl(Connection conn, Sql sql) throws java.sql.SQLException {
		this.stat = conn.prepareStatement(sql.getTranslatedSql());
		this.sql = sql;
	}

	public StatementImpl(Connection conn, Sql sql, int resultSetType, int resultSetConcurrency) throws java.sql.SQLException {
		this.stat = conn.prepareStatement(sql.getTranslatedSql(), resultSetType, resultSetConcurrency);
		this.sql = sql;
	}

	public StatementImpl(Connection conn, Sql sql, boolean returnGeneratedKey) throws java.sql.SQLException {
		this.stat = conn.prepareStatement(sql.getTranslatedSql(), returnGeneratedKey ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
		this.sql = sql;
	}

	protected Sql getSql() {
		return AssertUtils.assertNotNull(this.sql);
	}

	public ResultSetImpl executeResult() throws java.sql.SQLException {
		return new ResultSetImpl(this.stat.executeQuery());
	}

	public int executeGeneratedKey() throws java.sql.SQLException {
		long seq = executeGeneratedKeyLong();
		if (seq > Integer.MAX_VALUE) {
			throw new ArithmeticException("executeGeneratedKey() is out of range.");
		}
		return (int) seq;
	}

	public long executeGeneratedKeyLong() throws java.sql.SQLException {
		this.stat.executeUpdate();
		try (ResultSet rs = this.stat.getGeneratedKeys()) {
			if (rs.next()) {
				return rs.getLong(1);
			}
		}
		throw new SQLException("executeGeneratedKeyLong");
	}

	// Execute Results

	public <K, V> Map<K, V> executeMap(ResultSetMapper<K> key, ResultSetMapper<V> value, Map<K, V> newMap) throws java.sql.SQLException {
		try (ResultSetImpl rs = this.executeResult()) {
			while (rs.next()) {
				K k = key.map(rs);
				V v = value.map(rs);
				newMap.put(k, v);
			}
			return newMap;
		}
	}

	public <K, V> Map<K, V> executeMap(String keyColumn, String valueColumn, Map<K, V> newMap) throws java.sql.SQLException {
		try (ResultSetImpl rs = this.executeResult()) {
			while (rs.next()) {
				K k = ObjectUtils.cast(rs.getObject(keyColumn));
				V v = ObjectUtils.cast(rs.getObject(valueColumn));
				newMap.put(k, v);
			}
			return newMap;
		}
	}

	public <K, V> Map<K, V> executeMap(Map<K, V> newMap) throws java.sql.SQLException {
		try (ResultSetImpl rs = this.executeResult()) {
			while (rs.next()) {
				K k = ObjectUtils.cast(rs.getObject(1));
				V v = ObjectUtils.cast(rs.getObject(2));
				newMap.put(k, v);
			}
			return newMap;
		}
	}

	public <T> List<T> executeList(ResultSetMapper<T> mapper) throws java.sql.SQLException {
		try (ResultSetImpl rs = this.executeResult()) {
			List<T> list = new ArrayList<>();
			while (rs.next()) {
				T t = mapper.map(rs);
				list.add(t);
			}
			return list;
		}
	}

	public <T> T executeSingle(ResultSetMapper<T> mapper) throws java.sql.SQLException {
		try (ResultSetImpl rs = this.executeResult()) {
			T t = null;
			boolean rsRead = false;
			while (rs.next()) {
				if (rsRead) {
					throw new NonUniqueSqlException();
				}
				rsRead = true;
				t = mapper.map(rs);
			}
			return t;
		}
	}

	public <T> T executeScalar() throws java.sql.SQLException {
		try (ResultSetImpl rs = this.executeResult()) {
			Object obj = null;
			boolean rsRead = false;
			while (rs.next()) {
				if (rsRead) {
					throw new NonUniqueSqlException();
				}
				rsRead = true;
				obj = rs.getObject(1);
			}
			return ObjectUtils.cast(obj);
		}
	}

	public int executeCount() throws java.sql.SQLException {
		long count = executeCountLong();

		if (count > Integer.MAX_VALUE) {
			throw new ArithmeticException("executeCount() is out of range.");
		}
		return (int) count;
	}

	public long executeCountLong() throws java.sql.SQLException {
		try (ResultSetImpl rs = this.executeResult()) {
			long count = -1;
			while (rs.next()) {
				if (count >= 0) {
					throw new NonUniqueSqlException();
				}
				count = rs.getLong(1);
			}
			return count;
		}
	}

	public void executeQuery(ResultSetHandler handler) throws java.sql.SQLException {
		try (ResultSetImpl rs = this.executeResult()) {
			while (rs.next()) {
				handler.handle(rs);
			}
		}
	}

	public void executeStream(String streamLabel, OutputStream os, ResultSetHandler handler) throws java.sql.SQLException, IOException {
		try (ResultSetImpl rs = this.executeResult()) {
			boolean rsRead = false;
			while (rs.next()) {
				if (rsRead) {
					throw new NonUniqueSqlException();
				}
				rsRead = true;
				if (handler != null) {
					handler.handle(rs);
				}
				try (InputStream is = rs.getBinaryStream(streamLabel)) {
					IOUtils.copy(is, os);
				}
			}
		}
	}

	public void executeStream(String streamLabel, Writer w, ResultSetHandler handler) throws java.sql.SQLException, IOException {
		try (ResultSetImpl rs = this.executeResult()) {
			boolean rsRead = false;
			while (rs.next()) {
				if (rsRead) {
					throw new NonUniqueSqlException();
				}
				rsRead = true;
				if (handler != null) {
					handler.handle(rs);
				}
				try (Reader r = rs.getCharacterStream(streamLabel)) {
					IOUtils.copy(r, w);
				}
			}
		}
	}

	public void executeNStream(String streamLabel, Writer w, ResultSetHandler handler) throws java.sql.SQLException, IOException {
		try (ResultSetImpl rs = this.executeResult()) {
			boolean rsRead = false;
			while (rs.next()) {
				if (rsRead) {
					throw new NonUniqueSqlException();
				}
				rsRead = true;
				if (handler != null) {
					handler.handle(rs);
				}
				try (Reader r = rs.getNCharacterStream(streamLabel)) {
					IOUtils.copy(r, w);
				}
			}
		}
	}

	// Set LIKE Parameters
	// :name IS NULL OR name LIKE :name
	// :name = '' OR name LIKE :name

	public void setLikePattern(String parameterName, String value) throws java.sql.SQLException {
		setLikePattern(parameterName, value, LikeType.CONTAINS);
	}

	public void setLikePattern(String parameterName, String value, LikeType likeType) throws java.sql.SQLException {
		setString2(parameterName, SqlLikeEscaper.toLikePattern(value, likeType));
	}

	public void setNLikePattern(String parameterName, String value) throws java.sql.SQLException {
		setNLikePattern(parameterName, value, LikeType.CONTAINS);
	}

	public void setNLikePattern(String parameterName, String value, LikeType likeType) throws java.sql.SQLException {
		setNString2(parameterName, SqlLikeEscaper.toLikePattern(value, likeType));
	}

	// Set LIKE_ANY Parameters
	// name LIKE_ANY :names

	public void setLikeAny(String parameterName, String[] values) throws java.sql.SQLException {
		setLikeAny(parameterName, values, LikeType.CONTAINS, null);
	}

	public void setLikeAny(String parameterName, String[] values, LikeType likeType, String falseLikePattern) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			setString2(Sql.toParamName(parameterName, i), (i < values.length) ? SqlLikeEscaper.toLikePattern(values[i], likeType) : falseLikePattern);
		}
	}

	public void setNLikeAny(String parameterName, String[] values) throws java.sql.SQLException {
		setNLikeAny(parameterName, values, LikeType.CONTAINS, null);
	}

	public void setNLikeAny(String parameterName, String[] values, LikeType likeType, String falseLikePattern) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			setNString2(Sql.toParamName(parameterName, i), (i < values.length) ? SqlLikeEscaper.toLikePattern(values[i], likeType) : falseLikePattern);
		}
	}

	// Set IN Parameters
	// type IN :types

	public void setStringArray(String parameterName, String[] values) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			setString2(Sql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
		}
	}

	public void setNStringArray(String parameterName, String[] values) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			setNString2(Sql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
		}
	}

	public void setByteArray(String parameterName, byte[] values) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			if (i < values.length) {
				setByte(Sql.toParamName(parameterName, i), values[i]);
			} else {
				setByte2(Sql.toParamName(parameterName, i), null);
			}
		}
	}

	public void setShortArray(String parameterName, short[] values) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			if (i < values.length) {
				setShort(Sql.toParamName(parameterName, i), values[i]);
			} else {
				setShort2(Sql.toParamName(parameterName, i), null);
			}
		}
	}

	public void setIntArray(String parameterName, int[] values) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			if (i < values.length) {
				setInt(Sql.toParamName(parameterName, i), values[i]);
			} else {
				setInt2(Sql.toParamName(parameterName, i), null);
			}
		}
	}

	public void setLongArray(String parameterName, long[] values) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			if (i < values.length) {
				setLong(Sql.toParamName(parameterName, i), values[i]);
			} else {
				setLong2(Sql.toParamName(parameterName, i), null);
			}
		}
	}

	public void setFloatArray(String parameterName, float[] values) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			if (i < values.length) {
				setFloat(Sql.toParamName(parameterName, i), values[i]);
			} else {
				setFloat2(Sql.toParamName(parameterName, i), null);
			}
		}
	}

	public void setDoubleArray(String parameterName, double[] values) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			if (i < values.length) {
				setDouble(Sql.toParamName(parameterName, i), values[i]);
			} else {
				setDouble2(Sql.toParamName(parameterName, i), null);
			}
		}
	}

	public void setBigDecimalArray(String parameterName, java.math.BigDecimal[] values) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			setBigDecimal2(Sql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
		}
	}

	public void setDateArray(String parameterName, java.sql.Date[] values) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			setDate2(Sql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
		}
	}

	public void setTimestampArray(String parameterName, java.sql.Timestamp[] values) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			setTimestamp2(Sql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
		}
	}

	public void setTimeArray(String parameterName, java.sql.Time[] values) throws java.sql.SQLException {
		int arrayLen = this.getSql().getArrayLen(parameterName);
		AssertUtils.assertTrue(values.length <= arrayLen, "values is too long.");

		for (int i = 0; i < arrayLen; i++) {
			setTime2(Sql.toParamName(parameterName, i), (i < values.length) ? values[i] : null);
		}
	}

	// Set Nullable Parameters

	public void setBoolean2(String parameterName, Boolean value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.BIT);
		} else {
			setBoolean(parameterName, value);
		}
	}

	public void setChar2(String parameterName, String value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.CHAR);
		} else {
			setString(parameterName, value);
		}
	}

	public void setNChar2(String parameterName, String value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.NCHAR);
		} else {
			setNString(parameterName, value);
		}
	}

	public void setString2(String parameterName, String value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.VARCHAR);
		} else {
			setString(parameterName, value);
		}
	}

	public void setNString2(String parameterName, String value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.NVARCHAR);
		} else {
			setNString(parameterName, value);
		}
	}

	public void setByte2(String parameterName, Byte value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.TINYINT);
		} else {
			setByte(parameterName, value);
		}
	}

	public void setBytes2(String parameterName, byte[] value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.VARBINARY);
		} else {
			setBytes(parameterName, value);
		}
	}

	public void setShort2(String parameterName, Short value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.SMALLINT);
		} else {
			setShort(parameterName, value);
		}
	}

	public void setInt2(String parameterName, Integer value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.INTEGER);
		} else {
			setInt(parameterName, value);
		}
	}

	public void setLong2(String parameterName, Long value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.BIGINT);
		} else {
			setLong(parameterName, value);
		}
	}

	public void setFloat2(String parameterName, Float value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.REAL);
		} else {
			setFloat(parameterName, value);
		}
	}

	public void setDouble2(String parameterName, Double value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.DOUBLE);
		} else {
			setDouble(parameterName, value);
		}
	}

	public void setBigDecimal2(String parameterName, java.math.BigDecimal value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.NUMERIC);
		} else {
			setBigDecimal(parameterName, value);
		}
	}

	public void setDate2(String parameterName, java.sql.Date value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.DATE);
		} else {
			setDate(parameterName, value);
		}
	}

	public void setTimestamp2(String parameterName, java.sql.Timestamp value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.TIMESTAMP);
		} else {
			setTimestamp(parameterName, value);
		}
	}

	public void setTime2(String parameterName, java.sql.Time value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.TIME);
		} else {
			setTime(parameterName, value);
		}
	}

	public void setURL2(String parameterName, java.net.URL value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.DATALINK);
		} else {
			setURL(parameterName, value);
		}
	}

	public void setArray2(String parameterName, java.sql.Array value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.ARRAY);
		} else {
			setArray(parameterName, value);
		}
	}

	public void setSQLXML2(String parameterName, java.sql.SQLXML value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.SQLXML);
		} else {
			setSQLXML(parameterName, value);
		}
	}

	public void setRef2(String parameterName, java.sql.Ref value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.REF);
		} else {
			setRef(parameterName, value);
		}
	}

	public void setRowId2(String parameterName, java.sql.RowId value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.ROWID);
		} else {
			setRowId(parameterName, value);
		}
	}

	public void setClob2(String parameterName, java.sql.Clob value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.CLOB);
		} else {
			setClob(parameterName, value);
		}
	}

	public void setClob2(String parameterName, java.io.Reader value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.CLOB);
		} else {
			setClob(parameterName, value);
		}
	}

	public void setNClob2(String parameterName, java.sql.NClob value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.NCLOB);
		} else {
			setNClob(parameterName, value);
		}
	}

	public void setNClob2(String parameterName, java.io.Reader value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.NCLOB);
		} else {
			setNClob(parameterName, value);
		}
	}

	public void setAsciiStream2(String parameterName, java.io.InputStream value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.LONGVARCHAR);
		} else {
			setAsciiStream(parameterName, value);
		}
	}

	public void setCharacterStream2(String parameterName, java.io.Reader value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.LONGVARCHAR);
		} else {
			setCharacterStream(parameterName, value);
		}
	}

	public void setNCharacterStream2(String parameterName, java.io.Reader value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.LONGNVARCHAR);
		} else {
			setNCharacterStream(parameterName, value);
		}
	}

	public void setBlob2(String parameterName, java.sql.Blob value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.BLOB);
		} else {
			setBlob(parameterName, value);
		}
	}

	public void setBlob2(String parameterName, java.io.InputStream value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.BLOB);
		} else {
			setBlob(parameterName, value);
		}
	}

	public void setBinaryStream2(String parameterName, java.io.InputStream value) throws java.sql.SQLException {
		if (value == null) {
			setNull(parameterName, Types.LONGVARBINARY);
		} else {
			setBinaryStream(parameterName, value);
		}
	}

	// Set Parameter by Name

	public void setBoolean(String parameterName, boolean x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setBoolean(index, x);
		}
	}

	public void setString(String parameterName, java.lang.String x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setString(index, x);
		}
	}

	public void setNString(String parameterName, java.lang.String value) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setNString(index, value);
		}
	}

	public void setByte(String parameterName, byte x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setByte(index, x);
		}
	}

	public void setBytes(String parameterName, byte[] x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setBytes(index, x);
		}
	}

	public void setShort(String parameterName, short x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setShort(index, x);
		}
	}

	public void setInt(String parameterName, int x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setInt(index, x);
		}
	}

	public void setLong(String parameterName, long x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setLong(index, x);
		}
	}

	public void setFloat(String parameterName, float x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setFloat(index, x);
		}
	}

	public void setDouble(String parameterName, double x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setDouble(index, x);
		}
	}

	public void setBigDecimal(String parameterName, java.math.BigDecimal x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setBigDecimal(index, x);
		}
	}

	public void setDate(String parameterName, java.sql.Date x, java.util.Calendar cal) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setDate(index, x, cal);
		}
	}

	public void setDate(String parameterName, java.sql.Date x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setDate(index, x);
		}
	}

	public void setTimestamp(String parameterName, java.sql.Timestamp x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setTimestamp(index, x);
		}
	}

	public void setTimestamp(String parameterName, java.sql.Timestamp x, java.util.Calendar cal) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setTimestamp(index, x, cal);
		}
	}

	public void setTime(String parameterName, java.sql.Time x, java.util.Calendar cal) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setTime(index, x, cal);
		}
	}

	public void setTime(String parameterName, java.sql.Time x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setTime(index, x);
		}
	}

	public void setNull(String parameterName, int sqlType, java.lang.String typeName) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setNull(index, sqlType, typeName);
		}
	}

	public void setNull(String parameterName, int sqlType) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setNull(index, sqlType);
		}
	}

	public void setObject(String parameterName, java.lang.Object x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setObject(index, x);
		}
	}

	public void setObject(String parameterName, java.lang.Object x, int targetSqlType, int scaleOrLength) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setObject(index, x, targetSqlType, scaleOrLength);
		}
	}

	public void setObject(String parameterName, java.lang.Object x, java.sql.SQLType targetSqlType) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setObject(index, x, targetSqlType);
		}
	}

	public void setObject(String parameterName, java.lang.Object x, int targetSqlType) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setObject(index, x, targetSqlType);
		}
	}

	public void setObject(String parameterName, java.lang.Object x, java.sql.SQLType targetSqlType, int scaleOrLength) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setObject(index, x, targetSqlType, scaleOrLength);
		}
	}

	public void setURL(String parameterName, java.net.URL x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setURL(index, x);
		}
	}

	public void setArray(String parameterName, java.sql.Array x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setArray(index, x);
		}
	}

	public void setSQLXML(String parameterName, java.sql.SQLXML xmlObject) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setSQLXML(index, xmlObject);
		}
	}

	public void setRef(String parameterName, java.sql.Ref x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setRef(index, x);
		}
	}

	public void setRowId(String parameterName, java.sql.RowId x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setRowId(index, x);
		}
	}

	public void setClob(String parameterName, java.io.Reader reader, long length) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setClob(index, reader, length);
		}
	}

	public void setClob(String parameterName, java.sql.Clob x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setClob(index, x);
		}
	}

	public void setClob(String parameterName, java.io.Reader reader) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setClob(index, reader);
		}
	}

	public void setNClob(String parameterName, java.sql.NClob value) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setNClob(index, value);
		}
	}

	public void setNClob(String parameterName, java.io.Reader reader, long length) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setNClob(index, reader, length);
		}
	}

	public void setNClob(String parameterName, java.io.Reader reader) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setNClob(index, reader);
		}
	}

	public void setAsciiStream(String parameterName, java.io.InputStream x, int length) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setAsciiStream(index, x, length);
		}
	}

	public void setAsciiStream(String parameterName, java.io.InputStream x, long length) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setAsciiStream(index, x, length);
		}
	}

	public void setAsciiStream(String parameterName, java.io.InputStream x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setAsciiStream(index, x);
		}
	}

	public void setCharacterStream(String parameterName, java.io.Reader reader, long length) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setCharacterStream(index, reader, length);
		}
	}

	public void setCharacterStream(String parameterName, java.io.Reader reader, int length) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setCharacterStream(index, reader, length);
		}
	}

	public void setCharacterStream(String parameterName, java.io.Reader reader) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setCharacterStream(index, reader);
		}
	}

	public void setNCharacterStream(String parameterName, java.io.Reader value, long length) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setNCharacterStream(index, value, length);
		}
	}

	public void setNCharacterStream(String parameterName, java.io.Reader value) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setNCharacterStream(index, value);
		}
	}

	@Deprecated
	public void setUnicodeStream(String parameterName, java.io.InputStream x, int length) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setUnicodeStream(index, x, length);
		}
	}

	public void setBlob(String parameterName, java.sql.Blob x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setBlob(index, x);
		}
	}

	public void setBlob(String parameterName, java.io.InputStream inputStream) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setBlob(index, inputStream);
		}
	}

	public void setBlob(String parameterName, java.io.InputStream inputStream, long length) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setBlob(index, inputStream, length);
		}
	}

	public void setBinaryStream(String parameterName, java.io.InputStream x, long length) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setBinaryStream(index, x, length);
		}
	}

	public void setBinaryStream(String parameterName, java.io.InputStream x) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setBinaryStream(index, x);
		}
	}

	public void setBinaryStream(String parameterName, java.io.InputStream x, int length) throws java.sql.SQLException {
		for (int index : this.getSql().getIndexes(parameterName)) {
			this.stat.setBinaryStream(index, x, length);
		}
	}

	// java.sql.PreparedStatement

	@Override
	public int executeUpdate() throws java.sql.SQLException {
		return this.stat.executeUpdate();
	}

	@Override
	public long executeLargeUpdate() throws java.sql.SQLException {
		return this.stat.executeLargeUpdate();
	}

	@Override
	public boolean execute() throws java.sql.SQLException {
		return this.stat.execute();
	}

	@Override
	public java.sql.ResultSet executeQuery() throws java.sql.SQLException {
		return this.stat.executeQuery();
	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) throws java.sql.SQLException {
		this.stat.setBoolean(parameterIndex, x);
	}

	@Override
	public void setString(int parameterIndex, java.lang.String x) throws java.sql.SQLException {
		this.stat.setString(parameterIndex, x);
	}

	@Override
	public void setNString(int parameterIndex, java.lang.String value) throws java.sql.SQLException {
		this.stat.setNString(parameterIndex, value);
	}

	@Override
	public void setByte(int parameterIndex, byte x) throws java.sql.SQLException {
		this.stat.setByte(parameterIndex, x);
	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) throws java.sql.SQLException {
		this.stat.setBytes(parameterIndex, x);
	}

	@Override
	public void setShort(int parameterIndex, short x) throws java.sql.SQLException {
		this.stat.setShort(parameterIndex, x);
	}

	@Override
	public void setInt(int parameterIndex, int x) throws java.sql.SQLException {
		this.stat.setInt(parameterIndex, x);
	}

	@Override
	public void setLong(int parameterIndex, long x) throws java.sql.SQLException {
		this.stat.setLong(parameterIndex, x);
	}

	@Override
	public void setFloat(int parameterIndex, float x) throws java.sql.SQLException {
		this.stat.setFloat(parameterIndex, x);
	}

	@Override
	public void setDouble(int parameterIndex, double x) throws java.sql.SQLException {
		this.stat.setDouble(parameterIndex, x);
	}

	@Override
	public void setBigDecimal(int parameterIndex, java.math.BigDecimal x) throws java.sql.SQLException {
		this.stat.setBigDecimal(parameterIndex, x);
	}

	@Override
	public void setDate(int parameterIndex, java.sql.Date x, java.util.Calendar cal) throws java.sql.SQLException {
		this.stat.setDate(parameterIndex, x, cal);
	}

	@Override
	public void setDate(int parameterIndex, java.sql.Date x) throws java.sql.SQLException {
		this.stat.setDate(parameterIndex, x);
	}

	@Override
	public void setTimestamp(int parameterIndex, java.sql.Timestamp x) throws java.sql.SQLException {
		this.stat.setTimestamp(parameterIndex, x);
	}

	@Override
	public void setTimestamp(int parameterIndex, java.sql.Timestamp x, java.util.Calendar cal) throws java.sql.SQLException {
		this.stat.setTimestamp(parameterIndex, x, cal);
	}

	@Override
	public void setTime(int parameterIndex, java.sql.Time x, java.util.Calendar cal) throws java.sql.SQLException {
		this.stat.setTime(parameterIndex, x, cal);
	}

	@Override
	public void setTime(int parameterIndex, java.sql.Time x) throws java.sql.SQLException {
		this.stat.setTime(parameterIndex, x);
	}

	@Override
	public void setNull(int parameterIndex, int sqlType, java.lang.String typeName) throws java.sql.SQLException {
		this.stat.setNull(parameterIndex, sqlType, typeName);
	}

	@Override
	public void setNull(int parameterIndex, int sqlType) throws java.sql.SQLException {
		this.stat.setNull(parameterIndex, sqlType);
	}

	@Override
	public void setObject(int parameterIndex, java.lang.Object x) throws java.sql.SQLException {
		this.stat.setObject(parameterIndex, x);
	}

	@Override
	public void setObject(int parameterIndex, java.lang.Object x, int targetSqlType, int scaleOrLength) throws java.sql.SQLException {
		this.stat.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void setObject(int parameterIndex, java.lang.Object x, java.sql.SQLType targetSqlType) throws java.sql.SQLException {
		this.stat.setObject(parameterIndex, x, targetSqlType);
	}

	@Override
	public void setObject(int parameterIndex, java.lang.Object x, int targetSqlType) throws java.sql.SQLException {
		this.stat.setObject(parameterIndex, x, targetSqlType);
	}

	@Override
	public void setObject(int parameterIndex, java.lang.Object x, java.sql.SQLType targetSqlType, int scaleOrLength) throws java.sql.SQLException {
		this.stat.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
	}

	@Override
	public void setURL(int parameterIndex, java.net.URL x) throws java.sql.SQLException {
		this.stat.setURL(parameterIndex, x);
	}

	@Override
	public void setArray(int parameterIndex, java.sql.Array x) throws java.sql.SQLException {
		this.stat.setArray(parameterIndex, x);
	}

	@Override
	public void setSQLXML(int parameterIndex, java.sql.SQLXML xmlObject) throws java.sql.SQLException {
		this.stat.setSQLXML(parameterIndex, xmlObject);
	}

	@Override
	public void setRef(int parameterIndex, java.sql.Ref x) throws java.sql.SQLException {
		this.stat.setRef(parameterIndex, x);
	}

	@Override
	public void setRowId(int parameterIndex, java.sql.RowId x) throws java.sql.SQLException {
		this.stat.setRowId(parameterIndex, x);
	}

	@Override
	public void setClob(int parameterIndex, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.stat.setClob(parameterIndex, reader, length);
	}

	@Override
	public void setClob(int parameterIndex, java.sql.Clob x) throws java.sql.SQLException {
		this.stat.setClob(parameterIndex, x);
	}

	@Override
	public void setClob(int parameterIndex, java.io.Reader reader) throws java.sql.SQLException {
		this.stat.setClob(parameterIndex, reader);
	}

	@Override
	public void setNClob(int parameterIndex, java.sql.NClob value) throws java.sql.SQLException {
		this.stat.setNClob(parameterIndex, value);
	}

	@Override
	public void setNClob(int parameterIndex, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.stat.setNClob(parameterIndex, reader, length);
	}

	@Override
	public void setNClob(int parameterIndex, java.io.Reader reader) throws java.sql.SQLException {
		this.stat.setNClob(parameterIndex, reader);
	}

	@Override
	public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
		this.stat.setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public void setAsciiStream(int parameterIndex, java.io.InputStream x, long length) throws java.sql.SQLException {
		this.stat.setAsciiStream(parameterIndex, x, length);
	}

	@Override
	public void setAsciiStream(int parameterIndex, java.io.InputStream x) throws java.sql.SQLException {
		this.stat.setAsciiStream(parameterIndex, x);
	}

	@Override
	public void setCharacterStream(int parameterIndex, java.io.Reader reader, long length) throws java.sql.SQLException {
		this.stat.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setCharacterStream(int parameterIndex, java.io.Reader reader, int length) throws java.sql.SQLException {
		this.stat.setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setCharacterStream(int parameterIndex, java.io.Reader reader) throws java.sql.SQLException {
		this.stat.setCharacterStream(parameterIndex, reader);
	}

	@Override
	public void setNCharacterStream(int parameterIndex, java.io.Reader value, long length) throws java.sql.SQLException {
		this.stat.setNCharacterStream(parameterIndex, value, length);
	}

	@Override
	public void setNCharacterStream(int parameterIndex, java.io.Reader value) throws java.sql.SQLException {
		this.stat.setNCharacterStream(parameterIndex, value);
	}

	@Override
	@Deprecated
	public void setUnicodeStream(int parameterIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
		this.stat.setUnicodeStream(parameterIndex, x, length);
	}

	@Override
	public void setBlob(int parameterIndex, java.sql.Blob x) throws java.sql.SQLException {
		this.stat.setBlob(parameterIndex, x);
	}

	@Override
	public void setBlob(int parameterIndex, java.io.InputStream inputStream) throws java.sql.SQLException {
		this.stat.setBlob(parameterIndex, inputStream);
	}

	@Override
	public void setBlob(int parameterIndex, java.io.InputStream inputStream, long length) throws java.sql.SQLException {
		this.stat.setBlob(parameterIndex, inputStream, length);
	}

	@Override
	public void setBinaryStream(int parameterIndex, java.io.InputStream x, long length) throws java.sql.SQLException {
		this.stat.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void setBinaryStream(int parameterIndex, java.io.InputStream x) throws java.sql.SQLException {
		this.stat.setBinaryStream(parameterIndex, x);
	}

	@Override
	public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) throws java.sql.SQLException {
		this.stat.setBinaryStream(parameterIndex, x, length);
	}

	@Override
	public void addBatch() throws java.sql.SQLException {
		this.stat.addBatch();
	}

	@Override
	public void clearParameters() throws java.sql.SQLException {
		this.stat.clearParameters();
	}

	@Override
	public java.sql.ResultSetMetaData getMetaData() throws java.sql.SQLException {
		return this.stat.getMetaData();
	}

	@Override
	public java.sql.ParameterMetaData getParameterMetaData() throws java.sql.SQLException {
		return this.stat.getParameterMetaData();
	}

	// java.sql.Statement

	@Override
	public long executeLargeUpdate(java.lang.String sql, int[] columnIndexes) throws java.sql.SQLException {
		return this.stat.executeLargeUpdate(sql, columnIndexes);
	}

	@Override
	public long executeLargeUpdate(java.lang.String sql, java.lang.String[] columnNames) throws java.sql.SQLException {
		return this.stat.executeLargeUpdate(sql, columnNames);
	}

	@Override
	public long executeLargeUpdate(java.lang.String sql) throws java.sql.SQLException {
		return this.stat.executeLargeUpdate(sql);
	}

	@Override
	public long executeLargeUpdate(java.lang.String sql, int autoGeneratedKeys) throws java.sql.SQLException {
		return this.stat.executeLargeUpdate(sql, autoGeneratedKeys);
	}

	@Override
	public int executeUpdate(java.lang.String sql, int[] columnIndexes) throws java.sql.SQLException {
		return this.stat.executeUpdate(sql, columnIndexes);
	}

	@Override
	public int executeUpdate(java.lang.String sql, int autoGeneratedKeys) throws java.sql.SQLException {
		return this.stat.executeUpdate(sql, autoGeneratedKeys);
	}

	@Override
	public int executeUpdate(java.lang.String sql) throws java.sql.SQLException {
		return this.stat.executeUpdate(sql);
	}

	@Override
	public int executeUpdate(java.lang.String sql, java.lang.String[] columnNames) throws java.sql.SQLException {
		return this.stat.executeUpdate(sql, columnNames);
	}

	@Override
	public boolean execute(java.lang.String sql) throws java.sql.SQLException {
		return this.stat.execute(sql);
	}

	@Override
	public boolean execute(java.lang.String sql, int autoGeneratedKeys) throws java.sql.SQLException {
		return this.stat.execute(sql, autoGeneratedKeys);
	}

	@Override
	public boolean execute(java.lang.String sql, int[] columnIndexes) throws java.sql.SQLException {
		return this.stat.execute(sql, columnIndexes);
	}

	@Override
	public boolean execute(java.lang.String sql, java.lang.String[] columnNames) throws java.sql.SQLException {
		return this.stat.execute(sql, columnNames);
	}

	@Override
	public int[] executeBatch() throws java.sql.SQLException {
		return this.stat.executeBatch();
	}

	@Override
	public long[] executeLargeBatch() throws java.sql.SQLException {
		return this.stat.executeLargeBatch();
	}

	@Override
	public java.sql.ResultSet executeQuery(java.lang.String sql) throws java.sql.SQLException {
		return this.stat.executeQuery(sql);
	}

	@Override
	public void setQueryTimeout(int seconds) throws java.sql.SQLException {
		this.stat.setQueryTimeout(seconds);
	}

	@Override
	public void setCursorName(java.lang.String name) throws java.sql.SQLException {
		this.stat.setCursorName(name);
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws java.sql.SQLException {
		this.stat.setEscapeProcessing(enable);
	}

	@Override
	public void setFetchDirection(int direction) throws java.sql.SQLException {
		this.stat.setFetchDirection(direction);
	}

	@Override
	public void setFetchSize(int rows) throws java.sql.SQLException {
		this.stat.setFetchSize(rows);
	}

	@Override
	public void setLargeMaxRows(long max) throws java.sql.SQLException {
		this.stat.setLargeMaxRows(max);
	}

	@Override
	public void setMaxFieldSize(int max) throws java.sql.SQLException {
		this.stat.setMaxFieldSize(max);
	}

	@Override
	public void setMaxRows(int max) throws java.sql.SQLException {
		this.stat.setMaxRows(max);
	}

	@Override
	public void setPoolable(boolean poolable) throws java.sql.SQLException {
		this.stat.setPoolable(poolable);
	}

	@Override
	public long getLargeUpdateCount() throws java.sql.SQLException {
		return this.stat.getLargeUpdateCount();
	}

	@Override
	public int getUpdateCount() throws java.sql.SQLException {
		return this.stat.getUpdateCount();
	}

	@Override
	public int getQueryTimeout() throws java.sql.SQLException {
		return this.stat.getQueryTimeout();
	}

	@Override
	public void addBatch(java.lang.String sql) throws java.sql.SQLException {
		this.stat.addBatch(sql);
	}

	@Override
	public void cancel() throws java.sql.SQLException {
		this.stat.cancel();
	}

	@Override
	public void clearBatch() throws java.sql.SQLException {
		this.stat.clearBatch();
	}

	@Override
	public void clearWarnings() throws java.sql.SQLException {
		this.stat.clearWarnings();
	}

	@Override
	public void closeOnCompletion() throws java.sql.SQLException {
		this.stat.closeOnCompletion();
	}

	@Override
	public java.sql.Connection getConnection() throws java.sql.SQLException {
		return this.stat.getConnection();
	}

	@Override
	public int getFetchDirection() throws java.sql.SQLException {
		return this.stat.getFetchDirection();
	}

	@Override
	public int getFetchSize() throws java.sql.SQLException {
		return this.stat.getFetchSize();
	}

	@Override
	public java.sql.ResultSet getGeneratedKeys() throws java.sql.SQLException {
		return this.stat.getGeneratedKeys();
	}

	@Override
	public long getLargeMaxRows() throws java.sql.SQLException {
		return this.stat.getLargeMaxRows();
	}

	@Override
	public int getMaxFieldSize() throws java.sql.SQLException {
		return this.stat.getMaxFieldSize();
	}

	@Override
	public int getMaxRows() throws java.sql.SQLException {
		return this.stat.getMaxRows();
	}

	@Override
	public boolean getMoreResults() throws java.sql.SQLException {
		return this.stat.getMoreResults();
	}

	@Override
	public boolean getMoreResults(int current) throws java.sql.SQLException {
		return this.stat.getMoreResults(current);
	}

	@Override
	public java.sql.ResultSet getResultSet() throws java.sql.SQLException {
		return this.stat.getResultSet();
	}

	@Override
	public int getResultSetConcurrency() throws java.sql.SQLException {
		return this.stat.getResultSetConcurrency();
	}

	@Override
	public int getResultSetHoldability() throws java.sql.SQLException {
		return this.stat.getResultSetHoldability();
	}

	@Override
	public int getResultSetType() throws java.sql.SQLException {
		return this.stat.getResultSetType();
	}

	@Override
	public java.sql.SQLWarning getWarnings() throws java.sql.SQLException {
		return this.stat.getWarnings();
	}

	@Override
	public boolean isCloseOnCompletion() throws java.sql.SQLException {
		return this.stat.isCloseOnCompletion();
	}

	@Override
	public boolean isClosed() throws java.sql.SQLException {
		return this.stat.isClosed();
	}

	@Override
	public boolean isPoolable() throws java.sql.SQLException {
		return this.stat.isPoolable();
	}

	// java.sql.Wrapper

	@Override
	public boolean isWrapperFor(java.lang.Class<?> arg0) throws java.sql.SQLException {
		return this.stat.isWrapperFor(arg0);
	}

	@Override
	public <T> T unwrap(java.lang.Class<T> arg0) throws java.sql.SQLException {
		return this.stat.unwrap(arg0);
	}

	// java.lang.AutoCloseable

	@Override
	public void close() throws java.sql.SQLException {
		this.stat.close();
	}
}
