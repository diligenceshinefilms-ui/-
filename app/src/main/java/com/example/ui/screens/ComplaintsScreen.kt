package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.ComplaintItem
import com.example.ui.components.ComplaintStatusChip
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.GramSevaStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.GramSevaUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintsScreen(
  uiState: GramSevaUiState,
  onSearchChange: (String) -> Unit,
  onCategoryFilterChange: (String) -> Unit,
  onSelectComplaint: (ComplaintItem) -> Unit,
  onOpenNewComplaint: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = uiState.language
  var statusTab by remember { mutableStateOf("ALL") }

  val categories = listOf(
    "ALL" to (if (lang == AppLanguage.MARATHI) "सर्व विभाग" else "All"),
    "पाणी पुरवठा" to (if (lang == AppLanguage.MARATHI) "पाणी पुरवठा" else "Water"),
    "दिवाबत्ती / स्ट्रीट लाइट" to (if (lang == AppLanguage.MARATHI) "दिवाबत्ती" else "Lighting"),
    "रस्ते व गटारे" to (if (lang == AppLanguage.MARATHI) "रस्ते व गटारे" else "Roads"),
    "स्वच्छता व कचरा" to (if (lang == AppLanguage.MARATHI) "स्वच्छता" else "Sanitation")
  )

  val filteredComplaints = uiState.complaints.filter { comp ->
    val matchesQuery = uiState.complaintSearchQuery.isBlank() ||
      comp.complaintNumber.contains(uiState.complaintSearchQuery, ignoreCase = true) ||
      comp.subject.contains(uiState.complaintSearchQuery, ignoreCase = true) ||
      comp.description.contains(uiState.complaintSearchQuery, ignoreCase = true)

    val matchesCat = uiState.complaintCategoryFilter == "ALL" || comp.category.contains(uiState.complaintCategoryFilter, ignoreCase = true)

    val matchesStatus = when (statusTab) {
      "ACTIVE" -> comp.status != "RESOLVED" && comp.status != "CLOSED"
      "RESOLVED" -> comp.status == "RESOLVED" || comp.status == "CLOSED"
      else -> true
    }

    matchesQuery && matchesCat && matchesStatus
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = onOpenNewComplaint,
        containerColor = GreenPrimary,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(bottom = 60.dp)
      ) {
        Icon(Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = GramSevaStrings.registerNewComplaint(lang),
          fontWeight = FontWeight.Bold
        )
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Header info
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = GramSevaStrings.complaintsTitle(lang),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "नागरिक तक्रारींचे पारदर्शक व कालमर्यादेत निवारण",
            fontSize = 11.sp,
            color = TextSecondary
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = GreenContainer
        ) {
          Text(
            text = "${uiState.complaints.size} एकूण तक्रारी",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = GreenPrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      // Search Bar
      OutlinedTextField(
        value = uiState.complaintSearchQuery,
        onValueChange = onSearchChange,
        placeholder = { Text(GramSevaStrings.searchComplaints(lang), fontSize = 13.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GreenPrimary) },
        trailingIcon = {
          if (uiState.complaintSearchQuery.isNotEmpty()) {
            IconButton(onClick = { onSearchChange("") }) {
              Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(18.dp))
            }
          }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = GreenPrimary,
          unfocusedBorderColor = OutlineLight
        )
      )

      // Category Filter Chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(categories) { (key, label) ->
          val isSelected = uiState.complaintCategoryFilter == key
          FilterChip(
            selected = isSelected,
            onClick = { onCategoryFilterChange(key) },
            label = { Text(label, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = GreenPrimary,
              selectedLabelColor = Color.White
            )
          )
        }
      }

      // Status Filter Segmented Row
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          StatusTabButton(
            title = GramSevaStrings.statusAll(lang),
            isSelected = statusTab == "ALL",
            onClick = { statusTab = "ALL" },
            modifier = Modifier.weight(1f)
          )
          StatusTabButton(
            title = GramSevaStrings.statusActive(lang),
            isSelected = statusTab == "ACTIVE",
            onClick = { statusTab = "ACTIVE" },
            modifier = Modifier.weight(1f)
          )
          StatusTabButton(
            title = GramSevaStrings.statusResolved(lang),
            isSelected = statusTab == "RESOLVED",
            onClick = { statusTab = "RESOLVED" },
            modifier = Modifier.weight(1f)
          )
        }
      }

      // Complaints List
      if (filteredComplaints.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.SearchOff,
              contentDescription = null,
              tint = TextSecondary,
              modifier = Modifier.size(48.dp)
            )
            Text(
              text = if (lang == AppLanguage.MARATHI) "या निकषांनुसार कोणतीही तक्रार आढळली नाही" else "No complaints found matching filters",
              fontSize = 14.sp,
              color = TextSecondary,
              fontWeight = FontWeight.Medium
            )
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(bottom = 80.dp)
        ) {
          items(filteredComplaints, key = { "complaint_${it.id}" }) { complaint ->
            ComplaintCard(
              complaint = complaint,
              language = lang,
              onClick = { onSelectComplaint(complaint) }
            )
          }
        }
      }
    }
  }
}

@Composable
fun StatusTabButton(
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent)
      .clickable { onClick() }
      .padding(vertical = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = title,
      fontSize = 12.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      color = if (isSelected) GreenPrimary else TextSecondary
    )
  }
}

@Composable
fun ComplaintCard(
  complaint: ComplaintItem,
  language: AppLanguage,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = complaint.complaintNumber,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = GreenContainer
          ) {
            Text(
              text = complaint.category,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold,
              color = GreenPrimary,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        ComplaintStatusChip(status = complaint.status, language = language)
      }

      Text(
        text = complaint.subject,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1
      )

      Text(
        text = complaint.description,
        fontSize = 12.sp,
        color = TextSecondary,
        maxLines = 2,
        lineHeight = 17.sp
      )

      HorizontalDivider(color = OutlineLight.copy(alpha = 0.5f))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
          Text(
            text = "प्रभाग ${complaint.wardNumber} • ${complaint.submittedDate}",
            fontSize = 11.sp,
            color = TextSecondary
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
          Text(
            text = if (language == AppLanguage.MARATHI) "सविस्तर पहा" else "Track",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = GreenPrimary
          )
          Icon(Icons.Default.ChevronRight, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(16.dp))
        }
      }
    }
  }
}
