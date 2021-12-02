package uz.pdp.dagger2nuntium.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.pdp.dagger2nuntium.R
import uz.pdp.dagger2nuntium.adapters.CategoryAdapter
import uz.pdp.dagger2nuntium.databinding.FragmentMenuBinding
import uz.pdp.dagger2nuntium.models.Category
import uz.pdp.dagger2nuntium.ui.activity.HomeActivity
import uz.pdp.dagger2nuntium.utils.MySharedPreference


class MenuFragment : Fragment(R.layout.fragment_menu) {

    private val binding by viewBinding(FragmentMenuBinding::bind)
    lateinit var categoryAdapter: CategoryAdapter
    private lateinit var mySharedPreference: MySharedPreference
    private var categoryList = ArrayList<Category>()
    private var count = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mySharedPreference = MySharedPreference(requireContext())
        categoryList = ArrayList(mySharedPreference.getList())
        val list = listOf(
            "❤",
            "\uD83D\uDCB5",
            "\uD83D\uDE03",
            "\uD83D\uDD2C",
            "\uD83C\uDFC8",
            "\uD83D\uDDA5"
        )
        categoryAdapter = CategoryAdapter(categoryList,list, object
            : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(category: Category, position: Int) {
                if (category.chosen) {
                    categoryList[position].chosen = false
                    category.chosen = false
                } else {
                    categoryList[position].chosen = true
                    category.chosen = true
                }
                categoryAdapter.notifyItemChanged(position)
            }
        })

        binding.apply {
            rv.adapter = categoryAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        for (category in categoryList) {
            if (category.chosen)
                count++
        }

        if (count != 0) {
            mySharedPreference.setList(categoryList)
            mySharedPreference.setRegister()
        } else {
            Toast.makeText(
                requireContext(),
                "Select at least 1 category!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}