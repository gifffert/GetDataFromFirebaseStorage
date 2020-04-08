package ru.embersoft.getdatafromfirebasestorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("wallpapers")
        val imageList: ArrayList<Item> = ArrayList()
        progressBar.visibility = View.VISIBLE

        val listAllTask: Task<ListResult> = storageRef.listAll()
        listAllTask.addOnCompleteListener { result ->
            val items: List<StorageReference> = result.result!!.items
            //add cycle for add image url to list
            items.forEachIndexed { index, item ->
                item.downloadUrl.addOnSuccessListener {
                    Log.d("item", "$it")
                    imageList.add(Item(it.toString()))
                }.addOnCompleteListener {
                    recyclerView.adapter = ImageAdapter(imageList, this)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}
