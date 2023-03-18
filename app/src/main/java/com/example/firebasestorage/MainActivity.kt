package com.example.firebasestorage


import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {

     val storageRef = Firebase.storage.reference
     lateinit var uplaodBtn: Button
     lateinit var progresBar: ProgressBar
     lateinit var downloadBtn: Button
     lateinit var statusTet: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uplaodBtn = findViewById(R.id.upload_button)
        progresBar = findViewById(R.id.progresBar)
        statusTet = findViewById(R.id.status_text)
        downloadBtn = findViewById(R.id.download_bt)
        downloadBtn.setOnClickListener {
            val intent = Intent(this, MainAc2::class.java)
            startActivity(intent)
        }
        uplaodBtn.setOnClickListener {
            selectFile()
        }
    }
    private fun selectFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "pdf"
        startActivityForResult(intent, REQUEST_CODE)
    }
    private fun uploadFile(fileUri: Uri) {
        val fileName = fileUri.lastPathSegment!!
        val fileRef = storageRef.child(fileName)
        val uploadTask = fileRef.putFile(fileUri)
        uploadTask.addOnSuccessListener {
            progresBar.visibility = View.GONE
            statusTet.text = "Uplaod sucesful"
        }.addOnFailureListener {
            progresBar.visibility = View.GONE
            statusTet.text = "Uplaod failed"
        }.addOnProgressListener { taskSnapshot ->
            val progress = (80.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
            progresBar.progress = progress.toInt()
        }
    }
    companion object {
        const val REQUEST_CODE = 100
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { fileUri ->
                progresBar.visibility = View.VISIBLE
                statusTet.text = "Uploading......"
                uploadFile(fileUri)
            }
        }
    }
}

