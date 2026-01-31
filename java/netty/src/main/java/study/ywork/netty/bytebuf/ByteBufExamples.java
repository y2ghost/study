package study.ywork.netty.bytebuf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ByteProcessor;

// 本类用于演示ByteBuf类的使用方法
public class ByteBufExamples {
    private static final Random random = new Random();
    private static final ByteBuf DEMO_BUFFER = Unpooled.buffer(1024);
    private static final Channel NIO_DEMO_CHANNEL = new NioSocketChannel();

    // 支撑数组示例
    public static void heapBuffer() {
        ByteBuf heapBuf = DEMO_BUFFER;
        if (heapBuf.hasArray()) {
            byte[] data = heapBuf.array();
            // 计算第一个字节的偏移量和可读字节数
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            handleArray(data, offset, length);
        }
    }

    // 直接缓冲区示例
    public static void directBuffer() {
        ByteBuf directBuf = DEMO_BUFFER;
        if (!directBuf.hasArray()) {
            // 需要新的内存存储字节数据
            int length = directBuf.readableBytes();
            byte[] data = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(), data);
            handleArray(data, 0, length);
        }
    }

    // 复合缓冲区示例
    public static void byteBufComposite() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = DEMO_BUFFER;
        ByteBuf bodyBuf = DEMO_BUFFER;
        messageBuf.addComponents(headerBuf, bodyBuf);
        // 数据操作省略
        // 移除消息头并打印消息体数据
        messageBuf.removeComponent(0);

        for (ByteBuf buf : messageBuf) {
            System.out.println(buf.toString());
        }
    }

    // 访问复合缓冲区中的数据
    public static void byteBufCompositeArray() {
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();
        int length = compBuf.readableBytes();
        byte[] data = new byte[length];
        // 读取数据并写入data里面
        compBuf.getBytes(compBuf.readerIndex(), data);
        handleArray(data, 0, data.length);
    }

    // 字节操作
    public static void byteBufRelativeAccess() {
        ByteBuf buffer = DEMO_BUFFER;
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.getByte(i);
            System.out.println((char) b);
        }
    }

    // 读取所有数据
    public static void readAllData() {
        ByteBuf buffer = DEMO_BUFFER;
        while (buffer.isReadable()) {
            System.out.println(buffer.readByte());
        }
    }

    // 写入数据
    public static void write() {
        ByteBuf buffer = DEMO_BUFFER;
        while (buffer.writableBytes() >= 4) {
            buffer.writeInt(random.nextInt());
        }
    }

// 查找回车符
    public static void byteBufProcessor() {
        ByteBuf buffer = DEMO_BUFFER;
        int index = buffer.forEachByte(ByteProcessor.FIND_CR);
        System.out.println(index);
    }

    // 切片操作
    public static void byteBufSlice() {
        Charset utf8 = StandardCharsets.UTF_8;
        ByteBuf buf = Unpooled.copiedBuffer("bytebuf slice", utf8);
        ByteBuf sliced = buf.slice(0, 15);
        System.out.println(sliced.toString(utf8));
        buf.setByte(0, (byte) 'J');
        assert buf.getByte(0) == sliced.getByte(0);
    }

    // 拷贝操作
    public static void byteBufCopy() {
        Charset utf8 = StandardCharsets.UTF_8;
        ByteBuf buf = Unpooled.copiedBuffer("bytebuf copy", utf8);
        ByteBuf copy = buf.copy(0, 15);
        System.out.println(copy.toString(utf8));
        buf.setByte(0, (byte) 'J');
        assert buf.getByte(0) != copy.getByte(0);
    }

    public static void byteBufSetGet() {
        Charset utf8 = StandardCharsets.UTF_8;
        ByteBuf buf = Unpooled.copiedBuffer("bytebuf set and get", utf8);
        System.out.println((char) buf.getByte(0));
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.setByte(0, (byte) 'B');
        System.out.println((char) buf.getByte(0));
        assert readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
    }

    public static void byteBufWriteRead() {
        Charset utf8 = StandardCharsets.UTF_8;
        ByteBuf buf = Unpooled.copiedBuffer("bytebuf write", utf8);
        System.out.println((char) buf.readByte());
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.writeByte((byte) '?');
        assert readerIndex == buf.readerIndex();
        assert writerIndex != buf.writerIndex();
    }

    // 获取ByteBufAllocator方法
    public static void obtainingByteBufAllocatorReference(ChannelHandlerContext ctx) {
        Channel channel = NIO_DEMO_CHANNEL;
        ByteBufAllocator allocator1 = channel.alloc();
        System.out.println("从Channel获取引用: " + allocator1);
        ByteBufAllocator allocator2 = ctx.alloc();
        System.out.println("从ChannelHandlerContext获取引用: " + allocator2);
    }

    // 引用计数示例
    public static void referenceCounting(ByteBufAllocator allocator) {
        ByteBuf buffer = allocator.directBuffer();
        assert buffer.refCnt() == 1;
    }

    // 释放引用
    public static void releaseReferenceCountedObject() {
        ByteBuf buffer = DEMO_BUFFER;
        boolean released = buffer.release();
        System.out.println(released);
    }

    // 处理数据的桩函数
    private static void handleArray(byte[] data, int offset, int len) {
        System.out.printf("仅仅用于示例，不做实际的事儿: %s-%d-%d%n", data, offset, len);
    }

    private ByteBufExamples() {
    }
}
