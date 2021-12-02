package uz.pdp.dagger2nuntium.ui.fragment.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.mig35.carousellayoutmanager.CarouselLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.pdp.dagger2nuntium.App
import uz.pdp.dagger2nuntium.R
import uz.pdp.dagger2nuntium.adapters.HomeRvAdapter
import uz.pdp.dagger2nuntium.adapters.TabRvAdapter
import uz.pdp.dagger2nuntium.adapters.TabRvAdapter.*
import uz.pdp.dagger2nuntium.database.entity.Article
import uz.pdp.dagger2nuntium.databinding.FragmentHomeBinding
import uz.pdp.dagger2nuntium.databinding.ItemTabBinding
import uz.pdp.dagger2nuntium.resource.NewsResource
import uz.pdp.dagger2nuntium.utils.MySharedPreference
import uz.pdp.dagger2nuntium.utils.hide
import uz.pdp.dagger2nuntium.utils.navOptions
import uz.pdp.dagger2nuntium.utils.show
import uz.pdp.dagger2nuntium.viewmodels.NewsViewModel
import uz.pdp.dagger2nuntium.viewmodels.SaveViewModel
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val TAG = "HomeFragment"
    private lateinit var homeRvAdapter: HomeRvAdapter
    private lateinit var tabRvAdapter: TabRvAdapter
    private var categoryList =
        listOf("Random", "Health", "Business", "Entertainment", "Science", "Sports", "Technology")
    lateinit var mySharedPreference: MySharedPreference
    private var rec = ""

    @Inject
    lateinit var newsViewModel: NewsViewModel

    @Inject
    lateinit var saveViewModel: SaveViewModel

    private var isCreate = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.injectFragment(this)
        mySharedPreference = MySharedPreference(requireContext())




        mySharedPreference.getList().forEach {
            if (it.chosen) {
                rec = it.inRecycler
                return@forEach
            }
        }

        homeRvAdapter = HomeRvAdapter(object : HomeRvAdapter.OnNewsItemClickListener {
            override fun onItemClick(article: Article) {
                findNavController().navigate(
                    R.id.action_navigation_home_to_newsFragment,
                    Bundle().apply {
                        putSerializable("art", article)
                    }, navOptions()
                )
            }
        })



        tabRvAdapter =
            TabRvAdapter(saveViewModel, requireContext(), object : OnNewsItemClickListener {
                override fun onItemClick(article: Article) {
                    findNavController().navigate(
                        R.id.action_navigation_home_to_newsFragment,
                        Bundle().apply {
                            putSerializable("art", article)
                        }, navOptions()
                    )
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isCreate) {
            if (mySharedPreference.getString("lang") == "en") {
                loadTabData("General", "us")
                loadRec(rec, "us")
            } else if (mySharedPreference.getString("lang") == "es") {
                loadTabData("General", "us")
                loadRec(rec, "us")
            } else {
                loadTabData("General", mySharedPreference.getString("lang"))
                loadRec(rec, mySharedPreference.getString("lang"))
                isCreate = false
            }
        }

        binding.apply {

            voiceBtn.setOnClickListener {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                startActivityForResult(intent, 200)
            }

            recRv.adapter = homeRvAdapter

            tabRv.adapter = tabRvAdapter

            for (category in categoryList) {
                tabLayout.addTab(tabLayout.newTab().setText(category))
            }

            categoryList.forEachIndexed { index, s ->
                val tabBinding = ItemTabBinding.inflate(layoutInflater)
                tabBinding.text.text = s
                if (index == 0) {
                    tabBinding.card.setCardBackgroundColor(Color.parseColor("#FF475AD7"))
                    tabBinding.text.setTextColor(Color.parseColor("#FFFFFFFF"))
                } else {
                    tabBinding.card.setCardBackgroundColor(Color.parseColor("#FFF3F4F6"))
                    tabBinding.text.setTextColor(Color.parseColor("#FF666C8E"))
                }
                tabLayout.getTabAt(index)?.customView = tabBinding.root
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val itemTabBinding = ItemTabBinding.bind(tab!!.customView!!)
                    itemTabBinding.card.setCardBackgroundColor(Color.parseColor("#FF475AD7"))
                    itemTabBinding.text.setTextColor(Color.parseColor("#FFFFFFFF"))
                    if (tab.position == 0) {
                        if (mySharedPreference.getString("lang") == "en") {
                            loadTabData("General", "us")
                        } else if (mySharedPreference.getString("lang") == "es") {
                            loadTabData("General", "us")
                            loadRec(rec, "us")
                        } else {
                            loadTabData("General", mySharedPreference.getString("lang"))
                        }
                    } else {
                        if (mySharedPreference.getString("lang") == "en") {
                            loadTabData(categoryList[tab.position], "us")
                        } else if (mySharedPreference.getString("lang") == "es") {
                            loadTabData("General", "us")
                            loadRec(rec, "us")
                        } else {
                            loadTabData(
                                categoryList[tab.position],
                                mySharedPreference.getString("lang")
                            )
                        }
                    }

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    val itemTabBinding = ItemTabBinding.bind(tab!!.customView!!)
                    itemTabBinding.card.setCardBackgroundColor(Color.parseColor("#FFF3F4F6"))
                    itemTabBinding.text.setTextColor(Color.parseColor("#FF666C8E"))
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

            searchEditText.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    findNavController().navigate(
                        R.id.action_navigation_home_to_searchFragment,
                        Bundle().apply {
                            putString("str", textView.text.toString())
                        },
                        navOptions()
                    )
                    return@setOnEditorActionListener true
                } else return@setOnEditorActionListener false
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == AppCompatActivity.RESULT_OK) {
            val list: ArrayList<String>? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val voice = list?.get(0)
            if (voice?.contains(" ") == true) {
                Toast.makeText(requireContext(), "Only word is required!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                binding.searchEditText.setText(voice.toString())
                binding.searchEditText.setOnEditorActionListener { textView, i, keyEvent ->
                    if (i == EditorInfo.IME_ACTION_SEARCH) {
                        textView.text = voice
                        findNavController().navigate(
                            R.id.action_navigation_home_to_searchFragment,
                            Bundle().apply {
                                putString("str", textView.text.toString())
                            },
                            navOptions()
                        )
                        return@setOnEditorActionListener true
                    } else return@setOnEditorActionListener false
                }
            }
        }
    }

    private fun loadRec(category: String, country: String) {
        binding.apply {
            lifecycleScope.launch {
                newsViewModel.getByCategory(category, country).collect {
                    when (it) {
                        is NewsResource.Loading -> {
                            animationView2.show()
                        }
                        is NewsResource.Error -> {
                            animationView2.hide()
                            errorText2.text = it.message
                            errorText2.show()
                        }
                        is NewsResource.Success -> {
                            animationView2.hide()
                            errorText2.hide()
                            homeRvAdapter.submitList(it.list)
                        }
                    }
                }
            }
        }
    }

    private fun loadTabData(category: String, country: String) {
        binding.apply {
            lifecycleScope.launch {
                newsViewModel.getByCategory(category, country).collect {
                    when (it) {
                        is NewsResource.Loading -> {
                            animationView1.show()
                            tabRv.hide()
                        }
                        is NewsResource.Error -> {
                            animationView1.hide()
                            errorText1.text = it.message
                            errorText1.show()
                        }
                        is NewsResource.Success -> {
                            animationView1.hide()
                            errorText1.hide()
                            tabRv.show()
                            tabRvAdapter.submitList(it.list)
                            tabRv.smoothScrollToPosition(0)
                        }
                    }
                }
            }
        }
    }
}