// 事件队列设计模式

struct PlayMessage {
    SoundId id;
    int volume;
};

// 使用环形缓存技术
// 环形队列区间: [队首, 队尾)
// 缓存数据包含队首，不含队尾
// 队尾实际指向队列终点的下一个位置
class Audio {
public:
    static void init() {
        head = 0;
        tail = 0;
    }

    static void playSound(SoundId id, int volume);
    static void update();

private:
    static const int MAX_PENDING = 16;
    static PlayMessage pending[MAX_PENDING];
    static int head;
    static int tail;
};

void Audio::playSound(SoundId id, int volume) {
    // 合并请求
    for (int i = head; i!=tail; i = (i+1) % MAX_PENDING) {
        if (pending[i].id == id) {
            // 已有事件，取最大音量，然后无需入队
            pending[i].volume = max(volume, pendig[i].volume);
            return;
        }
    }

    assert((tail + 1) % MAX_PENDING != head);
    pending[tail].id = id;
    pending[tail].volume = volume;
    tail = (tail + 1) % MAX_PENDING;
}

void Audio::update() {
    // 队列里面没有数据直接退出
    if (head == tail) {
        return;
    }

    ResourceId resource = loadSound(pending_[i].id);
    int channel = findOpenChannel();
    
    if (channel == -1) {
        return;
    }
    
    startSound(resource, channel, pending_[i].volume);
    head = (head + 1) % MAX_PENDING;
}

