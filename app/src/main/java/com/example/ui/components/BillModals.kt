package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BillItem
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*

@Composable
fun TaxPaymentModal(
  bill: BillItem,
  language: AppLanguage,
  isProcessing: Boolean,
  onDismiss: () -> Unit,
  onConfirmPay: (BillItem, String) -> Unit
) {
  var selectedMethod by remember { mutableStateOf("UPI / PhonePe / GPay") }

  Dialog(
    onDismissRequest = { if (!isProcessing) onDismiss() },
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .padding(16.dp),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Payment,
              contentDescription = null,
              tint = GreenPrimary,
              modifier = Modifier.size(24.dp)
            )
            Column {
              Text(
                text = if (language == AppLanguage.MARATHI) "सुरक्षित कर भरणा" else "Secure Tax Payment",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = bill.billType,
                fontSize = 12.sp,
                color = GreenPrimary,
                fontWeight = FontWeight.Medium
              )
            }
          }
          if (!isProcessing) {
            IconButton(onClick = onDismiss) {
              Icon(Icons.Default.Close, contentDescription = "Close")
            }
          }
        }

        HorizontalDivider()

        // Bill Summary Card
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = GreenContainer.copy(alpha = 0.6f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = if (language == AppLanguage.MARATHI) "मालमत्ता क्र.:" else "Property No:",
                fontSize = 12.sp,
                color = TextSecondary
              )
              Text(
                text = bill.propertyNumber,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = if (language == AppLanguage.MARATHI) "करदाता:" else "Taxpayer:",
                fontSize = 12.sp,
                color = TextSecondary
              )
              Text(
                text = bill.consumerName,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = if (language == AppLanguage.MARATHI) "आकारणी वर्ष:" else "Year:",
                fontSize = 12.sp,
                color = TextSecondary
              )
              Text(
                text = bill.assessmentYear,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }

            HorizontalDivider(color = OutlineLight.copy(alpha = 0.5f))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (language == AppLanguage.MARATHI) "एकूण देय रक्कम:" else "Payable Amount:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = OnGreenContainer
              )
              Text(
                text = "₹ ${bill.amount.toInt()}/-",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GreenPrimary
              )
            }
          }
        }

        // Payment Options
        Text(
          text = if (language == AppLanguage.MARATHI) "भरणा पद्धत निवडा (Payment Gateway)" else "Select Payment Method",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )

        val methods = listOf(
          "UPI / PhonePe / GPay" to Icons.Default.QrCodeScanner,
          "डेबिट / क्रेडिट कार्ड (Cards)" to Icons.Default.CreditCard,
          "नेट बँकिंग (Net Banking)" to Icons.Default.AccountBalance
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          methods.forEach { (name, icon) ->
            val isSelected = selectedMethod == name
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) GreenContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
              border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, GreenPrimary) else null,
              modifier = Modifier
                .fillMaxWidth()
                .clickable { if (!isProcessing) selectedMethod = name }
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                RadioButton(
                  selected = isSelected,
                  onClick = { if (!isProcessing) selectedMethod = name },
                  colors = RadioButtonDefaults.colors(selectedColor = GreenPrimary)
                )
                Icon(imageVector = icon, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(20.dp))
                Text(
                  text = name,
                  fontSize = 13.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              }
            }
          }
        }

        // Security Assurance
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Security, contentDescription = null, tint = StatusResolved, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (language == AppLanguage.MARATHI) "२५६-बिट बँक-स्तरीय एनक्रिप्टेड व सुरक्षित" else "256-bit Secure Gateway",
            fontSize = 11.sp,
            color = TextSecondary
          )
        }

        // Submit Button
        Button(
          onClick = { onConfirmPay(bill, selectedMethod) },
          enabled = !isProcessing,
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
          shape = RoundedCornerShape(12.dp)
        ) {
          if (isProcessing) {
            CircularProgressIndicator(
              color = Color.White,
              strokeWidth = 2.dp,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (language == AppLanguage.MARATHI) "बँक पडताळणी सुरू आहे..." else "Verifying with Bank...",
              fontSize = 14.sp
            )
          } else {
            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (language == AppLanguage.MARATHI) "₹ ${bill.amount.toInt()} सुरक्षित भरा" else "Pay ₹ ${bill.amount.toInt()} Securely",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
fun OfficialReceiptModal(
  bill: BillItem,
  language: AppLanguage,
  onDismiss: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .padding(16.dp),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Receipt Header / Government Letterhead
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            GramPanchayatEmblem(size = 44)
            Column {
              Text(
                text = "महाराष्ट्र शासन",
                fontSize = 11.sp,
                color = SaffronSecondary,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "ग्रामपंचायत निचपूर",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GreenPrimary
              )
              Text(
                text = "ता. किनवट, जि. नांदेड",
                fontSize = 10.sp,
                color = TextSecondary
              )
            }
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        HorizontalDivider(thickness = 2.dp, color = GreenPrimary)

        // Receipt Title Badge
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(GoldContainer)
            .border(1.dp, GoldAccent, RoundedCornerShape(6.dp))
            .padding(vertical = 6.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "ई-कर भरणा अधिकृत पावती (TAX PAYMENT RECEIPT)",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = OnSaffronContainer
          )
        }

        // Details Grid
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
          border = androidx.compose.foundation.BorderStroke(1.dp, OutlineLight),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            ReceiptRow(
              label = if (language == AppLanguage.MARATHI) "पावती क्रमांक:" else "Receipt No:",
              value = bill.receiptNumber.ifEmpty { "GP-RCP-2026-9821" },
              isHighlight = true
            )
            ReceiptRow(
              label = if (language == AppLanguage.MARATHI) "भरणा दिनांक:" else "Date & Time:",
              value = bill.paidDate.ifEmpty { "०१ ऑक्टोबर २०२६, ११:४५ AM" }
            )
            ReceiptRow(
              label = if (language == AppLanguage.MARATHI) "व्यवहार आयडी (Txn ID):" else "Txn ID:",
              value = bill.transactionId.ifEmpty { "TXN9820194821" }
            )
            ReceiptRow(
              label = if (language == AppLanguage.MARATHI) "कर प्रकार:" else "Tax Head:",
              value = bill.billType
            )
            ReceiptRow(
              label = if (language == AppLanguage.MARATHI) "मालमत्ता क्रमांक:" else "Property No:",
              value = bill.propertyNumber
            )
            ReceiptRow(
              label = if (language == AppLanguage.MARATHI) "करदात्याचे नाव:" else "Taxpayer Name:",
              value = bill.consumerName
            )
            ReceiptRow(
              label = if (language == AppLanguage.MARATHI) "आकारणी वर्ष:" else "Assessment Year:",
              value = bill.assessmentYear
            )

            HorizontalDivider()

            ReceiptRow(
              label = if (language == AppLanguage.MARATHI) "एकूण भरलेली रक्कम:" else "Total Amount Paid:",
              value = "₹ ${bill.amount.toInt()}/-",
              isHighlight = true,
              valueColor = GreenPrimary
            )
          }
        }

        // Verification Seal
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(Icons.Default.Verified, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(16.dp))
              Text(
                text = "संगणकीय पावती - स्वाक्षरीची गरज नाही",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = GreenPrimary
              )
            }
            Text(
              text = "ग्रामपंचायत निचपूर महा-ई-सेवा प्रणाली",
              fontSize = 9.sp,
              color = TextSecondary
            )
          }

          // Digital Seal Stamp
          Box(
            modifier = Modifier
              .size(60.dp)
              .clip(CircleShape)
              .border(2.dp, GreenPrimary, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "VERIFIED\nGP-NCH",
              fontSize = 8.sp,
              fontWeight = FontWeight.Bold,
              color = GreenPrimary,
              textAlign = TextAlign.Center
            )
          }
        }

        // Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
          ) {
            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (language == AppLanguage.MARATHI) "पावती डाऊनलोड" else "Download PDF")
          }
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(0.6f)
          ) {
            Text(if (language == AppLanguage.MARATHI) "बंद करा" else "Close")
          }
        }
      }
    }
  }
}

@Composable
fun ReceiptRow(
  label: String,
  value: String,
  isHighlight: Boolean = false,
  valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = label,
      fontSize = 12.sp,
      color = TextSecondary
    )
    Text(
      text = value,
      fontSize = if (isHighlight) 13.sp else 12.sp,
      fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.SemiBold,
      color = valueColor
    )
  }
}
