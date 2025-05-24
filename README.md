# 🫧 Expo Floating Bubble

[![npm version](https://badge.fury.io/js/expo-floating-bubble.svg)](https://badge.fury.io/js/expo-floating-bubble)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform: Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)

A powerful React Native module for creating **floating bubbles** and **overlay windows** on Android devices. Perfect for building call interfaces, quick actions, floating controls, and any UI that needs to appear on top of other applications.

## ✨ Features

- 🫧 **Draggable Floating Bubbles** - Create moveable bubbles that float on top of all apps
- 🪟 **Custom Overlay Windows** - Build rich UI interfaces with native components
- 🎯 **Event-Driven Architecture** - Comprehensive event system for user interactions
- 🎨 **Highly Customizable** - Full control over appearance, positioning, and behavior
- 📱 **Android Optimized** - Native Android implementation with proper permission handling
- 🔧 **TypeScript Ready** - Complete type definitions and IntelliSense support
- ⚡ **Performance Focused** - Efficient native implementation with minimal overhead

## 🚀 Installation

```bash
npm install expo-floating-bubble
```

### Expo Development Build

This module requires a custom development build as it uses native Android features:

```bash
npx expo install expo-floating-bubble
npx expo run:android
```

### Expo Config Plugin

The module includes an automatic config plugin. Add it to your `app.json` or `expo.json`:

```json
{
  "expo": {
    "plugins": ["expo-floating-bubble"]
  }
}
```

## 📋 Prerequisites

- **Android API 23+** (Android 6.0+)
- **Overlay Permission** - Required for displaying content over other apps
- **Expo SDK 50+** or React Native 0.70+

## 🎯 Quick Start

### Basic Floating Bubble

```typescript
import ExpoFloatingBubble from 'expo-floating-bubble';

// Check and request permissions
const hasPermission = await ExpoFloatingBubble.canDrawOverlays();
if (!hasPermission) {
  ExpoFloatingBubble.requestOverlayPermission();
  return;
}

// Show a floating bubble
ExpoFloatingBubble.showBubble({
  iconUri: 'https://example.com/icon.png',
  bubbleColor: 0xFF4285F4, // Material Blue
  initialX: 100,
  initialY: 200
});
```

### Custom Overlay Window

```typescript
// Show a custom overlay with native components
ExpoFloatingBubble.showOverlay({
  componentConfig: {
    type: 'container',
    style: {
      backgroundColor: '#80000000',
      padding: 20,
      borderRadius: 16
    },
    children: [
      {
        type: 'text',
        text: 'Incoming Call',
        style: {
          color: '#FFFFFF',
          fontSize: 18,
          textAlign: 'center',
          fontWeight: 'bold'
        }
      },
      { type: 'spacer', height: 20 },
      {
        type: 'button',
        text: 'Answer',
        style: {
          backgroundColor: '#4CAF50',
          color: '#FFFFFF',
          padding: 12,
          borderRadius: 8
        },
        onPress: 'answer'
      },
      {
        type: 'button',
        text: 'Decline',
        style: {
          backgroundColor: '#F44336',
          color: '#FFFFFF',
          padding: 12,
          borderRadius: 8
        },
        onPress: 'decline'
      }
    ]
  }
});
```

## 📚 API Reference

### Methods

#### `showBubble(options?: ShowBubbleOptions): void`

Displays a draggable floating bubble on the screen.

```typescript
ExpoFloatingBubble.showBubble({
  iconUri: 'https://example.com/icon.png',    // Image URL, asset path, or resource URI
  iconResId: R.drawable.ic_notification,     // Android resource ID (alternative to iconUri)
  bubbleColor: 0xFF4285F4,                   // Bubble background color (ARGB format)
  initialX: 100,                             // Initial X position (pixels)
  initialY: 200                              // Initial Y position (pixels)
});
```

#### `hideBubble(): void`

Removes the floating bubble from the screen.

```typescript
ExpoFloatingBubble.hideBubble();
```

#### `showOverlay(options?: ShowOverlayOptions): void`

Displays a customizable overlay window with native components.

```typescript
ExpoFloatingBubble.showOverlay({
  initialData: { userId: '123', callId: 'abc' },  // Custom data passed to the overlay
  componentConfig: {
    // Component configuration (see Component System below)
  },
  customActions: {
    answer: {
      handler: (params) => console.log('Call answered:', params),
      description: 'Answer the incoming call'
    }
  }
});
```

#### `hideOverlay(): void`

Removes the overlay window from the screen.

```typescript
ExpoFloatingBubble.hideOverlay();
```

#### `updateOverlay(options?: ShowOverlayOptions): void`

Updates the content of an existing overlay window without recreating it.

```typescript
ExpoFloatingBubble.updateOverlay({
  componentConfig: {
    type: 'text',
    text: 'Call Connected',
    style: { color: '#4CAF50' }
  }
});
```

#### `canDrawOverlays(): Promise<boolean>`

Checks if the app has permission to draw overlays.

```typescript
const hasPermission = await ExpoFloatingBubble.canDrawOverlays();
```

#### `requestOverlayPermission(): void`

Opens the system settings to request overlay permission.

```typescript
ExpoFloatingBubble.requestOverlayPermission();
```

### Component System

The overlay system supports native Android components with a declarative configuration:

#### Container Component

```typescript
{
  type: 'container',
  style: {
    backgroundColor: '#80000000',  // Semi-transparent background
    padding: 16,                   // Padding in pixels
    borderRadius: 8                // Corner radius
  },
  children: [
    // Child components...
  ]
}
```

#### Text Component

```typescript
{
  type: 'text',
  text: 'Hello World!',
  style: {
    color: '#FFFFFF',              // Text color
    fontSize: 16,                  // Font size in SP
    textAlign: 'center',           // 'left', 'center', 'right'
    fontWeight: 'bold'             // 'normal', 'bold'
  }
}
```

#### Button Component

```typescript
{
  type: 'button',
  text: 'Click Me',
  style: {
    backgroundColor: '#4CAF50',    // Button background
    color: '#FFFFFF',              // Text color
    padding: 12,                   // Button padding
    borderRadius: 4                // Corner radius
  },
  onPress: 'customAction'          // Action ID or built-in action
}
```

#### Spacer Component

```typescript
{
  type: 'spacer',
  height: 20                       // Height in pixels
}
```

### Built-in Actions

- `'close'` - Closes the overlay
- `'endCall'` - Emits `onCallEnded` event and closes overlay

### Events

Listen to module events using Expo's event system:

```typescript
import { EventSubscription } from 'expo-floating-bubble';

// Bubble tapped event
const subscription = ExpoFloatingBubble.addListener('onBubbleTapped', (event) => {
  console.log('Bubble tapped at:', new Date(event.timestamp));
});

// Custom action event
ExpoFloatingBubble.addListener('onCustomAction', (event) => {
  const { actionId, params, timestamp } = event;
  console.log(`Action ${actionId} triggered:`, params);
});

// Call ended event
ExpoFloatingBubble.addListener('onCallEnded', (event) => {
  console.log('Call ended at:', new Date(event.timestamp));
});

// Permission events
ExpoFloatingBubble.addListener('onPermissionMissing', () => {
  console.log('Overlay permission is required');
});

// Don't forget to remove listeners
subscription.remove();
```

### Available Events

| Event | Description | Data |
|-------|-------------|------|
| `onBubbleTapped` | Bubble was tapped | `{ timestamp: number }` |
| `onBubbleShown` | Bubble displayed successfully | `{ success: boolean, reason?: string }` |
| `onBubbleHidden` | Bubble was hidden | `{ success: boolean, reason?: string }` |
| `onCallEnded` | End call button pressed | `{ timestamp: number }` |
| `onCustomAction` | Custom action triggered | `{ actionId: string, params: any, timestamp: number }` |
| `onPermissionMissing` | Overlay permission required | `{}` |
| `onRequestOverlayPermission` | Permission request initiated | `{ requested: boolean }` |
| `onOverlayError` | Overlay operation failed | `{ error: string }` |

## 🎨 Styling Guide

### Colors

Colors can be specified in multiple formats:

```typescript
// Hex strings
backgroundColor: '#FF4285F4'
backgroundColor: '#80000000'  // With alpha

// ARGB integers (recommended for better performance)
bubbleColor: 0xFF4285F4      // Opaque blue
bubbleColor: 0x80000000      // Semi-transparent black
```

### Dimensions

All dimensions are in pixels (Android DP):

```typescript
{
  padding: 16,        // 16dp padding
  fontSize: 18,       // 18sp font size
  borderRadius: 8,    // 8dp corner radius
  height: 200         // 200dp height
}
```

### Layout

Containers use vertical linear layout by default:

```typescript
{
  type: 'container',
  children: [
    { type: 'text', text: 'First item' },
    { type: 'spacer', height: 10 },
    { type: 'text', text: 'Second item' }
  ]
}
```

## 🔐 Permissions

### Overlay Permission (Required)

Android requires special permission to display content over other apps:

```typescript
// Check permission status
const hasPermission = await ExpoFloatingBubble.canDrawOverlays();

if (!hasPermission) {
  // Request permission (opens system settings)
  ExpoFloatingBubble.requestOverlayPermission();
  
  // Listen for permission events
  ExpoFloatingBubble.addListener('onPermissionMissing', () => {
    Alert.alert(
      'Permission Required',
      'Please enable "Display over other apps" permission to use floating bubbles.',
      [
        { text: 'Cancel', style: 'cancel' },
        { text: 'Open Settings', onPress: () => ExpoFloatingBubble.requestOverlayPermission() }
      ]
    );
  });
}
```

### Auto-handled Permissions

The expo config plugin automatically adds these permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

## 💡 Use Cases

### 📞 Call Interface

```typescript
// Incoming call overlay
ExpoFloatingBubble.showOverlay({
  initialData: { callerId: 'John Doe', number: '+1234567890' },
  componentConfig: {
    type: 'container',
    style: { backgroundColor: '#80000000', padding: 20, borderRadius: 16 },
    children: [
      { type: 'text', text: 'Incoming Call', style: { color: '#FFF', fontSize: 16 } },
      { type: 'text', text: 'John Doe', style: { color: '#FFF', fontSize: 20, fontWeight: 'bold' } },
      { type: 'spacer', height: 20 },
      { type: 'button', text: 'Answer', onPress: 'answer', style: { backgroundColor: '#4CAF50' } },
      { type: 'button', text: 'Decline', onPress: 'decline', style: { backgroundColor: '#F44336' } }
    ]
  }
});
```

### ⚡ Quick Actions

```typescript
// Floating action bubble
ExpoFloatingBubble.showBubble({
  iconUri: 'https://cdn-icons-png.flaticon.com/512/3524/3524636.png',
  bubbleColor: 0xFF2196F3,
  initialX: 50,
  initialY: 100
});

// Handle bubble tap for quick actions
ExpoFloatingBubble.addListener('onBubbleTapped', () => {
  ExpoFloatingBubble.showOverlay({
    componentConfig: {
      type: 'container',
      children: [
        { type: 'button', text: 'Take Screenshot', onPress: 'screenshot' },
        { type: 'button', text: 'Record Screen', onPress: 'record' },
        { type: 'button', text: 'Quick Note', onPress: 'note' }
      ]
    }
  });
});
```

## 🛠 Development

### Building from Source

```bash
git clone https://github.com/karam14/expo-floating-bubble.git
cd expo-floating-bubble
npm install
npm run build
```

### Testing

```bash
npm run lint
npm run test
```

### Example App

Check out the `/example` directory for a complete implementation example.

## 🐛 Troubleshooting

### Common Issues

**Bubble not appearing:**
- Check overlay permission: `await ExpoFloatingBubble.canDrawOverlays()`
- Ensure you're testing on a physical device (not emulator)
- Verify Android API level 23+

**TypeScript errors:**
- Make sure to import types: `import { ShowBubbleOptions } from 'expo-floating-bubble'`
- Update your TypeScript configuration if needed

**Build errors:**
- Clean and rebuild: `npx expo run:android --clear`
- Check Android SDK and build tools versions

### Performance Tips

- Use ARGB integers for colors instead of hex strings
- Minimize overlay updates - use `updateOverlay()` instead of hiding/showing
- Remove event listeners when not needed
- Use appropriate image sizes for bubble icons

## 📖 Examples

Find complete examples in our [GitHub repository](https://github.com/karam14/expo-floating-bubble/tree/main/example).

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](https://github.com/karam14/expo-floating-bubble/blob/main/CONTRIBUTING.md) for details.

## 📄 License

MIT License - see the [LICENSE](https://github.com/karam14/expo-floating-bubble/blob/main/LICENSE) file for details.

## 🙏 Acknowledgments

- Built with [Expo Modules API](https://docs.expo.dev/modules/)
- Inspired by Android's floating window capabilities
- Thanks to the React Native community

## 📞 Support

- 🐛 [Report Issues](https://github.com/karam14/expo-floating-bubble/issues)
- 💬 [Discussions](https://github.com/karam14/expo-floating-bubble/discussions)
- 📧 Email: krm-011@hotmail.com

---

Made with ❤️ by [karam14](https://github.com/karam14)
