/**
 * 享元模式示例
 */
class TreeModel {
    // ...
private:
    Mesh mesh;
    Texture bark;
    Texture leaves;
};

class Tree {
    // ...
private:
    TreeModel* model;
    Vector position;
    double height;
    double thickness;
    Color barkTint;
    Color leafTint;
};

/**
 * 享元对象几乎总是不变
 * 地形类示例
 */
class Terrain {
public:
    Terrian(int movementCost_, bool isWater_, Texture texture_)
    : movementCost(movementCost_),
      isWater(isWater_),
      texture(texture_)
    {}

    int getMovementCost() const { return movementCost; }
    bool isWater() const { return isWater; }
    const Texture& getTexture() const { return texture; }

private:
    int movementCost;
    bool isWater;
    Texture texture;
};

// 游戏世界管理地形数据
class World {
public:
    World()
    : grassTerrain(1, false, GRASS_TEXTURE),
      riverTerrain(2, true, RIVER_TEXTURE),
      hillTerrain(3, false, HILL_TEXTURE)
    {}

    void generateTerrain();
    const Terrain& getTile(int x, int y) const;

    // ...
private:
    Terrian* tiles[WIDTH][HEIGHT];
    Terrian grassTerrain;
    Terrain riverTerrain;
    Terrain hillTerrain;
};

// 描绘地形示例
void World::generateTerrain() {
    // 填充草地和山丘
    for (int x=0; x<WIDTH; x++) {
        for (int y=0; y<HEIGHT; y++) {
            if(0 == random(10)) {
                tiles[x][y] = &hillTerrain;
            } else {
                tiles[x][y] = &grassTerrain;
            }
        }
    }

    // 填充河流
    int x = random(WIDTH);
    for (int y=0; y<HEIGHT; y++) {
        tiles[x][y] = &riverTerrain;
    }
}

// 获取地形数据
const Terrain& World::getTile(int x, int y) const {
    return *tiles[x][y];
}
