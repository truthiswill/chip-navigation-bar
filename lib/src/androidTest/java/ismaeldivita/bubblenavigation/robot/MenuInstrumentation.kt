package ismaeldivita.bubblenavigation.robot

import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.common.views.KSwipeView
import com.agoda.kakao.common.views.KView
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import ismaeldivita.bubblenavigation.BubbleNavigationView
import ismaeldivita.bubblenavigation.R
import ismaeldivita.bubblenavigation.test.R as testR

class MenuInstrumentation(private val activity: MenuTestActivity) : Screen<MenuInstrumentation>() {

    private val result = KTextView { withId(testR.id.result) }
    private val menu = KView { withId(testR.id.menu) }
    private val scroll = KSwipeView { withId(testR.id.nestedScroll) }
    private lateinit var orientation: BubbleNavigationView.MenuOrientation

    fun loadMenuResource(@MenuRes id: Int, orientation: BubbleNavigationView.MenuOrientation) {
        this.orientation = orientation
        activity.runOnUiThread {
            activity.setOrientation(orientation)
            activity.loadMenuResource(id)
        }
    }

    fun noItemSelected(vararg id: Int) {
        result.hasText("0")
        id.forEach { item(it) { isSelected(false) } }
    }

    fun item(id: Int, block: MenuItemInstrumentation.() -> Unit) =
        MenuItemInstrumentation(id, orientation).apply { block() }

    fun disableItem(itemId: Int) {
        activity.runOnUiThread { activity.disabledItem(itemId) }
    }

    fun enableItem(itemId: Int) {
        activity.runOnUiThread { activity.enableItem(itemId) }
    }

    fun isDisplayed() {
        menu.isCompletelyDisplayed()
    }

    fun isHidden() {
        menu.isNotCompletelyDisplayed()
    }

    fun swipeUp() {
        scroll.swipeUp()
    }

    fun swipeDown() {
        scroll.swipeDown()
    }

    fun expand() {
        activity.runOnUiThread { activity.expand() }
    }

    fun collapse() {
        activity.runOnUiThread { activity.collapse() }
    }

    inner class MenuItemInstrumentation(
        private val id: Int,
        private val orientation: BubbleNavigationView.MenuOrientation
    ) : Screen<MenuItemInstrumentation>() {

        private val item = KView {
            withId(this@MenuItemInstrumentation.id)
        }

        private val title = KTextView {
            withParent { withParent { withId(this@MenuItemInstrumentation.id) } }
            withId(R.id.bnv_item_title)
        }

        private val icon = KImageView {
            withParent { withParent { withId(this@MenuItemInstrumentation.id) } }
            withId(R.id.bnv_item_icon)
        }

        fun isTitle(@StringRes text: Int) = title {
            hasText(text)
            isCompletelyDisplayed()
        }

        fun isTitleDisplayed(isDisplayed: Boolean) {
            if (isDisplayed) {
                title.isCompletelyDisplayed()
            } else {
                title.isNotCompletelyDisplayed()
            }
        }

        fun click() {
            icon.click()
        }

        fun isSelected(isSelected: Boolean) {
            if (isSelected) {
                item.isSelected()
                this@MenuInstrumentation.result.hasText(id.toString())

                if (orientation == BubbleNavigationView.MenuOrientation.HORIZONTAL) {
                    title.isCompletelyDisplayed()
                }
            } else {
                item.isNotSelected()
                this@MenuInstrumentation.result.hasNoText(id.toString())

                if (orientation == BubbleNavigationView.MenuOrientation.HORIZONTAL) {
                    title.isNotCompletelyDisplayed()
                }
            }
        }
    }
}


fun menu(rule: ActivityTestRule<MenuTestActivity>, block: MenuInstrumentation.() -> Unit) {
    MenuInstrumentation(rule.activity).apply { block() }
}