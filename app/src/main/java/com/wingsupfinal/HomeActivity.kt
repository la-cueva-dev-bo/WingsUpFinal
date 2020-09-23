package com.wingsupfinal

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.TextureView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private var user: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        user = bundle?.getString("user")
        val foto = bundle?.getString("foto")
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val hview = navView.getHeaderView(0)
        navView.itemIconTintList = null

        val textName = hview.findViewById(R.id.userNameTextView) as TextView
        val textEmail = hview.findViewById(R.id.emailTextView) as TextView
        val profilePick = hview.findViewById(R.id.profilePickImageView) as ImageView

        textName.text = user
        textEmail.text = email
        profilePick.let {
            Glide.with(this).load(foto).into(profilePick)
        }

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if(id==R.id.action_wpp){
            val installed: Boolean = appInstalledOrNot("com.whatsapp")
            val installed2: Boolean = appInstalledOrNot("com.whatsapp.w4b")

            if(installed){

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("http://api.whatsapp.com/send?phone=+59176727199&text=Hola Wings Up! Mi nombre es $user y necesito ayuda en:")
                startActivity(intent)
            }else{
                if(installed2){
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://api.whatsapp.com/send?phone=+591767271990&text=Hola Wings Up! Mi nombre es $user y necesito ayuda en:")
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "No tiene instalado la aplicación de WhatsApp", Toast.LENGTH_SHORT).show()
                }

            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun appInstalledOrNot(url: String): Boolean {
        val packageManager: PackageManager = packageManager
        val appInstalled: Boolean?
        appInstalled = try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES)
            true
        }catch (e: PackageManager.NameNotFoundException){
            false
        }
        return appInstalled!!
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}