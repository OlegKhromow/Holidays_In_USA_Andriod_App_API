package stu.cn.ua.lab4.model.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HolidayNetworkEntity {
    private String date;
    private String localName;
    private String name;
    private List<String> types;

    public String getDate() {
        return date;
    }

    public String getLocalName() {
        return localName;
    }

    public String getName() {
        return name;
    }

    public String getTypes() {
        StringBuilder type = new StringBuilder();
        for (String item : types) {
            type.append(item).append(", ");
        }
        type.delete(type.length()-2, type.length()-1);
        return type.toString();
    }
}
