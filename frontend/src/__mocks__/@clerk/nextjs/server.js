// Comprehensive mock for @clerk/nextjs/server
module.exports = {
  clerkMiddleware: jest.fn((callback) => {
    const mockMiddleware = jest.fn();
    if (callback) {
      mockMiddleware.mockImplementation(callback);
    }
    return mockMiddleware;
  }),
  auth: jest.fn(),
  currentUser: jest.fn(),
  redirectToSignIn: jest.fn(),
  redirectToSignUp: jest.fn(),
};
