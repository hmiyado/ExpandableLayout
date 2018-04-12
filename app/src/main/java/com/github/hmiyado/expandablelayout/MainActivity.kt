package com.github.hmiyado.expandablelayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

/**
 * ## 課題
レイアウト開閉するViewを自作してみる
https://github.com/chuross/expandable-layout
↑こんな感じの

## 仕様
- Viewを最初に表示する時は内包しているViewの一部だけ表示
- ボタンを押すと内包しているViewの全体を表示
- アニメーションの有無は問わず
- 再度ボタンを押すと内包しているViewの一部だけ表示していた状態に戻す
- アニメーションの有無は問わず
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).also {
            it.setOnClickListener {
                findViewById<ExpandableLayout>(R.id.expandable_layout).also {
                    it.toggle()
                }
            }
        }
    }
}
