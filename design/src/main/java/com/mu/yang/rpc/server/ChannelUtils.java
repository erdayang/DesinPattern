package com.mu.yang.rpc.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by yangxianda on 2017/2/25.
 */
public class ChannelUtils {
    /**
     * When the read or write buffer size is larger than this limit, i/o will be
     * done in chunks of this size. Most RPC requests and responses would be
     * be smaller.
     */
    private static int NIO_BUFFER_LIMIT = 8*1024; //should not be more than 64KB.

    public static int channelRead(ReadableByteChannel channel,
                            ByteBuffer buffer) throws IOException {

        int count = (buffer.remaining() <= NIO_BUFFER_LIMIT) ?
                channel.read(buffer) : channelIO(channel, null, buffer);
        return count;
    }

    public static int channelWrite(WritableByteChannel channel, ByteBuffer buffer) throws IOException {
        return buffer.remaining() <= NIO_BUFFER_LIMIT ? channel.write(buffer) : channelIO(null, channel, buffer);
    }

    public static int channelIO(ReadableByteChannel readCh,
                                 WritableByteChannel writeCh,
                                 ByteBuffer buf) throws IOException {

        int originalLimit = buf.limit();
        int initialRemaining = buf.remaining();
        int ret = 0;

        while (buf.remaining() > 0) {
            try {
                int ioSize = Math.min(buf.remaining(), NIO_BUFFER_LIMIT);
                buf.limit(buf.position() + ioSize);

                ret = (readCh == null) ? writeCh.write(buf) : readCh.read(buf);

                if (ret < ioSize) {
                    break;
                }

            } finally {
                buf.limit(originalLimit);
            }
        }

        int nBytes = initialRemaining - buf.remaining();
        return (nBytes > 0) ? nBytes : ret;
    }

    public static int channelWrite(WritableByteChannel channel, ByteBuffer buffer) throws IOException {
        return buffer.remaining() <= NIO_BUFFER_LIMIT ? channel.write(buffer) : channelIO(null, channel, buffer);
    }
}
