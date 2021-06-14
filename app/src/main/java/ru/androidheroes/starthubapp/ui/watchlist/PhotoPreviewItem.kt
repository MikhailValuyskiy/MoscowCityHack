package ru.androidheroes.starthubapp.ui.watchlist

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import androidx.core.content.ContextCompat
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import ru.androidheroes.starthubapp.R
import timber.log.Timber
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class PhotoPreviewItem(
    private val context: Context,
    private val showCameraScreen: () -> Unit
) : Item() {

    private val listener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) = Unit

        override fun onSurfaceTextureUpdated(p0: SurfaceTexture) = Unit

        override fun onSurfaceTextureDestroyed(p0: SurfaceTexture) = true

    }
    private val cameraStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraOpenCloseLock.release()
            cameraDevice = camera
            try {
                val texture = textureView.surfaceTexture

                // We configure the size of default buffer to be the size of camera preview we want.
                val size = context.resources.getDimensionPixelOffset(R.dimen.dimen_100dp)
                texture?.setDefaultBufferSize(size, size)

                // This is the output Surface we need to start preview.
                val surface = Surface(texture)

                // We set up a CaptureRequest.Builder with the output Surface.
                previewRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                previewRequestBuilder.addTarget(surface)

                // Here, we create a CameraCaptureSession for camera preview.
                cameraDevice?.createCaptureSession(listOf(surface),
                    object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            // The camera is already closed
                            if (cameraDevice == null) return

                            // When the session is ready, we start displaying the preview.
                            captureSession = cameraCaptureSession
                            try {
                                // Auto focus should be continuous for camera preview.
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                                // Finally, we start displaying the camera preview.
                                previewRequest = previewRequestBuilder.build()
                                captureSession?.setRepeatingRequest(previewRequest,
                                    null, backgroundHandler)
                            } catch (e: CameraAccessException) {
                                Timber.e(e.toString())
                            }
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {
                            Timber.e("Failed")
                        }
                    }, null)
            } catch (e: CameraAccessException) {
                Timber.e(e.toString())
            }
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraOpenCloseLock.release()
            cameraDevice?.close()
            cameraDevice = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Timber.e("StateCallback error: $error")
        }
    }

    private var cameraId: String? = null

    /**
     * An [AutoFitTextureView] for camera preview.
     */
    private lateinit var textureView: TextureView

    /**
     * A [CameraCaptureSession] for camera preview.
     */
    private var captureSession: CameraCaptureSession? = null

    /**
     * A reference to the opened [CameraDevice].
     */
    private var cameraDevice: CameraDevice? = null

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private var backgroundThread: HandlerThread = HandlerThread("CameraBackground").also { it.start() }

    /**
     * A [Handler] for running tasks in the background.
     */
    private var backgroundHandler: Handler = Handler(backgroundThread.looper)

    /**
     * [CaptureRequest.Builder] for the camera preview
     */
    private lateinit var previewRequestBuilder: CaptureRequest.Builder

    /**
     * [CaptureRequest] generated by [.previewRequestBuilder]
     */
    private lateinit var previewRequest: CaptureRequest

    /**
     * A [Semaphore] to prevent the app from exiting before closing the camera.
     */
    private val cameraOpenCloseLock = Semaphore(1)

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
//            textureView = viewHolder.preview_content
//            if (textureView.isAvailable) {
//                openCamera()
//            } else {
//                textureView.surfaceTextureListener = listener
//            }
//            viewHolder.setOnClickListener {
//                showCameraScreen.invoke()
//            }
    }



    override fun unbind(viewHolder: GroupieViewHolder) {
        super.unbind(viewHolder)
        closeCamera()
    }

    fun closeCamera() {
        try {
            cameraOpenCloseLock.acquire()
            captureSession?.close()
            captureSession = null
            cameraDevice?.close()
            cameraDevice = null
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }
    }

    override fun getLayout(): Int = R.layout.item_preview

    @SuppressLint("MissingPermission")
    private fun openCamera() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        setUpCameraOutputs(manager)
        try {
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }
            cameraId?.let {
                manager.openCamera(it, cameraStateCallback, backgroundHandler)
            }
        } catch (e: CameraAccessException) {
            Timber.e(e)
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera opening.", e)
        }
    }

    private fun setUpCameraOutputs(manager: CameraManager) {
        for (id in manager.cameraIdList) {
            val characteristic = manager.getCameraCharacteristics(id)
            if (characteristic.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                continue
            }
            cameraId = id
        }
    }
}
