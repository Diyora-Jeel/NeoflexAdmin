package com.nextininfotech.neoflexadmin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nextininfotech.neoflexadmin.R
import com.nextininfotech.neoflexadmin.constants.AppConstants
import com.nextininfotech.neoflexadmin.model.image.Backdrop
import com.nextininfotech.neoflexadmin.onClick.MainOnclick

class ImageAdapter(private val list: ArrayList<Backdrop>, val context: Context, private val mainOnclick: MainOnclick) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_item,parent,false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val item = list[position]

        Glide.with(context).load(AppConstants.IMAGE_URL+item.file_path).placeholder(R.drawable.ic_no_image).into(holder.imageView)

        holder.cardView.setOnClickListener {
            mainOnclick.onClick3(AppConstants.IMAGE_URL_ORIGINAL+item.file_path)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val cardView : CardView = itemView.findViewById(R.id.cardView)
        val imageView : ImageView = itemView.findViewById(R.id.imageView)
    }
}