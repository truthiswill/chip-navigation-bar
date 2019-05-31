package ismaeldivita.bubblenavigation.item

import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import ismaeldivita.bubblenavigation.BubbleNavigationView.MenuOrientation.VERTICAL
import ismaeldivita.bubblenavigation.robot.MenuTestActivity
import ismaeldivita.bubblenavigation.robot.menu
import ismaeldivita.bubblenavigation.test.R
import org.junit.Rule
import org.junit.Test

@LargeTest
class VerticalMenuTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule(MenuTestActivity::class.java)

    @Test
    fun testSelection() {
        menu(rule) {
            loadMenuResource(R.menu.test_menu, VERTICAL)

            noItemSelected(R.id.home, R.id.discover, R.id.search, R.id.settings)

            item(R.id.home) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.home)
            }

            item(R.id.search) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.search)
            }

            item(R.id.discover) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.discover)
            }

            item(R.id.settings) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.settings)
            }
        }
    }

    @Test
    fun testSelectionWithDisabledItems() {
        menu(rule) {
            loadMenuResource(R.menu.test_menu_disabled, VERTICAL)

            item(R.id.home) {
                isSelected(false)
                click()
                isSelected(false)
            }

            item(R.id.discover) {
                isSelected(false)
                click()
                isSelected(false)
            }

            item(R.id.search) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.search)
            }

            item(R.id.settings) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.settings)
            }
        }
    }

    @Test
    fun testDisableAnActiveItem() {
        menu(rule) {
            loadMenuResource(R.menu.test_menu, VERTICAL)

            item(R.id.home) {
                isSelected(false)
                click()
                isSelected(true)
                isTitle(R.string.home)
            }

            disableItem(R.id.home)

            noItemSelected(R.id.home, R.id.discover, R.id.search, R.id.settings)
        }
    }

    @Test
    fun testHideOnScrollIsDisable() {
        menu(rule) {
            loadMenuResource(R.menu.test_menu, VERTICAL)
            isDisplayed()
            swipeUp()
            isDisplayed()
            swipeDown()
            isDisplayed()
        }
    }


    @Test
    fun testCollapseBehavior() {
        menu(rule) {
            loadMenuResource(R.menu.test_menu, VERTICAL)
            item(R.id.home) { isTitleDisplayed(true) }
            collapse()
            item(R.id.home) { isTitleDisplayed(false) }
            expand()
            item(R.id.home) { isTitleDisplayed(true) }
        }
    }
}