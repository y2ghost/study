// 更新方法模式
// 游戏世界管理对象集合
// 每个对象实现一个更新方法模拟对象在一帧内的行为
// 每一帧游戏循环更新集合中的每一个对象

class Entity {
public:
    Entity()
    : x(0), y(0)
    {}

    virtual ~Entity() {}
    virtual void update() = 0;

    double x() const { return x; }
    double y() const { return y; }

    void setX(double val) { x = val; }
    void setY(double val) { y = val; }

private:
    double x;
    double y;
};

class World {
public:
    World()
    : numEntities(0)
    {}

    void gameLoop();

private:
    Entity* entities[MAX_ENTITIES];
    int numEntities;
};

void World::gameLoop() {
    while (true) {
        // 处理用户输入
        // 更新每个实体
        for (int i=0; i<numEntities; i++) {
            entities[i]->update();
        }

        // 物理和渲染
    }
}

// 更新方法建议放到组件类或是委托类中
// 如果存在大量不需要更新的类，建议单独的集合保存活动对象

