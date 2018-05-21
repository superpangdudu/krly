package com.krly.project.batterymanagement.batteryserver;

import cn.krly.project.batterymanagement.protocol.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Administrator on 2018/5/18.
 */
public class ProtocolDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in,
                          List<Object> out) throws Exception {
        if (in.readableBytes() < 4)
            return;

        if (in.readableBytes() > 256) {
            in.discardReadBytes();
            return;
        }

        //
        in.markReaderIndex();

        // ignore '##'
        byte head = in.readByte();
        if (head != '#') {
            in.discardReadBytes();
            return;
        }

        head = in.readByte();
        if (head != '#') {
            in.discardReadBytes();
            return;
        }

        // "BB" for HeartBeat
        byte high = in.readByte();
        int tmp = high;
        if (tmp == 'B') {
            tmp = in.readByte();
            in.resetReaderIndex();

            if (tmp != 'B')
                return;

            ByteBuf buf = in.readSlice(4).retain();
            out.add(buf);

            return;
        }

        if (in.readableBytes() < 6)
            return;

        byte low = in.readByte();
        in.resetReaderIndex();

        //
        int length = ProtocolUtils.hexByteToInt(new byte[] { high, low });
        if (in.readableBytes() < length)
            return;

        //
        ByteBuf buf = in.readSlice(length).retain();
        out.add(buf);
    }
}
