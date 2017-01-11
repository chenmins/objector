package org.chenmin.open;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.chenmin.open.objector.ColumnTypeObject;
import org.chenmin.open.objector.ColumnValueObject;
import org.chenmin.open.objector.IStoreTable;
import org.chenmin.open.objector.IStoreTableRow;
import org.chenmin.open.objector.PrimaryKeySchemaObject;
import org.chenmin.open.objector.PrimaryKeyValueObject;

import com.alicloud.openservices.tablestore.ClientConfiguration;
import com.alicloud.openservices.tablestore.ClientException;
import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.TableStoreException;
import com.alicloud.openservices.tablestore.model.AlwaysRetryStrategy;
import com.alicloud.openservices.tablestore.model.Column;
import com.alicloud.openservices.tablestore.model.ColumnValue;
import com.alicloud.openservices.tablestore.model.CreateTableResponse;
import com.alicloud.openservices.tablestore.model.DeleteRowRequest;
import com.alicloud.openservices.tablestore.model.DeleteRowResponse;
import com.alicloud.openservices.tablestore.model.DescribeTableRequest;
import com.alicloud.openservices.tablestore.model.DescribeTableResponse;
import com.alicloud.openservices.tablestore.model.GetRowRequest;
import com.alicloud.openservices.tablestore.model.GetRowResponse;
import com.alicloud.openservices.tablestore.model.PrimaryKey;
import com.alicloud.openservices.tablestore.model.PrimaryKeyBuilder;
import com.alicloud.openservices.tablestore.model.PrimaryKeyOption;
import com.alicloud.openservices.tablestore.model.PrimaryKeySchema;
import com.alicloud.openservices.tablestore.model.PrimaryKeyType;
import com.alicloud.openservices.tablestore.model.PrimaryKeyValue;
import com.alicloud.openservices.tablestore.model.PutRowRequest;
import com.alicloud.openservices.tablestore.model.PutRowResponse;
import com.alicloud.openservices.tablestore.model.Row;
import com.alicloud.openservices.tablestore.model.RowDeleteChange;
import com.alicloud.openservices.tablestore.model.RowPutChange;
import com.alicloud.openservices.tablestore.model.RowUpdateChange;
import com.alicloud.openservices.tablestore.model.SingleRowQueryCriteria;
import com.alicloud.openservices.tablestore.model.TableMeta;
import com.alicloud.openservices.tablestore.model.TableOptions;
import com.alicloud.openservices.tablestore.model.UpdateRowRequest;
import com.alicloud.openservices.tablestore.model.UpdateRowResponse;
import com.alicloud.openservices.tablestore.model.internal.CreateTableRequestEx;
import com.google.inject.Singleton;

@Singleton
public class TableStoreService implements ITableStoreService {
	SyncClient client = null;

	public TableStoreService() {
		super();
		init();
	}

	@Override
	public boolean exsit(IStoreTable table) {
		return exsit(table.getTablename());
	}

	public boolean exsit(String tablename) {
		DescribeTableRequest request = new DescribeTableRequest(tablename);
		try {
			DescribeTableResponse response = client.describeTable(request);
			TableMeta tableMeta = response.getTableMeta();
			if (tableMeta != null && tableMeta.getTableName() != null) {
				return true;
			}
		} catch (TableStoreException e) {
			return false;
		} catch (ClientException e) {
			return false;
		}
		return false;
	}

	@Override
	public boolean createTable(IStoreTable table) {
		TableMeta tableMeta = new TableMeta(table.getTablename());
		List<PrimaryKeySchemaObject> primaryKey = table.getPrimaryKey();
		for (PrimaryKeySchemaObject k : primaryKey) {

			if (k.getOption() != null) {
				switch (k.getType()) {
				case INTEGER:
					tableMeta.addPrimaryKeyColumn(
							new PrimaryKeySchema(k.getName(), PrimaryKeyType.INTEGER, PrimaryKeyOption.AUTO_INCREMENT));
					break;
				case STRING:
					tableMeta.addPrimaryKeyColumn(
							new PrimaryKeySchema(k.getName(), PrimaryKeyType.STRING, PrimaryKeyOption.AUTO_INCREMENT));
					break;
				case BINARY:
					tableMeta.addPrimaryKeyColumn(
							new PrimaryKeySchema(k.getName(), PrimaryKeyType.BINARY, PrimaryKeyOption.AUTO_INCREMENT));
					break;
				default:
					break;
				}
			} else {
				switch (k.getType()) {
				case INTEGER:
					tableMeta.addPrimaryKeyColumn(new PrimaryKeySchema(k.getName(), PrimaryKeyType.INTEGER));
					break;
				case STRING:
					tableMeta.addPrimaryKeyColumn(new PrimaryKeySchema(k.getName(), PrimaryKeyType.STRING));
					break;
				case BINARY:
					tableMeta.addPrimaryKeyColumn(new PrimaryKeySchema(k.getName(), PrimaryKeyType.BINARY));
					break;
				default:
					break;
				}
			}
		}
		// 数据的过期时�? 单位�? -1代表永不过期. 假如设置过期时间为一�? 即为 365 * 24 * 3600.
		int timeToLive = -1;
		// 保存的最大版本数, 设置�?即代表每列上�?��保存3个最新的版本.
		int maxVersions = 1;
		TableOptions tableOptions = new TableOptions(timeToLive, maxVersions);
		CreateTableRequestEx request = new CreateTableRequestEx(tableMeta, tableOptions);
		CreateTableResponse r = client.createTable(request);
		return r.getRequestId() != null;
	}

	@Override
	public boolean init() {
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		// 设置建立连接的超时时间�?
		clientConfiguration.setConnectionTimeoutInMillisecond(5000);
		// 设置socket超时时间�?
		clientConfiguration.setSocketTimeoutInMillisecond(5000);
		// 设置重试策略，若不设置，采用默认的重试策略�?
		clientConfiguration.setRetryStrategy(new AlwaysRetryStrategy());
		// TODO
		client = new SyncClient(Config.get("TS_ENDPOINT"), Config.get("ALIYUN_ACCESS_KEY"),
				Config.get("ALIYUN_SECRET_KEY"), Config.get("TS_INSTANCENAME"), clientConfiguration);

		return false;
	}

	@Override
	public boolean putRow(IStoreTableRow row) {
		return putRow(row.getTablename(), row.getPrimaryKeyValue(), row.getColumnValue());
	}

	public boolean putRow(String tablename, Map<String, PrimaryKeyValueObject> primaryKey,
			Map<String, ColumnValueObject> column) {
		PrimaryKey primaryKeys = buildKey(primaryKey);
		RowPutChange rowPutChange = new RowPutChange(tablename, primaryKeys);
		List<Column> list = covert(column);
		for (Column c : list) {
			rowPutChange.addColumn(c);
		}
		PutRowResponse r = client.putRow(new PutRowRequest(rowPutChange));
		return r.getRequestId() != null;
	}

	private List<Column> covert(Map<String, ColumnValueObject> column) {
		List<Column> list = new ArrayList<Column>();
		for (String ck : column.keySet()) {
			ColumnValueObject cv = column.get(ck);
			Column c = null;
			switch (cv.getType()) {
			case INTEGER:
				c = new Column(ck, ColumnValue.fromLong((Long) cv.getValue()));
				break;
			case STRING:
				c = new Column(ck, ColumnValue.fromString(cv.getValue().toString()));
				break;
			case BOOLEAN:
				c = new Column(ck, ColumnValue.fromBoolean((Boolean) cv.getValue()));
				break;
			case DOUBLE:
				c = new Column(ck, ColumnValue.fromDouble((Double) cv.getValue()));
				break;
			case BINARY:
				c = new Column(ck, ColumnValue.fromBinary((byte[]) cv.getValue()));
				break;
			default:
				break;
			}
			if (c != null) {
				list.add(c);
			}
		}
		return list;
	}

	private PrimaryKey buildKey(Map<String, PrimaryKeyValueObject> primaryKey) {
		// 构�?主键
		PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
		for (String pk : primaryKey.keySet()) {
			PrimaryKeyValueObject pkv = primaryKey.get(pk);
			switch (pkv.getType()) {
			case INTEGER:
				primaryKeyBuilder.addPrimaryKeyColumn(pk, PrimaryKeyValue.fromLong((Long) pkv.getValue()));
				break;
			case STRING:
				primaryKeyBuilder.addPrimaryKeyColumn(pk, PrimaryKeyValue.fromString((String) pkv.getValue()));
				break;
			case BINARY:
				primaryKeyBuilder.addPrimaryKeyColumn(pk, PrimaryKeyValue.fromBinary((byte[]) pkv.getValue()));
				break;
			default:
				break;
			}
		}
		PrimaryKey primaryKeys = primaryKeyBuilder.build();
		return primaryKeys;
	}

	@Override
	public boolean getRow(IStoreTableRow row) {
		PrimaryKey primaryKeys = buildKey(row.getPrimaryKeyValue());
		// 读一�?
		SingleRowQueryCriteria criteria = new SingleRowQueryCriteria(row.getTablename(), primaryKeys);
		// 设置读取�?��版本
		criteria.setMaxVersions(1);
		GetRowResponse getRowResponse = client.getRow(new GetRowRequest(criteria));
		if (getRowResponse == null)
			return false;
		Row rows = getRowResponse.getRow();
		if (rows == null)
			return false;
		Column[] cols = rows.getColumns();
		Map<String, ColumnValueObject> v = new LinkedHashMap<String, ColumnValueObject>();
		for (Column c : cols) {
			ColumnValue cv = c.getValue();
			ColumnValueObject cvo = null;
			switch (cv.getType()) {
			case STRING:
				cvo = new ColumnValueObject(cv.asString(), ColumnTypeObject.STRING);
				break;
			case INTEGER:
				cvo = new ColumnValueObject(cv.asLong(), ColumnTypeObject.INTEGER);
				break;
			case BOOLEAN:
				cvo = new ColumnValueObject(cv.asBoolean(), ColumnTypeObject.BOOLEAN);
				break;
			case DOUBLE:
				cvo = new ColumnValueObject(cv.asDouble(), ColumnTypeObject.DOUBLE);
				break;
			case BINARY:
				cvo = new ColumnValueObject(cv.asBinary(), ColumnTypeObject.BINARY);
				break;

			default:
				break;
			}
			v.put(c.getName(), cvo);
		}
		row.setColumnValue(v);
		return getRowResponse.getRequestId() != null;
	}

	@Override
	public boolean deleteRow(IStoreTableRow row) {
		PrimaryKey primaryKeys = buildKey(row.getPrimaryKeyValue());
		RowDeleteChange rowDeleteChange = new RowDeleteChange(row.getTablename(), primaryKeys);
		DeleteRowResponse r = client.deleteRow(new DeleteRowRequest(rowDeleteChange));
		return r.getRequestId() != null;
	}

	@Override
	public boolean updateRow(IStoreTableRow row) {
		PrimaryKey primaryKeys = buildKey(row.getPrimaryKeyValue());
		RowUpdateChange rowUpdateChange = new RowUpdateChange(row.getTablename(), primaryKeys);
		List<Column> list = covert(row.getColumnValue());
		for (Column c : list) {
			rowUpdateChange.put(c);
		}
		UpdateRowResponse r = client.updateRow(new UpdateRowRequest(rowUpdateChange));
		return r.getRequestId() != null;
	}

}
