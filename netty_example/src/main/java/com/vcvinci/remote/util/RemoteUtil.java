package com.vcvinci.remote.util;

import com.vcvinci.common.exception.ErrorCodes;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static com.vcvinci.common.util.Utils.getHost;
import static com.vcvinci.common.util.Utils.getPort;

public class RemoteUtil {

    private static String localHost = null;

    private static final Logger log = LoggerFactory.getLogger(RemoteUtil.class);

    public static void closeChannel(Channel channel) {
        final String addrRemote = RemoteHelper.parseChannelRemoteAddr(channel);
        channel.close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("closeChannel: close the connection to remote address[{}] result: {}", addrRemote,
                        future.isSuccess());
            }
        });
    }

    public static String[] parseStr(String str, String regex) {
        return str.split(regex);
    }

    public static String getLocalHost() throws Exception {
        if (localHost != null) {
            return localHost;
        }
        localHost = findLocalHost();
        return localHost;
    }

    private static String findLocalHost() throws SocketException, UnknownHostException {
        // 遍历网卡，查找一个非回路ip地址并返回
        final Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        InetAddress ipv6Address = null;
        while (enumeration.hasMoreElements()) {
            final NetworkInterface networkInterface = enumeration.nextElement();
            final Enumeration<InetAddress> en = networkInterface.getInetAddresses();
            while (en.hasMoreElements()) {
                final InetAddress address = en.nextElement();
                if (!address.isLoopbackAddress()) {
                    if (address instanceof Inet6Address) {
                        ipv6Address = address;
                    } else {
                        // 优先使用ipv4
                        return normalizeHostAddress(address);
                    }
                }

            }

        }
        // 没有ipv4，再使用ipv6
        if (ipv6Address != null) {
            return normalizeHostAddress(ipv6Address);
        }
        final InetAddress localHost = InetAddress.getLocalHost();
        return normalizeHostAddress(localHost);
    }

    public static String normalizeHostAddress(final InetAddress localHost) {
        if (localHost instanceof Inet6Address) {
            return "[" + localHost.getHostAddress() + "]";
        } else {
            return localHost.getHostAddress();
        }
    }

    public static String getClientHost(final Channel channel) {
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }

    public static List<InetSocketAddress> parseAndValidateAddresses(List<String> urls) {
        List<InetSocketAddress> addresses = new ArrayList<>();

        for (String url : urls) {
            if (StringUtils.isBlank(url)) {
                continue;
            }
            try {
                String host = getHost(url);
                Integer port = getPort(url);
                if (host == null || port == null) {
                    throw ErrorCodes.COMMON_PARAM_ERROR.buildException("Invalid url in urls: " + url);
                }

                InetSocketAddress address = new InetSocketAddress(host, port);

                if (address.isUnresolved()) {
                    log.warn("Removing server {} from bootstrap.servers as DNS resolution failed for {}", url, host);
                } else {
                    addresses.add(address);
                }
            } catch (IllegalArgumentException e) {
                throw ErrorCodes.CLIENT_UNKNOWN_ERROR.buildException("Invalid url or port in urls:" + url);
            }
        }

        if (addresses.isEmpty()) {
            throw ErrorCodes.COMMON_PARAM_ERROR.buildException("No resolvable url in given urls " + urls);
        }
        return addresses;
    }
}
