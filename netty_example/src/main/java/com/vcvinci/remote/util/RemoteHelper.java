package com.vcvinci.remote.util;

import io.netty.channel.Channel;

import java.net.SocketAddress;

public class RemoteHelper {

    private static String UNKOWN_ADDRESS = "999.999.999.999";

    public static String parseChannelRemoteAddr(final Channel channel) {
        if (null == channel) {
            return UNKOWN_ADDRESS;
        }
        SocketAddress remote = channel.remoteAddress();
        final String addr = remote != null ? remote.toString() : "";

        if (addr.length() > 0) {
            int index = addr.lastIndexOf("/");
            if (index >= 0) {
                return addr.substring(index + 1);
            }

            return addr;
        }

        return UNKOWN_ADDRESS;
    }
}
