package com.bma.gon.cadavreexquis.animation

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.bma.gon.cadavreexquis.R

class AnimationsUtils(val context:Context) {

    val fadeOut:Animation
    val fadeIn:Animation

    init {
        fadeOut = AnimationUtils.loadAnimation(this.context, R.anim.abc_shrink_fade_out_from_bottom)
        fadeIn = AnimationUtils.loadAnimation(this.context, R.anim.abc_grow_fade_in_from_bottom)
    }

    fun showNewText(newText:String, view:TextView){
        if (!view.text.toString().equals(newText)){
            view.startAnimation(fadeOut)
            view.text = newText
            view.startAnimation(fadeIn)
        }
    }

}