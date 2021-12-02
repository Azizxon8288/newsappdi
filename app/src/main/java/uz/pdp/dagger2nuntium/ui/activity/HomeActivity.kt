package uz.pdp.dagger2nuntium.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import uz.pdp.dagger2nuntium.App
import uz.pdp.dagger2nuntium.R
import uz.pdp.dagger2nuntium.databinding.ActivityHomeBinding
import uz.pdp.dagger2nuntium.utils.hide
import uz.pdp.dagger2nuntium.utils.show

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.my_nav_host_fragment)

        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.newsFragment -> {
                    hideBottom()
                }
                R.id.searchFragment ->{
                    hideBottom()
                }
                else -> showBottom()
            }
        }

//        setupSmoothBottomMenu()
    }

//    private fun setupSmoothBottomMenu() {
//        val popupMenu = PopupMenu(this, null)
//        popupMenu.inflate(R.menu.menu_bottom_bar)
//        val menu = popupMenu.menu
//        binding.bottomBar.setupWithNavController(menu, navController)
//    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun hideBottom() {
        binding.navView.animate().translationY(binding.navView.height.toFloat())
        binding.navView.hide()
    }

    private fun showBottom() {
        binding.navView.animate().translationY(0F)
        binding.navView.show()
    }
}