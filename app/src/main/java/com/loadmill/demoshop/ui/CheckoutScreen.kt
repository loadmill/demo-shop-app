package com.loadmill.demoshop.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loadmill.demoshop.Cart
import com.loadmill.demoshop.CheckoutForm
import com.loadmill.demoshop.R

@Composable
fun CheckoutScreen(
    cart: Cart,
    form: CheckoutForm,
    showErrors: Boolean,
    onBack: () -> Unit,
    onFormChange: (CheckoutForm) -> Unit,
    onPlaceOrder: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(Modifier.fillMaxSize()) {
        ScreenHeader("Checkout", onBack, scrollState.value > 0)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(start = ScreenPadding, end = ScreenPadding, top = 8.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Section("Contact") {
                FormField("Full name", form.name, showErrors) { onFormChange(form.copy(name = it)) }
                FormField("Email", form.email, showErrors, KeyboardType.Email) { onFormChange(form.copy(email = it)) }
            }
            Section("Shipping") {
                FormField("Address", form.address, showErrors) { onFormChange(form.copy(address = it)) }
                DeliveryOption()
            }
            Section("Payment") {
                DemoCard(form)
                FormField("Card number", form.cardNumber, showErrors, KeyboardType.Number) {
                    onFormChange(form.copy(cardNumber = it))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(Modifier.weight(1f)) {
                        FormField("Expiry (MM/YY)", form.expiry, showErrors) { onFormChange(form.copy(expiry = it)) }
                    }
                    Box(Modifier.weight(1f)) {
                        FormField("CVC", form.cvc, showErrors, KeyboardType.Number) { onFormChange(form.copy(cvc = it)) }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LineIcon(R.drawable.ic_lock, null, Modifier.size(16.dp), tint = Muted)
                    Spacer(Modifier.width(6.dp))
                    Text("Demo checkout. Your card is never charged.", style = MaterialTheme.typography.bodySmall, color = Muted)
                }
            }
            Section("Order summary") {
                SoftPanel {
                    OrderLines(cart)
                    Spacer(Modifier.height(18.dp))
                    PriceSummary(cart)
                }
            }
        }
        BottomBar {
            if (showErrors && !form.isValid) {
                Text(
                    "Please fill in all fields.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 10.dp),
                )
            }
            TotalAndAction(cart.totalCents) { modifier ->
                PrimaryButton("Place order", onPlaceOrder, modifier)
            }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(title, Modifier.padding(bottom = 2.dp))
        content()
    }
}

@Composable
private fun DeliveryOption() {
    Row(
        Modifier
            .fillMaxWidth()
            .border(1.5.dp, Ink, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(20.dp)
                .border(1.5.dp, Ink, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(Modifier.size(10.dp).background(Ink, CircleShape))
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text("Standard delivery", style = MaterialTheme.typography.titleSmall)
            Text("3–5 business days", style = MaterialTheme.typography.bodySmall, color = Muted)
        }
        Text("Free", style = MaterialTheme.typography.titleSmall, color = Success)
    }
}

@Composable
private fun DemoCard(form: CheckoutForm) {
    Column(
        Modifier
            .fillMaxWidth()
            .height(196.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF2B2B2B), Ink, Color(0xFF3A2A22))))
            .padding(22.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Loadmill Demo Shop",
                fontFamily = Jakarta,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                letterSpacing = (-0.6).sp,
                color = Color.White,
                maxLines = 1,
                modifier = Modifier.weight(1f),
            )
            Text(
                "DEMO",
                style = MaterialTheme.typography.labelSmall,
                color = Ink,
                modifier = Modifier
                    .background(Clay, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            )
        }
        Spacer(Modifier.height(18.dp))
        Box(
            Modifier
                .size(width = 40.dp, height = 30.dp)
                .background(Color(0xFFD9C3A0), RoundedCornerShape(6.dp)),
        )
        Spacer(Modifier.weight(1f))
        Text(
            form.cardNumber.ifBlank { "•••• •••• •••• ••••" },
            fontFamily = Jakarta,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            letterSpacing = 1.5.sp,
            color = Color.White,
        )
        Spacer(Modifier.height(12.dp))
        Row {
            CardDetail("Card holder", form.name.ifBlank { "Your name" }, Modifier.weight(1f))
            CardDetail("Expires", form.expiry.ifBlank { "MM/YY" })
        }
    }
}

@Composable
private fun CardDetail(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = Color.White.copy(alpha = 0.6f))
        Text(value, style = MaterialTheme.typography.labelMedium, color = Color.White, maxLines = 1)
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    showErrors: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
) {
    val isError = showErrors && value.isBlank()
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        supportingText = if (isError) {
            { Text("Required") }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(16.dp),
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Ink,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = Ink,
            unfocusedLabelColor = Muted,
            cursorColor = Ink,
        ),
        modifier = Modifier.fillMaxWidth(),
    )
}
