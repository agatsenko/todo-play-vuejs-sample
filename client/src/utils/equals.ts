import { IIdentity } from "@/utils/identity";

export interface IEquatable extends IIdentity {
  canEquals(v: any): boolean;

  equals(v: any): boolean;
}


export abstract class Equatable implements IEquatable {
  static equals(v1?: IEquatable | null, v2?: IEquatable | null): boolean {
    let result: boolean;
    if (v1 === undefined) {
      result = v2 === undefined;
    }
    else if (v1 === null) {
      result = v2 === null;
    }
    else {
      result = v1.equals(v2);
    }
    return result;
  }

  abstract get __identity__(): string;

  canEquals(v: any): boolean {
    return v !== undefined && v !== null && v.__identity__ === this.__identity__;
  }

  abstract equals(v: any): boolean;
}
