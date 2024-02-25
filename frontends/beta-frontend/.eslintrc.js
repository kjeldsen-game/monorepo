module.exports = {
  extends: ['next/core-web-vitals', 'next', 'prettier', 'plugin:react/recommended', 'plugin:react-hooks/recommended'],
  plugins: ['react', 'prettier', '@emotion'],
  overrides: [
    {
      files: ['*.ts', '*.tsx'],
      extends: ['plugin:@typescript-eslint/eslint-recommended', 'plugin:@typescript-eslint/recommended', 'prettier', 'plugin:react/recommended'],
      parser: '@typescript-eslint/parser',
      plugins: ['@typescript-eslint'],
      rules: {
        'react/react-in-jsx-scope': 'off',
        'no-shadow': 'off',
        '@typescript-eslint/no-shadow': ['error'],
        'import/no-unresolved': 'off',
        'react/require-default-props': 'off',
        'no-use-before-define': 'off',
        '@typescript-eslint/no-use-before-define': ['error'],
        'react/no-unknown-property': ['error', { ignore: ['css'] }],
        'react/prop-types': 'off',
      },
    },
  ],
}
