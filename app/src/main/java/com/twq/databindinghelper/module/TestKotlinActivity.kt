package com.twq.databindinghelper.module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.twq.databindinghelper.R
import com.twq.databindinghelper.base.DataBindingActivity
import com.twq.databindinghelper.databinding.ActivityAnimatorBinding

/**
 * Created by Administrator on 2018/2/7 0007.
 */
class TestKotlinActivity : DataBindingActivity<ActivityAnimatorBinding>() {
    override fun create(savedInstanceState: Bundle?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_animator
    }
    companion object {
        fun launch(context:Context){
            val intent = Intent(context, TestKotlinActivity::class.java)
            context.startActivity(intent)
        }
    }

}