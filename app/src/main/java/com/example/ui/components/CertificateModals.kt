package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CertificateApplication
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*

@Composable
fun DigitalCertificateModal(
  cert: CertificateApplication,
  language: AppLanguage,
  onDismiss: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .fillMaxHeight(0.92f)
        .padding(vertical = 12.dp),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(18.dp)
      ) {
        // Close button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Black)
          }
        }

        // Certificate Container with Double Border
        Surface(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .border(3.dp, GreenPrimary, RoundedCornerShape(8.dp))
            .padding(4.dp)
            .border(1.dp, GoldAccent, RoundedCornerShape(6.dp))
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
          color = Color(0xFFFDFAF4) // Warm parchment/ivory
        ) {
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Ashok Stambh / GP Seal
            GramPanchayatEmblem(size = 46)

            Text(
              text = "महाराष्ट्र शासन",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = SaffronSecondary,
              letterSpacing = 1.sp
            )
            Text(
              text = "ग्रामपंचायत कार्यालय निचपूर",
              fontSize = 18.sp,
              fontWeight = FontWeight.ExtraBold,
              color = GreenPrimary,
              textAlign = TextAlign.Center
            )
            Text(
              text = "पंचायत समिती किनवट | जिल्हा परिषद नांदेड",
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = Color.DarkGray
            )

            HorizontalDivider(thickness = 1.5.dp, color = SaffronSecondary, modifier = Modifier.padding(vertical = 4.dp))

            // Certificate Type Title
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = GreenPrimary,
              modifier = Modifier.padding(vertical = 4.dp)
            ) {
              Text(
                text = " ।। " + cert.certificateType.uppercase() + " ।। ",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
              )
            }

            // Certificate Metadata
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "दाखला क्र.: ${cert.verificationCode.ifEmpty { cert.applicationId }}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
              )
              Text(
                text = "दिनांक: ${cert.approvedDate.ifEmpty { cert.appliedDate }}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Formal Marathi Certificate Body Text
            val bodyText = "प्रमाणित करण्यात येते की, श्री / श्रीमती ${cert.applicantName}, " +
              "नातेवाईक: ${cert.relativeName}, हे ग्रामपंचायत निचपूर (ता. किनवट, जि. नांदेड) च्या हद्दीतील अधिकृत कायमस्वरूपी रहिवासी असून ते आमच्या व्यक्तीश: माहितीचे आहेत.\n\n" +
              "त्यांचा ओळख पुरावा ${cert.idProofType} (क्र. ${cert.idProofNumber}) ची ग्रामपंचायत अभिलेखाशी तपासणी करण्यात आली आहे.\n\n" +
              "हा दाखला त्यांच्या अर्जावरून व विनंतीनुसार '${cert.purpose}' या शासकीय/शैक्षणिक उपयोगासाठी प्रदान करण्यात येत आहे."

            Text(
              text = bodyText,
              fontSize = 12.sp,
              lineHeight = 20.sp,
              color = Color(0xFF1B1B1B),
              textAlign = TextAlign.Justify,
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Verification Barcode & QR Code representation
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                .padding(8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.QrCode2, contentDescription = "Verification QR", tint = GreenPrimary, modifier = Modifier.size(44.dp))
              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "DIGITALLY SIGNED & VERIFIED",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = GreenPrimary
                )
                Text(
                  text = "Security Hash: 8F2A-99B1-42C0",
                  fontSize = 8.sp,
                  color = Color.Gray
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Signatures Grid
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Bottom
            ) {
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Draw, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(24.dp))
                Text(text = "स्वाक्षरी / पडताळणी", fontSize = 9.sp, color = Color.Gray)
                Text(
                  text = "श्री. महेश मुंडे\nग्रामसेवक / ग्रा.वि.अ.",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  textAlign = TextAlign.Center,
                  color = Color.Black
                )
              }

              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(24.dp))
                Text(text = "डिजिटल स्वाक्षरी", fontSize = 9.sp, color = Color.Gray)
                Text(
                  text = "श्री. सदाशिव मुंडे\nसरपंच, ग्रा.पं. निचपूर",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  textAlign = TextAlign.Center,
                  color = Color.Black
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

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
            Text(if (language == AppLanguage.MARATHI) "दाखला डाऊनलोड करा (PDF)" else "Download Certificate")
          }
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(0.5f)
          ) {
            Text(if (language == AppLanguage.MARATHI) "बंद" else "Close")
          }
        }
      }
    }
  }
}

@Composable
fun NewCertificateModal(
  language: AppLanguage,
  isProcessing: Boolean,
  onDismiss: () -> Unit,
  onSubmit: (String, String, String, String, String, String) -> Unit
) {
  var selectedType by remember { mutableStateOf("रहिवासी दाखला (Residence Certificate)") }
  var applicantName by remember { mutableStateOf("अशोक घारगे") }
  var relativeName by remember { mutableStateOf("विठ्ठलराव घारगे (वडील)") }
  var proofType by remember { mutableStateOf("आधार कार्ड") }
  var proofNumber by remember { mutableStateOf("9821-4567-1234") }
  var purpose by remember { mutableStateOf("") }

  val certOptions = listOf(
    "रहिवासी दाखला (Residence Certificate)",
    "जन्म नोंद दाखला (Birth Record)",
    "मृत्यू नोंद दाखला (Death Record)",
    "ना-हरकत प्रमाणपत्र (NOC - वीज/बांधकाम)",
    "शौचालय दाखला (Sanitation Certificate)"
  )

  Dialog(
    onDismissRequest = { if (!isProcessing) onDismiss() },
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.94f)
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
            Icon(Icons.Default.Description, contentDescription = null, tint = GreenPrimary)
            Text(
              text = if (language == AppLanguage.MARATHI) "नवीन दाखल्यासाठी अर्ज" else "Apply for Certificate",
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

        HorizontalDivider()

        // Certificate Type Selector
        Text(
          text = if (language == AppLanguage.MARATHI) "दाखल्याचा प्रकार निवडा:" else "Select Certificate Type:",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold
        )

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          certOptions.forEach { type ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth()
            ) {
              RadioButton(
                selected = selectedType == type,
                onClick = { selectedType = type },
                colors = RadioButtonDefaults.colors(selectedColor = GreenPrimary)
              )
              Text(
                text = type,
                fontSize = 12.sp,
                fontWeight = if (selectedType == type) FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }

        OutlinedTextField(
          value = applicantName,
          onValueChange = { applicantName = it },
          label = { Text(if (language == AppLanguage.MARATHI) "अर्जदाराचे पूर्ण नाव" else "Applicant Full Name") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
          value = relativeName,
          onValueChange = { relativeName = it },
          label = { Text(if (language == AppLanguage.MARATHI) "वडिलांचे / पतीचे नाव" else "Father/Husband Name") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
          value = proofNumber,
          onValueChange = { proofNumber = it },
          label = { Text(if (language == AppLanguage.MARATHI) "ओळख पुरावा क्रमांक (उदा. आधार)" else "ID Proof Number") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
          value = purpose,
          onValueChange = { purpose = it },
          label = { Text(if (language == AppLanguage.MARATHI) "दाखला कशासाठी हवा आहे? (कारण)" else "Purpose of Certificate") },
          placeholder = { Text("उदा. कॉलेज प्रवेश, बँक खाते, शिष्यवृत्ती") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        Button(
          onClick = {
            if (purpose.isNotBlank() && applicantName.isNotBlank()) {
              onSubmit(selectedType, applicantName, relativeName, proofType, proofNumber, purpose)
            }
          },
          enabled = !isProcessing && purpose.isNotBlank() && applicantName.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
          shape = RoundedCornerShape(12.dp)
        ) {
          if (isProcessing) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
          } else {
            Text(if (language == AppLanguage.MARATHI) "अर्ज सादर करा" else "Submit Application")
          }
        }
      }
    }
  }
}
