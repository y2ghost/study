// 接口继承示例
interface Component {
  w: number;
  h: number;
  enableEvents(enable: boolean): void;
}

interface Button extends Component {
  label: string;
}

class RadioButton implements Button {
  h: number;
  label: string;
  w: number;
  private enable: boolean;

  constructor(h: number, w: number, label: string) {
    this.h = h;
    this.w = w;
    this.label = label;
  }

  enableEvents(enable: boolean): void {
    this.enable = enable;
  }
}

let radioButton: Button = new RadioButton(100, 20, "test");
radioButton.enableEvents(true);
console.log(radioButton);

// 扩展接口来描述属性
interface Component2 {
  w: number;
  h: number;
}

interface Button2 extends Component2 {
  label: string;
  onClick(): void;
}

let btn: Button2 = {
  w: 200,
  h: 20,
  label: "test",
  onClick: function () {
    console.log("button clicked");
  },
};
console.log(btn);
btn.onClick();

// 继承多个接口
interface Component3 {
  w: number;
  h: number;
}

interface Clickable3 {
  onClick(): void;
}

interface Button3 extends Component3, Clickable3 {
  label: string;
}

let btn2: Button3 = {
  w: 100,
  h: 20,
  label: "test",
  onClick: function () {
    console.log("button clicked");
  },
};
console.log(btn2);
btn.onClick();
