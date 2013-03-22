package com.pwf.protohelper.models;

import com.pwf.mvcme.Repository;
import com.pwf.mvcme.Savable;
import java.util.Collection;

/**
 *
 * @author mfullen
 */
public interface NetworkDataRepository extends Repository<NetworkData>, Savable
{
    Collection<FlatNetworkData> load();

    void addAll(Collection<NetworkData> networkData);
}
