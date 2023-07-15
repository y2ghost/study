/**
 * 观察者模式示例
 * 删除观察者对象，考虑析构函数调用removeObserver方法解除自身的注册
 * 删除被观察对象，一般先发送死亡通知给所有观察者，然后删除对象
 * 函数风格编程方式下，可以采用注册成员函数指针作为观察者
 */

class Observer {
public:
    virtual ~Observer();
    virtual void onNotify(const Entity& entity, Event event) = 0;
};

// 实现观察者-成就系统类
class Achievements: public Observer {
public:
    virtual void onNotity(const Entity& entity, Event event) {
        swith(event) {
        case EVENT_ENTITY_FALL:
            if (entity.isHero() && heroIsOnBridge) {
                unlock(ACHIVEMENT_FELL_OFF_BRIDGE);
            }
            break;
            // ...
        }
    }

private:
    void unlock(Achivement achievement) {
        // 解锁成就逻辑
    }

    bool heroIsOnBridge;
};

// 被观察者(主题)
class Subject {
public:
    void addObserver(Observer* observer) {
        // TODO 添加观察者
        numObservers++;
    }

    void removeObserver(Observer* observer) {
        // TODO 删除观察者
        numObservers--;
    }

protected:
    // 发送通知方法示例
    void notify(const Entity& entity, Event event) {
        for (int i=0; i<numObservers; i++) {
            observers[i]->onNotity(entity, event);
        }
    }

private:
    Observer* observers[MAX_OBSERVERS];
    int numObservers;
};


// 游戏物理系统实现可被观察
class Physics: public Subject {
public:
    void updateEntity(Entity& entity) {
        bool wasOnSurface entity.isOnSurface();
        entity.accelerate(GRAVITY);
        entity.update();

        if (wasOnSurface && !entity.isOnSurface()) {
            notity(entity, EVENT_START_FALL);
        }
    }
};

