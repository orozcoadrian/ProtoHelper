package com.pwf.core;

/**
 *
 * @author mfullen
 */
public interface ProtoFilter
{
    static final String PROTOBUF_CLASSPATH_STRING = "com.google.protobuf.";

    String getClassPackagePath();
}
