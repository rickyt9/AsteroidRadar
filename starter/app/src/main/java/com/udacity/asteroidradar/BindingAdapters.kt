package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("pictureOfDay")
fun bindPictureOfDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    if (pictureOfDay != null && pictureOfDay.mediaType == "image") {
        val imageUri = pictureOfDay.url.toUri().buildUpon().scheme("https").build()
        Picasso.with(imageView.context)
            .load(imageUri)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(imageView)
    } else {
        imageView.setImageResource(R.drawable.placeholder_picture_of_day)
    }
}

@BindingAdapter("pictureDescription")
fun bindPictureOfDayDescription(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    val context = imageView.context

    imageView.contentDescription = if (pictureOfDay != null && pictureOfDay.mediaType == "image") {
        context.getString(R.string.nasa_picture_of_day_content_description_format, pictureOfDay.title)
    } else {
        context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("asteroidDescription")
fun bindAsteroidImageDescription(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context

    imageView.contentDescription = if (isHazardous) {
        context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("astronomicalUnitDescription")
fun bindAstronomicalUnitDescription(textView: TextView, number: Double) {
    val context = textView.context
    textView.contentDescription = context
        .getString(R.string.astronomical_unit_description_format, number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
