export class BaseError extends Error {
  readonly cause?: Error;

  constructor(message: string, cause?: Error) {
    super(message);

    this.name = this.constructor.name;
    this.message = message;
    if (!this.stack) {
      if (typeof Error.captureStackTrace === "function") {
        Error.captureStackTrace(this, this.constructor);
      }
      else {
        this.stack = (new Error(message)).stack;
      }
    }
    this.cause = cause;
    if (cause) {
      this.stack = this.stack + "\nCaused by: ";
      if (cause.stack) {
        this.stack += cause.stack;
      }
      else {
        this.stack += `${cause.name}: ${cause.message}`;
      }
    }
  }
}

export class InvalidArgError extends BaseError {
}

export class InvalidStateError extends BaseError {
}
