package com.github.lecho.mobilization.async;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Leszek on 07.10.2016.
 */

@IgnoreExtraProperties
public class JsonDataVersion {

    public static final String JSON_NODE = "json_data";
    public static final long DEFAULT_VERSION = 1;
    public long version;
}
