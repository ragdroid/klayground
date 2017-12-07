package com.ragdroid.mvi.helpers

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by garimajain on 19/11/17.
 */
@Singleton
class MarvelBindingAdapter @Inject
constructor(val requestManager: RequestManager) {


    /**
     * Bind Glide with an ImageView.
     * https://medium.com/fueled-android/data-binding-adapter-write-bind-repeat-50e9c64fe806
     *
     * @param view the ImageView to bind to Glide.
     * @param src The URL of the image to load.
     * @param placeholder The placeholder icon.
     * @param error The error icon.
     * @param blurValue The blur radius value between 1 and 25.
     * @param cropCircle Crop the image in a circle of not.
     */
    @android.databinding.BindingAdapter(value = *arrayOf("src", "placeholder", "error", "blur",
            "cropCircle", "centerCrop"), requireAll = false)
    fun setImageUrl(view: ImageView, src: String, placeholder: Drawable?, error: Drawable?,
                    blurValue: Int, cropCircle: Boolean, centerCrop: Boolean) {

        val glideBuilder = requestManager.load(src)

        val requestOptions = RequestOptions()

        placeholder?.let { requestOptions.placeholder(placeholder) }

        requestOptions.apply {

            if (cropCircle) {
                circleCrop()
            }

            if (centerCrop) {
                centerCrop()
            }

            if (error != null) {
                error(error)
            }
        }

        glideBuilder.apply(requestOptions)

        glideBuilder.into(view)
    }



}

class MarvelBindingComponent
@Inject constructor(val adapter: MarvelBindingAdapter):
        android.databinding.DataBindingComponent {

    override fun getMarvelBindingAdapter(): MarvelBindingAdapter {
        return adapter
    }

}