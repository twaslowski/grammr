export class ApiError {
  code: number;
  message: string;

  constructor(code: number, message: string) {
    this.code = code;
    this.message = message;
  }

  isUnauthorized(): boolean {
    return this.code === 401;
  }
}
