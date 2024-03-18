package com.example.aexpress.adapters

import android.content.Context

class CategoryAdapter(context: Context?, categories: ArrayList<Category?>?) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder?>() {
    var context: Context?
    var categories: ArrayList<Category?>?

    init {
        this.context = context
        this.categories = categories
    }

    @NonNull
    @Override
    fun onCreateViewHolder(@NonNull parent: ViewGroup?, viewType: Int): CategoryViewHolder? {
        return CategoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_categories, parent, false)
        )
    }

    @Override
    fun onBindViewHolder(@NonNull holder: CategoryViewHolder?, position: Int) {
        val category: Category = categories.get(position)
        holder.binding.label.setText(Html.fromHtml(category.getName()))
        Glide.with(context)
            .load(category.getIcon())
            .into(holder.binding.image)
        holder.binding.image.setBackgroundColor(Color.parseColor(category.getColor()))
        holder.itemView.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(view: View?) {
                val intent = Intent(context, CategoryActivity::class.java)
                intent.putExtra("catId", category.getId())
                intent.putExtra("categoryName", category.getName())
                context.startActivity(intent)
            }
        })
    }

    @Override
    fun getItemCount(): Int {
        return categories.size()
    }

    inner class CategoryViewHolder(@NonNull itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemCategoriesBinding?

        init {
            binding = ItemCategoriesBinding.bind(itemView)
        }
    }
}
