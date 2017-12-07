package com.ragdroid.mvi.items

import android.databinding.DataBindingUtil
import android.view.View
import com.fueled.reclaim.BaseItem
import com.fueled.reclaim.BaseViewHolder
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.ItemCharacterBinding
import com.ragdroid.mvi.models.CharacterModel

/**
 * Created by garimajain on 22/11/17.
 */
class CharacterItem(val model: CharacterModel) :
        BaseItem<CharacterModel, Void, CharacterItem.ViewHolder>(model, null) {

    override fun updateItemViews() {
        viewHolder.binding.model = model
    }

    override fun onCreateViewHolder(view: View): ViewHolder = ViewHolder(view)

    override fun getType(): Enum<out Enum<*>> = ItemTypes.CHARACTER_MARVEL

    override fun getLayoutId(): Int = R.layout.item_character

    class ViewHolder(view: View): BaseViewHolder(view) {
        val binding: ItemCharacterBinding = DataBindingUtil.bind(view)
    }
}