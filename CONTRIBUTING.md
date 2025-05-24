# Contributing to Expo Floating Bubble

Thank you for your interest in contributing to expo-floating-bubble! We welcome contributions from the community and are grateful for your support.

## 🚀 Getting Started

### Prerequisites

- Node.js 16+ and npm
- Android Studio and Android SDK
- Expo CLI
- Git

### Development Setup

1. **Fork and clone the repository:**
   ```bash
   git clone https://github.com/your-username/expo-floating-bubble.git
   cd expo-floating-bubble
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Build the module:**
   ```bash
   npm run build
   ```

4. **Test your changes:**
   ```bash
   npm run lint
   npm run test
   ```

## 🐛 Reporting Issues

Before creating an issue, please:

1. **Search existing issues** to avoid duplicates
2. **Use the latest version** of the module
3. **Provide a clear description** of the problem
4. **Include reproduction steps** with minimal code example
5. **Specify your environment:**
   - Expo SDK version
   - React Native version
   - Android version
   - Device/emulator details

### Issue Template

```
**Describe the bug**
A clear description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:
1. Call method '...'
2. With options '...'
3. See error

**Expected behavior**
What you expected to happen.

**Screenshots/Code**
Add screenshots or code snippets if applicable.

**Environment:**
- Expo SDK: [e.g. 50.0.0]
- React Native: [e.g. 0.73.0]
- Android: [e.g. API 33]
- Device: [e.g. Samsung Galaxy S21]

**Additional context**
Any other context about the problem.
```

## 💡 Feature Requests

We welcome feature requests! Please:

1. **Check existing feature requests** to avoid duplicates
2. **Describe the use case** and why it would be valuable
3. **Provide implementation ideas** if you have them
4. **Consider backward compatibility** implications

## 🔧 Development Guidelines

### Code Style

- Use **TypeScript** for all new code
- Follow **existing code style** and conventions
- Add **JSDoc comments** for public APIs
- Use **meaningful variable and function names**
- Keep functions **small and focused**

### Android Development

- Write **Kotlin code** following Android best practices
- Use **proper error handling** and logging
- Test on **multiple Android versions** (API 23+)
- Follow **Material Design** guidelines for UI components

### TypeScript/JavaScript

- Use **strict TypeScript** settings
- Export **proper type definitions**
- Write **comprehensive JSDoc** documentation
- Follow **React Native** best practices

### Testing

- Add **unit tests** for new functionality
- Test on **real devices** when possible
- Verify **permission handling** works correctly
- Test **edge cases** and error scenarios

## 📝 Pull Request Process

1. **Create a feature branch:**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** following the guidelines above

3. **Write or update tests** as needed

4. **Update documentation:**
   - Add JSDoc comments for new APIs
   - Update README.md if needed
   - Update CHANGELOG.md

5. **Test your changes:**
   ```bash
   npm run build
   npm run lint
   npm run test
   ```

6. **Commit with clear messages:**
   ```bash
   git commit -m "feat: add new overlay component type"
   git commit -m "fix: resolve bubble positioning on API 34"
   git commit -m "docs: update API documentation"
   ```

7. **Push and create a pull request:**
   ```bash
   git push origin feature/your-feature-name
   ```

### Pull Request Template

When creating a PR, please include:

- **Description** of what the PR does
- **Related issue** number (if applicable)
- **Type of change** (bug fix, feature, docs, etc.)
- **Testing performed**
- **Screenshots** (for UI changes)
- **Breaking changes** (if any)

### Commit Message Format

We follow conventional commits:

- `feat:` - New features
- `fix:` - Bug fixes
- `docs:` - Documentation changes
- `style:` - Code style changes
- `refactor:` - Code refactoring
- `test:` - Adding tests
- `chore:` - Maintenance tasks

## 🏗 Project Structure

```
expo-floating-bubble/
├── src/                          # TypeScript source files
│   ├── ExpoFloatingBubble.types.ts    # Type definitions
│   ├── ExpoFloatingBubbleModule.ts    # Main module
│   └── index.ts                       # Public exports
├── android/                      # Android native code
│   └── src/main/java/expo/modules/floatingbubble/
│       ├── ExpoFloatingBubbleModule.kt      # Main Android module
│       └── ExpoFloatingBubbleService.kt     # Background service
├── plugin/                       # Expo config plugin
│   └── src/withExpoFloatingBubble.ts
├── build/                        # Compiled output
└── docs/                         # Documentation
```

## 🧪 Testing

### Manual Testing

1. **Test on real devices** - Emulators may not support overlay permissions
2. **Test different Android versions** - API 23, 28, 31, 33+
3. **Test permission flows** - Grant, deny, revoke scenarios
4. **Test edge cases** - Large overlays, many bubbles, memory pressure

### Automated Testing

```bash
# Run linting
npm run lint

# Run TypeScript compilation
npm run build

# Run tests (when available)
npm run test
```

## 📋 Code Review Checklist

Before submitting, ensure:

- [ ] Code follows project style guidelines
- [ ] All tests pass
- [ ] Documentation is updated
- [ ] No breaking changes (or properly documented)
- [ ] Error handling is implemented
- [ ] Performance implications are considered
- [ ] Android permissions are handled correctly
- [ ] TypeScript types are accurate and complete

## 🎯 Areas for Contribution

We especially welcome contributions in these areas:

### High Priority
- **iOS support** - Currently Android-only
- **Additional overlay components** - More native UI components
- **Performance optimizations** - Memory usage, rendering efficiency
- **Testing framework** - Automated testing setup

### Medium Priority
- **Web support** - Fallback implementations for web
- **Accessibility** - Better screen reader support
- **Animation support** - Smooth transitions and animations
- **Configuration options** - More customization possibilities

### Documentation
- **API examples** - More real-world usage examples
- **Video tutorials** - Step-by-step implementation guides
- **Migration guides** - Version upgrade instructions
- **Troubleshooting** - Common issues and solutions

## 🤝 Community

- **Be respectful** and inclusive in all interactions
- **Help others** by answering questions and reviewing PRs
- **Share your use cases** to help guide development
- **Provide feedback** on proposed changes

## 📞 Getting Help

If you need help with contributing:

1. **Check existing documentation** and issues first
2. **Join discussions** in GitHub Discussions
3. **Ask questions** in issues with the "question" label
4. **Contact maintainers** via email for complex topics

## 🙏 Recognition

Contributors will be:

- **Listed in CHANGELOG.md** for their contributions
- **Credited in release notes** for significant features
- **Added to contributors list** in the repository

Thank you for contributing to expo-floating-bubble! 🚀
