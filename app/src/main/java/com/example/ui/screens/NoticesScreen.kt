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
import com.example.data.model.DailyDiaryFeed
import com.example.data.model.GramSabhaMeeting
import com.example.data.model.NoticeItem
import com.example.ui.localization.AppLanguage
import com.example.ui.theme.*
import com.example.ui.viewmodel.GramSevaUiState

@Composable
fun NoticesScreen(
  uiState: GramSevaUiState,
  onSubTabChange: (Int) -> Unit,
  onOpenNoticeAdmin: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = uiState.language
  val currentSubTab = uiState.noticesSubTab
  var selectedCategory by remember { mutableStateOf("ALL") }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = if (lang == AppLanguage.MARATHI) "सूचना फलक व ग्रामसभा" else "Notices & Gram Sabha",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "ग्रामपंचायतीचे अधिकृत जाहीर प्रगटन व निर्णय",
          fontSize = 11.sp,
          color = TextSecondary
        )
      }

      if (uiState.isAdminMode && currentSubTab == 0) {
        Button(
          onClick = onOpenNoticeAdmin,
          colors = ButtonDefaults.buttonColors(containerColor = SaffronSecondary),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "सूचना जोडा", fontSize = 11.sp)
        }
      }
    }

    // 3 Sub-Tabs: Notice Board, Gram Sabha, Daily Diary
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        SubTabPill(
          title = if (lang == AppLanguage.MARATHI) "सूचना फलक" else "Notices",
          icon = Icons.Default.Campaign,
          isSelected = currentSubTab == 0,
          onClick = { onSubTabChange(0) },
          modifier = Modifier.weight(1f)
        )
        SubTabPill(
          title = if (lang == AppLanguage.MARATHI) "ग्रामसभा व ठराव" else "Gram Sabha",
          icon = Icons.Default.Groups,
          isSelected = currentSubTab == 1,
          onClick = { onSubTabChange(1) },
          modifier = Modifier.weight(1f)
        )
        SubTabPill(
          title = if (lang == AppLanguage.MARATHI) "दैनिक रोजनिशी" else "Daily Diary",
          icon = Icons.Default.MenuBook,
          isSelected = currentSubTab == 2,
          onClick = { onSubTabChange(2) },
          modifier = Modifier.weight(1f)
        )
      }
    }

    when (currentSubTab) {
      0 -> NoticesContent(
        notices = uiState.notices,
        selectedCategory = selectedCategory,
        onCategorySelect = { selectedCategory = it },
        lang = lang
      )
      1 -> GramSabhaContent(
        meetings = uiState.gramSabhaMeetings,
        lang = lang
      )
      2 -> DailyDiaryContent(
        feed = uiState.diaryFeed,
        lang = lang
      )
    }
  }
}

// 1. Notices Tab
@Composable
fun NoticesContent(
  notices: List<NoticeItem>,
  selectedCategory: String,
  onCategorySelect: (String) -> Unit,
  lang: AppLanguage
) {
  val categories = listOf("ALL", "पाणीपुरवठा", "कर वसुली", "आरोग्य", "योजना")
  val filtered = notices.filter {
    selectedCategory == "ALL" || it.category == selectedCategory
  }

  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      items(categories) { cat ->
        val isSelected = selectedCategory == cat
        FilterChip(
          selected = isSelected,
          onClick = { onCategorySelect(cat) },
          label = { Text(if (cat == "ALL") "सर्व सूचना" else cat, fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = GreenPrimary,
            selectedLabelColor = Color.White
          )
        )
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(bottom = 90.dp)
    ) {
      items(filtered, key = { "notice_${it.id}" }) { notice ->
        NoticeCard(notice = notice, lang = lang)
      }
    }
  }
}

@Composable
fun NoticeCard(notice: NoticeItem, lang: AppLanguage) {
  val isHigh = notice.priority == "HIGH"
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isHigh) SaffronContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
    ),
    border = if (isHigh) androidx.compose.foundation.BorderStroke(1.dp, SaffronSecondary.copy(alpha = 0.5f)) else null,
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
          if (isHigh) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = Color.Red
            ) {
              Text(
                text = "तातडीची सूचना",
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = GreenContainer
          ) {
            Text(
              text = notice.category,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = GreenPrimary,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Text(text = notice.date, fontSize = 11.sp, color = TextSecondary)
      }

      Text(
        text = notice.titleMr,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      Text(
        text = notice.descriptionMr,
        fontSize = 12.sp,
        color = TextSecondary,
        lineHeight = 17.sp
      )

      if (notice.attachmentName.isNotEmpty()) {
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
            Icon(Icons.Default.AttachFile, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(16.dp))
            Text(
              text = notice.attachmentName,
              fontSize = 11.sp,
              color = GreenPrimary,
              fontWeight = FontWeight.Medium
            )
          }
          TextButton(
            onClick = { /* Simulated download */ },
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("डाऊनलोड", fontSize = 11.sp)
          }
        }
      }
    }
  }
}

// 2. Gram Sabha Tab
@Composable
fun GramSabhaContent(
  meetings: List<GramSabhaMeeting>,
  lang: AppLanguage
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = GreenContainer)
      ) {
        Column(
          modifier = Modifier.padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.HowToVote, contentDescription = null, tint = GreenPrimary)
            Text(
              text = if (lang == AppLanguage.MARATHI) "लोकशाहीचे मंदिर - ग्रामसभा" else "Democratic Gram Sabha",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = OnGreenContainer
            )
          }
          Text(
            text = "गावाच्या सर्वांगीण विकासासाठी प्रत्येक ग्रामस्थाचा सहभाग महत्त्वाचा आहे. ग्रामसभेला आवर्जून उपस्थित राहा.",
            fontSize = 11.sp,
            color = TextSecondary,
            lineHeight = 15.sp
          )
        }
      }
    }

    items(meetings, key = { "sabha_${it.id}" }) { meeting ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (meeting.isCompleted) Color(0xFFE0E0E0) else SaffronContainer
            ) {
              Text(
                text = if (meeting.isCompleted) "सभा संपन्न (Minutes Available)" else "आगामी ग्रामसभा (Upcoming)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (meeting.isCompleted) Color.DarkGray else SaffronSecondary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }

            Text(
              text = meeting.meetingDate,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = GreenPrimary
            )
          }

          Text(
            text = meeting.subjectMr,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.Schedule, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
            Text(text = meeting.meetingTime, fontSize = 12.sp, color = TextSecondary)
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.Place, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
            Text(text = meeting.venueMr, fontSize = 12.sp, color = TextSecondary)
          }

          HorizontalDivider(color = OutlineLight.copy(alpha = 0.5f))

          Text(
            text = "सभेची विषयपत्रिका (Agenda Items):",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          val agendaItems = meeting.agendaPoints.split("|")
          agendaItems.forEachIndexed { index, point ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              verticalAlignment = Alignment.Top
            ) {
              Text(text = "${index + 1}.", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
              Text(text = point, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 16.sp)
            }
          }

          if (meeting.isCompleted && meeting.resolutionsSummaryMr.isNotEmpty()) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = GreenContainer.copy(alpha = 0.6f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Text(
                  text = "सभेत मंजूर प्रमुख ठराव (Resolutions):",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = OnGreenContainer
                )
                Text(
                  text = meeting.resolutionsSummaryMr,
                  fontSize = 11.sp,
                  color = OnGreenContainer,
                  lineHeight = 16.sp
                )
                Text(
                  text = "उपस्थित नागरिक संख्या: ${meeting.attendanceCount}",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = GreenPrimary
                )
              }
            }
          }
        }
      }
    }
  }
}

// 3. Daily Diary Tab
@Composable
fun DailyDiaryContent(
  feed: List<DailyDiaryFeed>,
  lang: AppLanguage
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    item {
      Text(
        text = if (lang == AppLanguage.MARATHI) "ग्रामपंचायत दैनिक कामकाज रोजनिशी" else "Daily Village Diary Feed",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }

    items(feed, key = { "diary_${it.id}" }) { item ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = GreenContainer
            ) {
              Text(
                text = item.date,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }
            Text(
              text = item.officerInCharge,
              fontSize = 10.sp,
              color = TextSecondary
            )
          }

          Text(
            text = item.titleMr,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Text(
            text = item.descriptionMr,
            fontSize = 12.sp,
            color = TextSecondary,
            lineHeight = 17.sp
          )
        }
      }
    }
  }
}
