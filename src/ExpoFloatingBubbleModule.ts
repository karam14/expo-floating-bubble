import { NativeModule, requireNativeModule } from 'expo';
import { ShowBubbleOptions, ShowOverlayOptions } from './ExpoFloatingBubble.types';

declare class ExpoFloatingBubbleModule extends NativeModule {
  /**
   * Requests permission to draw overlays on top of other apps.
   * This is required on Android API 23+ before showing floating bubbles or overlays.
   * Opens the system settings where the user can grant the permission.
   * 
   * @example
   * ```typescript
   * ExpoFloatingBubble.requestOverlayPermission();
   * ```
   */
  requestOverlayPermission(): void;

  /**
   * Checks if the app has permission to draw overlays on top of other apps.
   * 
   * @returns Promise that resolves to true if permission is granted, false otherwise
   * 
   * @example
   * ```typescript
   * const hasPermission = await ExpoFloatingBubble.canDrawOverlays();
   * if (!hasPermission) {
   *   ExpoFloatingBubble.requestOverlayPermission();
   * }
   * ```
   */
  canDrawOverlays(): Promise<boolean>;

  /**
   * Shows a draggable floating bubble on the screen.
   * The bubble can be moved around and tapped to trigger events.
   * 
   * @param options Configuration options for the bubble appearance and position
   * 
   * @example
   * ```typescript
   * ExpoFloatingBubble.showBubble({
   *   iconUri: 'https://example.com/icon.png',
   *   bubbleColor: 0xFF0000FF, // Blue color
   *   initialX: 100,
   *   initialY: 200
   * });
   * ```
   */
  showBubble(options?: ShowBubbleOptions): void;

  /**
   * Hides the currently displayed floating bubble.
   * This will remove the bubble from the screen and stop the foreground service.
   * 
   * @example
   * ```typescript
   * ExpoFloatingBubble.hideBubble();
   * ```
   */
  hideBubble(): void;

  /**
   * Shows a customizable overlay window on top of other apps.
   * The overlay can contain native UI components like buttons, text, and containers.
   * 
   * @param options Configuration options for the overlay content and behavior
   * 
   * @example
   * ```typescript
   * ExpoFloatingBubble.showOverlay({
   *   initialData: { callId: '123' },
   *   componentConfig: {
   *     type: 'container',
   *     children: [
   *       { type: 'text', text: 'Call Controls' },
   *       { type: 'button', text: 'End Call', onPress: 'endCall' }
   *     ]
   *   }
   * });
   * ```
   */
  showOverlay(options?: ShowOverlayOptions): void;

  /**
   * Hides the currently displayed overlay window.
   * This will remove the overlay from the screen.
   * 
   * @example
   * ```typescript
   * ExpoFloatingBubble.hideOverlay();
   * ```
   */
  hideOverlay(): void;

  /**
   * Updates the content and configuration of an existing overlay.
   * If no overlay is currently shown, this will create a new one.
   * 
   * @param options New configuration options for the overlay
   * 
   * @example
   * ```typescript
   * ExpoFloatingBubble.updateOverlay({
   *   componentConfig: {
   *     type: 'container',
   *     children: [
   *       { type: 'text', text: 'Call Ended' },
   *       { type: 'button', text: 'Close', onPress: 'close' }
   *     ]
   *   }
   * });
   * ```
   */
  updateOverlay(options?: ShowOverlayOptions): void;
}

// This loads the native module via JSI on native, or the shim on web.
export default requireNativeModule<ExpoFloatingBubbleModule>('ExpoFloatingBubble');
