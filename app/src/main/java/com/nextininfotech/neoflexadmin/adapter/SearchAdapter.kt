package com.nextininfotech.neoflexadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nextininfotech.neoflexadmin.R
import com.nextininfotech.neoflexadmin.constants.AppConstants.Companion.IMAGE_URL
import com.nextininfotech.neoflexadmin.model.movie.Result
import com.nextininfotech.neoflexadmin.onClick.MainOnclick

class SearchAdapter(private val context: Context, private val list : ArrayList<Result>,private val mainOnclick: MainOnclick) : RecyclerView.Adapter<SearchAdapter.NowPlayingViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_item,parent,false)
        return NowPlayingViewHolder(view)
    }

    override fun onBindViewHolder(holder: NowPlayingViewHolder, position: Int) {
        val item = list[position]

        Glide.with(context).load(IMAGE_URL+item.poster_path).placeholder(R.drawable.ic_no_image).into(holder.imageView)
        holder.title.text = item.title
        holder.body.text = item.overview
        holder.date.text = item.release_date

        holder.cardView.setOnClickListener {
            mainOnclick.onClick(item.id)
        }

        holder.imageView.setOnClickListener {
            mainOnclick.onClick2(item.id)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class NowPlayingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val cardView : LinearLayout = itemView.findViewById(R.id.cardView)
        val imageView : ImageView = itemView.findViewById(R.id.imageView)
        val title : TextView = itemView.findViewById(R.id.title)
        val body : TextView = itemView.findViewById(R.id.body)
        val date : TextView = itemView.findViewById(R.id.date)
    }

}