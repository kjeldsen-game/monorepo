// Optional: configure or set up a testing framework before each test.
// If you delete this file, remove `setupFilesAfterEnv` from `jest.config.js`

// Used for __tests__/testing-library.js
// Learn more: https://github.com/testing-library/jest-dom
import { TextEncoder, TextDecoder } from 'util';
import '@testing-library/jest-dom'

global.TextEncoder = TextEncoder as any;
global.TextDecoder = TextDecoder as any;