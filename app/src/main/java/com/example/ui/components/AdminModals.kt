package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ComplaintItem
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.SaffronSecondary

@Composable
fun AdminUpdateComplaintModal(
  complaint: ComplaintItem,
  language: AppLanguage,
  isProcessing: Boolean,
  onDismiss: () -> Unit,
  onSubmit: (String, String, String) -> Unit
) {
  var selectedStatus by remember { mutableStateOf(complaint.status) }
  var remark by remember { mutableStateOf(complaint.officerRemark) }

  val statuses = listOf(
    "SUBMITTED" to "दाखल (Submitted)",
    "UNDER_REVIEW" to "तपासणी सुरू (Under Review)",
    "ASSIGNED" to "अधिकारी नियुक्त (Assigned)",
    "IN_PROGRESS" to "काम सुरू आहे (In Progress)",
    "RESOLVED" to "निवारण झाले (Resolved)",
    "CLOSED" to "तक्रार बंद (Closed)"
  )

  Dialog(
    onDismissRequest = { if (!isProcessing) onDismiss() },
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
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = SaffronSecondary)
            Text(
              text = if (language == AppLanguage.MARATHI) "तक्रार स्थिती व शेरा अद्ययावत" else "Officer Action",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
          if (!isProcessing) {
            IconButton(onClick = onDismiss) {
              Icon(Icons.Default.Close, contentDescription = "Close")
            }
          }
        }

        HorizontalDivider()

        Text(
          text = "तक्रार क्र.: ${complaint.complaintNumber}\nविषय: ${complaint.subject}",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Text(
          text = "नवीन स्थिती निवडा:",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          statuses.forEach { (statusKey, label) ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth()
            ) {
              RadioButton(
                selected = selectedStatus == statusKey,
                onClick = { selectedStatus = statusKey },
                colors = RadioButtonDefaults.colors(selectedColor = SaffronSecondary)
              )
              Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (selectedStatus == statusKey) FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }

        OutlinedTextField(
          value = remark,
          onValueChange = { remark = it },
          label = { Text("ग्रामपंचायत अधिकृत शेरा / कृती अहवाल") },
          placeholder = { Text("उदा. पथदिव्याची नवीन ट्यूब बसवून दुरुस्ती पूर्ण केली आहे.") },
          minLines = 3,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        Button(
          onClick = {
            onSubmit(complaint.id, selectedStatus, remark)
          },
          enabled = !isProcessing && remark.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
          colors = ButtonDefaults.buttonColors(containerColor = SaffronSecondary),
          shape = RoundedCornerShape(12.dp)
        ) {
          if (isProcessing) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
          } else {
            Text("स्थिती जतन करा (Save Update)", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun AdminPublishNoticeModal(
  language: AppLanguage,
  isProcessing: Boolean,
  onDismiss: () -> Unit,
  onPublish: (String, String, String, String, String, String) -> Unit
) {
  var titleMr by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("सार्वजनिक") }
  var priority by remember { mutableStateOf("NORMAL") }
  var descriptionMr by remember { mutableStateOf("") }

  val categories = listOf("सार्वजनिक", "पाणीपुरवठा", "कर वसुली", "आरोग्य", "योजना")

  Dialog(
    onDismissRequest = { if (!isProcessing) onDismiss() },
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
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.Campaign, contentDescription = null, tint = GreenPrimary)
            Text(
              text = if (language == AppLanguage.MARATHI) "नवीन जाहीर सूचना प्रसिद्ध करा" else "Publish Notice",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
          if (!isProcessing) {
            IconButton(onClick = onDismiss) {
              Icon(Icons.Default.Close, contentDescription = "Close")
            }
          }
        }

        HorizontalDivider()

        OutlinedTextField(
          value = titleMr,
          onValueChange = { titleMr = it },
          label = { Text("सूचनेचे शीर्षक (मराठीत)") },
          placeholder = { Text("उदा. पाणीपुरवठा वेळेत बदल") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        Text(text = "वर्गवारी निवडा:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          categories.forEach { cat ->
            FilterChip(
              selected = category == cat,
              onClick = { category = cat },
              label = { Text(cat, fontSize = 11.sp) }
            )
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Text(text = "प्राधान्य (Priority):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
          Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
              selected = priority == "NORMAL",
              onClick = { priority = "NORMAL" }
            )
            Text("सामान्य", fontSize = 12.sp)
          }
          Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
              selected = priority == "HIGH",
              onClick = { priority = "HIGH" },
              colors = RadioButtonDefaults.colors(selectedColor = Color.Red)
            )
            Text("तातडीची (High)", fontSize = 12.sp, color = Color.Red, fontWeight = FontWeight.Bold)
          }
        }

        OutlinedTextField(
          value = descriptionMr,
          onValueChange = { descriptionMr = it },
          label = { Text("सविस्तर जाहीर प्रगटन तपशील") },
          placeholder = { Text("ग्रामस्थांसाठी महत्त्वाची सूचना लिहा...") },
          minLines = 3,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        Button(
          onClick = {
            if (titleMr.isNotBlank() && descriptionMr.isNotBlank()) {
              onPublish(titleMr, titleMr, category, priority, descriptionMr, descriptionMr)
            }
          },
          enabled = !isProcessing && titleMr.isNotBlank() && descriptionMr.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
          colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
          shape = RoundedCornerShape(12.dp)
        ) {
          if (isProcessing) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
          } else {
            Text("गावात प्रसिद्ध करा (Publish)", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
