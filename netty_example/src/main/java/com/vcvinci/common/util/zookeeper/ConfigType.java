/**
 * FileName: ConfigType
 * Author:   HuangTaiHong
 * Date:     2018/12/3 14:22
 * Description: 配置类型
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.util.zookeeper;

import java.util.HashSet;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈配置类型〉
 *
 * @author HuangTaiHong
 * @create 2018/12/3
 * @since 1.0.0
 */
public enum ConfigType {
    TOPIC("topics");

    private final String label;

    ConfigType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Set<String> getAllConfigType() {
        Set<String> configTypes = new HashSet<>();
        for (ConfigType configType : ConfigType.values()) {
            configTypes.add(configType.label);
        }
        return configTypes;
    }

    public ConfigType returnEnum(String label) {
        return getByLabel(label);
    }

    public static ConfigType getByLabel(String label) {
        if (label == null) {
            return null;
        }

        if (values() == null) {
            return null;
        }

        for (ConfigType t : values()) {
            if (t.getLabel().equals(label)) {
                return t;
            }
        }
        return null;
    }
}