package kindof.gokzachievements.commands;

import org.apache.commons.collections4.keyvalue.DefaultMapEntry;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum EParameter {
    modes_list_string(
            "kz_timer",
            new DefaultMapEntry<>("vnl", "kz_vanilla"),
            new DefaultMapEntry<>("vanilla", "kz_vanilla"),
            new DefaultMapEntry<>("kzt", "kz_timer"),
            new DefaultMapEntry<>("kztimer", "kz_timer"),
            new DefaultMapEntry<>("skz", "kz_simple"),
            new DefaultMapEntry<>("simplekz", "kz_simple")
    ),
    has_teleports(
            "false",
            new DefaultMapEntry<>("tp", "true"),
            new DefaultMapEntry<>("pro", "false")
    ),
    limit(
            "3",
            new DefaultMapEntry<>("0", "3")
    ),
    tickrate("128"),
    stage("0"),
    map_name(),
    jumptype(
            "longjump",
            new DefaultMapEntry<>("lj", "longjump"),
            new DefaultMapEntry<>("bh", "bhop"),
            new DefaultMapEntry<>("multibh", "multibhop"),
            new DefaultMapEntry<>("mbh", "multibhop"),
            new DefaultMapEntry<>("wj", "weirdjump"),
            new DefaultMapEntry<>("dropbh", "dropbhop"),
            new DefaultMapEntry<>("dbh", "dropbhop"),
            new DefaultMapEntry<>("cj", "countjump"),
            new DefaultMapEntry<>("laj", "ladderjump")
    ),
    is_crouch_bind(
            "true",
            new DefaultMapEntry<>("bind", "true"),
            new DefaultMapEntry<>("nobind", "false"),
            new DefaultMapEntry<>("no-bind", "false")
    );

    private Map<String, String> paramValues;
    private String defaultValue;

    @SafeVarargs
    EParameter(String defaultValue, Map.Entry<String, String>... paramEntries) {
        this.defaultValue = defaultValue;
        paramValues = new HashMap<>();
        for (Map.Entry<String, String> paramEntry : paramEntries) {
            paramValues.put(paramEntry.getKey(), paramEntry.getValue());
        }
    }

    @SafeVarargs
    EParameter(Map.Entry<String, String>... paramEntries) {
        this(null, paramEntries);
    }

    EParameter(String defaultValue) {
        this.defaultValue = defaultValue;
        paramValues = null;
    }

    EParameter() {
        this.defaultValue = null;
        this.paramValues = null;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getApiParameter(String paramValue) {
        if (paramValue != null) {
            paramValue = paramValue.toLowerCase();

            if (paramValues == null) {
                return paramValue;
            }

            String result = paramValues.get(paramValue);
            if (result == null) {
                if (this == modes_list_string || this == has_teleports) {
                    return defaultValue;
                } else if (this == limit) {
                    if (StringUtils.isNumeric(paramValue)) {
                        return paramValue;
                    }
                    return defaultValue;
                }
                return paramValue;
            }
            return result;
        }

        return defaultValue;
    }
}
