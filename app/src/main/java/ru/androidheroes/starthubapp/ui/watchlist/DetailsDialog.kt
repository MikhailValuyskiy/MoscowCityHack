package ru.androidheroes.starthubapp.ui.watchlist

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dialog_details.*
import ru.androidheroes.starthubapp.R

class ConfirmationDialog(
    private val title: String,
    private val category: String,
    private val price: String,
    private val url: String,
    private val desc: String = "",
    context: Context
) :
    BottomSheetDialog(context, R.style.TransparentBottomSheet) {

    override fun onCreate(savedInstanceState: Bundle?) {
        val contentView = View.inflate(context, R.layout.dialog_details, null)
        setContentView(contentView)
        super.onCreate(savedInstanceState)

        tv_bottom_sheet_heading.text = title

        text0.text = "Категория: " + category
        strana.text = "Страна производитель: " + "Россия"
        sostav.text = "Описание: " + desc
        price_tv.text = "Цена: " + price

        if (!url.isNullOrEmpty()) {
            Picasso.get().load(url).into(preview)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {
        fun newInstance(title: String, category: String, price: String, url: String, desc: String, context: Context): ConfirmationDialog {
            val dialog = ConfirmationDialog(title, category, price, url, desc, context)
            dialog.show()
            return dialog
        }
    }
}