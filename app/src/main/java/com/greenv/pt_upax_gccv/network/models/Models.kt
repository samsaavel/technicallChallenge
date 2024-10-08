package com.greenv.pt_upax_gccv.network.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PokemonModel(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?,
    var spriteUrl: String? = null,
) : Parcelable {

    val id: Int?
        get() {
            return url?.trimEnd('/')?.substringAfterLast("/")?.toIntOrNull()
        }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PokemonModel> {
        override fun createFromParcel(parcel: Parcel): PokemonModel {
            return PokemonModel(parcel)
        }

        override fun newArray(size: Int): Array<PokemonModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class DetailsModel(
    @SerializedName("height")
    val height: Int,
    @SerializedName("sprites")
    val sprites: Sprite,
    @SerializedName("types")
    val types: ArrayList<Type>,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
)

data class Sprite(
    @SerializedName("front_default")
    val front_default: String?,
    @SerializedName("back_default")
    val back_default: String?,
    @SerializedName("front_female")
    val front_female: String?,
    @SerializedName("back_female")
    val back_female: String?,
    @SerializedName("front_shiny")
    val front_shiny: String?,
    @SerializedName("back_shiny")
    val back_shiny: String?,
) {
    fun getAvailableUrls(): List<String> {
        val urlList = mutableListOf<String>()
        if (!front_default.isNullOrEmpty()) urlList.add(front_default)
        if (!back_default.isNullOrEmpty()) urlList.add(back_default)
        if (!front_female.isNullOrEmpty()) urlList.add(front_female)
        if (!back_female.isNullOrEmpty()) urlList.add(back_female)
        if (!front_shiny.isNullOrEmpty()) urlList.add(front_shiny)
        if (!back_shiny.isNullOrEmpty()) urlList.add(back_shiny)
        return urlList
    }
}

data class Type(
    @SerializedName("slot")
    val slot: Int,
    @SerializedName("type")
    val type: PokemonModel,
)

data class PokemonResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val result: ArrayList<PokemonModel>,
)