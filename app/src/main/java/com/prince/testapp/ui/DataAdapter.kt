package com.prince.testapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prince.testapp.R
import com.prince.testapp.pojo.Row
import kotlinx.android.synthetic.main.list_row_data.view.*

/**
 * Data ADapter for List view
 */
class DataAdapter(val data : ArrayList<Row>):RecyclerView.Adapter<DataAdapter.PostHolder>() {
    lateinit var context: Context
    fun updateData(newDataList: ArrayList<Row>) {
        data.clear()
        data.addAll(newDataList)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        context = parent.context
        return PostHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_row_data,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.bind(data[position])
    }


    inner class PostHolder(dataView: View) : RecyclerView.ViewHolder(dataView) {


        fun bind(row: Row) {
           itemView.tvTitle.text = row.title
            itemView.tvDesc.text = row.description
            Glide.with(context)
                .load(row.imageHref)
                .centerCrop()
                .error(R.drawable.ic_file_download_black_24dp)
                .override(300, 300)
                .into(itemView.ivData)


        }
    }


}