package com.pwf.protohelper.models;

import com.pwf.mvc.Repository;
import java.util.Collection;

/**
 *
 * @author mfullen
 */
public interface NetworkDataRepository extends Repository<NetworkData>
{
    Collection<FlatNetworkData> load();

    void addAll(Collection<NetworkData> networkData);
}
