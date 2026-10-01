package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ComplaintItem
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*

@Composable
fun ComplaintDetailModal(
  complaint: ComplaintItem,
  language: AppLanguage,
  isAdmin: Boolean,
  onDismiss: () -> Unit,
  onOpenAdminUpdate: (ComplaintItem) -> Unit
) {
  val stages = listOf(
    "SUBMITTED" to (if (language == AppLanguage.MARATHI) "१. तक्रार दाखल झाली" else "1. Submitted"),
    "UNDER_REVIEW" to (if (language == AppLanguage.MARATHI) "२. प्राथमिक तपासणी" else "2. Under Review"),
    "ASSIGNED" to (if (language == AppLanguage.MARATHI) "३. अधिकारी नियुक्त" else "3. Assigned"),
    "IN_PROGRESS" to (if (language == AppLanguage.MARATHI) "४. प्रत्यक्ष काम सुरू" else "4. In Progress"),
    "RESOLVED" to (if (language == AppLanguage.MARATHI) "५. तक्रार निवारण" else "5. Resolved"),
    "CLOSED" to (if (language == AppLanguage.MARATHI) "६. तक्रार बंद" else "6. Closed")
  )

  val stageIndex = when (complaint.status) {
    "SUBMITTED" -> 0
    "UNDER_REVIEW" -> 1
    "ASSIGNED" -> 2
    "IN_PROGRESS" -> 3
    "RESOLVED" -> 4
    "CLOSED" -> 5
    else -> 0
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .fillMaxHeight(0.88f)
        .padding(vertical = 16.dp),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(20.dp)
      ) {
        // Top Header
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
              imageVector = Icons.Default.ReportProblem,
              contentDescription = null,
              tint = SaffronSecondary,
              modifier = Modifier.size(24.dp)
            )
            Column {
              Text(
                text = complaint.complaintNumber,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = complaint.category,
                fontSize = 12.sp,
                color = GreenPrimary,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        // Scrollable content
        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Status Banner
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = GreenContainer,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  text = if (language == AppLanguage.MARATHI) "सद्यस्थिती" else "Current Status",
                  fontSize = 11.sp,
                  color = TextSecondary
                )
                Text(
                  text = when (complaint.status) {
                    "SUBMITTED" -> if (language == AppLanguage.MARATHI) "दाखल झाली" else "Submitted"
                    "UNDER_REVIEW" -> if (language == AppLanguage.MARATHI) "तपासणी सुरू" else "Under Review"
                    "ASSIGNED" -> if (language == AppLanguage.MARATHI) "अधिकारी नियुक्त" else "Assigned"
                    "IN_PROGRESS" -> if (language == AppLanguage.MARATHI) "काम सुरू आहे" else "In Progress"
                    "RESOLVED" -> if (language == AppLanguage.MARATHI) "निवारण झाले" else "Resolved"
                    "CLOSED" -> if (language == AppLanguage.MARATHI) "बंद" else "Closed"
                    else -> complaint.status
                  },
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = GreenPrimary
                )
              }
              ComplaintStatusChip(status = complaint.status, language = language)
            }
          }

          // Subject & Description
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = if (language == AppLanguage.MARATHI) "तक्रार विषय:" else "Subject:",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = TextSecondary
            )
            Text(
              text = complaint.subject,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = if (language == AppLanguage.MARATHI) "सविस्तर माहिती:" else "Description:",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = TextSecondary
            )
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = complaint.description,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                modifier = Modifier.padding(10.dp)
              )
            }
          }

          // Citizen Details
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Text(
                text = if (language == AppLanguage.MARATHI) "तक्रारदार व स्थळ माहिती" else "Citizen & Location Info",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary
              )
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = if (language == AppLanguage.MARATHI) "नाव:" else "Name:",
                  fontSize = 12.sp,
                  color = TextSecondary
                )
                Text(
                  text = complaint.citizenName,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = if (language == AppLanguage.MARATHI) "प्रभाग क्र.:" else "Ward No:",
                  fontSize = 12.sp,
                  color = TextSecondary
                )
                Text(
                  text = "प्रभाग क्र. ${complaint.wardNumber}",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = if (language == AppLanguage.MARATHI) "ठिकाण:" else "Location:",
                  fontSize = 12.sp,
                  color = TextSecondary
                )
                Text(
                  text = complaint.locationText,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = if (language == AppLanguage.MARATHI) "दाखल दिनांक:" else "Date:",
                  fontSize = 12.sp,
                  color = TextSecondary
                )
                Text(
                  text = complaint.submittedDate,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }

          // Official Gram Panchayat Response
          if (complaint.officerRemark.isNotEmpty()) {
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = SaffronContainer.copy(alpha = 0.6f),
              border = androidx.compose.foundation.BorderStroke(1.dp, SaffronSecondary.copy(alpha = 0.3f)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = null,
                    tint = SaffronSecondary,
                    modifier = Modifier.size(18.dp)
                  )
                  Text(
                    text = if (language == AppLanguage.MARATHI) "ग्रामपंचायत अधिकृत उत्तर / शेरा" else "Official Panchayat Remark",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSaffronContainer
                  )
                }
                Text(
                  text = complaint.officerRemark,
                  fontSize = 13.sp,
                  lineHeight = 19.sp,
                  color = OnSaffronContainer
                )
                if (complaint.assignedOfficer.isNotEmpty()) {
                  Text(
                    text = "नियुक्त अधिकारी: ${complaint.assignedOfficer}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary
                  )
                }
                if (complaint.resolvedDate.isNotEmpty()) {
                  Text(
                    text = "निवारण दिनांक: ${complaint.resolvedDate}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenPrimary
                  )
                }
              }
            }
          }

          // 6-Stage Lifecycle Progress Timeline
          Text(
            text = if (language == AppLanguage.MARATHI) "तक्रार निवारण टप्पे (Lifecycle Tracker)" else "Redressal Lifecycle Tracker",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            stages.forEachIndexed { index, pair ->
              val isPastOrCurrent = index <= stageIndex
              val isCurrent = index == stageIndex
              val itemColor = when {
                isCurrent -> GreenPrimary
                isPastOrCurrent -> StatusResolved
                else -> Color.LightGray
              }

              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isPastOrCurrent) itemColor else Color(0xFFE0E0E0)),
                  contentAlignment = Alignment.Center
                ) {
                  if (isPastOrCurrent) {
                    Icon(
                      imageVector = if (isCurrent) Icons.Default.Adjust else Icons.Default.Check,
                      contentDescription = null,
                      tint = Color.White,
                      modifier = Modifier.size(14.dp)
                    )
                  }
                }

                Text(
                  text = pair.second,
                  fontSize = 13.sp,
                  fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                  color = if (isPastOrCurrent) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
              }
            }
          }
        }

        // Bottom Action (Officer Admin Action or Close)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (isAdmin) {
            Button(
              onClick = {
                onOpenAdminUpdate(complaint)
              },
              modifier = Modifier.weight(1f),
              colors = ButtonDefaults.buttonColors(containerColor = SaffronSecondary)
            ) {
              Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (language == AppLanguage.MARATHI) "स्थिती बदला / शेरा द्या" else "Update Status",
                fontSize = 13.sp
              )
            }
          }
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(if (isAdmin) 0.6f else 1f)
          ) {
            Text(if (language == AppLanguage.MARATHI) "बंद करा" else "Close")
          }
        }
      }
    }
  }
}
