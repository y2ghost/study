// 游戏循环模式
// 一个游戏循环在游玩中不断运行
// 每一次循环，无阻塞地处理玩家输入，更新游戏状态，渲染游戏
// 它追踪时间的消耗并控制游戏的速度


// 代码示例 - 追逐时间，以固定的时间间隔更新游戏
double previous = getCurrentTime();
double lag = 0.0;

while (true) {
    double current = getCurrentTime();
    double elapsed = current - previous;
    previous = current;
    lag += elapsed;
    processInput();
    
    while (lag >= MS_PER_UPDATE) { 
        update();
        lag -= MS_PER_UPDATE;
    }

    render();
}
