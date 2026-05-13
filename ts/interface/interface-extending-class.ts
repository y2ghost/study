// 接口可以继承类，继承的属性和方法均为实现
class Component {
  private width: number;
  private height: number;

  constructor(width: number, height: number) {
    this.width = width;
    this.height = height;
  }

  display(): void {
    console.log("displaying");
  }
}

interface Widget extends Component {
  hide(): void;
}

class Button extends Component implements Widget {
  hide(): void {
    console.log("hiding");
  }
}

let w: Widget = new Button(2, 5);
console.log(w);
w.display();
w.hide();

// 多继承类示例
class Panel {
  private width: number;
  private height: number;

  constructor(width: number, height: number) {
    this.width = width;
    this.height = height;
  }
}

class Frame {
  modal: boolean;
  constructor(modal: boolean) {
    this.modal = modal;
  }
}

interface Widget2 extends Panel, Frame {
  x: number;
  y: number;
}

class Box extends Panel implements Widget2 {
  modal: boolean;
  x: number;
  y: number;
}

let box: Box = new Box(200, 100);
box.modal = true;
console.log(box);
