package com.pwf.protohelper.controllers;

import com.google.protobuf.Message;
import com.pwf.plugin.network.client.NetworkEventListener;
import com.pwf.protohelper.models.NetworkData;
import com.pwf.protohelper.models.NetworkDataRepository;

/**
 *
 * @author mfullen
 */
class NetworkEventListenerImpl implements NetworkEventListener<Message>
{
    private final NetworkData data;
    private final NetworkDataRepository activeConnectionsRepository;

    public NetworkEventListenerImpl(NetworkData data, NetworkDataRepository activeConnectionsRepository)
    {
        this.data = data;
        this.activeConnectionsRepository = activeConnectionsRepository;
    }

    public void onClientConnected()
    {
        activeConnectionsRepository.create(data);
    }

    public void onClientDisconnected()
    {
        activeConnectionsRepository.delete(data);
    }

    public void onMessageReceived(Message message)
    {
        //todo capture message received statistics
    }

    public void onMessageSent(Message message)
    {
        //todo capture message sent statistics
    }
}
