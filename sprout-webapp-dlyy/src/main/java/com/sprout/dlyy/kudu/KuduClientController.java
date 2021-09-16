package com.sprout.dlyy.kudu;

import com.sprout.web.base.BaseController;
import org.apache.kudu.Schema;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.client.KuduTable;
import org.apache.kudu.client.KuduTableStatistics;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/dlyy/kudu")
public class KuduClientController extends BaseController {
    private KuduClient client = KuduUtils.getKuduClient();
    @GetMapping("view")
    public String view(Model model) {
        try {
            List<KuduTableMeta> metaList = new ArrayList<>();
            List<String> tableList = client.getTablesList().getTablesList();
            for (String tableName : tableList) {
                //tableName = tableName.replaceAll("impala::","");
               // tableName = tableName.replaceAll("zdlonghu.","");
                KuduTable kuduTable = client.openTable(tableName);
                Schema schema = kuduTable.getSchema();
                KuduTableMeta meta = new KuduTableMeta();
                meta.setTableId(kuduTable.getTableId());
                meta.setComment(kuduTable.getComment());
                meta.setNumReplicate(kuduTable.getNumReplicas());
                meta.setRowCount(schema.getRowSize());
               // meta.setTableSize(schema.);
                meta.setTableName(tableName);
                metaList.add(meta);
            }
            model.addAttribute("tableList",metaList);
        } catch (KuduException e) {
            model.addAttribute("tableList",new ArrayList<>());
            e.printStackTrace();
        }
        return "dlyy/kudu";
    }

    @GetMapping("showData")
    public String showData(Model model, String tableName) {
        Map<String, Object> rs = KuduUtils.getTableData(tableName);
        model.addAttribute("meta",rs.get("meta"));
        model.addAttribute("data",rs.get("data"));
        model.addAttribute("tableName", tableName);
        return "dlyy/show";
    }
}
