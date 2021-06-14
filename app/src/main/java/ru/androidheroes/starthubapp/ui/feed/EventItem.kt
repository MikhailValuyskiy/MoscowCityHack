package ru.androidheroes.starthubapp.ui.feed

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_with_expert.*
import kotlinx.android.synthetic.main.item_with_text.*
import kotlinx.android.synthetic.main.item_with_text.content
import kotlinx.android.synthetic.main.item_with_text.date
import kotlinx.android.synthetic.main.item_with_text.description
import kotlinx.android.synthetic.main.item_with_text.description2
import kotlinx.android.synthetic.main.item_with_text.image_preview
import kotlinx.android.synthetic.main.item_with_text.movie_rating
import ru.androidheroes.starthubapp.R
import ru.androidheroes.starthubapp.data.MockRepository
import ru.androidheroes.starthubapp.ui.event_details.loadTransformationImage

class EventItem(
    private val content: EventDetails,
    private val onClick: (goods: EventDetails) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_with_text

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.description.text = content.title
        viewHolder.description2.text = content.address
        viewHolder.date.text = content.date
        viewHolder.movie_rating.rating = 5.0f
        viewHolder.like.isChecked = content.isInFavorite
        viewHolder.content.setOnClickListener {
            onClick.invoke(content)
        }

        Picasso.get()
            .load(content.imageUrl)
            .into(viewHolder.image_preview)
    }
}

class NearEventItem(
    private val content: EventDetails,
    private val onClick: (goods: EventDetails) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_with_text_horizontal

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.description.text = content.title
        viewHolder.movie_rating.rating = 5.0f
        viewHolder.like.isChecked = content.isInFavorite
        viewHolder.content.setOnClickListener {
            onClick.invoke(content)
        }

        Picasso.get()
            .load(content.imageUrl)
            .into(viewHolder.image_preview)
    }
}

class ExpertArticleItem(
    private val content: EventDetails,
    private val onClick: (goods: EventDetails) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_with_expert

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val person = MockRepository.getPeople().shuffled()[0]
        viewHolder.description.text = content.title
        viewHolder.description2.text = content.address
        viewHolder.date.text = content.date
        viewHolder.movie_rating.rating = 5.0f
        viewHolder.content.setOnClickListener {
            onClick.invoke(content)
        }

        Picasso.get()
            .load(content.imageUrl)
            .into(viewHolder.image_preview)

        viewHolder.person_avatar.loadTransformationImage(
            person.imageUrl,
            CropCircleTransformation()
        )
    }
}

