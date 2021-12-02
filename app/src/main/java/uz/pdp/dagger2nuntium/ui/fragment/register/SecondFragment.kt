package uz.pdp.dagger2nuntium.ui.fragment.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.pdp.dagger2nuntium.R
import uz.pdp.dagger2nuntium.databinding.FragmentSecondBinding
import uz.pdp.dagger2nuntium.models.Category
import uz.pdp.dagger2nuntium.utils.MySharedPreference

class SecondFragment : Fragment(R.layout.fragment_second) {

    private val binding by viewBinding(FragmentSecondBinding::bind)
    lateinit var mySharedPreference: MySharedPreference
    private var categoryList = arrayListOf(
        Category("Health", false),
        Category("Business", false),
        Category("Entertainment", false),
        Category("Science", false),
        Category("Sports", false),
        Category("Technology", false)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mySharedPreference = MySharedPreference(requireContext())

        binding.getBtn.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragment_to_thirdFragment)
            mySharedPreference.setList(categoryList)
        }

    }
}