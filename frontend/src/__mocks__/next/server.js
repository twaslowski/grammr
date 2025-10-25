// Mock for Next.js server components
module.exports = {
  NextRequest: class MockNextRequest {
    constructor(url) {
      this.url = url;
      this.nextUrl = new URL(url);
    }
  },
  NextResponse: {
    rewrite: jest.fn(),
    next: jest.fn(),
    json: jest.fn(),
    redirect: jest.fn(),
  },
};
