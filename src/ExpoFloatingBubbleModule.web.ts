import { registerWebModule, NativeModule } from 'expo';

class ExpoFloatingBubbleModule extends NativeModule {
  async requestOverlayPermission() {
    console.warn('Floating bubble is not supported on web.');
  }
  async canDrawOverlays(): Promise<boolean> {
    return false;
  }
  async showBubble() {
    console.warn('Floating bubble is not supported on web.');
  }
  async hideBubble() {
    console.warn('Floating bubble is not supported on web.');
  }
}

export default registerWebModule(ExpoFloatingBubbleModule, 'ExpoFloatingBubbleModule');
