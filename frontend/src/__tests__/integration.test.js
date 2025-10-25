/**
 * @jest-environment node
 */

// Integration test to verify middleware and validation work together
describe('Integration: Middleware + Environment Validation', () => {
  let originalEnv;
  let consoleErrorSpy;
  let processExitSpy;

  beforeEach(() => {
    originalEnv = { ...process.env };
    consoleErrorSpy = jest.spyOn(console, 'error').mockImplementation();
    processExitSpy = jest.spyOn(process, 'exit').mockImplementation();

    // Clear module cache
    jest.resetModules();
  });

  afterEach(() => {
    process.env = originalEnv;
    consoleErrorSpy.mockRestore();
    processExitSpy.mockRestore();
    jest.clearAllMocks();
  });

  it('should successfully load middleware with valid environment', () => {
    // Set complete valid environment
    process.env.BACKEND_HOST = 'https://api.example.com';
    process.env.TTS_HOST = 'https://tts.example.com';
    process.env.NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY = 'pk_test_valid_key';
    process.env.CLERK_SECRET_KEY = 'sk_test_valid_secret';
    process.env.NODE_ENV = 'production';

    expect(() => {
      require('../middleware');
    }).not.toThrow();

    expect(processExitSpy).not.toHaveBeenCalled();
    expect(consoleErrorSpy).not.toHaveBeenCalled();
  });

  it('should fail fast on invalid environment during middleware load', () => {
    // Set invalid environment (missing required vars)
    process.env.BACKEND_HOST = 'https://api.example.com';
    // Missing other required variables

    require('../middleware');

    expect(processExitSpy).toHaveBeenCalledWith(1);
    expect(consoleErrorSpy).toHaveBeenCalledWith(
      expect.stringContaining('Missing required environment variables:'),
    );
  });

  it('should validate URL formats during middleware initialization', () => {
    // Set invalid URLs
    process.env.BACKEND_HOST = 'not-a-valid-url';
    process.env.TTS_HOST = 'also-invalid';
    process.env.NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY = 'pk_test_key';
    process.env.CLERK_SECRET_KEY = 'sk_test_secret';

    require('../middleware');

    expect(processExitSpy).toHaveBeenCalledWith(1);
  });
});

// Test that validate-env script can be run independently
describe('Integration: Standalone validate-env script', () => {
  let originalEnv;

  beforeEach(() => {
    originalEnv = { ...process.env };
    jest.clearAllMocks();
  });

  afterEach(() => {
    process.env = originalEnv;
  });

  it('should work as standalone validation', () => {
    // Set valid environment
    process.env.BACKEND_HOST = 'http://localhost:8080';
    process.env.TTS_HOST = 'http://localhost:8081';
    process.env.CLERK_SECRET_KEY = 'sk_test_456';

    const { validateEnvironment } = require('../../validate-env');

    expect(() => validateEnvironment()).not.toThrow();
  });
});
