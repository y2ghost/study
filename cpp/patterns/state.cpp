// 状态模式实例
enum State {
    STATE_STANDING, // 站立状态
    STATE_JUMPING,  // 跳跃状态
    STATE_DUCKING,  // 卧倒状态
    STATE_DIVING    // 跳斩状态
};

// 简单处理状态函数示例
void Heroine::handleInput(Input input) {
    switch(state) {
    case STATE_STANDING:
        if (input == PRESS_B) {
            state = STATE_JUMPING;
            yVelocity = JUMP_VELOCITY;
            setGraphics(IMAGE_JUMP);
        } else if (input == PRESS_DOWN) {
            state = STATE_DUCKING;
            setGraphics(IMAGE_DUCK);
        }
        break;
    case STATE_JUMPING:
        if (input == PRESS_DOWN) {
            state = STATE_DIVING;
            setGraphics(IMAGE_DIVE);
        }
        break;
    case STATE_DUCKING:
        if (input == RELEASE_DOWN) {
            state = STATE_STANDING;
            setGraphics(IMAGE_STAND);
        }
        break;
    case STATE_DIVING:
        if (完成跳斩动作) {
            state = STATE_STANDING;
            setGraphics(IMAGE_STAND);
        }
        break;
    }
}

// 状态类示例
// 如果状态没有字段，只有一个虚方法，可以简化为状态函数

class StandingState;
class DuckingState;
class JumpingState;
class DivingState;

class HeroineState {
public:
    virtual ~HeroineState() {}
    // 进入状态，此处用于控制状态的贴图
    virtual void enter(Heroine& heroine) {}
    // 处理按键输入
    virtual void handleInput(Heroine& heroine, Input input) {}
    // 更新英雄信息
    virtual void update(Heroine& heroine) {}

public:
    static StandingState standing;
    static DuckingState ducking;
    static JumpingState jumping;
    static DivingState diving;
};

class StandingState: public HeroineState {
public:
    virtual void enter(Heroine& heroine) {
        heroine.setGraphics(IMAGE_STAND);
    }

    virtual void handleInput(Heroine& heroine, Input input) {
        if (input == PRESS_B) {
            // 跳跃状态
            heroine.state = &HeroineState::jumping;
        }
    }
};

// 此处简化了只有一个英雄，所以可以使用静态状态维护chargeTime
// 如果多个英雄，则需要考虑动态分配的实例化状态
class DuckingState: public HeroineState {
public:
    DuckingState()
    : chargeTime(0)
    {}

    virtual void enter(Heroine& heroine) {
        heroine.setGraphics(IMAGE_DUCK);
    }

    virtual void handleInput(Heroine& heroine, Input input) {
        if (input == RELEASE_DOWN) {
            // 站立状态
            heroine.state = &HeroineState::standing;
        }
    }

    virtual void update(Heroine& heroine) {
        chargeTime++;
        if (chargeTime > MAX_CHARGE) {
            heroine.superBomb();
        }
    }

private:
    int chargeTime;
};

class Heroine {
public:
    virtual void handleInput(Input input) {
        state->handleInput(*this, input);
        state->enter(*this);
    }

    virtual void update() {
        state->update(*this);
    }

private:
    HeroineState* state;
}

// 分层状态机示例 - 就是可以有父状态，目的复用代码
// 在地面上的状态
class OnGroundState: public HoroineState {
public:
    virtual void handleInput(Heroine& heroine, Input input) {
        if (input == PRESS_B) {
            // 处理跳跃
        } else if (input == PRESS_DOWN) {
            // 处理俯卧
        }
    }
};

class DuckingState: public OnGroundState {
public:
    virtual void handleInput(Heroine& heroine, Input input) {
        if (input == RELEASE_DOWN) {
            // 处理站立
        } else {
            // 返回上层处理
            OnGroudState::handleInput(heroine, input);
        }
    }
};

// 记住前一个状态的实现思路：下推自动机
// 使用出入栈的方法实现
// 示意图
// ---------------------
//          入栈    出栈
// 站立     开火    站立
//          站立
// ---------------------
//
