module.exports = {
  moduleFileExtensions: [
    "ts",
    "tsx",
    "js",
    "jsx",
    "json",
    "vue"
  ],
  transform: {
    "^.+\\.vue$": "vue-jest",
    ".+\\.(css|styl|less|sass|scss|png|jpg|ttf|woff|woff2)$": "jest-transform-stub",
    "^.+\\.tsx?$": "ts-jest"
  },
  moduleNameMapper: {
    "^@/(.*)$": "<rootDir>/src/$1"
  },
  snapshotSerializers: [
    "jest-serializer-vue"
  ],
  testMatch: [
    "**/tests/**/*.spec.(js|jsx|ts|tsx)|**/tests/**/*.tests.(js|jsx|ts|tsx)"
  ]
}
