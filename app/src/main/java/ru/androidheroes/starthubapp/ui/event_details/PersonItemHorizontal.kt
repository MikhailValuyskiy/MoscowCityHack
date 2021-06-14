package ru.androidheroes.starthubapp.ui.event_details

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.person_item_horizontal.*
import ru.androidheroes.starthubapp.R
import ru.androidheroes.starthubapp.ui.feed.Person

class PersonItemHorizontal(
    private val content: Person
) : Item() {

    override fun getLayout() = R.layout.person_item_horizontal

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.description.text = content.name.replace(" ", "\n")
        viewHolder.description2.text = "Компетенции:\n" + content.bio
        Picasso.get()
            .load(content.imageUrl)
            .into(viewHolder.person_image_preview)


    }
}

