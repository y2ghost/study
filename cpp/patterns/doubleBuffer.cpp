// 双缓冲模式示例
// 用序列的操作模拟瞬间或者同时发送的事情

class FrameBuffer {
public:
    FrameBuffer() { clear(); }
    void clear {
        for (int i=0; i<WIDTH*HEIGHT; i++) {
            pixels[i] = WHITE;
        }
    }

    void draw(int x, int y) {
        pixels[(WIDTH*y) + x] = BLACK;
    }

    const char* getPixels() {
        return pixels;
    }

private:
    static const int WIDTH = 160;
    static const int HEIGHT = 120;
    char pixels[WIDTH * HEIGHT];
};

class Scene {
public:
    Scene()
    : current(&buffers[0]),
      next(&buffers[1]
    {}

    void draw() {
        next->clear();
        buffer.draw(1, 1);
        buffer.draw(2, 1);
        buffer.draw(1, 4);
        buffer.draw(2, 4);
        buffer.draw(3, 5);
        buffer.draw(4, 5);
        swap();
    }

    FrameBuffer& getBuffer() { return *current; }

private:
    void swap() {
        FrameBuffer* temp = current;
        current = next;
        next = temp;
    }

    FrameBuffer buffers[0];
    FrameBuffer* current;
    FrameBuffer* next;
};

// 扇巴掌游戏，保证演员顺序无论怎么调整
// 确保看到被扇巴掌的状态是一致的
class Actor {
public:
    Actor(): currentSlapped(false) {}
    virtual ~Actor() {}
    virtual void update() = 0;

    void swap() {
        currentSlapped = nextSlapped;
        // 清空新的"下一个缓冲区-此处为一个bool变量"
        nextSlapped = false;
    }

    // 模拟扇巴掌动作
    void slap() { nextSlapped = true; }
    bool wasSlapped() { return currentSlapped; }

private:
    // 用于读状态
    bool currentSlapped;
    // 用于写状态
    bool nextSlapped;
};

// 更新角色状态示例
void Stage::update() {
    for (int i=0; i<NUM_ACTORS; i++) {
        actors[i]->update();
    }

    // 状态切换必须单独循环处理，也就是必须所有演员更新完毕之后
    for (int i=0; i<NUM_ACTORS; i++) {
        actors[i]->swap();
    }
}
