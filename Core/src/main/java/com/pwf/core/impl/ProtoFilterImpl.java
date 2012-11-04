package com.pwf.core.impl;

import com.pwf.core.ProtoFilter;

/**
 *
 * @author mfullen
 */
class ProtoFilterImpl implements ProtoFilter
{
    public ProtoFilterImpl()
    {
    }

    public String getClassPackagePath()
    {
        return ProtoFilter.PROTOBUF_CLASSPATH_STRING;
    }
}
