package com.ragdroid.mvi.items

import android.databinding.DataBindingUtil
import android.view.View
import com.fueled.reclaim.BaseItem
import com.fueled.reclaim.BaseViewHolder
import com.fueled.reclaim.ItemHandlerProvider
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.ItemCharacterBinding
import com.ragdroid.mvi.models.CharacterItemHandler
import com.ragdroid.mvi.models.CharacterItemState

/**
 * Created by garimajain on 22/11/17.
 */
class CharacterItem(val state: CharacterItemState, handlerProvider: ItemHandlerProvider<CharacterItemHandler>) :
        BaseItem<CharacterItemState, CharacterItemHandler, CharacterItem.ViewHolder>(state, handlerProvider) {

    override fun updateItemViews() {
        viewHolder.binding.state = state
        viewHolder.binding.handler = itemHandlerProvider.itemHandler
    }

    override fun onCreateViewHolder(view: View): ViewHolder = ViewHolder(view)

    override fun getType(): Enum<out Enum<*>> = ItemTypes.CHARACTER_MARVEL

    override fun getLayoutId(): Int = R.layout.item_character

    class ViewHolder(view: View): BaseViewHolder(view) {
        val binding: ItemCharacterBinding = DataBindingUtil.bind(view)
    }

    override fun isContentsTheSame(newItem: BaseItem<*, *, *>?): Boolean {
        return super.isContentsTheSame(newItem)
    }

    override fun isTheSame(newItem: BaseItem<*, *, *>?): Boolean {
        return super.isTheSame(newItem)
    }
}