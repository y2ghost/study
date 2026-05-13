// 原型设计模式示例

// 怪物类
class Monster() {
public:
    virtual ~Monster() {}
    virtual Monster* clone() = 0;
    // ...
}

class Ghost: public Monster {
    public:
    Ghost(int health_, int speed_)
    : health(health_),
      speed(speed_)
    {}

    virtual Monster* clone() {
        return new Ghost(health_, speed_);
    }

private:
    int health;
    int speed;
};


// 怪物生产类
class Spawner {
public:
    Spawner(Monster* prototype_)
    : prototype(prototype_)
    {}

    Monster* spawnMonster() {
        return prototype->clone();
    }

private:
    /**
     * 此处存储的是对象实例
     * 可以考虑存储生产函数指针，spawnMonster函数回调函数即可
     */
    Monster* prototype;
}

// 使用示例
int main(int ac, char* av[]) {
    Monster* ghostPrototype = new Ghost(15, 3);
    Spawner* ghostSpawner = new Spawner(ghostPrototype);
    // ..
    return 0;
}

