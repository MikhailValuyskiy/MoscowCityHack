package ru.androidheroes.starthubapp.ui.event_details

import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_with_text.*
import kotlinx.android.synthetic.main.item_with_text_circle.*
import kotlinx.android.synthetic.main.item_with_text_circle.content
import ru.androidheroes.starthubapp.R
import ru.androidheroes.starthubapp.ui.feed.EventDetails
import ru.androidheroes.starthubapp.ui.feed.Person

class PersonItem(
    private val content: Person,
    private val onClick: (p: Person) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_with_text_circle

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.name_tv.text = content.name.replace(" ", "\n")
        viewHolder.person_avatar.loadTransformationImage(
            content.imageUrl,
            CropCircleTransformation()
        )

        viewHolder.content.setOnClickListener {
            onClick.invoke(content)
        }
    }
}

fun ImageView.loadTransformationImage(imgUrl: String?, transformation: Transformation) {
    Picasso.get()
        .load(imgUrl)
        .transform(transformation)
        .into(this)
}


