package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.cupcake.ui.SelectOptionScreen
import org.junit.Rule
import org.junit.Test
import com.example.cupcake.R
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.StartOrderScreen

class CupcakeOrderScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeOrderUiState = OrderUiState(
        quantity = 6,
        flavor = "Vanilla",
        date = "Wed Jul 21",
        price = "$100",
        pickupOptions = listOf()
    )

    @Test
    fun selectOptionScreen_verifyContent() {
        // Given list of options
        val flavours = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        // And sub total
        val subTotal = "$300"

        // When SelectOptionScreen is loaded
        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subTotal, options = flavours)
        }

        // Then all the options are displayed on the screen.
        flavours.forEach { flavour ->
            composeTestRule.onNodeWithText(flavour).assertIsDisplayed()
        }

        // And then the subtotal is displayed correctly.
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                subTotal
            )
        ).assertIsDisplayed()

        // And then the next button is disabled
        composeTestRule.onNodeWithStringId(R.string.next).assertIsNotEnabled()
    }

    @Test
    fun startOrderScreen_verifyContent() {

        val quantityOptions = listOf(
            Pair(R.string.one_cupcake, 1),
            Pair(R.string.six_cupcakes, 6),
            Pair(R.string.twelve_cupcakes, 12)
        )

        // When startOrderScreen is loaded
        composeTestRule.setContent {
            StartOrderScreen(quantityOptions)
        }

        //Image is displayed correctly
        composeTestRule.onNodeWithContentDescription(
            composeTestRule.activity.getString(R.string.image_cupcake)
        ).assertIsDisplayed()

        // Order cupcakes is displayed correctly.
        composeTestRule.onNodeWithStringId(R.string.order_cupcakes).assertIsDisplayed()

        // Then all the options are displayed on the screen.
        quantityOptions.forEach { quantityOption ->
            composeTestRule.onNodeWithText(
                composeTestRule.activity.getString(quantityOption.first)
            ).assertIsDisplayed()
        }

        // Then all the options are Enabled on the screen.
        quantityOptions.forEach { quantityOption ->
            composeTestRule.onNodeWithText(
                composeTestRule.activity.getString(quantityOption.first)
            ).assertIsEnabled()
        }
    }

    /**
    * When list of options and subtotal are provided to SelectOptionScreen, and one of the option
    * is selected, then the next button is enabled.
    */
    @Test
    fun selectOptionScreen_optionSelected_NextButtonEnabled() {
        // Given list of options
        val flavours = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        // And sub total
        val subTotal = "$100"

        // When SelectOptionScreen is loaded
        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subTotal, options = flavours)
        }

        // And one option is selected
        composeTestRule.onNodeWithText("Vanilla").performClick()

        // Then the next button is disabled
        composeTestRule.onNodeWithStringId(R.string.next).assertIsEnabled()
    }

    /**
     * When a OrderUiState is provided to Summary Screen, then the flavor, date and subtotal is
     * displayed on the screen.
     */
    @Test
    fun summaryScreen_verifyContentDisplay() {
        // When Summary Screen is loaded
        composeTestRule.setContent {
            OrderSummaryScreen(
                orderUiState = fakeOrderUiState,
                onCancelButtonClicked = {},
                onSendButtonClicked = { _, _ -> },
            )
        }

        // Then the UI is updated correctly.
        composeTestRule.onNodeWithText(fakeOrderUiState.flavor).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeOrderUiState.date).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                fakeOrderUiState.price
            )
        )
    }
}