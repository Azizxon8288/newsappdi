package uz.pdp.dagger2nuntium.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.pdp.dagger2nuntium.databinding.ItemCategoryBinding
import uz.pdp.dagger2nuntium.models.Category

class CategoryAdapter(
    private val list: ArrayList<Category>,
    private val emojiList: List<String>,
    var onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<CategoryAdapter.Vh>() {

    inner class Vh(var itemCategoryBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemCategoryBinding.root) {

        fun onBind(category: Category, position: Int) {
            itemCategoryBinding.apply {
                emoji.isSingleLine = true
                emoji.isSelected = true
                text.isSingleLine = true
                text.isSelected = true

                emoji.text = emojiList[position]
                text.text = category.inRecycler
                if (!category.chosen) {
                    text.setTextColor(Color.parseColor("#666C8E"))
                    layout.setCardBackgroundColor(Color.parseColor("#F3F4F6"))
                } else {
                    text.setTextColor(Color.parseColor("#FFFFFF"))
                    layout.setCardBackgroundColor(Color.parseColor("#475AD7"))
                }

                layout.setOnClickListener {
                    onItemClickListener.onItemClick(category, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onItemClick(category: Category, position: Int)
    }
}