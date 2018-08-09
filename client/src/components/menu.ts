export class MenuItem {
  private _id: string;
  private _title: string;
  private _path: string;

  constructor(id: string, title: string, path: string) {
    this._id = id;
    this._title = title;
    this._path = path;
  }

  get id(): string {
    return this._id;
  }

  get title(): string {
    return this._title;
  }

  get path(): string {
    return this._path;
  }

  toString(): string {
    return `${MenuItem.constructor.name} {id: ${this.id}, title: ${this.title}, path: ${this.path}}`;
  }
}
