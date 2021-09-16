package com.sprout.dlyy.impala.util;

import com.sprout.common.util.SproutDateUtils;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;

import java.util.*;

public class ImpalaUtils {

    private static KuduClient client;

    public static KuduClient getKuduClient() {
        if (client == null) {
            client = new KuduClient.KuduClientBuilder("10.18.18.16:7051").defaultOperationTimeoutMs(60000)
                    .defaultAdminOperationTimeoutMs(60000).build();
        }
        return client;
    }

    public static Map<String, Object> getTableData(String tableName) {
        Map<String, Object> rs = new HashMap<>();
        try {
            KuduTable kuduTable = getKuduClient().openTable(tableName);
            //first get table columns
            List<ColumnSchema> columnSchemaList = kuduTable.getSchema().getColumns();
            //KuduTableStatistics statistics = getKuduClient().getTableStatistics(tableName);
            KuduScanner.KuduScannerBuilder scannerBuilder = getKuduClient().newScannerBuilder(kuduTable);
            KuduScanner scanner = scannerBuilder.build();
            //get table data
            List<Map<String, Object>> resultList = new ArrayList<>();
            /*boolean b = true;
            int count = 0;*/
            while (scanner.hasMoreRows()) {
                RowResultIterator rowResults = scanner.nextRows();
                while (rowResults.hasNext()) {
                    RowResult result = rowResults.next();

                    Map<String, Object> data = new HashMap<>();
                    for (ColumnSchema columnSchema : columnSchemaList) {
                        String columnName = columnSchema.getName();
                        Type type = columnSchema.getType();
                        switch (type) {
                            case INT8:
                            case INT16:
                            case INT32:
                            case INT64:
                                if (result.getObject(columnName) != null) {
                                    data.put(columnName, result.getObject(columnName).toString());
                                } else {
                                    data.put(columnName, "-");
                                }
                                break;
                            case BOOL:
                                data.put(columnName, String.valueOf(result.getBoolean(columnName)));
                                break;
                            case FLOAT:
                                data.put(columnName, String.valueOf(result.getFloat(columnName)));
                                break;
                            case DOUBLE:
                                data.put(columnName, String.valueOf(result.getDouble(columnName)));
                                break;
                            case DATE:
                                Date date = result.getDate(columnName);
                                if (Objects.nonNull(date)) {
                                    data.put(columnName, SproutDateUtils.format(date));
                                } else {
                                    data.put(columnName, "-");
                                }
                                break;
                            case DECIMAL:
                                data.put(columnName, String.valueOf(result.getDecimal(columnName)));
                                break;
                            default:
                                if (Objects.nonNull(result.getObject(columnName))) {
                                    data.put(columnName, String.valueOf(result.getObject(columnName)));
                                } else {
                                    data.put(columnName,"-");
                                }
                                break;
                        }
                    }
                    resultList.add(data);
                    /*count++;
                    if (count > 5000) {
                        b = false;
                    }*/
                }
            }
            scanner.close();
            rs.put("meta", columnSchemaList);
            rs.put("data", resultList);
        } catch (KuduException e) {
            e.printStackTrace();
        }
        return rs;
    }

}
