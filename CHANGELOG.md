# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2025-05-24

### 🎉 Initial Release

**Core Features:**
- ✨ **Floating Bubble**: Create draggable floating bubbles that appear on top of other apps
- 🪟 **Overlay Windows**: Display customizable overlay interfaces with native components
- 🎯 **Event System**: Comprehensive event handling for user interactions
- 📱 **Android Support**: Full Android implementation with overlay permissions
- 🔧 **TypeScript**: Complete TypeScript support with detailed type definitions

**Floating Bubble Features:**
- Draggable floating bubble that can be moved around the screen
- Customizable bubble icon (URI, resource ID, or asset path)
- Configurable bubble color and initial position
- Tap detection with event emission
- Automatic screen boundary constraints
- Foreground service for persistent operation

**Overlay Features:**
- Customizable overlay windows with native Android components
- Support for containers, text, buttons, and spacers
- Flexible styling system (colors, padding, borders, fonts)
- Custom action handlers for button interactions
- Dynamic overlay updates without recreation
- Built-in actions: close, end call, and custom actions

**Event System:**
- `onBubbleTapped`: Fired when the floating bubble is tapped
- `onBubbleShown`: Fired when bubble is successfully displayed
- `onBubbleHidden`: Fired when bubble is removed
- `onCallEnded`: Fired when end call button is pressed
- `onCustomAction`: Fired for custom button actions
- `onPermissionMissing`: Fired when overlay permission is not granted
- `onOverlayError`: Fired when overlay operations fail

**Permission Management:**
- Automatic overlay permission detection
- Request overlay permission functionality
- Permission status checking
- Graceful fallback when permissions are denied

**Developer Experience:**
- Comprehensive TypeScript definitions
- Detailed JSDoc documentation
- Expo config plugin for automatic setup
- Example configurations and usage patterns
- Error handling and logging

**API Methods:**
- `showBubble(options?)`: Display a floating bubble
- `hideBubble()`: Hide the floating bubble
- `showOverlay(options?)`: Display an overlay window
- `hideOverlay()`: Hide the overlay window
- `updateOverlay(options?)`: Update existing overlay content
- `requestOverlayPermission()`: Request overlay permissions
- `canDrawOverlays()`: Check overlay permission status

### 🛠 Technical Details

**Android Implementation:**
- Native Kotlin implementation
- WindowManager integration for overlay display
- Touch event handling for drag and tap detection
- Background service for persistent operation
- React Native event bridge integration

**Build System:**
- Expo modules framework
- Gradle build configuration
- TypeScript compilation
- Automatic plugin registration

**Dependencies:**
- expo-modules-core: ~1.5.0
- @expo/config-plugins: ~9.0.0
- Android API 23+ support
- Overlay permission requirement (Android 6.0+)

### 📚 Documentation

- Complete API documentation with JSDoc
- TypeScript interface definitions
- Usage examples and best practices
- Permission handling guidelines
- Configuration options reference
