package com.nextininfotech.neoflexadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nextininfotech.neoflexadmin.R
import com.nextininfotech.neoflexadmin.constants.AppConstants.Companion.IMAGE_URL
import com.nextininfotech.neoflexadmin.model.MovieWatchModel
import com.nextininfotech.neoflexadmin.onClick.ImageVideoOnClick

class WatchMovieAdapter(var list : ArrayList<MovieWatchModel>, val context: Context, private val imageVideoOnClick: ImageVideoOnClick) : RecyclerView.Adapter<WatchMovieAdapter.HindiViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HindiViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.movie_watch_item, parent, false)
        return HindiViewHolder(view)
    }

    fun filterList(filterlist: ArrayList<MovieWatchModel>) {
        list = filterlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HindiViewHolder, position: Int) {

        val item = list[position]

        holder.title.text = item.name

        Glide.with(context).load(IMAGE_URL+item.photo).placeholder(R.drawable.ic_no_image).into(holder.poster)

        holder.deleteBtn.setOnClickListener {
            imageVideoOnClick.onClickIV(item.postKey.toString(),position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class HindiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val poster : ImageView = itemView.findViewById(R.id.posterIv)
        val title : TextView = itemView.findViewById(R.id.titleTv)
        val deleteBtn : ImageButton = itemView.findViewById(R.id.deleteButton)
    }
}