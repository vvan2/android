package com.example.capston_camera

import android.R
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Bundle
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    // 카메라 미리보기를 위한 TextureView
    private var textureView: TextureView? = null

    // 카메라 디바이스를 참조하기 위한 객체
    private var cameraDevice: CameraDevice? = null

    // 캡처 요청을 만들기 위한 Builder
    private var captureRequestBuilder: CaptureRequest.Builder? = null

    // 카메라 캡처 세션
    private var cameraCaptureSession: CameraCaptureSession? = null

    // TextureView의 상태를 감지하는 리스너
    private val textureListener: SurfaceTextureListener = object : SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            // 텍스쳐뷰가 사용 가능할 때 카메라를 여는 함수 호출
            openCamera()
        }

        // 텍스처 크기가 변경될 때 호출
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        }

        // 텍스쳐가 파괴될 때 호출
        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return false
        }

        // 텍스쳐가 업데이트될 때 호출
        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        }
    }

    // 카메라 상태 콜백
    private val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            // 카메라가 성공적으로 열리면 CameraDevice 인스턴스를 할당하고 미리보기 시작
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            // 카메라 연결이 끊기면 닫음
            cameraDevice!!.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            // 에러 발생시 카메라 닫고 null 할당
            cameraDevice!!.close()
            cameraDevice = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textureView = findViewById<TextureView>(R.id.textureView)
        // TextureView에 리스너 설정
        textureView.setSurfaceTextureListener(textureListener)
        val takePictureButton = findViewById<Button>(R.id.btn_takepicture)
        takePictureButton.setOnClickListener {
            // 사진 촬영 버튼 클릭 이벤트
            // takePicture(); // 사진 촬영 메소드 호출
        }
    }

    // 카메라 미리보기를 생성하는 메소드
    private fun createCameraPreview() {
        try {
            val texture = checkNotNull(textureView!!.surfaceTexture)
            // 텍스처의 크기를 카메라 미리보기 크기로 설정
            texture.setDefaultBufferSize(1920, 1080)
            val surface: Surface = Surface(texture)
            // 미리보기를 위한 CaptureRequest.Builder 설정
            captureRequestBuilder =
                cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder!!.addTarget(surface)
            // 카메라 캡처 세션 생성
            cameraDevice!!.createCaptureSession(
                listOf(surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        // 카메라가 이미 닫혔다면 아무 작업도 수행하지 않음
                        if (cameraDevice == null) {
                            return
                        }
                        // 캡처 세션 시작
                        cameraCaptureSession = session
                        updatePreview()
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        // 구성 실패시 Toast 메시지 출력
                        Toast.makeText(
                            this@MainActivity,
                            "Configuration change",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    // 카메라를 여는 메소드
    private fun openCamera() {
        val manager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = manager.cameraIdList[0]
            // 카메라 권한 체크
            if (ActivityCompat.checkSelfPermission(
                    this,
                    "android.permission.CAMERA"
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf("android.permission.CAMERA"),
                    REQUEST_CAMERA_PERMISSION
                )
                return
            }
            // 카메라 오픈, 상태 콜백과 핸들러 설정
            manager.openCamera(cameraId, stateCallback, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    // 카메라 미리보기를 업데이트하는 메소드
    private fun updatePreview() {
        if (cameraDevice == null) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            return
        }
        // 자동 모드로 미리보기 설정
        captureRequestBuilder!!.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO)
        try {
            // 미리보기 요청을 반복하여 캡처 세션에 제출
            cameraCaptureSession!!.setRepeatingRequest(captureRequestBuilder!!.build(), null, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        // 액티비티가 일시정지되면 카메라를 닫음
        super.onPause()
        if (cameraDevice != null) {
            cameraDevice!!.close()
            cameraDevice = null
        }
    }

    companion object {
        // 카메라 사용 권한 요청 코드
        private const val REQUEST_CAMERA_PERMISSION = 200
    }
}
