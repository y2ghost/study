// 类型对象设计模式

// 怪物品种类
class Breed {
public:
    Breed(int health_, const char* attack_)
    : health(health_),
      attack(attack_)
    {}

    int getHealth() { return health; }
    const char* getAttack() { return attack; }

private:
    int health;
    const char* attack;
}

// 怪物类
class Monster {
public:
    Monster(Breed& breed_)
    : health(breed_.getHealth())
      breed(breed)
    {}

    const char* getAttack() {
        return breed.getAttack();
    }

private:
    int health;
    // breed对象决定了怪物类型
    Breed& breed;
};

