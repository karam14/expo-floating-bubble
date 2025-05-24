package expo.modules.floatingbubble

import android.graphics.Color
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import android.content.Context
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebSettings
import androidx.core.content.ContextCompat
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ExpoFloatingBubbleModule : Module() {
  
  companion object {
    private var overlayView: android.view.View? = null
    private var moduleInstance: ExpoFloatingBubbleModule? = null
    private var customActionHandlers: Map<String, Map<String, Any?>>? = null
    
    fun emitBubbleTappedEvent(timestamp: Long) {
      moduleInstance?.let { module ->
        android.util.Log.d("FloatingBubble", "Emitting bubble tapped event with timestamp: $timestamp")
        module.sendEvent("onBubbleTapped", mapOf("timestamp" to timestamp.toDouble()))
      } ?: run {
        android.util.Log.w("FloatingBubble", "Module instance not available, cannot emit event")
      }
    }
    
    fun executeCustomAction(actionId: String, params: Map<String, Any?> = emptyMap()) {
      moduleInstance?.let { module ->
        android.util.Log.d("FloatingBubble", "Executing custom action: $actionId with params: $params")
        module.sendEvent("onCustomAction", mapOf(
          "actionId" to actionId,
          "params" to params,
          "timestamp" to System.currentTimeMillis().toDouble()
        ))
      } ?: run {
        android.util.Log.w("FloatingBubble", "Module instance not available, cannot execute custom action")
      }
    }
    
    fun showOverlay(context: Context, options: Map<String, Any?>) {
      android.util.Log.d("FloatingBubble", "showOverlay called with options: $options")
      
      // Guard overlay permission
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
          !Settings.canDrawOverlays(context)) {
        android.util.Log.w("FloatingBubble", "No overlay permission, returning")
        return
      }

      if (overlayView != null) {
        android.util.Log.w("FloatingBubble", "Overlay already exists, returning")
        return
      }

      // Store custom action handlers if provided
      customActionHandlers = options["customActions"] as? Map<String, Map<String, Any?>>
      android.util.Log.d("FloatingBubble", "Stored custom actions: $customActionHandlers")

      // Always use native overlay with custom React-defined components
      showNativeOverlay(context, options)
    }
    
    private fun showNativeOverlay(context: Context, options: Map<String, Any?>) {
      android.util.Log.d("FloatingBubble", "Creating native Android overlay with React-defined components...")
      
      // Get component configuration from options
      val componentConfig = options["componentConfig"] as? Map<String, Any?> ?: createDefaultConfig(options)
      
      val overlayLayout = createNativeViewFromConfig(context, componentConfig)

      overlayView = overlayLayout
      addOverlayToWindow(context, overlayLayout)
      
      android.util.Log.d("FloatingBubble", "Native overlay with React components created successfully")
    }
    
    private fun createDefaultConfig(options: Map<String, Any?>): Map<String, Any?> {
      val tappedAt = (options["initialData"] as? Map<*, *>)?.get("tappedAt")
      return mapOf(
        "type" to "container",
        "style" to mapOf(
          "backgroundColor" to "#80000000",
          "padding" to 40,
          "borderRadius" to 16
        ),
        "children" to listOf(
          mapOf(
            "type" to "text",
            "text" to "Active Call Controls",
            "style" to mapOf(
              "color" to "#FFFFFF",
              "fontSize" to 18,
              "textAlign" to "center",
              "fontWeight" to "bold"
            )
          ),
          mapOf("type" to "spacer", "height" to 20),
          mapOf(
            "type" to "button",
            "text" to "End Call",
            "style" to mapOf(
              "backgroundColor" to "#FF4444",
              "color" to "#FFFFFF",
              "padding" to 12,
              "borderRadius" to 8
            ),
            "onPress" to "endCall"
          ),
          if (tappedAt != null) mapOf(
            "type" to "text",
            "text" to "Tapped at: ${java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date(tappedAt.toString().toDouble().toLong()))}",
            "style" to mapOf(
              "color" to "#CCCCCC",
              "fontSize" to 14,
              "textAlign" to "center"
            )
          ) else null,
          mapOf(
            "type" to "button",
            "text" to "Close",
            "style" to mapOf(
              "backgroundColor" to "#666666",
              "color" to "#FFFFFF",
              "padding" to 8,
              "borderRadius" to 4
            ),
            "onPress" to "close"
          )
        ).filterNotNull()
      )
    }
    
    private fun createNativeViewFromConfig(context: Context, config: Map<String, Any?>): android.view.View {
      val type = config["type"] as? String ?: "container"
      val style = config["style"] as? Map<String, Any?> ?: emptyMap()
      
      return when (type) {
        "container" -> createContainer(context, config, style)
        "text" -> createText(context, config, style)
        "button" -> createButton(context, config, style)
        "spacer" -> createSpacer(context, config, style)
        else -> android.view.View(context)
      }
    }
    
    private fun createContainer(context: Context, config: Map<String, Any?>, style: Map<String, Any?>): android.widget.LinearLayout {
      return android.widget.LinearLayout(context).apply {
        orientation = android.widget.LinearLayout.VERTICAL
        
        // Apply styles
        style["backgroundColor"]?.let { color ->
          setBackgroundColor(android.graphics.Color.parseColor(color as String))
        }
        style["padding"]?.let { padding ->
          val p = (padding as Number).toInt()
          setPadding(p, p, p, p)
        }
        style["borderRadius"]?.let { radius ->
          // For rounded corners, we'd need to use a drawable background
          val drawable = android.graphics.drawable.GradientDrawable()
          drawable.cornerRadius = (radius as Number).toFloat()
          style["backgroundColor"]?.let { color ->
            drawable.setColor(android.graphics.Color.parseColor(color as String))
          }
          background = drawable
        }
        
        // Add children
        val children = config["children"] as? List<Map<String, Any?>> ?: emptyList()
        children.forEach { childConfig ->
          addView(createNativeViewFromConfig(context, childConfig))
        }
      }
    }
    
    private fun createText(context: Context, config: Map<String, Any?>, style: Map<String, Any?>): android.widget.TextView {
      return android.widget.TextView(context).apply {
        text = config["text"] as? String ?: ""
        
        // Apply styles
        style["color"]?.let { color ->
          setTextColor(android.graphics.Color.parseColor(color as String))
        }
        style["fontSize"]?.let { size ->
          textSize = (size as Number).toFloat()
        }
        style["textAlign"]?.let { align ->
          gravity = when (align as String) {
            "center" -> android.view.Gravity.CENTER
            "left" -> android.view.Gravity.START
            "right" -> android.view.Gravity.END
            else -> android.view.Gravity.START
          }
        }
        style["fontWeight"]?.let { weight ->
          if (weight == "bold") {
            setTypeface(typeface, android.graphics.Typeface.BOLD)
          }
        }
      }
    }
    
    private fun createButton(context: Context, config: Map<String, Any?>, style: Map<String, Any?>): android.widget.Button {
      return android.widget.Button(context).apply {
        text = config["text"] as? String ?: "Button"
        
        // Apply styles
        style["backgroundColor"]?.let { color ->
          setBackgroundColor(android.graphics.Color.parseColor(color as String))
        }
        style["color"]?.let { color ->
          setTextColor(android.graphics.Color.parseColor(color as String))
        }
        style["padding"]?.let { padding ->
          val p = (padding as Number).toInt()
          setPadding(p, p, p, p)
        }
        
        // Handle onPress
        setOnClickListener {
          val action = config["onPress"] as? String
          when (action) {
            "close" -> hideOverlay(context)
            "endCall" -> {
              android.util.Log.d("FloatingBubble", "End Call button pressed")
              // Emit event to React Native
              moduleInstance?.sendEvent("onCallEnded", mapOf("timestamp" to System.currentTimeMillis().toDouble()))
              hideOverlay(context)
            }
            else -> {
              // Handle custom actions
              if (action != null) {
                android.util.Log.d("FloatingBubble", "Custom button pressed: $action")
                executeCustomAction(action, mapOf("buttonText" to text.toString()))
              }
            }
          }
        }
      }
    }
    
    private fun createSpacer(context: Context, config: Map<String, Any?>, style: Map<String, Any?>): android.view.View {
      return android.view.View(context).apply {
        val height = config["height"] as? Number ?: style["height"] as? Number ?: 10
        layoutParams = android.widget.LinearLayout.LayoutParams(
          android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
          height.toInt()
        )
      }
    }
    
    private fun addOverlayToWindow(context: Context, view: android.view.View) {
      // WindowManager params
      val params = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
          WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
          WindowManager.LayoutParams.TYPE_PHONE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
          WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
        PixelFormat.TRANSLUCENT
      ).apply {
        gravity = Gravity.CENTER
        x = 0
        y = 0
      }

      // Add to screen
      try {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.addView(view, params)
        android.util.Log.d("FloatingBubble", "Overlay view successfully added to WindowManager")
      } catch (e: Exception) {
        android.util.Log.e("FloatingBubble", "Error adding overlay view to WindowManager", e)
        overlayView = null
      }
    }

    fun hideOverlay(context: Context) {
      overlayView?.let { view ->
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.removeView(view)
        overlayView = null
      }
    }
    
    fun updateOverlay(context: Context, options: Map<String, Any?>) {
      android.util.Log.d("FloatingBubble", "updateOverlay called with options: $options")
      
      // Guard overlay permission
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
          !Settings.canDrawOverlays(context)) {
        android.util.Log.w("FloatingBubble", "No overlay permission, returning")
        return
      }

      // Check if overlay exists
      if (overlayView == null) {
        android.util.Log.w("FloatingBubble", "No overlay to update, creating new one")
        showOverlay(context, options)
        return
      }

      // Update custom action handlers if provided
      customActionHandlers = options["customActions"] as? Map<String, Map<String, Any?>>
      android.util.Log.d("FloatingBubble", "Updated custom actions: $customActionHandlers")

      // Get component configuration from options
      val componentConfig = options["componentConfig"] as? Map<String, Any?> ?: createDefaultConfig(options)
      
      // Create new overlay view with updated config
      val newOverlayLayout = createNativeViewFromConfig(context, componentConfig)
      
      // Replace the existing overlay
      val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
      
      // Remove old overlay
      overlayView?.let { oldView ->
        windowManager.removeView(oldView)
      }
      
      // Add new overlay
      overlayView = newOverlayLayout
      addOverlayToWindow(context, newOverlayLayout)
      
      android.util.Log.d("FloatingBubble", "Overlay updated successfully")
    }
  }

  override fun definition() = ModuleDefinition {
    Name("ExpoFloatingBubble")

    OnCreate {
      moduleInstance = this@ExpoFloatingBubbleModule
      android.util.Log.d("FloatingBubble", "Module instance created and stored")
    }

    OnDestroy {
      moduleInstance = null
      android.util.Log.d("FloatingBubble", "Module instance cleared")
    }

    Events(
      "onBubbleShown",
      "onBubbleHidden",
      "onBubbleTapped",
      "onPermissionMissing",
      "onRequestOverlayPermission",
      "onOverlayError",
      "onCallEnded",
      "onCustomAction"
    )
    
    Function("showOverlay") { options: Map<String, Any?> ->
      ExpoFloatingBubbleModule.showOverlay(appContext.reactContext!!, options)
    }

    Function("hideOverlay") {
      ExpoFloatingBubbleModule.hideOverlay(appContext.reactContext!!)
    }

    Function("updateOverlay") { options: Map<String, Any?> ->
      ExpoFloatingBubbleModule.updateOverlay(appContext.reactContext!!, options)
    }

    Function("requestOverlayPermission") {
      val activity = appContext.currentActivity
      if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (!Settings.canDrawOverlays(activity)) {
          val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${activity.packageName}")
          )
          activity.startActivity(intent)
          sendEvent("onRequestOverlayPermission", mapOf("requested" to true))
        }
      }
      null
    }

    Function("canDrawOverlays") {
      val context = appContext.reactContext
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Settings.canDrawOverlays(context)
      } else {
        true
      }
    }

    Function("showBubble") { options: Map<String, Any?> ->
      val context = appContext.reactContext
        ?: run {
          sendEvent("onBubbleShown", mapOf("success" to false, "reason" to "noContext"))
          return@Function Unit
        }

      val iconUri = options["iconUri"] as? String
      val iconResId = (options["iconResId"] as? Double)?.toInt() ?: R.drawable.ic_bubble
      val bubbleColor = (options["bubbleColor"] as? Double)?.toInt() ?: Color.WHITE
      val initialX = (options["initialX"] as? Double)?.toInt() ?: 100
      val initialY = (options["initialY"] as? Double)?.toInt() ?: 300

      val intent = Intent(context, FloatingBubbleService::class.java).apply {
        putExtra("iconResId", iconResId)
        putExtra("iconUri", iconUri)
        putExtra("bubbleColor", bubbleColor)
        putExtra("initialX", initialX)
        putExtra("initialY", initialY)
      }

      ContextCompat.startForegroundService(context, intent)
      sendEvent("onBubbleShown", mapOf("success" to true))
      Unit
    }

    Function("hideBubble") {
      val context = appContext.reactContext
      if (context == null) {
        sendEvent("onBubbleHidden", mapOf("success" to false, "reason" to "noContext"))
        return@Function null
      }
      val intent = Intent(context, FloatingBubbleService::class.java)
      context.stopService(intent)
      sendEvent("onBubbleHidden", mapOf("success" to true))
      null
    }

    // Simple function to emit bubble tap event from Service
    Function("emitBubbleTappedFromService") { timestamp: Double ->
      android.util.Log.d("FloatingBubble", "emitBubbleTappedFromService called with timestamp: $timestamp")
      sendEvent("onBubbleTapped", mapOf("timestamp" to timestamp))
    }
  }
}
