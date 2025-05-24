/**
 * Configuration options for showing a floating bubble
 */
export interface ShowBubbleOptions {
  /**
   * URI of the icon to display in the bubble.
   * Can be a URL (http/https), file path, or asset path.
   * @example 'https://example.com/icon.png' or 'asset://icon.png'
   */
  iconUri?: string;
  
  /**
   * Resource ID of a drawable icon to use as fallback or primary icon.
   * This should be a drawable resource from your Android app.
   */
  iconResId?: number;
  
  /**
   * Background color of the bubble in ARGB format.
   * @example 0xFF0000FF for blue, 0xFFFF0000 for red
   */
  bubbleColor?: number;
  
  /**
   * Initial X position of the bubble on screen in pixels.
   * @default 100
   */
  initialX?: number;
  
  /**
   * Initial Y position of the bubble on screen in pixels.
   * @default 300
   */
  initialY?: number;
}

/**
 * Configuration options for showing an overlay window
 */
export interface ShowOverlayOptions {
  /**
   * Initial data to pass to the overlay components.
   * This data can be accessed within the component configuration.
   */
  initialData?: any;
  
  /**
   * Configuration for the overlay's UI components.
   * Defines the layout and styling of the overlay window.
   */
  componentConfig?: ComponentConfig;
  
  /**
   * Custom action handlers for button presses and other interactions.
   * Maps action IDs to their corresponding handlers and descriptions.
   */
  customActions?: { [actionId: string]: CustomAction };
}

/**
 * Defines a custom action that can be triggered from overlay components
 */
export interface CustomAction {
  /**
   * Function to execute when the action is triggered.
   * Receives parameters from the triggering component.
   */
  handler: (params: any) => void;
  
  /**
   * Optional description of what this action does.
   * Useful for debugging and documentation.
   */
  description?: string;
}

/**
 * Configuration for a UI component in the overlay
 */
export interface ComponentConfig {
  /**
   * Type of the component to render
   */
  type: 'container' | 'text' | 'button' | 'spacer';
  
  /**
   * Text content for text and button components
   */
  text?: string;
  
  /**
   * Child components for container components.
   * Only applies to 'container' type components.
   */
  children?: ComponentConfig[];
  
  /**
   * Styling options for the component
   */
  style?: {
    /**
     * Background color in hex format
     * @example '#FF0000' for red, '#80000000' for semi-transparent black
     */
    backgroundColor?: string;
    
    /**
     * Text color in hex format
     * @example '#FFFFFF' for white, '#000000' for black
     */
    color?: string;
    
    /**
     * Font size in pixels
     * @example 16, 18, 24
     */
    fontSize?: number;
    
    /**
     * Padding around the component in pixels
     * @example 10, 20, 30
     */
    padding?: number;
    
    /**
     * Border radius for rounded corners in pixels
     * @example 8, 16, 20
     */
    borderRadius?: number;
    
    /**
     * Text alignment within the component
     */
    textAlign?: 'left' | 'center' | 'right';
    
    /**
     * Font weight for text components
     */
    fontWeight?: 'normal' | 'bold';
    
    /**
     * Height of the component in pixels.
     * Primarily used for spacer components.
     */
    height?: number;
  };
  
  /**
   * Action to perform when the component is pressed.
   * Can be a predefined action ('close', 'endCall') or a custom action ID.
   */
  onPress?: 'close' | 'endCall' | string;
}

/**
 * Represents a subscription to an event that can be removed
 */
export interface EventSubscription {
  /**
   * Removes the event listener subscription
   */
  remove(): void;
}

/**
 * Event handlers for floating bubble module events
 */
export type ExpoFloatingBubbleModuleEvents = {
  /**
   * Fired when the floating bubble is tapped
   * @param data Contains the timestamp of when the bubble was tapped
   */
  onBubbleTapped?: (data: { timestamp: number }) => void;
  
  /**
   * Fired when the bubble is successfully shown or fails to show
   * @param data Contains success status and optional error reason
   */
  onBubbleShown?: (data: { success: boolean; reason?: string }) => void;
  
  /**
   * Fired when the bubble is hidden
   * @param data Contains success status and optional error reason
   */
  onBubbleHidden?: (data: { success: boolean; reason?: string }) => void;
  
  /**
   * Fired when a call is ended through the overlay
   * @param data Contains the timestamp of when the call was ended
   */
  onCallEnded?: (data: { timestamp: number }) => void;
  
  /**
   * Fired when a custom action is triggered from an overlay component
   * @param data Contains action ID, parameters, and timestamp
   */
  onCustomAction?: (data: { actionId: string; params: any; timestamp: number }) => void;
  
  /**
   * Fired when overlay permission is missing
   */
  onPermissionMissing?: () => void;
  
  /**
   * Fired when overlay permission is requested
   * @param data Contains whether the permission request was initiated
   */
  onRequestOverlayPermission?: (data: { requested: boolean }) => void;
  
  /**
   * Fired when an error occurs with the overlay
   * @param data Contains the error message
   */
  onOverlayError?: (data: { error: string }) => void;
};

/**
 * Props for the ExpoFloatingBubbleView component (if implemented)
 */
export type ExpoFloatingBubbleViewProps = {
  /**
   * Style object for the view component
   */
  style?: any;
};
