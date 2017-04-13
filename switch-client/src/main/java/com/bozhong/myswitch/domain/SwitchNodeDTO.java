package com.bozhong.myswitch.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bozhong.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by renyueliang on 17/4/12.
 */
public class SwitchNodeDTO extends BaseDTO {

    private static final long serialVersionUID = -318686480121392572L;

    private String nodeName;

    private String allPath;

    private String dataJson;

    private List<SwitchDataDTO> switchDataList;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getAllPath() {
        return allPath;
    }

    public void setAllPath(String allPath) {
        this.allPath = allPath;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;

        if (StringUtil.isNotBlank(dataJson)) {

            Map<String, SwitchDataDTO> map = JSON.parseObject(
                    dataJson, new TypeReference<Map<String, SwitchDataDTO>>() {
                    });
            List<SwitchDataDTO> list = new ArrayList<>();

            list.addAll(map.values());
            setSwitchDataList(list);
        }

    }

    public List<SwitchDataDTO> getSwitchDataList() {
        return switchDataList;
    }

    public void setSwitchDataList(List<SwitchDataDTO> switchDataList) {
        this.switchDataList = switchDataList;
    }
}
