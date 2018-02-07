package com.twq.databindinghelper.module

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.transition.AutoTransition
import android.support.transition.TransitionManager
import android.support.transition.TransitionSet
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.twq.databindinghelper.R
import com.twq.databindinghelper.base.DataBindingActivity
import com.twq.databindinghelper.databinding.ActivityAnimatorBinding
import com.twq.databindinghelper.util.DeviceUtil

/**
 * 动画
 * Created by Administrator on 2018/1/25 0025.
 */

class AnimatorActivity : DataBindingActivity<ActivityAnimatorBinding>() {

    private var transitionSet: TransitionSet? = null
    private var isExpand: Boolean = false
    private var ScreenWidth: Int = 0//屏幕宽度

    override fun create(savedInstanceState: Bundle?) {
        binding.button.setOnClickListener { CreateAnim() }
        isExpand = false
        ScreenWidth = DeviceUtil.getScreenWidth(mContext)
        binding.layoutToolbar.background.mutate().alpha = 0
        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            //改变toolbar的透明度
            changeToolbarAlpha()
            if (binding.scrollView.scrollY >= binding.img.height - binding.layoutToolbar.height && !isExpand) {
                exPand()//靠系统提供的Transition实现view宽度改变
                isExpand = true
                //                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(new ViewWrapper(getBinding().layoutTitle), "width", ScreenWidth);//这是靠动画来改变view宽度
                //                    setAnimator(objectAnimator, 1);
            } else if (binding.scrollView.scrollY <= 0 && isExpand) {
                reDuce()
                isExpand = false
                //                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(new ViewWrapper(getBinding().layoutTitle), "width", dip2px(70));
                //                    setAnimator(objectAnimator, 0);
            }
        }
    }

    private fun changeToolbarAlpha() {
        val scrollY = binding.scrollView.scrollY
        //快速下拉会引起瞬间scrollY<0
        if (scrollY < 0) {
            binding.layoutToolbar.background.mutate().alpha = 0
            return
        }
        //计算当前透明度比率
        val radio = Math.min(1f, scrollY / (binding.img.height - binding.layoutToolbar.height * 1f))
        //设置透明度
        binding.layoutToolbar.background.mutate().alpha = (radio * 0xFF).toInt()
    }

    private fun CreateAnim() {
        // 步骤1：设置属性数值的初始值 & 结束值
        val valueAnimator = ValueAnimator.ofInt(binding.button.layoutParams.width, 500)
        // 步骤2：设置动画的播放各种属性
        valueAnimator.duration = 1000
        // 步骤3：将属性数值手动赋值给对象的属性:此处是将 值 赋给 按钮的宽度
        // 设置更新监听器：即数值每次变化更新都会调用该方法
        valueAnimator.addUpdateListener { animation ->
            binding.button.layoutParams.width = animation.animatedValue as Int
            // 每次值变化时，将值手动赋值给对象的属性
            // 即将每次变化后的值 赋 给按钮的宽度，这样就实现了按钮宽度属性的动态变化
            // 步骤4：刷新视图，即重新绘制，从而实现动画效果
            binding.button.requestLayout()
        }
        valueAnimator.start()//动画启动
    }

    private fun setAnimator(objectAnimator: ObjectAnimator, status: Int) {
        objectAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                if (status == 0) {
                    binding.tvTitle.text = "搜索"
                } else if (status == 1) {
                    binding.tvTitle.text = "搜索简书的内容和朋友"
                }
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })
        objectAnimator.duration = 300
        objectAnimator.start()
    }

    private fun exPand() {
        binding.tvTitle.text = "搜索简书的内容和朋友"
        //        getBinding().tvTitle.setText("搜索");
        val LayoutParams = binding.layoutTitle.layoutParams as RelativeLayout.LayoutParams
        LayoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT
        LayoutParams.setMargins(dip2px(10f), dip2px(10f), dip2px(10f), dip2px(10f))
        binding.layoutTitle.layoutParams = LayoutParams
        //开始动画
        beginDelayedTransition(binding.layoutTitle)
    }

    private fun reDuce() {
        binding.tvTitle.text = "搜索"
        val LayoutParams = binding.layoutTitle.layoutParams as RelativeLayout.LayoutParams
        LayoutParams.width = dip2px(70f)
        LayoutParams.setMargins(dip2px(10f), dip2px(10f), dip2px(10f), dip2px(10f))
        binding.layoutTitle.layoutParams = LayoutParams
        //开始动画
        beginDelayedTransition(binding.layoutTitle)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_animator
    }

    private fun beginDelayedTransition(view: ViewGroup) {
        transitionSet = AutoTransition()
        transitionSet!!.duration = 200
        TransitionManager.beginDelayedTransition(view, transitionSet)
    }

    private fun dip2px(dpVale: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpVale * scale + 0.5f).toInt()
    }

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, AnimatorActivity::class.java)
            context.startActivity(intent)
        }
    }
}
