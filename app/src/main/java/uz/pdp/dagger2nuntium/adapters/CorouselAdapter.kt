package uz.pdp.dagger2nuntium.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.pdp.dagger2nuntium.R
import uz.pdp.dagger2nuntium.databinding.ItemCorouselBinding

class CorouselAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<CorouselAdapter.Vh>() {

    inner class Vh(private val itemCorousel: ItemCorouselBinding) :
        RecyclerView.ViewHolder(itemCorousel.root) {

        fun onBind(string: String) {
            itemCorousel.apply {
                Picasso.get().load(string).into(img)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCorouselBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
}