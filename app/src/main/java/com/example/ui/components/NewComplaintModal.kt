package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewComplaintModal(
  language: AppLanguage,
  isProcessing: Boolean,
  onDismiss: () -> Unit,
  onSubmit: (String, String, String, Int, String, String, String, String) -> Unit
) {
  val categories = listOf(
    "पाणी पुरवठा" to Icons.Default.WaterDrop,
    "रस्ते व गटारे" to Icons.Default.AltRoute,
    "दिवाबत्ती / स्ट्रीट लाइट" to Icons.Default.Lightbulb,
    "स्वच्छता व कचरा" to Icons.Default.Delete,
    "आरोग्य सुविधा" to Icons.Default.LocalHospital,
    "इतर समस्या" to Icons.Default.Category
  )

  var selectedCategory by remember { mutableStateOf(categories[0].first) }
  var subject by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var wardNumber by remember { mutableStateOf(2) }
  var locationText by remember { mutableStateOf("") }
  var citizenName by remember { mutableStateOf("अशोक घारगे") }
  var citizenMobile by remember { mutableStateOf("9422114589") }
  var hasAttachedPhoto by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = { if (!isProcessing) onDismiss() },
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .fillMaxHeight(0.92f)
        .padding(vertical = 12.dp),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(20.dp)
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
            Icon(Icons.Default.AddCircle, contentDescription = null, tint = GreenPrimary)
            Text(
              text = if (language == AppLanguage.MARATHI) "नवीन तक्रार नोंदणी" else "File a Complaint",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold
            )
          }
          if (!isProcessing) {
            IconButton(onClick = onDismiss) {
              Icon(Icons.Default.Close, contentDescription = "Close")
            }
          }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Category selector chips
          Text(
            text = if (language == AppLanguage.MARATHI) "तक्रार विभाग निवडा:" else "Select Grievance Category:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            categories.chunked(2).forEach { rowList ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                rowList.forEach { (catName, icon) ->
                  val isSelected = selectedCategory == catName
                  Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) GreenContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, GreenPrimary) else null,
                    modifier = Modifier
                      .weight(1f)
                      .clickable { selectedCategory = catName }
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                      Icon(icon, contentDescription = null, tint = if (isSelected) GreenPrimary else TextSecondary, modifier = Modifier.size(16.dp))
                      Text(
                        text = catName,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) GreenPrimary else MaterialTheme.colorScheme.onSurface
                      )
                    }
                  }
                }
              }
            }
          }

          OutlinedTextField(
            value = subject,
            onValueChange = { subject = it },
            label = { Text(if (language == AppLanguage.MARATHI) "तक्रारीचा विषय (उदा. पथदिवा बंद आहे)" else "Subject") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          )

          OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(if (language == AppLanguage.MARATHI) "तक्रारीचे सविस्तर वर्णन" else "Detailed Description") },
            placeholder = { Text("समस्येची सविस्तर माहिती लिहा...") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          )

          // Ward Selector
          Text(
            text = if (language == AppLanguage.MARATHI) "प्रभाग क्रमांक (Ward No):" else "Select Ward:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            (1..6).forEach { ward ->
              val isSelected = wardNumber == ward
              FilterChip(
                selected = isSelected,
                onClick = { wardNumber = ward },
                label = { Text("प्रभाग $ward", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = GreenPrimary,
                  selectedLabelColor = Color.White
                )
              )
            }
          }

          OutlinedTextField(
            value = locationText,
            onValueChange = { locationText = it },
            label = { Text(if (language == AppLanguage.MARATHI) "नेमके ठिकाण / लँडमार्क" else "Location Landmark") },
            placeholder = { Text("उदा. मारुती मंदिरामागे, घर क्र. ४२१ समोर") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          )

          // Photo upload preview simulator
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { hasAttachedPhoto = !hasAttachedPhoto }
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Icon(
                imageVector = if (hasAttachedPhoto) Icons.Default.CheckCircle else Icons.Default.AddPhotoAlternate,
                contentDescription = null,
                tint = if (hasAttachedPhoto) GreenPrimary else TextSecondary
              )
              Column {
                Text(
                  text = if (hasAttachedPhoto) "फोटो संलग्न केला आहे (IMG_GP_01.jpg)" else "समस्येचे छायाचित्र जोडा (ऐच्छिक)",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = if (hasAttachedPhoto) GreenPrimary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = if (hasAttachedPhoto) "बदलण्यासाठी पुन्हा स्पर्श करा" else "कॅमेरा किंवा गॅलरीतून फोटो अपलोड करा",
                  fontSize = 10.sp,
                  color = TextSecondary
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
          onClick = {
            if (subject.isNotBlank() && description.isNotBlank()) {
              onSubmit(
                selectedCategory,
                subject,
                description,
                wardNumber,
                locationText.ifEmpty { "प्रभाग क्र. $wardNumber" },
                citizenName,
                citizenMobile,
                if (hasAttachedPhoto) "photo_attached" else ""
              )
            }
          },
          enabled = !isProcessing && subject.isNotBlank() && description.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
          shape = RoundedCornerShape(12.dp)
        ) {
          if (isProcessing) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
          } else {
            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (language == AppLanguage.MARATHI) "तक्रार दाखल करा" else "Submit Complaint")
          }
        }
      }
    }
  }
}
