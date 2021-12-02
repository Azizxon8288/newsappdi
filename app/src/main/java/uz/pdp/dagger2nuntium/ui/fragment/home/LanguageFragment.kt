package uz.pdp.dagger2nuntium.ui.fragment.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.card.MaterialCardView
import com.yariksoffice.lingver.Lingver
import uz.pdp.dagger2nuntium.R
import uz.pdp.dagger2nuntium.databinding.FragmentLanguageBinding
import uz.pdp.dagger2nuntium.utils.MySharedPreference
import uz.pdp.dagger2nuntium.utils.hide
import uz.pdp.dagger2nuntium.utils.show


class LanguageFragment : Fragment(R.layout.fragment_language) {

    private val binding by viewBinding(FragmentLanguageBinding::bind)
    lateinit var mySharedPreference: MySharedPreference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mySharedPreference = MySharedPreference(requireContext())

        val language = mySharedPreference.getString("lang")


        Lingver.getInstance().setLocale(
            requireContext(),
            mySharedPreference.getString("lang")
        )
        binding.language.text = getString(R.string.language)

        binding.apply {
            fun select(card: MaterialCardView, text: TextView, image: ImageView) {
                card.setCardBackgroundColor(Color.parseColor("#FF475AD7"))
                text.setTextColor(Color.parseColor("#FFFFFFFF"))
                image.show()
            }

            when (language) {
                "ru" -> {
                    select(russian, text2, image2)
                }
                "de" -> {
                    select(german, text3, image3)
                }
                "es" -> {
                    select(spanish, text4, image4)
                }
                else -> {
                    select(english, text1, image1)
                }
            }

            fun clear() {
                english.setCardBackgroundColor(Color.parseColor("#FFF3F4F6"))
                russian.setCardBackgroundColor(Color.parseColor("#FFF3F4F6"))
                german.setCardBackgroundColor(Color.parseColor("#FFF3F4F6"))
                spanish.setCardBackgroundColor(Color.parseColor("#FFF3F4F6"))

                text1.setTextColor(Color.parseColor("#FF666C8E"))
                text2.setTextColor(Color.parseColor("#FF666C8E"))
                text3.setTextColor(Color.parseColor("#FF666C8E"))
                text4.setTextColor(Color.parseColor("#FF666C8E"))

                image1.hide()
                image2.hide()
                image3.hide()
                image4.hide()
            }


            english.setOnClickListener {
                clear()
                select(english, text1, image1)
                mySharedPreference.setString("lang", "en")
                Lingver.getInstance().setLocale(
                    requireContext(),
                    mySharedPreference.getString("lang")
                )
            }
            russian.setOnClickListener {
                clear()
                select(russian, text2, image2)
                mySharedPreference.setString("lang", "ru")
                Lingver.getInstance().setLocale(
                    requireContext(),
                    mySharedPreference.getString("lang")
                )
            }
            german.setOnClickListener {
                clear()
                select(german, text3, image3)
                mySharedPreference.setString("lang", "de")
                Lingver.getInstance().setLocale(
                    requireContext(),
                    mySharedPreference.getString("lang")
                )
            }
            spanish.setOnClickListener {
                clear()
                select(spanish, text4, image4)
                mySharedPreference.setString("lang", "es")
                Lingver.getInstance().setLocale(
                    requireContext(),
                    mySharedPreference.getString("lang")
                )
            }
            back.setOnClickListener {
                findNavController().popBackStack()
            }

        }

    }

}