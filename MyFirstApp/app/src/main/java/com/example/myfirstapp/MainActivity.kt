package com.example.myfirstapp

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.item -> {
                val editText = findViewById<EditText>(R.id.editTextTextPersonName)
                val message = editText.text.toString()
                intent = Intent(this, DisplayMessageActivity::class.java).apply {
                    putExtra(EXTRA_MESSAGE, message)
                }
                startActivity(intent)
            }
            R.id.item_2-> {
                    Toast.makeText(this, item.title, Toast.LENGTH_LONG).show()
                    return true
                }
        }
        return true
    }


    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editTextTextPersonName)
        val message = editText.text.toString()
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    fun anim(v : View){
        val set: Animator? = AnimatorInflater.loadAnimator(this, R.animator.anima)
            .apply {
                setTarget(v)
                start()
            }

    }

    fun frag(v : View){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = BlankFragment()
        fragmentTransaction.add(R.id.fragment, fragment)
        fragmentTransaction.commit()


    }


   fun anima (view: View) {
       val image: ImageView = findViewById(R.id.imageView)
       val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this, R.anim.teste)
       image.startAnimation(hyperspaceJump)
   }
}

