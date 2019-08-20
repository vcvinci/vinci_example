package com.vcvinci.remote;

import com.vcvinci.protocol.request.Request;
import com.vcvinci.protocol.response.Response;
import io.netty.channel.Channel;

public interface RemoteServer extends BaseRemote {

    int localListenPort();

    Response invokeSync(final Channel channel,
                        final Request request,
                        final long timeoutMillis) throws InterruptedException;
}
