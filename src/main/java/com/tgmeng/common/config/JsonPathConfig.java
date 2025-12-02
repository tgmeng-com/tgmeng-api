package com.tgmeng.common.config;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;

public class JsonPathConfig {
    public static final Configuration DEFAULT_CONF = Configuration.defaultConfiguration()
            .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
}
