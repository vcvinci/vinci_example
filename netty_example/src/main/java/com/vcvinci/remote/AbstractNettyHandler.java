package com.vcvinci.remote;

import com.vcvinci.protocol.request.RequestResponse;
import io.netty.channel.SimpleChannelInboundHandler;

public abstract class AbstractNettyHandler extends SimpleChannelInboundHandler<RequestResponse> {

}
