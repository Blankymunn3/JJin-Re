package com.jjin_re.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jjin_re.R
import com.jjin_re.model.NoticeModel
import com.jjin_re.utils.SharedPreferenceHelper
import com.jjin_re.utils.Utils.getNoticeDiffTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NoticeExpandableListAdapter(val activity: AppCompatActivity, val list: List<NoticeModel>): BaseExpandableListAdapter() {
    override fun getGroup(groupPosition: Int): NoticeModel {
        return list[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val context: Context = parent.context

        if (view == null) {
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.layout_notice_list, parent, false)
        }

        val titleTextView: TextView = view!!.findViewById(R.id.tv_notice_title)
        val createdAtTextView: TextView = view.findViewById(R.id.tv_notice_created_at)
        val noticeNewImageView: ImageView = view.findViewById(R.id.iv_has_new_info)
        val lastNoticeString: Array<String?> = SharedPreferenceHelper.getLastNoticeCheck(activity).toTypedArray()

        var isNoticeCheck = true
        if (lastNoticeString.isEmpty()) isNoticeCheck = getNoticeDiffTime(getGroup(groupPosition).created_at) < 7
        else {
            for (noticeItem in lastNoticeString) {
                if (noticeItem != getGroup(groupPosition).uId) {
                    isNoticeCheck = getNoticeDiffTime(getGroup(groupPosition).created_at) < 7
                } else {
                    isNoticeCheck = false
                    break
                }
            }
        }



        createdAtTextView.visibility = View.VISIBLE
        if (!isNoticeCheck) noticeNewImageView.visibility = View.GONE
        else noticeNewImageView.visibility = View.VISIBLE

        titleTextView.text = getGroup(groupPosition).title
        val inputPattern = "yyyy-MM-dd HH:mm:ss"
        val outputPattern = "yyyy-MM-dd"
        val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
        val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
        var changeDate: String = getGroup(groupPosition).created_at.replace("T", " ").replace("Z", "").replace(".000", "")

        try {
            val date: Date = inputFormat.parse(getGroup(groupPosition).created_at.replace("T", " ").replace("Z", "").replace(".000", ""))!!
            changeDate = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        createdAtTextView.text = changeDate

        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return list[groupPosition].contents
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val context: Context = parent.context

        if (view == null) {
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.layout_notice_content, parent, false)
        }

        val contentsTextView: TextView = view!!.findViewById(R.id.tv_notice_contents)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contentsTextView.text = getChild(groupPosition, 0).toString()
        } else {
            contentsTextView.text = getChild(groupPosition, 0).toString()
        }

        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return list.size
    }

}