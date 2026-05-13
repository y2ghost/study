/*
 * 命令模式是一种回调的面向对象实现
 * 命令是具项化的方法调用
 */
class Command {
public:
    virtual ~Command() {}
    virtual void execute(GameActor& actor) = 0;
    // 支持撤销
    virtual void undo() = 0;
};

// 模拟回合制游戏的移动操作
class MoveUnitCommand: public Command {
public:
    MoveUnitCommand(Unit* unit_, int x_, int y_)
    : unit(unit_),
      x(x_),
      y(y_),
      xBefore(0),
      yBefore(0)
    {}

    virtual void execute(GameActor& actor) {
        xBefore = unit->x();
        yBefore = unit->y();
        unit->moveTo(x, y);
    }

    virtual void undo() {
        unit->moveTo(xBefore, yBefore);
    }

private:
    Unit* unit;
    int x;
    int y;
    int xBefore;
    int yBefore;
};

class JumpCommand: public Command {
public:
    virtual void execute(GameActor& actor) { actor.jump(); }
    virtual void undo() { }
};

class FireCommand: public Command {
public:
    virtual void execute(GameActor& actor) { actor.fireGun(); }
    virtual void undo() { }
};

class InputHandler {
public:
    Command* handleInput();
    // ...

private:
    // ...
};

Command* InputHandler::handleInput() {
    Unit* unit = getSelectedUnit();
    if (isPressed(BUTTON_UP)) {
        // 向上移动单位
        int destY = unit->y() - 1;
        return new MoveUnitCommand(unit, unit->x(), destY);
    }

    if (isPressed(BUTTON_DOWN)) {
        // 向下移动单位
        int destY = unit->y() + 1;
        return new MoveUnitCommand(unit, unit->x(), destY);
    }

    if (isPressed(BUTTON_X)) {
        return buttonX;
    }

    if (isPressed(BUTTON_Y)) {
        return buttonY;
    }

    if (isPressed(BUTTON_A)) {
        return buttonA;
    }

    if (isPressed(BUTTON_B)) {
        return buttonB;
    }

    return NULL;
}

/**
 *  撤销和重做功能可以配合undo方法和记录指令(命令)列表来实现
 *  命令列表示例
 *  之前的 command1 | command2 | command3 | command4 | ... 之后的
 *                      撤销     当前命令    重做
 * 当前命令代表用户现在执行的命令
 * 撤销操作就是执行当前命令的undo操作，然后命令列表指针往后退
 * 重做操作就是执行当前命令后面的命令，然后命令列表指针往前进
 * 如果撤销之后选择了新命令，则清除当前命令之后的全部命令
 */
int main(int ac, char* av[]) {
    // 处理命令代码
    InputHandler inputHandler = new InputHandler;
    Command* command = inputHandler.handleInput();

    if (NULL != command) {
        command->execute(actor);
    }

    // ...
    return 0;
}

