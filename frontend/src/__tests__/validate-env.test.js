/**
 * @jest-environment node
 */

const { validateEnvironment } = require('../../validate-env');

describe('validate-env.js', () => {
  let originalEnv;
  let consoleErrorSpy;
  let consoleLogSpy;
  let processExitSpy;

  beforeEach(() => {
    // Store original environment
    originalEnv = { ...process.env };

    // Mock console methods
    consoleErrorSpy = jest.spyOn(console, 'error').mockImplementation();
    consoleLogSpy = jest.spyOn(console, 'log').mockImplementation();

    // Mock process.exit
    processExitSpy = jest.spyOn(process, 'exit').mockImplementation();
  });

  afterEach(() => {
    // Restore original environment
    process.env = originalEnv;

    // Restore console methods
    consoleErrorSpy.mockRestore();
    consoleLogSpy.mockRestore();
    processExitSpy.mockRestore();
  });

  describe('validateEnvironment', () => {
    const requiredVars = {
      BACKEND_HOST: 'http://localhost:8080',
      TTS_HOST: 'http://localhost:8081',
      CLERK_SECRET_KEY: 'sk_test_456',
    };

    it('should pass validation when all required variables are present', () => {
      // Set all required environment variables
      Object.keys(requiredVars).forEach((key) => {
        process.env[key] = requiredVars[key];
      });

      expect(() => validateEnvironment()).not.toThrow();
      expect(processExitSpy).not.toHaveBeenCalled();
      expect(consoleLogSpy).toHaveBeenCalledWith(
        '🔍 Validating environment variables...',
      );
      expect(consoleLogSpy).toHaveBeenCalledWith(
        '✅ All environment variables validated successfully!\n',
      );
    });

    it('should fail validation when required variables are missing', () => {
      // Clear all environment variables
      Object.keys(requiredVars).forEach((key) => {
        delete process.env[key];
      });

      validateEnvironment();

      expect(consoleErrorSpy).toHaveBeenCalledWith(
        '❌ Missing required environment variables:',
      );
      expect(processExitSpy).toHaveBeenCalledWith(1);
    });

    it('should fail validation when some required variables are missing', () => {
      // Set only some variables
      process.env.BACKEND_HOST = requiredVars.BACKEND_HOST;
      process.env.TTS_HOST = requiredVars.TTS_HOST;
      // Missing CLERK variables

      validateEnvironment();

      expect(consoleErrorSpy).toHaveBeenCalledWith(
        '❌ Missing required environment variables:',
      );
      expect(consoleErrorSpy).toHaveBeenCalledWith('   CLERK_SECRET_KEY');
      expect(processExitSpy).toHaveBeenCalledWith(1);
    });

    it('should validate URL formats and fail for invalid URLs', () => {
      // Set invalid URLs
      process.env.BACKEND_HOST = 'invalid-url';
      process.env.TTS_HOST = 'also-invalid';
      process.env.CLERK_SECRET_KEY = requiredVars.CLERK_SECRET_KEY;

      validateEnvironment();

      expect(consoleErrorSpy).toHaveBeenCalledWith(
        '❌ Invalid URL format in BACKEND_HOST or TTS_HOST:',
        expect.any(String),
      );
      expect(processExitSpy).toHaveBeenCalledWith(1);
    });

    it('should mask sensitive values in console output', () => {
      Object.keys(requiredVars).forEach((key) => {
        process.env[key] = requiredVars[key];
      });

      validateEnvironment();

      // Check that secret keys are masked
      expect(consoleLogSpy).toHaveBeenCalledWith(
        '   CLERK_SECRET_KEY=sk_test_...',
      );

      // Check that non-secret values are not masked
      expect(consoleLogSpy).toHaveBeenCalledWith(
        '   BACKEND_HOST=http://localhost:8080',
      );
      expect(consoleLogSpy).toHaveBeenCalledWith(
        '   TTS_HOST=http://localhost:8081',
      );
    });

    it('should log optional environment variables when present', () => {
      // Set required variables
      Object.keys(requiredVars).forEach((key) => {
        process.env[key] = requiredVars[key];
      });

      // Set optional variables
      process.env.NODE_ENV = 'production';
      process.env.PORT = '3000';

      validateEnvironment();

      expect(consoleLogSpy).toHaveBeenCalledWith(
        'ℹ️  Optional environment variables found:',
      );
      expect(consoleLogSpy).toHaveBeenCalledWith('   NODE_ENV=production');
      expect(consoleLogSpy).toHaveBeenCalledWith('   PORT=3000');
    });

    it('should handle valid URL formats correctly', () => {
      process.env.BACKEND_HOST = 'https://api.example.com:8080';
      process.env.TTS_HOST = 'http://tts.example.com';
      process.env.CLERK_SECRET_KEY = requiredVars.CLERK_SECRET_KEY;

      expect(() => validateEnvironment()).not.toThrow();
      expect(processExitSpy).not.toHaveBeenCalled();
    });
  });
});
