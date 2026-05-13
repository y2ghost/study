package study.ywork.netty.coder;

import io.netty.channel.CombinedChannelDuplexHandler;

// CombinedChannelDuplexHandler的目的集成编解码器，利于编解码复用
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
    public CombinedByteCharCodec() {
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}
