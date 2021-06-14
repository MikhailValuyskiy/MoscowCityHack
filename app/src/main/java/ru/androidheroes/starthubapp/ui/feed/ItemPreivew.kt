package ru.androidheroes.starthubapp.ui.feed

import android.view.View
import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_preview.*
import ru.androidheroes.starthubapp.R

data class EventDetails(
    // Уникальный ID мероприятия
    var id: Int,
    // Название мероприятия
    val title: String,
    // Название категории
    val categoryName: String,
    // Тип категории
    val categoryType: EventCategory = EventCategory.DEMO_DAY,
    // Краткое описание
    val desc: String,
    // Дата проведения
    val date: String,
    // Адрес проведения
    val address: String,
    // Фотография обложки
    val imageUrl: String,
    val image: Int? = null,
    var isInFavorite: Boolean = false,
    // Email для связи
    val email: String = "admin@moscowhack.ru"
)

enum class EventCategory(name: String) {
    DEMO_DAY("Демо-день"),
    FORUM("Форум"),
    WEBINAR("Вебинар"),
    ONLINE_LESSON("Онлайн-лекция"),
    LESSON("Лекция"),
    SESSION("Стратегическая сессия"),
    TABLE(" Круглый стол")
}

data class Person(
    var id: Int,
    val name: String,
    val age: Int,
    val bio: String,
    val status: String,
    val imageUrl: String
)

data class Project(
    var id: Int,
    val title: String,
    val category: String,
    val desc: String,
    val imageUrl: String,
    val image: Int? = null,
    val ownerId: Int
)

class PreviewItem(
    private val eventInfo: EventDetails,
    private val onClick: (EventDetails) -> Unit
) : Item() {
    override fun getLayout() = R.layout.item_preview

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.content.setOnClickListener {
            onClick.invoke(eventInfo)
        }
        viewHolder.stock_name.text = eventInfo.title
        viewHolder.stock_code.text = eventInfo.categoryName
        viewHolder.percent_in_portfolio_value.text = eventInfo.date

        viewHolder.date_value.text = eventInfo.address
        viewHolder.date_value.visibility = View.VISIBLE

        if (!eventInfo.imageUrl.isNullOrEmpty()) {
            Picasso.get().load(eventInfo.imageUrl).into(viewHolder.image_preview2)
        }

    }
}

