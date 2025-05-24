package expo.modules.floatingbubble

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.app.NotificationCompat

import java.io.InputStream
import java.net.URL




class FloatingBubbleService : Service() {
  private var windowManager: WindowManager? = null
  private var bubbleView: View? = null
  private var overlayView: View? = null

  override fun onBind(intent: Intent?): IBinder? = null
  override fun onCreate() {
    super.onCreate()
  }  private fun sendBubbleTappedEvent() {
    try {
      android.util.Log.d("FloatingBubble", "sendBubbleTappedEvent() called")
      val timestamp = System.currentTimeMillis()
      android.util.Log.d("FloatingBubble", "Calling ExpoFloatingBubbleModule.emitBubbleTappedEvent with timestamp: $timestamp")
      ExpoFloatingBubbleModule.emitBubbleTappedEvent(timestamp)
    } catch (e: Exception) {
      android.util.Log.e("FloatingBubble", "Error in sendBubbleTappedEvent", e)
    }
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    if (bubbleView != null) return START_STICKY

    // Read configuration from the Intent
    val iconResId = intent?.getIntExtra("iconResId", android.R.drawable.ic_dialog_info)
      ?: android.R.drawable.ic_dialog_info
    val iconUri = intent?.getStringExtra("iconUri")
    val bubbleColor = intent?.getIntExtra("bubbleColor", Color.WHITE) ?: Color.WHITE
    val initialX = intent?.getIntExtra("initialX", 100) ?: 100
    val initialY = intent?.getIntExtra("initialY", 300) ?: 300

    // Start foreground notification if required
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channelId = "bubble_channel"
      val channelName = "Floating Bubble"
      val notificationManager = getSystemService(NotificationManager::class.java)
      val channel =
        NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
      notificationManager.createNotificationChannel(channel)

      val notification: Notification =
        NotificationCompat.Builder(this, channelId)
          .setContentTitle("Floating Bubble Active")
          .setContentText("The floating bubble is running.")
          .setSmallIcon(iconResId)
          .build()

      startForeground(1, notification)
    }

    // Inflate the bubble view
    windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    bubbleView = LayoutInflater.from(this).inflate(R.layout.bubble_layout, null)
    
    // Apply icon and background color
    bubbleView!!
      .findViewById<ImageView>(R.id.bubble_icon)
      .apply {        // Try to load from URI first, fallback to resource ID
        android.util.Log.d("FloatingBubble", "iconUri: $iconUri")
        if (!iconUri.isNullOrEmpty()) {
          try {
            android.util.Log.d("FloatingBubble", "Attempting to load from URI: $iconUri")
            
            if (iconUri.startsWith("http://") || iconUri.startsWith("https://")) {
              android.util.Log.d("FloatingBubble", "Loading HTTP URI")
              // Handle HTTP URLs by downloading asynchronously
              Thread {
                try {
                  val bitmap = BitmapFactory.decodeStream(URL(iconUri).openStream())
                  if (bitmap != null) {
                    post {
                      android.util.Log.d("FloatingBubble", "Successfully loaded bitmap from HTTP URI")
                      setImageBitmap(bitmap)
                    }
                  } else {
                    post {
                      android.util.Log.w("FloatingBubble", "Failed to decode bitmap from HTTP stream")
                      setImageResource(iconResId)
                    }
                  }
                } catch (e: Exception) {
                  post {
                    android.util.Log.e("FloatingBubble", "Failed to load from HTTP URI: $iconUri", e)
                    setImageResource(iconResId)
                  }
                }
              }.start()
            } else {
              val inputStream: InputStream? = when {
                iconUri.startsWith("android.resource://") -> {
                  android.util.Log.d("FloatingBubble", "Loading android.resource URI")
                  contentResolver.openInputStream(Uri.parse(iconUri))
                }
                iconUri.startsWith("file://") -> {
                  android.util.Log.d("FloatingBubble", "Loading file URI")
                  contentResolver.openInputStream(Uri.parse(iconUri))
                }
                else -> {
                  android.util.Log.d("FloatingBubble", "Loading asset path")
                  assets.open(iconUri.removePrefix("asset://"))
                }
              }
              
              inputStream?.use { stream ->
                val bitmap = BitmapFactory.decodeStream(stream)
                if (bitmap != null) {
                  android.util.Log.d("FloatingBubble", "Successfully loaded bitmap from URI")
                  setImageBitmap(bitmap)
                } else {
                  android.util.Log.w("FloatingBubble", "Failed to decode bitmap from stream")
                  setImageResource(iconResId)
                }
              } ?: run {
                android.util.Log.w("FloatingBubble", "Failed to open input stream")
                setImageResource(iconResId)
              }
            }
          } catch (e: Exception) {
            android.util.Log.e("FloatingBubble", "Failed to load from URI: $iconUri", e)
            // Fallback to resource ID if URI loading fails
            setImageResource(iconResId)
          }
        } else {
          android.util.Log.d("FloatingBubble", "No iconUri provided, using resource ID: $iconResId")
          setImageResource(iconResId)
        }
        setBackgroundColor(bubbleColor)
      }

    // Measure the view to get its true width/height before layout
    bubbleView!!.measure(
      MeasureSpec.UNSPECIFIED,
      MeasureSpec.UNSPECIFIED
    )
    val viewW = bubbleView!!.measuredWidth
    val viewH = bubbleView!!.measuredHeight

    // Build WindowManager.LayoutParams with drag-friendly flags
    val params = WindowManager.LayoutParams(
  WindowManager.LayoutParams.WRAP_CONTENT,
  WindowManager.LayoutParams.WRAP_CONTENT,
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
  else
    WindowManager.LayoutParams.TYPE_PHONE,
  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
    or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
    or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
  PixelFormat.TRANSLUCENT
).apply {
  gravity = Gravity.TOP or Gravity.START
  x = initialX
  y = initialY
}

    // Determine real screen size (including nav-bars)
    val (screenW, screenH) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      val bounds = windowManager!!.currentWindowMetrics.bounds
      bounds.width() to bounds.height()
    } else {
      val dm = DisplayMetrics()
      @Suppress("DEPRECATION")
      windowManager!!.defaultDisplay.getRealMetrics(dm)
      dm.widthPixels to dm.heightPixels
    }
    val maxX = screenW - viewW
    val maxY = screenH - viewH    // Track touch coordinates and move bubble
    var startX = 0
    var startY = 0
    var touchX = 0f
    var touchY = 0f

    bubbleView!!.setOnTouchListener { v, ev ->
      when (ev.action) {
        MotionEvent.ACTION_DOWN -> {
          startX = params.x
          startY = params.y
          touchX = ev.rawX
          touchY = ev.rawY
          true
        }
        MotionEvent.ACTION_MOVE -> {
          val newX = (startX + (ev.rawX - touchX)).toInt()
          val newY = (startY + (ev.rawY - touchY)).toInt()
          
          params.x = newX.coerceIn(0, maxX)
          params.y = newY.coerceIn(0, maxY)
          
          try {
            windowManager?.updateViewLayout(bubbleView, params)
          } catch (e: Exception) {
            e.printStackTrace()
          }
          true
        }        MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
          val deltaX = kotlin.math.abs(ev.rawX - touchX)
          val deltaY = kotlin.math.abs(ev.rawY - touchY)
          android.util.Log.d("FloatingBubble", "ACTION_UP: deltaX=$deltaX, deltaY=$deltaY")
          if (deltaX < 10 && deltaY < 10) {
            android.util.Log.d("FloatingBubble", "Bubble tapped! Sending event...")
            // Send event to React Native instead of bringing app to front
            sendBubbleTappedEvent()
          }
          true
        }
        else -> false
      }
    }

    // Finally, add the bubble to the window
    windowManager?.addView(bubbleView, params)
    return START_STICKY
  }  override fun onDestroy() {
  super.onDestroy()
  // Remove bubble
  bubbleView?.let { windowManager?.removeView(it) }
  // Remove overlay
  overlayView?.let { windowManager?.removeView(it) }
  overlayView = null
  bubbleView = null
}
}
