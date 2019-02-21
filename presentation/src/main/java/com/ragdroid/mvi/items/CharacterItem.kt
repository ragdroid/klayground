package com.ragdroid.mvi.items

import androidx.databinding.DataBindingUtil
import android.view.View
import com.fueled.reclaim.BaseItem
import com.fueled.reclaim.BaseViewHolder
import com.fueled.reclaim.ItemPresenterProvider
import com.ragdroid.mvi.R
import com.ragdroid.mvi.databinding.ItemCharacterBinding
import com.ragdroid.mvi.models.CharacterItemPresenter
import com.ragdroid.mvi.models.CharacterItemState
import timber.log.Timber

/**
 * Created by garimajain on 22/11/17.
 */
class CharacterItem(val state: CharacterItemState, presenterProvider: ItemPresenterProvider<CharacterItemPresenter>) :
        BaseItem<CharacterItemState, CharacterItemPresenter, CharacterItem.ViewHolder>(state, presenterProvider) {

    override fun updateItemViews() {
        viewHolder.binding?.state = state
        viewHolder.binding?.handler = itemPresenterProvider.itemPresenter
    }

    override fun onCreateViewHolder(view: View): ViewHolder = ViewHolder(view)

    override fun getType(): Enum<out Enum<*>> = ItemTypes.CHARACTER_MARVEL

    override fun getLayoutId(): Int = R.layout.item_character

    class ViewHolder(view: View): BaseViewHolder(view) {
        val binding: ItemCharacterBinding? = DataBindingUtil.bind(view)
    }

        override fun isContentsTheSame(newItem: BaseItem<*, *, *>?): Boolean {
        if (newItem is CharacterItem) {
            val isContentSame = itemData.equals(newItem.itemData)
            Timber.d("isContentSame: $isContentSame")
            return isContentSame
        }
        return super.isContentsTheSame(newItem)
    }

    override fun isTheSame(newItem: BaseItem<*, *, *>?): Boolean {
        if (newItem is CharacterItem) {
            val isSame = itemData.characterId == newItem.itemData.characterId
            Timber.d("isSame: $isSame")
            return isSame
        }
        return super.isContentsTheSame(newItem)
    }
}