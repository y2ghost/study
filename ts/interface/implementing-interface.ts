// 实现接口示例
interface Task {
  name: String;
  run(arg: any): void;
}

class MyTask implements Task {
  name: String;
  constructor(name: String) {
    this.name = name;
  }

  run(arg: any): void {
    console.log(`running: ${this.name}, arg: ${arg}`);
  }
}

let myTask: Task = new MyTask("someTask");
myTask.run("test");

// 多接口实现示例
interface Shape {
  draw(): void;
}

interface Editable {
  canEdit: boolean;
  commitChanges(): void;
}

class Square implements Shape, Editable {
  canEdit: boolean;
  constructor(canEdit: boolean) {
    this.canEdit = canEdit;
  }

  commitChanges(): void {
    if (this.canEdit) {
      console.log("changes committed");
    }
  }

  draw(): void {
    console.log("drawing");
  }
}

let square: Square = new Square(true);
square.draw();
square.commitChanges();
