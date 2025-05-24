/**
 * Expo Floating Bubble Module
 * 
 * A React Native module for creating floating bubbles and overlay windows on Android.
 * Provides functionality to show draggable floating bubbles and customizable overlay interfaces
 * that can appear on top of other applications.
 * 
 * @example
 * ```typescript
 * import ExpoFloatingBubble from 'expo-floating-bubble';
 * 
 * // Show a floating bubble
 * ExpoFloatingBubble.showBubble({
 *   iconUri: 'https://example.com/icon.png',
 *   initialX: 100,
 *   initialY: 200
 * });
 * 
 * // Show an overlay
 * ExpoFloatingBubble.showOverlay({
 *   componentConfig: {
 *     type: 'container',
 *     children: [
 *       { type: 'text', text: 'Hello World!' },
 *       { type: 'button', text: 'Close', onPress: 'close' }
 *     ]
 *   }
 * });
 * ```
 */

// Reexport the native module. On web, it will be resolved to ExpoFloatingBubbleModule.web.ts
// and on native platforms to ExpoFloatingBubbleModule.ts
export { default } from './ExpoFloatingBubbleModule';

/**
 * React component for floating bubble functionality (if implemented)
 */
export { default as ExpoFloatingBubbleView } from './ExpoFloatingBubbleView';

/**
 * Type definitions for the floating bubble module
 * Includes interfaces for configuration options, events, and component props
 */
export * from  './ExpoFloatingBubble.types';
