package ru.androidheroes.starthubapp.ui.feed

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.androidheroes.starthubapp.R

class ProjectItem(
    private val content: EventDetails,
    private val onClick: (goods: EventDetails) -> Unit
) : Item() {

    override fun getLayout() = R.layout.project_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.description.text = content.title
        viewHolder.like.isChecked = content.isInFavorite
        viewHolder.content.setOnClickListener {
            onClick.invoke(content)
        }

        Picasso.get()
            .load(content.imageUrl)
            .into(viewHolder.image_preview)
    }
}