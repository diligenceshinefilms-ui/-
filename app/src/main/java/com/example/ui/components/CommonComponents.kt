package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*

@Composable
fun GramPanchayatEmblem(
  modifier: Modifier = Modifier,
  size: Int = 40
) {
  Box(
    modifier = modifier
      .size(size.dp)
      .clip(CircleShape)
      .background(GreenPrimary)
      .border(1.5.dp, GoldAccent, CircleShape),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = Icons.Default.AccountBalance,
      contentDescription = "Gram Panchayat Seal",
      tint = Color.White,
      modifier = Modifier.size((size * 0.6).dp)
    )
  }
}

@Composable
fun ComplaintStatusChip(
  status: String,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor, label) = when (status) {
    "SUBMITTED" -> Triple(
      StatusSubmitted.copy(alpha = 0.15f),
      StatusSubmitted,
      if (language == AppLanguage.MARATHI) "दाखल झाली" else "Submitted"
    )
    "UNDER_REVIEW" -> Triple(
      StatusUnderReview.copy(alpha = 0.15f),
      StatusUnderReview,
      if (language == AppLanguage.MARATHI) "तपासणी सुरू" else "Under Review"
    )
    "ASSIGNED" -> Triple(
      StatusAssigned.copy(alpha = 0.15f),
      StatusAssigned,
      if (language == AppLanguage.MARATHI) "अधिकारी नियुक्त" else "Assigned"
    )
    "IN_PROGRESS" -> Triple(
      StatusInProgress.copy(alpha = 0.15f),
      StatusInProgress,
      if (language == AppLanguage.MARATHI) "काम सुरू आहे" else "In Progress"
    )
    "RESOLVED" -> Triple(
      StatusResolved.copy(alpha = 0.15f),
      StatusResolved,
      if (language == AppLanguage.MARATHI) "निवारण झाले" else "Resolved"
    )
    "CLOSED" -> Triple(
      StatusClosed.copy(alpha = 0.15f),
      StatusClosed,
      if (language == AppLanguage.MARATHI) "तक्रार बंद" else "Closed"
    )
    else -> Triple(
      Color.Gray.copy(alpha = 0.15f),
      Color.DarkGray,
      status
    )
  }

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = bgColor,
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Box(
        modifier = Modifier
          .size(6.dp)
          .clip(CircleShape)
          .background(textColor)
      )
      Text(
        text = label,
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}

@Composable
fun CertificateStatusChip(
  status: String,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor, label) = when (status) {
    "PENDING" -> Triple(
      StatusPending.copy(alpha = 0.15f),
      Color(0xFFE65100),
      if (language == AppLanguage.MARATHI) "प्रलंबित" else "Pending"
    )
    "APPROVED" -> Triple(
      StatusApproved.copy(alpha = 0.15f),
      StatusApproved,
      if (language == AppLanguage.MARATHI) "मंजूर (डिजिटल स्वाक्षरीत)" else "Approved"
    )
    "REJECTED" -> Triple(
      StatusRejected.copy(alpha = 0.15f),
      StatusRejected,
      if (language == AppLanguage.MARATHI) "नामंजूर" else "Rejected"
    )
    else -> Triple(Color.LightGray, Color.Black, status)
  }

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = bgColor,
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Icon(
        imageVector = if (status == "APPROVED") Icons.Default.Verified else Icons.Default.HourglassEmpty,
        contentDescription = null,
        tint = textColor,
        modifier = Modifier.size(12.dp)
      )
      Text(
        text = label,
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}
