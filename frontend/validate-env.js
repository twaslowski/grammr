#!/usr/bin/env node

/**
 * Environment validation script for startup
 * This ensures all required environment variables are present before starting the Next.js server
 */

const requiredEnvVars = [
  'BACKEND_HOST',
  'TTS_HOST',
  'NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY',
  'CLERK_SECRET_KEY',
];

const optionalEnvVars = ['NODE_ENV', 'PORT'];

function validateEnvironment() {
  console.log('🔍 Validating environment variables...');

  const missing = [];
  const present = [];

  // Check required variables
  requiredEnvVars.forEach((varName) => {
    if (!process.env[varName]) {
      missing.push(varName);
    } else {
      present.push(varName);
    }
  });

  // Report status
  if (present.length > 0) {
    console.log('✅ Required environment variables found:');
    present.forEach((varName) => {
      const value = process.env[varName];
      // Mask sensitive values for logging
      const displayValue =
        varName.includes('SECRET') || varName.includes('KEY')
          ? value.substring(0, 8) + '...'
          : value;
      console.log(`   ${varName}=${displayValue}`);
    });
  }

  // Check optional variables
  const presentOptional = optionalEnvVars.filter(
    (varName) => process.env[varName],
  );
  if (presentOptional.length > 0) {
    console.log('ℹ️  Optional environment variables found:');
    presentOptional.forEach((varName) => {
      console.log(`   ${varName}=${process.env[varName]}`);
    });
  }

  if (missing.length > 0) {
    console.error('❌ Missing required environment variables:');
    missing.forEach((varName) => console.error(`   ${varName}`));
    console.error('\n💡 Set these environment variables and try again.');
    process.exit(1);
  }

  // Validate URL formats
  try {
    new URL(process.env.BACKEND_HOST);
    new URL(process.env.TTS_HOST);
  } catch (error) {
    console.error(
      '❌ Invalid URL format in BACKEND_HOST or TTS_HOST:',
      error.message,
    );
    process.exit(1);
  }

  console.log('✅ All environment variables validated successfully!\n');
}

// Run validation if this script is executed directly
if (require.main === module) {
  validateEnvironment();
}

module.exports = { validateEnvironment };
