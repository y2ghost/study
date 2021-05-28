const data = [
  {
    name: "烤三文鱼",
    ingredients: [
      { name: "三文鱼", amount: 1, measurement: "磅" },
      { name: "松子", amount: 1, measurement: "杯" },
      { name: "黄油生菜", amount: 2, measurement: "杯" },
      { name: "黄南瓜", amount: 1, measurement: "只" },
      { name: "橄榄油", amount: 0.5, measurement: "杯" },
      { name: "大蒜", amount: 3, measurement: "瓣" },
    ],
    steps: [
      "将烤箱预热至350度.",
      "将橄榄油撒在玻璃烤盘上.",
      "加入三文鱼，大蒜和松子...",
      "烤15分钟.",
      "加入胡桃南瓜并放入...",
      "从烤箱中取出，冷却15分钟 ....",
    ],
  },
  {
    name: "鱼炸玉米饼",
    ingredients: [
      { name: "白鱼", amount: 1, measurement: "磅" },
      { name: "奶酪", amount: 1, measurement: "杯" },
      { name: "冰淇淋", amount: 2, measurement: "杯" },
      { name: "西红柿", amount: 2, measurement: "个" },
      { name: "玉米饼", amount: 3, measurement: "个" },
    ],
    steps: [
      "把鱼放在烤架上烤至热.",
      "把鱼放在3个玉米饼上。",
      "在上面放些生菜、西红柿和奶酪",
    ],
  },
];

const Ingredients = ({ ingredients = [] }) => (
  <ul className="ingredients">
    {ingredients.map((ingredient, i) => (
      <li key={i}>{ingredient.name}</li>
    ))}
  </ul>
);

const Instructions = ({ steps = [] }) => (
  <section className="instructions">
    <h3>烹饪说明</h3>
    {steps.map((step, i) => (
      <p key={i}>{step}</p>
    ))}
  </section>
);

const Recipe = ({ name, ingredients, steps }) => (
  <section id={name.toLowerCase().replace(/ /g, "-")}>
    <h2>{name}</h2>
    <Ingredients ingredients={ingredients} />
    <Instructions steps={steps} />
  </section>
);

const Menu = ({ title, recipes }) => (
  <article>
    <header>
      <h1>{title}</h1>
    </header>
    <div className="recipes">
      {recipes.map((recipe, i) => (
        <Recipe key={i} {...recipe} />
      ))}
    </div>
  </article>
);

ReactDOM.render(
  <Menu recipes={data} title="美味的食谱" />,
  document.getElementById("react-container")
);
