package com.nextininfotech.neoflexadmin.model.image

data class Image(

    val backdrops: ArrayList<Backdrop>,
    val id: Int,
    val logos: List<Logo>,
    val posters: List<Poster>
)