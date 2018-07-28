export class MenuItem {
  private _id: string;
  private _title: string;
  private _componentName: string;

  constructor(id: string, title: string, componentName: string) {
    this._id = id;
    this._title = title;
    this._componentName = componentName;
  }

  get id(): string {
    return this._id;
  }

  get title(): string {
    return this._title;
  }

  get componentName(): string {
    return this._componentName;
  }

  toString(): string {
    return `${MenuItem.constructor.name} {id: ${this.id}, title: ${this.title}, componentName: ${this.componentName}}`;
  }
}
