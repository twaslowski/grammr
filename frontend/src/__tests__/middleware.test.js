/**
 * @jest-environment node
 */

describe('middleware', () => {
  let originalEnv;
  let consoleErrorSpy;
  let mockProcessExit;

  beforeEach(() => {
    // Store original environment
    originalEnv = { ...process.env };

    // Mock console.error
    consoleErrorSpy = jest.spyOn(console, 'error').mockImplementation();

    // Mock process.exit
    mockProcessExit = jest.spyOn(process, 'exit').mockImplementation();

    // Clear module cache to ensure fresh import
    jest.resetModules();
  });

  afterEach(() => {
    // Restore original environment
    process.env = originalEnv;

    // Restore console and process
    consoleErrorSpy.mockRestore();
    mockProcessExit.mockRestore();

    // Clear all mocks
    jest.clearAllMocks();
  });

  describe('config matcher', () => {
    it('should export the correct matcher configuration', () => {
      // Set valid environment variables
      process.env.BACKEND_HOST = 'http://localhost:8080';
      process.env.TTS_HOST = 'http://localhost:8081';
      process.env.CLERK_SECRET_KEY = 'sk_test_456';

      const middlewareModule = require('../middleware');
      const config = middlewareModule.config;

      expect(config).toHaveProperty('matcher');
      expect(config.matcher).toEqual([
        '/((?!_next|[^?]*\\.(?:html?|css|js(?!on)|jpe?g|webp|png|gif|svg|ttf|woff2?|ico|csv|docx?|xlsx?|zip|webmanifest)).*)',
        '/(api|trpc)(.*)',
        '/user/(.*)',
      ]);
    });
  });
});
