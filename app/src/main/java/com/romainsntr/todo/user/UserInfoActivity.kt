package com.romainsntr.todo.user

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.google.modernstorage.mediastore.FileType
import com.google.modernstorage.mediastore.MediaStoreRepository
import com.google.modernstorage.mediastore.SharedPrimary
import com.romainsntr.todo.R
import com.romainsntr.todo.network.Api
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

class UserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("UserInfoActivity","OK")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        val buttonCamera = findViewById<Button>(R.id.take_picture_button)
        buttonCamera.setOnClickListener {
            launchCameraWithPermission()
        }

        val buttonGallery = findViewById<Button>(R.id.upload_image_button)
        buttonGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
    }

    override fun onResume() {
        super.onResume()
        val imageView = findViewById<ImageView>(R.id.image_view)

        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            imageView.load(userInfo.avatar) {
                // affiche une image par défaut en cas d'erreur:
                error(R.drawable.ic_launcher_background)
            }
        }
    }

    private val permissionAndCameraLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
        // pour simplifier on ne fait rien ici, il faudra que le user re-clique sur le bouton
    }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { accepted ->
            if (accepted) launchCamera() // lancer l'action souhaitée
            else showExplanation() // afficher une explication
        }

    private fun launchCameraWithPermission() {
        val camPermission = Manifest.permission.CAMERA
        val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val permissionStatus = checkSelfPermission(camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        val isExplanationNeeded = shouldShowRequestPermissionRationale(camPermission)
        when {
            mediaStore.canWriteSharedEntries() && isAlreadyAccepted -> launchCamera() // lancer l'action souhaitée
            isExplanationNeeded -> showExplanation() // afficher une explication
            else -> permissionAndCameraLauncher.launch(arrayOf(camPermission, storagePermission)) // lancer la demande de permission
        }
    }

    private fun showExplanation() {
        // ici on construit une pop-up système (Dialog) pour expliquer la nécessité de la demande de permission
        AlertDialog.Builder(this)
            .setMessage("🥺 On a besoin de la caméra, vraiment! 👉👈")
            .setPositiveButton("Bon, ok") { _, _ -> launchAppSettings() /* ouvrir les paramètres de l'app */ }
            .setNegativeButton("Nope") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun launchAppSettings() {
        // Cet intent permet d'ouvrir les paramètres de l'app (pour modifier les permissions déjà refusées par ex)
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        // ici pas besoin de vérifier avant car on vise un écran système:
        startActivity(intent)
    }

    private fun handleImage(imageUri: Uri) {
        // afficher l'image dans l'ImageView
        lifecycleScope.launch {
            Api.userWebService.updateAvatar(convert(imageUri)).body()
        }
    }

    private fun convert(uri: Uri): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
        )
    }

    val mediaStore by lazy { MediaStoreRepository(this) }

    // créer un launcher pour la caméra
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { accepted ->
            val view = findViewById<Button>(R.id.upload_image_button)// n'importe quelle vue (ex: un bouton, binding.root, window.decorView, ...)
                if (accepted) handleImage(photoUri)
                else Snackbar.make(view, "Échec!", Snackbar.LENGTH_LONG).show()
        }

    // utiliser
    private lateinit var photoUri: Uri
    private fun launchCamera() {
        lifecycleScope.launch {
            photoUri = mediaStore.createMediaUri(
                filename = "picture-${UUID.randomUUID()}.jpg",
                type = FileType.IMAGE,
                location = SharedPrimary
            ).getOrThrow()
            cameraLauncher.launch(photoUri)
        }
    }

    // register
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if(it != null) {
            handleImage(it)
        }
    }

}