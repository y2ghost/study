typedef struct AVFormatContext AVFormatContext;
typedef struct AVCodecContext AVCodecContext;
typedef struct AVPacket AVPacket;
typedef struct AVStream AVStream;

void logging(const char *fmt, ...);
void log_packet(const AVFormatContext *ctx, const AVPacket *pkt);
void print_timing(char *name, AVFormatContext *avf, AVCodecContext *avc, AVStream *avs);

