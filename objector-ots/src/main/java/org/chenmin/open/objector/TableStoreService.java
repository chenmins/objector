package org.chenmin.open.objector;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.alicloud.openservices.tablestore.ClientConfiguration;
import com.alicloud.openservices.tablestore.ClientException;
import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.TableStoreException;
import com.alicloud.openservices.tablestore.model.AlwaysRetryStrategy;
import com.alicloud.openservices.tablestore.model.CapacityUnit;
import com.alicloud.openservices.tablestore.model.Column;
import com.alicloud.openservices.tablestore.model.ColumnValue;
import com.alicloud.openservices.tablestore.model.CreateTableResponse;
import com.alicloud.openservices.tablestore.model.DeleteRowRequest;
import com.alicloud.openservices.tablestore.model.DeleteRowResponse;
import com.alicloud.openservices.tablestore.model.DeleteTableRequest;
import com.alicloud.openservices.tablestore.model.DeleteTableResponse;
import com.alicloud.openservices.tablestore.model.DescribeTableRequest;
import com.alicloud.openservices.tablestore.model.DescribeTableResponse;
import com.alicloud.openservices.tablestore.model.Direction;
import com.alicloud.openservices.tablestore.model.GetRangeRequest;
import com.alicloud.openservices.tablestore.model.GetRangeResponse;
import com.alicloud.openservices.tablestore.model.GetRowRequest;
import com.alicloud.openservices.tablestore.model.GetRowResponse;
import com.alicloud.openservices.tablestore.model.PrimaryKey;
import com.alicloud.openservices.tablestore.model.PrimaryKeyBuilder;
import com.alicloud.openservices.tablestore.model.PrimaryKeyColumn;
import com.alicloud.openservices.tablestore.model.PrimaryKeyOption;
import com.alicloud.openservices.tablestore.model.PrimaryKeySchema;
import com.alicloud.openservices.tablestore.model.PrimaryKeyType;
import com.alicloud.openservices.tablestore.model.PrimaryKeyValue;
import com.alicloud.openservices.tablestore.model.PutRowRequest;
import com.alicloud.openservices.tablestore.model.PutRowResponse;
import com.alicloud.openservices.tablestore.model.RangeRowQueryCriteria;
import com.alicloud.openservices.tablestore.model.ReservedThroughput;
import com.alicloud.openservices.tablestore.model.ReturnType;
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
		int timeToLive = table.timeToLive();
		// 保存的最大版本数, 设置�?即代表每列上�?��保存3个最新的版本.
		int maxVersions = table.maxVersions();
		int writeCapacityUnit = table.writeCapacityUnit();
		long maxTimeDeviation = table.maxTimeDeviation();
		int readCapacityUnit = table.readCapacityUnit();
		TableOptions tableOptions = new TableOptions(timeToLive, maxVersions,maxTimeDeviation);
		CreateTableRequestEx request = new CreateTableRequestEx(tableMeta, tableOptions,
				new ReservedThroughput(new CapacityUnit(readCapacityUnit, writeCapacityUnit)));
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
		return putRow(row,row.getTablename(), row.getPrimaryKeyValue(), row.getColumnValue());
	}

	private boolean putRow(IStoreTableRow row,String tablename, Map<String, PrimaryKeyValueObject> primaryKey,
			Map<String, ColumnValueObject> column) {
		PrimaryKey primaryKeys = buildKey(row);
		RowPutChange rowPutChange = new RowPutChange(tablename, primaryKeys);
		List<Column> list = covert(column);
		for (Column c : list) {
			rowPutChange.addColumn(c);
		}
		if(row.autoPrimaryKey()!=null&&!row.autoPrimaryKey().isEmpty()){
			//这里设置返回类型为RT_PK，意思是在返回结果中包含PK列的值。如果不设置ReturnType，默认不返回。
			rowPutChange.setReturnType(ReturnType.RT_PK);
		}
		PutRowResponse r = client.putRow(new PutRowRequest(rowPutChange));
		if(row.autoPrimaryKey()!=null&&!row.autoPrimaryKey().isEmpty()){
			Row rows = r.getRow();
			PrimaryKey pk = rows.getPrimaryKey();
			Map<String, PrimaryKeyValueObject> v =  new LinkedHashMap<String, PrimaryKeyValueObject>(); 
			long id = pk.getPrimaryKeyColumn(row.autoPrimaryKey()).getValue().asLong();
			v.put(row.autoPrimaryKey(), PrimaryKeyValueObject.fromLong(id));
			row.setPrimaryKeyValue(v );
		}
		return r.getRequestId() != null;
	}

	private List<Column> covert(Map<String, ColumnValueObject> column) {
		List<Column> list = new ArrayList<Column>();
		for (String ck : column.keySet()) {
			ColumnValueObject cv = column.get(ck);
			if (cv.getValue() == null)
				continue;
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

	private PrimaryKey buildKey(IStoreTableRow row ) {
		// 构�?主键
		 Map<String, PrimaryKeyValueObject> primaryKey = row.getPrimaryKeyValue();
		PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
		for (String pk : primaryKey.keySet()) {
			if (row.autoPrimaryKey()!=null&&!row.autoPrimaryKey().isEmpty() && row.autoPrimaryKey().equals(pk)) {
				Long pkAsLong = primaryKey.get(pk).asLong();
				if(pkAsLong==null||pkAsLong==0){
					//替换之用于新增
					primaryKeyBuilder.addPrimaryKeyColumn(pk, PrimaryKeyValue.AUTO_INCREMENT);
				}else{
					//如果pk自增有值，则替换，用于getrow时候。
					primaryKeyBuilder.addPrimaryKeyColumn(pk,PrimaryKeyValue.fromLong(pkAsLong));
				}
			} else {
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

		}
		PrimaryKey primaryKeys = primaryKeyBuilder.build();
		return primaryKeys;
	}

	@Override
	public boolean getRow(IStoreTableRow row) {
		int max = 1;
		GetRowResponse getRowResponse = getRow(row, max);
		if (getRowResponse == null)
			return false;
		return getRowResponse.getRequestId() != null;
	}

	private GetRowResponse getRow(IStoreTableRow row, int max) {
		PrimaryKey primaryKeys = buildKey(row );
		// 读一�?
		SingleRowQueryCriteria criteria = new SingleRowQueryCriteria(row.getTablename(), primaryKeys);
		// 设置读取�?��版本
		criteria.setMaxVersions(max);
		// TimeRange timeRange;
		// criteria.setTimeRange(timeRange);
		GetRowResponse getRowResponse = client.getRow(new GetRowRequest(criteria));
		if (getRowResponse == null)
			return getRowResponse;
		Row rows = getRowResponse.getRow();
		if (rows == null)
			return null;
		reback(row, rows);
		return getRowResponse;
	}

	private void reback(IStoreTableRow row, PrimaryKey  rows) {
		Map<String, PrimaryKeyColumn> pk = rows.getPrimaryKeyColumnsMap();
		Map<String, PrimaryKeyValueObject> p = new LinkedHashMap<String, PrimaryKeyValueObject>();
		for(String pa:pk.keySet()){
			PrimaryKeyValueObject pvo = covert(pk.get(pa).getValue());
			p.put(pa, pvo );
		}
		row.setPrimaryKeyValue(p);
	}
	
	private void reback(IStoreTableRow row, Row rows) {
		if(rows.getPrimaryKey().getDataSize()!=0){
			//判断是否需要转换主键
			Map<String, PrimaryKeyColumn> pk = rows.getPrimaryKey().getPrimaryKeyColumnsMap();
			Map<String, PrimaryKeyValueObject> p = new LinkedHashMap<String, PrimaryKeyValueObject>();
			for(String pa:pk.keySet()){
				PrimaryKeyValueObject pvo = covert(pk.get(pa).getValue());
				p.put(pa, pvo );
			}
			row.setPrimaryKeyValue(p);
		}
		//此处为列
		Column[] cols = rows.getColumns();
		Map<String, ColumnValueObject> v = new LinkedHashMap<String, ColumnValueObject>();
		for (Column c : cols) {
			// ColumnValue cv = c.getValue();
			// 取出最后版本的字
			Column cc = rows.getLatestColumn(c.getName());
			ColumnValue cv = cc.getValue();
			ColumnValueObject cvo = covert(cv);
			v.put(c.getName(), cvo);
		}
		row.setColumnValue(v);
	}
	
	private PrimaryKeyValueObject covert(PrimaryKeyValue cv) {
		PrimaryKeyValueObject cvo = null;
		switch (cv.getType()) {
		case STRING:
			cvo = new PrimaryKeyValueObject(cv.asString(), PrimaryKeyTypeObject.STRING);
			break;
		case INTEGER:
			cvo = new PrimaryKeyValueObject(cv.asLong(), PrimaryKeyTypeObject.INTEGER);
			break;
		case BINARY:
			cvo = new PrimaryKeyValueObject(cv.asBinary(), PrimaryKeyTypeObject.BINARY);
			break;
		default:
			break;
		}
		return cvo;
	}

	private ColumnValueObject covert(ColumnValue cv) {
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
		return cvo;
	}

	@Override
	public boolean getByMaxVersions(IStoreTableRow row, int max,
			NavigableMap<String, NavigableMap<Long, ColumnValueObject>> columnMap) {
		GetRowResponse getRowResponse = getRow(row, max);
		if (getRowResponse == null)
			return false;
		NavigableMap<String, NavigableMap<Long, ColumnValue>> cm = getRowResponse.getRow().getColumnsMap();
		for (String colname : cm.keySet()) {
			NavigableMap<Long, ColumnValueObject> ccm = new TreeMap<Long, ColumnValueObject>();
			NavigableMap<Long, ColumnValue> accm = cm.get(colname);
			for (Long accmTime : accm.keySet()) {
				ColumnValueObject cvo = covert(accm.get(accmTime));
				ccm.put(accmTime, cvo);
			}
			columnMap.put(colname, ccm);
		}
		return getRowResponse.getRequestId() != null;
	}

	@Override
	public boolean deleteRow(IStoreTableRow row) {
		PrimaryKey primaryKeys = buildKey(row );
		RowDeleteChange rowDeleteChange = new RowDeleteChange(row.getTablename(), primaryKeys);
		DeleteRowResponse r = client.deleteRow(new DeleteRowRequest(rowDeleteChange));
		return r.getRequestId() != null;
	}

	@Override
	public boolean updateRow(IStoreTableRow row) {
		PrimaryKey primaryKeys = buildKey(row );
		RowUpdateChange rowUpdateChange = new RowUpdateChange(row.getTablename(), primaryKeys);
		List<Column> list = covert(row.getColumnValue());
		for (Column c : list) {
			rowUpdateChange.put(c);
		}
		UpdateRowResponse r = client.updateRow(new UpdateRowRequest(rowUpdateChange));
		return r.getRequestId() != null;
	}

	@Override
	public boolean deleteTable(IStoreTable table) {
		DeleteTableRequest request = new DeleteTableRequest(table.getTablename());
		DeleteTableResponse r = client.deleteTable(request);
		return r.getRequestId() != null;
	}

	@Override
	public boolean increment(IStoreTableRow row) {

//        RowUpdateChange rowUpdateChange = new RowUpdateChange(TABLE_NAME, primaryKey);
//
//        // 将price列值+10，不允许设置时间戳
//        rowUpdateChange.increment(new Column("price", ColumnValue.fromLong(10)));
//
//        // 设置ReturnType将原子计数器的结果返回
//        rowUpdateChange.addReturnColumn("price");
//        rowUpdateChange.setReturnType(ReturnType.RT_AFTER_MODIFY);
//        
//        // 对price列发起原子计数器操作
//        UpdateRowResponse response = client.updateRow(new UpdateRowRequest(rowUpdateChange));
//
//        // 打印出更新后的新值
//        Row row = response.getRow();
		PrimaryKey primaryKeys = buildKey(row );
		RowUpdateChange rowUpdateChange = new RowUpdateChange(row.getTablename(), primaryKeys);
		List<Column> list = covert(row.getColumnValue());
		for (Column c : list) {
			rowUpdateChange.increment(c);
			rowUpdateChange.addReturnColumn(c.getName());
		}
		rowUpdateChange.setReturnType(ReturnType.RT_AFTER_MODIFY);
		UpdateRowResponse r = client.updateRow(new UpdateRowRequest(rowUpdateChange));
		Row rows = r.getRow();
		reback(row, rows);
		return r.getRequestId() != null;
	}

	@Override
	public IStoreTableRow getRange(IStoreTableRow start, IStoreTableRow end,List<IStoreTableRow> range,boolean asc,int limit)
			throws StoreException {
		RangeRowQueryCriteria rangeRowQueryCriteria = new RangeRowQueryCriteria(start.getTablename());

        // 设置起始主键
        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        Map<String, PrimaryKeyValueObject> s1 = start.getPrimaryKeyValue();
        for(String pk:start.getPrimaryKeyValue().keySet()){
        	PrimaryKeyValueObject pkv = s1.get(pk);
        	if(pkv.getValue()!=null){
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
        	}else{
        		if(asc){
        			 primaryKeyBuilder.addPrimaryKeyColumn(pk, PrimaryKeyValue.INF_MIN);
        		}else{
        			 primaryKeyBuilder.addPrimaryKeyColumn(pk, PrimaryKeyValue.INF_MAX);
        		}
        		
        	}
        }
        rangeRowQueryCriteria.setInclusiveStartPrimaryKey(primaryKeyBuilder.build());

        // 设置结束主键
        primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
        Map<String, PrimaryKeyValueObject> s2 = end.getPrimaryKeyValue();
        for(String pk:start.getPrimaryKeyValue().keySet()){
        	PrimaryKeyValueObject pkv = s2.get(pk);
        	if(pkv.getValue()!=null){
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
        	}else{
        		 if(asc){
        			 primaryKeyBuilder.addPrimaryKeyColumn(pk, PrimaryKeyValue.INF_MAX);
        		}else{
        			 primaryKeyBuilder.addPrimaryKeyColumn(pk, PrimaryKeyValue.INF_MIN);
        		}
        	}
        }
        rangeRowQueryCriteria.setExclusiveEndPrimaryKey(primaryKeyBuilder.build());
        rangeRowQueryCriteria.setLimit(limit);
        if(asc){
        	 rangeRowQueryCriteria.setDirection(Direction.FORWARD);
        }else{
        	 rangeRowQueryCriteria.setDirection(Direction.BACKWARD);
        }
        rangeRowQueryCriteria.setMaxVersions(1);
        System.out.println("GetRange的结果为:");
        GetRangeResponse getRangeResponse =null;
        getRangeResponse = client.getRange(new GetRangeRequest(rangeRowQueryCriteria));
        for (Row row : getRangeResponse.getRows()) {
        	
        	try {
				IStoreTableRow a = start.getClass().newInstance();
				reback(a ,row);
				range.add(a);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
//            System.out.println(row);
        }
        // 若nextStartPrimaryKey不为null, 则继续读取.
        IStoreTableRow next = null;
		try {
			next = start.getClass().newInstance();
			if (getRangeResponse.getNextStartPrimaryKey() != null) {
				reback(next ,getRangeResponse.getNextStartPrimaryKey());
			}else{
				next = null;
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        while (true) {
//            getRangeResponse = client.getRange(new GetRangeRequest(rangeRowQueryCriteria));
//            for (Row row : getRangeResponse.getRows()) {
//            	
//            	try {
//					IStoreTableRow a = start.getClass().newInstance();
//					reback(a ,row);
//					range.add(a);
//				} catch (InstantiationException e) {
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				}
//                System.out.println(row);
//            }
//            // 若nextStartPrimaryKey不为null, 则继续读取.
//            if (getRangeResponse.getNextStartPrimaryKey() != null) {
//                rangeRowQueryCriteria.setInclusiveStartPrimaryKey(getRangeResponse.getNextStartPrimaryKey());
//            } else {
//                break;
//            }
//        }
        return next;
	}

}
