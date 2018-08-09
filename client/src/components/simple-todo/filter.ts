export const enum FilterValue {
  All = "All",
  Active = "Active",
  Completed = "Done",
}

export class Filter {
  private _selected = FilterValue.All;

  get selected(): FilterValue {
    return this._selected;
  }

  set selected(value: FilterValue) {
    this._selected = value;
  }

  get all(): FilterValue {
    return FilterValue.All;
  }

  get active(): string {
    return FilterValue.Active;
  }

  get completed(): string {
    return FilterValue.Completed;
  }
}

