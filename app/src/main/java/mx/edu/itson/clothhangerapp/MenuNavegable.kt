package mx.edu.itson.clothhangerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class MenuNavegable : AppCompatActivity() {

    protected fun setupBottomNavigation() {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    if(this !is PrincipalActivity) {
                        startActivity(Intent(this, PrincipalActivity::class.java))
                    }
                    true
                }

                R.id.nav_hoy -> {
                    if(this !is RegistroDiarioActivity) {
                        startActivity(Intent(this, RegistroDiarioActivity::class.java))
                    }
                    true
                }

                R.id.nav_outfits -> {
                    if(this !is OutfitsActivity) {
                        startActivity(Intent(this, OutfitsActivity::class.java))
                    }
                    true
                }

                R.id.nav_perfil -> {
                    if(this !is ConfiguracionActivity) {
                        startActivity(Intent(this, ConfiguracionActivity::class.java))
                    }
                    true
                }
                else -> false
            }
        }
    }

    // Resalta el ítem correspondiente a la actividad actual
    protected fun setSelectedItem(itemId: Int) {
        findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = itemId;
    }

}